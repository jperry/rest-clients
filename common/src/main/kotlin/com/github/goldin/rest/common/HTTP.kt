package com.github.goldin.rest.common

import com.google.api.client.http.HttpResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.GenericUrl

class HTTP
{
    fun get( url : String ): String?
    {
        val request  = NetHttpTransport().createRequestFactory()?.buildGetRequest( GenericUrl( url ))
        val response = request?.execute()
        return response?.parseAsString()
    }
}
