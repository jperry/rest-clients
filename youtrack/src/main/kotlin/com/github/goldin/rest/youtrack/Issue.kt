package com.github.goldin.rest.youtrack

import java.util.Map
import java.util.HashMap
import com.google.api.client.json.GenericJson
import java.util.List
import com.google.api.client.util.Key
import com.google.api.client.util.ArrayMap


/**
* YouTrack issue.
* http://confluence.jetbrains.net/display/YTD4/Get+an+Issue
*/
class Issue
{
    [Key] public  var id         : String                          = ""
    [Key] public  var jiraId     : Any?                            = null
    [Key] private var tag        : Array<ArrayMap<String, String>> = array()
    [Key] private var field      : Array<ArrayMap<String, String>> = array()
    [Key] private var comment    : Array<ArrayMap<String, String>> = array()
/*
    public var projectShortName : String = ""
    public var numberInProject  : String = ""
    public var summary          : String = ""
    public var description      : String = ""
    public var created          : String = ""
    public var updated          : String = ""
    public var updaterName      : String = ""
    public var updaterFullName  : String = ""
    public var reporterName     : String = ""
    public var reporterFullName : String = ""
    public var resolved         : String = ""
    public var votes            : String = ""
    public var commentsCount    : String = ""
    public var permittedGroup   : String = ""
    public var comment          : String = ""
    public var fields           : Map<String, String> = hashMap()
*/

    /**
     * Writer Json-based issue representation to bean properties.
     */
    fun update(): Issue
    {
        return this
    }
}
