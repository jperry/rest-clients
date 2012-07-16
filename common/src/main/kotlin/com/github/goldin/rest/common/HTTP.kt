package com.github.goldin.rest.common

import com.google.api.client.http.HttpResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.GenericUrl


fun get( url : String ): String?
{
    val request  = NetHttpTransport().createRequestFactory()?.buildGetRequest( GenericUrl( url ))
    val response = request?.execute()
    return response?.parseAsString()
}


fun main( args : Array<String> )
{
    println( get( "https://twitter.com/hhariri" ))
    println( get( "http://code.google.com/p/google-http-java-client/" ))
    println( get( "http://code.google.com/" ))
}
