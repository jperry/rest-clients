package com.github.goldin.rest.youtrack

import com.google.api.client.util.ArrayMap
import com.google.api.client.util.Key
import java.util.Date
import java.util.List
import java.util.Map
import kotlin.test.assertNotNull
import com.github.goldin.rest.common.verifyNotNull
import com.github.goldin.rest.common.updateObject
import com.github.goldin.rest.common.convertToMap


/**
 * YouTrack issue instance.
 * Created and partially initialized by Json parser when reading Json response, later updated by [[update]].
 * Fields annotated with [Key] duplicate Json fields.
 */
class Issue
{
    [Key] public var id               : String           = ""
    [Key] public var jiraId           : Any              = Object()

    [Key] private var tag             : Array<ArrayMap<String, String>> = array()
    [Key] private var field           : Array<ArrayMap<String, Any>?>   = array()
    [Key] private var comment         : Array<ArrayMap<String, String>> = array()

          public var tags             : List<String>?     = null
          public var projectShortName : String?           = null
          public var numberInProject  : String?           = null
          public var summary          : String?           = null
          public var description      : String?           = null
          public var created          : Date?             = null
          public var updated          : Date?             = null
          public var resolved         : Date?             = null
          public var updaterName      : String?           = null
          public var updaterFullName  : String?           = null
          public var reporterName     : String?           = null
          public var reporterFullName : String?           = null
          public var commentsCount    : Integer?          = null
          public var comments         : List<Comment>?    = null
          public var votes            : Integer?          = null
          public var customFields     : Map<String, Any>? = null
          public var permittedGroup   : String?           = null


    /**
     * Updates an instance by converting Json-based issue representation to bean properties.
     */
    fun update(): Issue
    {
        /**
         * Properties that should be already set by Json parser.
         */
        verifyNotNull( id, tag, field, comment )

        /**
         * Reading tags from array of maps (every map has a single "value" entry).
         */
        tags = tag.map{ it.get( "value" )!! }

        /**
         * Converting array of maps (every map has two entries: field's "name" and "value") to map of fields: name => value.
         */
        val fieldsMap = convertToMap<ArrayMap<String, Any>, String, Any>( field ) {
            map -> #( map.get( "name"  )!! as String, map.get( "value" )!! )
        }

        /**
         * Updating object fields and getting back a map of unrecognized fields.
         */
        customFields = updateObject( this, fieldsMap )

        return this
    }
}
