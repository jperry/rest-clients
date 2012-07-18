package com.github.goldin.rest.common

import java.util.Map
import kotlin.nullable.forEach
import java.util.TreeMap
import java.lang.reflect.Field
import java.util.List
import java.util.Date
import kotlin.test.assertFalse


/**
 * General purpose reflection utilities class.
 */
class Reflection
{
    class object
    {

       /**
        * Updates object specified using map of its fields (name => value), converting them to an appropriate type.
        */
        fun updateObject( o : Any, map: Map<String, String> ): Unit
        {
            val objectFields : Map<String, Field> = hashMap()
            o.javaClass.getFields()!!.forEach{ ( f : Field? ) -> if ( f != null ) objectFields.put( f.getName(), f ) }

            for ( e in map )
            {
                val fieldName  : String          = e!!.key
                val fieldValue : String          = e!!.value
                val field      : Field           = objectFields.get( fieldName )!!
                val fieldType  : Class<out Any?> = field.getType()!!
                val convertedFieldValue : Any?   = if ( fieldType.equals( javaClass<String> ())) fieldValue else
                                                   if ( fieldType.equals( javaClass<Boolean>())) Boolean.valueOf( fieldValue ) else
                                                   if ( fieldType.equals( javaClass<Integer>())) Integer.valueOf( fieldValue ) else
                                                   if ( fieldType.equals( javaClass<Date>   ())) Date( Long.valueOf( fieldValue )!!.toLong()) else
                                                   null

                assertFalse( convertedFieldValue == null,
                             "Unknown type [$fieldType] of field [$fieldName] for instance of class [${ o.javaClass }]" )

                field.set( o, convertedFieldValue )
            }
        }
    }
}