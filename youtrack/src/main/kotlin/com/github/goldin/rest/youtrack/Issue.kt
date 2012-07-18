package com.github.goldin.rest.youtrack

import java.util.Map
import java.util.HashMap
import com.google.api.client.json.GenericJson
import java.util.List
import com.google.api.client.util.Key
import com.google.api.client.util.ArrayMap
import java.util.Date
import com.github.goldin.rest.common.Reflection


/**
* YouTrack issue.
* http://confluence.jetbrains.net/display/YTD4/Get+an+Issue
*/
class Issue
{
    [Key] public var id               : String       = ""
    [Key] public var jiraId           : Any?         = null
          public val tags             : List<String> = arrayList()
          public var projectShortName : String       = ""
          public var numberInProject  : String       = ""
          public var summary          : String       = ""
          public var description      : String       = ""
          public var created          : Date         = Date()
          public var updated          : Date         = Date()
          public var updaterName      : String = ""
          public var updaterFullName  : String = ""
          public var reporterName     : String = ""
          public var reporterFullName : String = ""
          public var commentsCount    : Int    = -1
          public var votes            : Int    = -1

    [Key] private var tag        : Array<ArrayMap<String, String>> = array()
    [Key] private var field      : Array<ArrayMap<String, String>> = array()
    [Key] private var comment    : Array<ArrayMap<String, String>> = array()

/*
    public var resolved         : String = ""
    public var permittedGroup   : String = ""
    public var comment          : String = ""
    public var fields           : Map<String, String> = hashMap()
*/

    /**
     * Writer Json-based issue representation to bean properties.
     */
    fun update(): Issue
    {
        /**
         * Adding all tags from array of maps (every map has a single "value" entry).
         */
        tags.addAll( tag.map{ it.get( "value" )!! })

        /**
         * Converting array of maps (every map has two entries: field's "name" and "value") to map of fields: name => value.
         */
        val fieldsMap = hashMap<String, String>()
        field.forEach{ fieldsMap.put( it.get( "name" ), it.get( "value" )) }

        Reflection.updateObject( this, fieldsMap )

        return this
    }
}
