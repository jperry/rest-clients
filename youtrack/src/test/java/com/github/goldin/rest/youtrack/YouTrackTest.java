package com.github.goldin.rest.youtrack;

import org.junit.Test;

import java.util.Arrays;

import static junit.framework.TestCase.*;


/**
 * {@link YouTrack} test.
 */
public class YouTrackTest
{
    private final YouTrack yt = new YouTrack( "http://evgenyg.myjetbrains.com/youtrack/" );


    @Test
    public void testIssueExists()
    {
        assertTrue ( yt.issueExists( "pl-633"  ));
        assertTrue ( yt.issueExists( "pl-121"  ));
        assertFalse( yt.issueExists( "pl-6331" ));
        assertFalse( yt.issueExists( "pl-1211" ));
    }


    @Test
    public void testIssue()
    {
        final Issue issue = yt.issue( "pl-101" );

        assertEquals ( "pl-101", issue.getId());
        assertNotNull( issue.getJiraId());
        assertEquals ( Arrays.asList( "tag1", "tag2" ), issue.getTags());
        assertEquals ( "pl",     issue.getProjectShortName());
        assertEquals ( "101",    issue.getNumberInProject());
        assertEquals ( "<filter>/<process> enhancements",    issue.getSummary());
        assertTrue   ( issue.getDescription().trim().startsWith( "* List<File> instead of Collection<File>" ));
    }
}
