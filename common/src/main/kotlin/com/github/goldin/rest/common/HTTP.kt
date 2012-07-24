package com.github.goldin.rest.common

import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpContent
import com.google.api.client.http.HttpHeaders
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.GenericJson
import com.google.api.client.json.JsonObjectParser
import com.google.api.client.json.jackson.JacksonFactory
import com.google.api.client.util.ObjectParser
import java.util.Map
import kotlin.test.assertTrue
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.test.assertNotNull
import com.google.api.client.json.Json


class HTTP
{
    class object
    {
        private val factory : HttpRequestFactory = NetHttpTransport().createRequestFactory()!!
        private val log     : Logger             = LoggerFactory.getLogger( javaClass<HTTP>())!!
    }


    /**
     * General request invoked - invokes an HTTP request and returns the corresponding response.
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

        assertNotNull( title, "HTTP.request(): one of isHead/isGet/isPost needs to be specified" )

        val t        = System.currentTimeMillis()
        val request  = if ( isHead ) factory.buildHeadRequest( GenericUrl( url ))!! else
                       if ( isGet  ) factory.buildGetRequest ( GenericUrl( url ))!! else
                       if ( isPost ) factory.buildPostRequest( GenericUrl( url ), content!! )!! else
                                     null!!

        if ( headers != null )
        {
            val httpHeaders = HttpHeaders()
            for ( entry : Map.Entry<String, String>? in headers.entrySet())
            {
                httpHeaders.set( entry!!.getKey(), entry.getValue())
            }
            request.setHeaders( httpHeaders )
        }

        if ( isPost )
        {

            request.setContent( assertNotNull( content, "Content needs to be specified for POST request" ))
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


    /**
     * Sends a HEAD request and returns a corresponding [[HttpResponse]].
     */
    fun headRequest ( url : String ) : HttpResponse = request( url = url, isHead = true )

    /**
     * Sends a GET request and returns a corresponding [[HttpResponse]].
     */
    fun getRequest ( url : String ) : HttpResponse = request( url = url, isGet  = true )

    /**
     * Sends a POST request and returns a corresponding [[HttpResponse]].
     */
    fun postRequest ( url     : String,
                      content : HttpContent ): HttpResponse = request( url = url, isPost = true, content = content )
    /**
     * Retrieves a status code of sending a HEAD request.
     */
    fun statusCode ( url : String ) : Int = headRequest( url ).getStatusCode()

    /**
     * Retrieves [[String]] response of sending a GET request.
     */
    fun responseAsString ( url : String ) : String = getRequest ( url ).parseAsString()!!

    /**
     * Retrieves [[GenericJson]] response of sending a GET request.
     */
    fun responseAsJson ( url : String ) : GenericJson = responseAsJson( url, javaClass<GenericJson>())

    /**
     * Retrieves [[T]] response of sending a GET request.
     */
    fun responseAsJson<T> ( url : String, rtype : Class<T> ) : T = request( isGet   = true,
                                                                            url     = url,
                                                                            headers = hashMap( #( "Accept", Json.CONTENT_TYPE )),
                                                                            parser  = JsonObjectParser( JacksonFactory())).
                                                                   parseAs( rtype )!! as T
}
