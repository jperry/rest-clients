package com.github.goldin.rest.common

import java.lang.reflect.Field
import java.util.Date
import java.util.List
import java.util.Map
import kotlin.test.assertNotNull
import java.util.Collection
import kotlin.nullable.toList


/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~
 * General purpose utilities.
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~
 */


/**
 * Updates object specified using map of its fields (name => value), converting them to an appropriate type.
 * Returns map of unrecognized fields (fields that don't appear to be object's fields).
 */
public fun updateObject( o : Any, map: Map<String, Any> ): Map<String, Any>
{
    val unrecognizedFields: Map<String, Any> = hashMap()

   /**
    * Mapping of object fields: field name => field instance
    */
    val objectFields = convertToMap<Field, String, Field>( o.javaClass.getFields()!! ){
        field -> #( field.getName()!!, field )
    }

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
                val fieldType                  = field.getType()
                val fieldValueConverted : Any? =
                    if ( fieldType.equals( javaClass<String> ())) fieldValue else
                    if ( fieldType.equals( javaClass<Integer>())) Integer.valueOf( fieldValueS )!! else
                    if ( fieldType.equals( javaClass<Date>   ())) Date( Long.valueOf( fieldValueS )!!.toLong()) else
                    null

                field.set( o, assertNotNull( fieldValueConverted,
                                             "Unknown type [$fieldType] of field [$fieldName] for instance of class [${ o.javaClass.getName() }]" ))
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


/**
 * Verifies none of objects specified is null.
 */
public inline fun verifyNotNull( vararg objects: Any? ): Unit = for ( o in objects ){ assertNotNull( o ) }



/**
 * Converts array to Map by passing each element to an operation and inserting a tuple returned (key, value) into a Map.
 * Types:
 * T? - type of array elements, some elements can be null
 * K  - type of tuple's first element returned by an operation and used as map's key
 * V  - type of tuple's second element returned by an operation and used as map's value
 */
public inline fun <T, K, V> convertToMap( array: Array<T?>, operation: ( T ) -> Tuple2<K, V> ) : Map<K, V>
{
    val map : Map<K, V> = hashMap()

    for ( o in array.filter{ it != null })
    {
        val tuple = operation( o!! )
        map.put( tuple._1, tuple._2 )
    }

    return map
}


/**
 * Converts collection to Map by passing each element to an operation and inserting a tuple returned (key, value) into a Map.
 * Types:
 * T? - type of collection elements, some elements can be null
 * K  - type of tuple's first element returned by an operation and used as map's key
 * V  - type of tuple's second element returned by an operation and used as map's value
 */
public inline fun <T, K, V> convertToMap( collection: Collection<T?>, operation: ( T ) -> Tuple2<K, V> ) : Map<K, V>
{
    val map : Map<K, V> = hashMap()

    for ( o in collection.filter{ it != null })
    {
        val tuple = operation( o!! )
        map.put( tuple._1, tuple._2 )
    }

    return map
}