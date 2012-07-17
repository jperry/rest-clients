package com.github.goldin.rest.common

import com.google.api.client.http.HttpResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpResponseException
import com.google.api.client.http.LowLevelHttpResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Map
import java.util.Collections
import com.google.api.client.http.HttpContent
import com.google.api.client.http.HttpHeaders
import kotlin.nullable.all
import kotlin.test.assertFalse
import com.google.api.client.util.ObjectParser
import kotlin.test.assertTrue
import com.google.api.client.json.GenericJson
import com.google.api.client.json.JsonObjectParser
import com.google.api.client.json.jackson.JacksonFactory


class HTTP
{
    final val factory : HttpRequestFactory = NetHttpTransport().createRequestFactory()!!
    final val log     : Logger             = LoggerFactory.getLogger( "HTTP" )!!


    /**
     * Invokes GET or HEAD request and return response.
     */
    fun request( url     : String,
                 isHead  : Boolean              = false,
                 isGet   : Boolean              = false,
                 isPost  : Boolean              = false,
                 headers : Map<String, String>? = null,
                 content : HttpContent?         = null,
                 parser  : ObjectParser?        = null ): HttpResponse
    {
        val title = if ( isHead ) "HEAD" else
                    if ( isGet  ) "GET"  else
                    if ( isPost ) "POST" else
                                  null

        assertFalse( title == null, "HTTP.request(): one of isHead/isGet/isPost needs to be specified" )

        if ( log.isDebugEnabled())
        {
            log.debug( "${ title!! }: [${ url }]" )
        }

        val t        = System.currentTimeMillis()
        val request  = if ( isHead ) factory.buildHeadRequest( GenericUrl( url ))!! else
                       if ( isGet  ) factory.buildGetRequest ( GenericUrl( url ))!! else
                       if ( isPost ) factory.buildPostRequest( GenericUrl( url ), content!! )!! else
                                     null!!

        if ( headers != null )
        {
            val httpHeaders = HttpHeaders()
            for ( entry in headers.entrySet())
            {
                httpHeaders.set( entry!!.getKey(), entry.getValue())
            }
            request.setHeaders( httpHeaders )
        }

        if ( isPost )
        {
            assertTrue( content != null, "Content needs to be specified for POST request" )
            request.setContent( content!! )
        }

        if ( parser != null )
        {
            request.setParser( parser )
        }

        val response = request.setThrowExceptionOnExecuteError( false )!!.execute()!!

        if ( log.isDebugEnabled())
        {
            log.debug( "${ title!! }: [${ url }] - [${ response.getStatusCode()}] (${ System.currentTimeMillis() - t } ms)" )
        }

        return response
    }


    fun headRequest     ( url     : String )     : HttpResponse = request( url = url, isHead = true )
    fun getRequest      ( url     : String )     : HttpResponse = request( url = url, isGet  = true )
    fun postRequest     ( url     : String,
                          content : HttpContent ): HttpResponse = request( url = url, isPost = true, content = content )
    fun statusCode      ( url     : String )     : Int          = headRequest( url ).getStatusCode()
    fun responseAsString( url     : String )     : String       = getRequest ( url ).parseAsString()!!
    fun responseAsJson  ( url     : String )     : GenericJson  = request( isGet   = true,
                                                                           url     = url,
                                                                           headers = hashMap( #( "Accept", "application/json" )),
                                                                           parser  = JsonObjectParser( JacksonFactory())).
                                                                  parseAs( javaClass<GenericJson>())!!
}
