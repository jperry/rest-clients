package com.github.goldin.rest.youtrack

import java.util.Map
import java.util.HashMap
import com.google.api.client.json.GenericJson


/**
 * YouTrack issue.
 * http://confluence.jetbrains.net/display/YTD4/Get+an+Issue
 * http://evgenyg.myjetbrains.com/youtrack/rest/issue/pl-101/
 */
class Issue
{
    public var id               : String = ""
    public var jiraId           : String = ""
    public var tag              : String = ""
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
}