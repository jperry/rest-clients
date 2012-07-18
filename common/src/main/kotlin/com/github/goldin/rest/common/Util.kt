package com.github.goldin.rest.common

import java.lang.reflect.Field
import java.util.Date
import java.util.List
import java.util.Map
import kotlin.test.assertNotNull


/**
 * General purpose reflection utilities class.
 */
class Reflection
{
   /**
    * Updates object specified using map of its fields (name => value), converting them to an appropriate type.
    * Returns map of unrecognized fields 9fields that don't appear to be object's fields).
    */
    fun updateObject( o : Any, map: Map<String, Any> ): Map<String, Any>
    {
        val unrecognizedFields: Map<String, Any>   = hashMap()
        val objectFields      : Map<String, Field> = hashMap()

       /**
        * Mapping of object fields: field name => field instance
        */
        o.javaClass.getFields()!!.forEach{ ( f : Field? ) -> if ( f != null ) objectFields.put( f.getName(), f ) }

        for ( e in map )
        {
            val fieldName : String  = e!!.key

            try
            {
                val field       : Field?  = objectFields.get( fieldName )
                val fieldValue  : Any     = e!!.value
                val fieldValueS : String? = if ( fieldValue is String  ) fieldValue else
                                            if ( fieldValue is List<*> ) fieldValue.get( 0 ).toString() else
                                            null

                assertNotNull( fieldValueS,
                               "Failed to String-ify field's [$fieldName] value [$fieldValue] of type [${ fieldValue.javaClass.getName() }}]" )

                if ( field == null )
                {
                    unrecognizedFields.put( fieldName, fieldValueS )
                }
                else
                {
                    val fieldType : Class<out Any?>? = field.getType()
                    val convertedFieldValue : Any?    = if ( fieldType.equals( javaClass<String> ())) fieldValue else
                                                        if ( fieldType.equals( javaClass<Integer>())) Integer.valueOf( fieldValueS )!! else
                                                        if ( fieldType.equals( javaClass<Date>   ())) Date( Long.valueOf( fieldValueS )!!.toLong()) else
                                                        null

                    assertNotNull( convertedFieldValue,
                                   "Unknown type [$fieldType] of field [$fieldName] for instance of class [${ o.javaClass.getName() }]" )

                    field.set( o, convertedFieldValue )
                }
            }
            catch( t: Throwable )
            {
                throw RuntimeException( "Failed to update field [$fieldName] of object of type [${ o.javaClass.getName() }]",
                                        t )
            }
        }

        return unrecognizedFields
    }
}