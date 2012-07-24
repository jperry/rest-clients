package com.github.goldin.rest.youtrack

import com.google.api.client.util.Key
import java.util.Date
import kotlin.test.assertEquals
import com.github.goldin.rest.common.verifyNotNull
import java.math.BigDecimal
import kotlin.test.assertTrue
import kotlin.test.assertFalse


/**
 * YouTrack comment.
 */
class Comment
{
    [Key] public  var id          : String?     = null
    [Key] public  var issueId     : String?     = null
    [Key] public  var jiraId      : String?     = null
    [Key] public  var parentId    : String?     = null
    [Key] public  var author      : String?     = null
    [Key] public  var deleted     : Boolean?    = null
    [Key] public  var text        : String?     = null

    [Key] private var created     : BigDecimal? = null
          public  var createdDate : Date?       = null

    [Key] private var updated     : BigDecimal? = null
          public  var updatedDate : Date?       = null


    /**
     * Updates an instance by converting Json-based representation to bean properties.
     */
    fun update(): Comment
    {
        verifyNotNull( id, issueId, parentId, author, deleted, text, created, updated )
        assertFalse  ( text!!.trim().isEmpty())
        assertTrue   ( created!!.longValue() > 0 )

        createdDate = Date( created!!.longValue())

        if ( updated!!.longValue() > 0 )
        {
            updatedDate = Date( updated!!.longValue())
        }

        return this
    }
}