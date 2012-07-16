package com.github.goldin.rest.common

import com.google.api.client.http.HttpResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpResponseException
import com.google.api.client.http.LowLevelHttpResponse
import java.util.logging.Logger
import java.util.logging.Level


class HTTP
{
    final val factory : HttpRequestFactory = NetHttpTransport().createRequestFactory()!!
    final val log     : Logger             = Logger.getLogger( "HTTP" )!!;


    fun getResponse( url : String ): HttpResponse
    {
        log.info( "Http request: [${ url }]" )
        val t        = System.currentTimeMillis()
        val request  = factory.buildGetRequest( GenericUrl( url ))!!
        val response = request.setThrowExceptionOnExecuteError( false )!!.execute()!!

        log.info( "Http request: [${ url }] - [${ response.getStatusCode()}] (${ System.currentTimeMillis() - t } ms)" )
        return response
    }


    fun getResponseAsString( url : String ): String
    {
        return getResponse( url ).parseAsString()!!
    }
}
