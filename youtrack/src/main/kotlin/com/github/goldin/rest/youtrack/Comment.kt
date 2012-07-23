package com.github.goldin.rest.youtrack

import com.google.api.client.util.Key
import java.util.Date


/**
 * YouTrack comment.
 */
class Comment
{
    [Key] public var id       : String?  = null
    [Key] public var issueId  : String?  = null
    [Key] public var jiraId   : String?  = null
    [Key] public var parentId : String?  = null
    [Key] public var author   : String?  = null
    [Key] public var deleted  : Boolean? = null
    [Key] public var created  : Date?    = null
    [Key] public var updated  : Date?    = null
    [Key] public var text     : String?  = null
}