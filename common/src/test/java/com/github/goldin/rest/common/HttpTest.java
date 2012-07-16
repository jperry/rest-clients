package com.github.goldin.rest.common;

import org.junit.Test;

import static junit.framework.TestCase.*;


/**
 * {@link HTTP} test.
 */
public class HttpTest
{
    private boolean contains( String url, String content )
    {
        return new HTTP().get( url ).contains( content );
    }


    @Test
    public void testHttpGet()
    {
        assertTrue( contains( "http://ya.ru",                "http://yandex.ru/" ));
        assertTrue( contains( "https://twitter.com/hhariri", "Developer, Technical Evangelist at JetBrains" ));
        assertTrue( contains( "http://code.google.com/",     "Project Hosting on Google Code" ));
    }
}
