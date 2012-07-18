package com.github.goldin.rest.youtrack

import com.github.goldin.rest.common.Reflection
import com.google.api.client.util.ArrayMap
import com.google.api.client.util.Key
import java.util.Date
import java.util.List
import java.util.Map
import kotlin.test.assertNotNull
import com.github.goldin.rest.common.verifyNotNull


/**
 * YouTrack issue instance.
 * Created and partially initialized by Json parser when reading Json response, later updated by [[convert]].
 */
class Issue
{
    [Key] public var id               : String?           = null
    [Key] public var jiraId           : Any?              = null
          public var tags             : List<String>?     = null
          public var projectShortName : String?           = null
          public var numberInProject  : String?           = null
          public var summary          : String?           = null
          public var description      : String?           = null
          public var created          : Date?             = null
          public var updated          : Date?             = null
          public var updaterName      : String?           = null
          public var updaterFullName  : String?           = null
          public var reporterName     : String?           = null
          public var reporterFullName : String?           = null
          public var commentsCount    : Integer?          = null
          public var votes            : Integer?          = null
          public var customFields     : Map<String, Any>? = null

    [Key] private var tag        : Array<ArrayMap<String, String>> = array()
    [Key] private var field      : Array<ArrayMap<String, Any>>    = array()
    [Key] private var comment    : Array<ArrayMap<String, String>> = array()

/*
    public var resolved         : String = ""
    public var permittedGroup   : String = ""
    public var comment          : String = ""
    public var fields           : Map<String, String> = hashMap()
*/

    /**
     * Updates an instance by converting Json-based issue representation to bean properties.
     */
    fun update(): Issue
    {
        /**
         * Properties that are already set by Json parser.
         */
        verifyNotNull( id, tag, field, comment )

        /**
         * Adding all tags from array of maps (every map has a single "value" entry).
         */
        tags = arrayList()
        tags!!.addAll( tag.map{ it.get( "value" )!! })

        /**
         * Converting array of maps (every map has two entries: field's "name" and "value") to map of fields: name => value.
         */
        val fieldsMap = hashMap<String, Any>()
        field.forEach{ fieldsMap.put( it.get( "name"  )!! as String, it.get( "value" )!! )}

        /**
         * Updating object fields and getting back a map of unrecognized fields.
         */
        customFields = Reflection().updateObject( this, fieldsMap )

        return this
    }
}
