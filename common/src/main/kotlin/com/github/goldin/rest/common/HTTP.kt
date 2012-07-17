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


class HTTP
{
    final val factory : HttpRequestFactory = NetHttpTransport().createRequestFactory()!!
    final val log     : Logger             = LoggerFactory.getLogger( "HTTP" )!!


    /**
     * Invokes GET or HEAD request and return response.
     */
    private fun response( url : String, isGet : Boolean = true ): HttpResponse
    {
        val title = if ( isGet ) "GET" else "HEAD"

        if ( log.isDebugEnabled())
        {
            log.debug( "$title: [${ url }]" )
        }

        val t        = System.currentTimeMillis()
        val request  = if ( isGet ) factory.buildGetRequest ( GenericUrl( url ))!!
                       else         factory.buildHeadRequest( GenericUrl( url ))!!

        val response = request.setThrowExceptionOnExecuteError( false )!!.execute()!!

        if ( log.isDebugEnabled())
        {
            log.debug( "$title: [${ url }] - [${ response.getStatusCode()}] (${ System.currentTimeMillis() - t } ms)" )
        }

        return response
    }


    fun headRequest     ( url : String ): HttpResponse = response( url = url, isGet = false )
    fun getRequest      ( url : String ): HttpResponse = response( url = url, isGet = true  )
    fun statusCode      ( url : String ): Int          = headRequest( url ).getStatusCode()
    fun responseAsString( url : String ): String       = getRequest ( url ).parseAsString()!!
}
