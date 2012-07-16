package com.github.goldin.rest.common

import com.google.api.client.http.HttpResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpResponseException
import com.google.api.client.http.LowLevelHttpResponse


class HTTP
{
    final val factory : HttpRequestFactory? = NetHttpTransport().createRequestFactory()


    fun getResponse( url : String ): HttpResponse?
    {
        val    request  = factory!!.buildGetRequest( GenericUrl( url ))!!
        val    response = request.setThrowExceptionOnExecuteError( false )!!.execute()
        return response
    }


    fun getResponseAsString( url : String ): String?
    {
        return getResponse( url )!!.parseAsString()
    }
}
