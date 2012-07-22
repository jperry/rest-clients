package com.github.goldin.rest.youtrack;

import static junit.framework.TestCase.*;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.TimeZone;


/**
 * {@link YouTrack} test.
 */
public class YouTrackTest
{
    /**
     * Returns {@link DateFormat} instance formatting dates into "Mon Jul 19 22:20:38 2010".
     * http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
     */
    private static DateFormat dateFormat()
    {
        final DateFormat dateFormat = new SimpleDateFormat( "EEE MMM dd HH:mm:ss yyyy" );
        dateFormat.setTimeZone( TimeZone.getTimeZone( "GMT" ));
        return dateFormat;
    }


    private final YouTrack yt = new YouTrack( "http://rest-clients.myjetbrains.com/youtrack/" );


    @Test
    public void testIssueExists()
    {
        assertTrue ( yt.issueExists( "pl-101"  ));
        assertTrue ( yt.issueExists( "pl-121"  ));
        assertFalse( yt.issueExists( "pl-6331" ));
        assertFalse( yt.issueExists( "pl-1211" ));
    }


    @Test
    public void testExistingIssue() throws ParseException
    {
        final Issue      issue      = yt.issue( "pl-101" );
        final DateFormat dateFormat = dateFormat();

        assertEquals ( "pl-101", issue.getId());
        assertNotNull( issue.getJiraId() );
        assertEquals ( Arrays.asList( "tag1", "tag2" ), issue.getTags() );
        assertEquals ( "pl",     issue.getProjectShortName());
        assertEquals ( "101", issue.getNumberInProject() );
        assertEquals ( "<filter>/<process> enhancements",    issue.getSummary());
        assertTrue   ( issue.getDescription().trim().startsWith( "* List<File> instead of Collection<File>" ) );
        assertEquals ( "Mon Jul 19 20:20:38 2010", dateFormat.format( issue.getCreated()));
        assertEquals ( "Sun Feb 27 22:42:16 2011", dateFormat.format( issue.getUpdated()));
        assertEquals ( null, issue.getResolved());
        assertEquals ( "evgenyg",       issue.getUpdaterName());
        assertEquals ( "Evgeny Goldin", issue.getUpdaterFullName());
        assertEquals ( "evgenyg",       issue.getReporterName());
        assertEquals ( "Evgeny Goldin", issue.getReporterFullName());

    }


    @Test( expected = IssueNotFoundException.class )
    public void testNonExistingIssue()
    {
        yt.issue( "pl-11111" );
    }
}
