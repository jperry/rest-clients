package com.github.goldin.rest.common;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;


/**
 * {@link HTTP} test.
 */
public class HttpTest
{
    private boolean contains( String url, String content )
    {
        return new HTTP().getResponseAsString( url ).contains( content );
    }


    private boolean isStatusCode( String url, int statusCode )
    {
        return ( new HTTP().getResponse( url ).getStatusCode() == statusCode );
    }


    @Test
    public void testHttpGet()
    {
        assertTrue( contains( "http://ya.ru",                "http://yandex.ru/" ));
        assertTrue( contains( "https://twitter.com/hhariri", "Developer, Technical Evangelist at JetBrains" ));
        assertTrue( contains( "http://code.google.com/",     "Project Hosting on Google Code" ));
    }


    @Test
    public void testHttpResponse()
    {
        assertTrue( isStatusCode( "http://ya.ru",                  200 ));
        assertTrue( isStatusCode( "http://code.google.com/",       200 ));
        assertTrue( isStatusCode( "http://www.jetbrains.com/aaaa", 404 ));
        assertTrue( isStatusCode( "http://goo.gl/api/shorten",     405 ));
    }
}
