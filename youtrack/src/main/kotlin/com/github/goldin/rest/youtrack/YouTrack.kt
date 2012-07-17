package com.github.goldin.rest.youtrack

import com.github.goldin.rest.common.HTTP
import java.util.logging.Logger
import kotlin.test.assertFalse
import java.util.List
import java.util.Collections
import java.io.InputStreamReader
import java.util.Map
import com.google.api.client.json.GenericJson
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.client.json.JsonObjectParser


/**
 * YouTrack REST API wrapper:
 * http://confluence.jetbrains.net/display/YTD4/General+REST+API
 * http://confluence.jetbrains.net/display/YTD4/YouTrack+REST+API+Reference
 */
class YouTrack ( url : String )
{
    private val http       : HTTP       = HTTP()
    private val urlBuilder : UrlBuilder = UrlBuilder( url )

    /**
     * Checks that an issue specified exists.
     * http://confluence.jetbrains.net/display/YTD4/Check+that+an+Issue+Exists
     */
    fun issueExists( issueId: String ): Boolean
    {
        checkNotNull( issueId, "'issueId' is null" )
        return http.statusCode( urlBuilder.issueExists( issueId )) == 200
    }


    /**
     * Retrieves issue specified.
     */
    fun issue( issueId : String ): Issue = issue( issueId, arrayList())


    /**
     * Retrieves issue specified.
     */
    fun issue( issueId : String,
               fields  : List<String>? = null ): Issue
    {
        checkNotNull( issueId, "'issueId' is null" )

        val json  = http.responseAsJson( urlBuilder.issue( issueId ))
        val issue = Issue()
        issue.id  = json.get( "id" ).toString()
        return issue
    }
}