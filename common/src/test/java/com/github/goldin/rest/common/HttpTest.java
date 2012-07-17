package com.github.goldin.rest.common;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

import static junit.framework.TestCase.assertTrue;


/**
 * {@link HTTP} tests.
 */
public class HttpTest
{
    private final HTTP http = new HTTP();


    @Test
    public void testHttpHead() throws IOException
    {
        assertTrue( http.headRequest( "http://ya.ru"                                       ).parseAsString().isEmpty());
        assertTrue( http.headRequest( "http://www.jetbrains.com"                           ).parseAsString().isEmpty());
        assertTrue( http.headRequest( "http://www.this-page-intentionally-left-blank.org/" ).parseAsString().isEmpty());
    }


    @Test
    public void testHttpHeadWithException() throws IOException
    {
        for ( String url : Arrays.asList( "http://google.com", "http://code.google.com/", "https://twitter.com/hhariri" ))
        {
            try
            {
                http.headRequest( url ).parseAsString();
                assertTrue( "Expected to throw EOFException for [" + url + "]", false );
            }
            catch ( EOFException expected ){}
        }
    }


    @Test
    public void testHttpGet()
    {
        assertTrue( http.responseAsString( "http://ya.ru"                       ).contains( "http://yandex.ru/" ));
        assertTrue( http.responseAsString( "https://twitter.com/hhariri"        ).contains( "Developer, Technical Evangelist at JetBrains" ));
        assertTrue( http.responseAsString( "http://code.google.com/"            ).contains( "Project Hosting on Google Code" ));
        assertTrue( http.responseAsString( "http://www.jetbrains.com/teamcity/" ).contains( "continuous integration (CI) server" ));
    }


    @Test
    public void testHttpHeadStatusCode()
    {
        assertTrue( http.headRequest ( "http://ya.ru"                  ).getStatusCode() == 200 );
        assertTrue( http.headRequest ( "http://code.google.com/"       ).getStatusCode() == 200 );
        assertTrue( http.headRequest ( "http://www.jetbrains.com/aaaa" ).getStatusCode() == 404 );
        assertTrue( http.headRequest ( "http://goo.gl/api/shorten"     ).getStatusCode() == 405 );
    }


    @Test
    public void testHttpGetStatusCode()
    {
        assertTrue( http.getRequest( "http://ya.ru"                  ).getStatusCode() == 200 );
        assertTrue( http.getRequest( "http://code.google.com/"       ).getStatusCode() == 200 );
        assertTrue( http.getRequest( "http://www.jetbrains.com/aaaa" ).getStatusCode() == 404 );
        assertTrue( http.getRequest( "http://goo.gl/api/shorten"     ).getStatusCode() == 405 );
    }


    @Test
    public void testHttpStatusCode()
    {
        assertTrue( http.statusCode( "http://ya.ru"                  ) == 200 );
        assertTrue( http.statusCode( "http://code.google.com/"       ) == 200 );
        assertTrue( http.statusCode( "http://www.jetbrains.com/aaaa" ) == 404 );
        assertTrue( http.statusCode( "http://goo.gl/api/shorten"     ) == 405 );
    }
}
