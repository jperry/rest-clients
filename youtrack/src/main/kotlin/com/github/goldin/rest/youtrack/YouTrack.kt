package com.github.goldin.rest.youtrack

import com.github.goldin.rest.common.HTTP
import java.util.logging.Logger
import kotlin.test.assertFalse
import java.util.List
import java.util.Collections


/**
 * YouTrack REST API wrapper:
 * http://confluence.jetbrains.net/display/YTD4/General+REST+API
 * http://confluence.jetbrains.net/display/YTD4/YouTrack+REST+API+Reference
 */
class YouTrack ( val url : String )
{
    private val http : HTTP = HTTP()

    /**
     * Checks that an issue specified exists.
     * http://confluence.jetbrains.net/display/YTD4/Check+that+an+Issue+Exists
     */
    fun issueExists( issueId: String ): Boolean
    {
        checkNotNull( issueId, "'issueId' is null" )
        assertFalse ( url.endsWith( "/" ))

        return http.statusCode( "${ url }/rest/issue/${ issueId }/exists" ) == 200
    }


    /**
     * Retrieves issue specified.
     */
    fun issue( issueId : String,
               fields  : List<String> = Collections.emptyList<String>()!! ): Issue
    {
        checkNotNull( issueId, "'issueId' is null" )
        assertFalse ( url.endsWith( "/" ))

        return Issue()
    }
}