package com.github.goldin.rest.youtrack

import com.github.goldin.rest.common.HTTP
import java.util.List
import kotlin.test.assertTrue


/**
 * YouTrack REST API wrapper:
 * http://confluence.jetbrains.net/display/YTD4/General+REST+API
 * http://confluence.jetbrains.net/display/YTD4/YouTrack+REST+API+Reference
 */
class YouTrack ( url : String )
{
    class object
    {
        private val http : HTTP = HTTP()
    }

    private val urlBuilder : UrlBuilder = UrlBuilder( url )

    /**
     * Checks that an issue specified exists.
     * http://confluence.jetbrains.net/display/YTD4/Check+that+an+Issue+Exists
     */
    fun issueExists( issueId: String ): Boolean = http.statusCode( urlBuilder.issueExists( issueId )) == 200


    /**
     * Retrieves issue specified.
     * http://confluence.jetbrains.net/display/YTD4/Get+an+Issue
     */
    fun issue( issueId : String ): Issue = issue( issueId, arrayList())


    /**
     * Retrieves issue specified.
     * http://confluence.jetbrains.net/display/YTD4/Get+an+Issue
     */
    fun issue( issueId : String,
               fields  : List<String> = arrayList()): Issue
    {
        if ( ! issueExists( issueId )){ throw IssueNotFoundException( issueId ) }

        return http.responseAsJson( urlBuilder.issue( issueId ), javaClass<Issue>()).
               update( issueId )
    }
}