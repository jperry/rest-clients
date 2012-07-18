package com.github.goldin.rest.youtrack

import kotlin.test.assertFalse


/**
 * Builds YouTrack REST URLs.
 */
class UrlBuilder( url : String )
{
    private val url : String = if ( url.endsWith( "/" )) url.substring( 0, url.length() - 1 )
                               else                      url
    {
        assertFalse( this.url.trim().isEmpty() || this.url.endsWith( "/" ))
    }


    /**
     * http://confluence.jetbrains.net/display/YTD4/Get+an+Issue
     */
    fun issue ( issueId : String ): String
    {
        checkNotNull( issueId, "'issueId' can't be null" )
        return "$url/rest/issue/$issueId"
    }


    /**
     * http://confluence.jetbrains.net/display/YTD4/Check+that+an+Issue+Exists
     */
    fun issueExists( issueId : String ): String
    {
        checkNotNull( issueId, "'issueId' can't be null" )
        return "${ issue( issueId ) }/exists"
    }
}
