package com.github.goldin.rest.youtrack

import kotlin.test.assertFalse
import kotlin.test.assertTrue


/**
 * Builds YouTrack REST URLs.
 */
class UrlBuilder( url : String )
{
    private val url : String = if ( url.endsWith( "/" )) url.substring( 0, url.length() - 1 )
                               else                      url
    {
        assertFalse( url.trim().isEmpty() || url.endsWith( "/" ))
    }

    fun issue      ( issueId : String ) = "$url/rest/issue/$issueId"
    fun issueExists( issueId : String ) = "${ issue( issueId ) }/exists"
}
