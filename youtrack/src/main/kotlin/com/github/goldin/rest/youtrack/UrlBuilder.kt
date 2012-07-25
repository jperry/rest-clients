package com.github.goldin.rest.youtrack

import kotlin.test.assertFalse
import java.util.Map
import java.net.URLEncoder
import java.util.Collection
import java.util.List


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


    /**
     * [[URLEncoder.encode()]] wrapper using "UTF-8" charset.
     */
    private fun encode( s : String ): String = URLEncoder.encode( s, "UTF-8" )!!


    /**
     * Builds a URL (url?a=b&c=d) from tuples of arguments ( #(a,b), #(c,d), .. ).
     */
    fun url( url: String, arguments: Collection<Tuple2<String, String>> ): String = url + '?' + arguments.map {
        tuple -> "${ encode( tuple._1 ) }=${ encode( tuple._2 ) }"
    }.
    makeString( "&" )
}
