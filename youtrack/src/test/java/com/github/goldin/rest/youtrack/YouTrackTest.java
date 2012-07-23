package com.github.goldin.rest.youtrack;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static junit.framework.TestCase.*;


/**
 * {@link YouTrack} test.
 */
public class YouTrackTest
{

    private final YouTrack yt = new YouTrack( "http://rest-clients.myjetbrains.com/youtrack/" );

    private DateFormat dateFormat;

    /**
     * Initializes {@link DateFormat} instance to format dates into "Mon Jul 19 20:20:38 2010"-like Strings (GMT timezone).
     * http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
     */
    @Before
    public void initDateFormat()
    {
        dateFormat = new SimpleDateFormat( "EEE MMM dd HH:mm:ss yyyy" );
        dateFormat.setTimeZone( TimeZone.getTimeZone( "GMT" ));
    }


    @Test
    public void testIssueExists()
    {
        assertTrue ( yt.issueExists( "pl-101"  ));
        assertTrue ( yt.issueExists( "pl-121"  ));
        assertFalse( yt.issueExists( "pl-6331" ));
        assertFalse( yt.issueExists( "pl-1211" ));
    }


    @Test
    public void testUnresolvedIssue () throws ParseException
    {
        /**
         * http://rest-clients.myjetbrains.com/youtrack/issue/pl-101
         */

        final Issue               issue        = yt.issue( "pl-101" );
        final Map<String, String> customFields = new HashMap<String, String>(){{
           put( "Subsystem",    "copy-maven-plugin" );
           put( "Fix versions", "Pool" );
           put( "State",        "Submitted" );
           put( "Type",         "Task" );
           put( "Assignee",     "evgenyg" );
           put( "Priority",     "Normal" );
        }};

        assertEquals ( "pl-101", issue.getId());
        assertNull   ( issue.getJiraId());
        assertEquals ( Arrays.asList( "tag1", "tag2" ), issue.getTags() );
        assertEquals ( "pl",  issue.getProjectShortName());
        assertEquals ( 101,   ( int ) issue.getNumberInProject() );
        assertEquals ( "<filter>/<process> enhancements",    issue.getSummary());
        assertTrue   ( issue.getDescription().trim().startsWith( "* List<File> instead of Collection<File>" ) );
        assertEquals ( "Mon Jul 19 20:20:38 2010", dateFormat.format( issue.getCreated()));
        assertEquals ( "Mon Jul 23 10:58:18 2012", dateFormat.format( issue.getUpdated()));
        assertEquals ( null, issue.getResolved());
        assertEquals ( "evgenyg",       issue.getUpdaterName());
        assertEquals ( "Evgeny Goldin", issue.getUpdaterFullName());
        assertEquals ( "evgenyg",       issue.getReporterName());
        assertEquals ( "Evgeny Goldin", issue.getReporterFullName());
        assertEquals ( 2,               ( int ) issue.getCommentsCount());
        assertEquals ( 3,               issue.getComments().size());
        assertEquals ( "Comment 1. Update.", issue.getComment( 0 ).getText());
        assertEquals ( "Comment 2",          issue.getComment( 1 ).getText());
        assertEquals ( "Comment 3",          issue.getComment( 2 ).getText());
        assertFalse  ( issue.getComment( 0 ).getDeleted());
        assertTrue   ( issue.getComment( 1 ).getDeleted());
        assertFalse  ( issue.getComment( 2 ).getDeleted());
        assertEquals ( 0,            ( int ) issue.getVotes());
        assertEquals ( customFields, issue.getCustomFields());
        assertEquals ( null,         issue.getPermittedGroup());
    }



    @Test
    public void testResolvedIssue () throws ParseException
    {
        /**
         * http://rest-clients.myjetbrains.com/youtrack/issue/pl-357
         */

        final Issue               issue        = yt.issue( "pl-357" );
        final Map<String, String> customFields = new HashMap<String, String>(){{
           put( "Subsystem",      "jenkins-maven-plugin" );
           put( "Fix versions",   "0.2.4" );
           put( "State",          "Fixed" );
           put( "Type",           "Feature" );
           put( "Assignee",       "evgenyg" );
           put( "Priority",       "Major" );
           put( "Fixed in build", "835" );
        }};

        assertEquals ( "pl-357", issue.getId());
        assertNull   ( issue.getJiraId());
        assertEquals ( 0, issue.getTags().size() );
        assertEquals ( "pl", issue.getProjectShortName() );
        assertEquals ( 357,  ( int ) issue.getNumberInProject() );
        assertEquals ( "Support GitHub plugin", issue.getSummary() );
        assertEquals ( "This is description.", issue.getDescription() );
        assertEquals ( "Sat Mar 05 19:16:56 2011", dateFormat.format( issue.getCreated() ) );
        assertEquals ( "Mon Jul 23 16:26:16 2012", dateFormat.format( issue.getUpdated() ) );
        assertEquals ( "Sun Apr 29 16:22:07 2012", dateFormat.format( issue.getResolved() ));
        assertEquals ( "evgenyg",       issue.getUpdaterName());
        assertEquals ( "Evgeny Goldin", issue.getUpdaterFullName());
        assertEquals ( "evgenyg", issue.getReporterName() );
        assertEquals ( "Evgeny Goldin", issue.getReporterFullName() );
        assertEquals ( 1, ( int ) issue.getCommentsCount() );
        assertEquals ( 1, issue.getComments().size() );
        assertTrue   ( issue.getComment( 0 ).getText().startsWith( "If [https://wiki.jenkins-ci.org/display/JENKINS/Github+Plugin Jenkins GitHub plugin] is installed" ) );
        assertFalse  ( issue.getComment( 0 ).getDeleted() );
        assertEquals ( 0,            ( int ) issue.getVotes());
        assertEquals ( customFields, issue.getCustomFields());
        assertEquals ( null,         issue.getPermittedGroup());
    }


    @Test
    public void testRetrieveIssues()
    {
        /**
         * http://rest-clients.myjetbrains.com/youtrack/issues/pl?q=sort+by%3A+%7Bissue+id%7D+asc+
         */

        int issuesFound = 0;

        for ( int j = 10; j <= 50; j++ )
        {
            String issueId = "pl-" + j;
            if ( yt.issueExists( issueId ))
            {
                final Issue issue = yt.issue( issueId );
                assertEquals( issueId,   issue.getId());
                assertEquals( "pl",      issue.getProjectShortName());
                assertEquals( j,         ( int ) issue.getNumberInProject());
                assertEquals( "evgenyg", issue.getCustomFields().get( "Assignee" ));

                if ( issue.getResolved() != null )
                {
                    assertTrue   ( new Date().getTime() > issue.getResolved().getTime());
                    assertNotNull( issue.getCustomFields().get( "Fixed in build" ));
                    assertTrue   ( Integer.valueOf(( String ) issue.getCustomFields().get( "Fixed in build" )) > 0 );
                }

                issuesFound++;
            }
        }

        assertEquals( 14, issuesFound );
    }


    @Test( expected = IssueNotFoundException.class )
    public void testNonExistingIssue()
    {
        yt.issue( "pl-11111" );
    }


    @Test( expected = CommentNotFoundException.class )
    public void testNonExistingComment()
    {
        yt.issue( "pl-101" ).getComment( 3 );
    }
}
