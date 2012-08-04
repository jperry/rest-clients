package com.github.goldin.rest.youtrack;

import static junit.framework.TestCase.*;
import com.github.goldin.rest.common.BaseTest;
import com.github.goldin.rest.common.HTTP;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.zip.Adler32;
import java.util.zip.CRC32;


/**
 * {@link YouTrack} test.
 */
public class YouTrackTest extends BaseTest
{
    private final YouTrack yt = new YouTrack( restClientsUrl );

    private DateFormat dateFormat;
    private Random     random;


    /**
     * Initializes {@link DateFormat} instance to format dates into "Mon Jul 19 20:20:38 2010"-like Strings (GMT timezone).
     * http://docs.oracle.com/javase/6/docs/api/java/text/SimpleDateFormat.html
     */
    @Before
    public void init()
    {
        dateFormat = dateFormat( "EEE MMM dd HH:mm:ss yyyy", "GMT" );
        random     = random();
    }


    @Test
    @Ignore
    public void testWadl() throws IOException
    {
        final String wadl = new HTTP().responseAsString( new UrlBuilder( jetbrainsUrl ).wadl());
        assertEquals( 1880008272L, checksum( wadl, "UTF-8", new Adler32()));
        assertEquals( 1654558566L, checksum( wadl, "UTF-8", new CRC32()));
        assertEquals( toString( "/application.wadl" ).trim(), wadl.trim());
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
        assertEquals ( Arrays.asList( "tag1", "tag2" ), issue.getTags());
        assertEquals ( "pl", issue.getProjectShortName());
        assertEquals ( 101, ( int ) issue.getNumberInProject());
        assertEquals ( "<filter>/<process> enhancements", issue.getSummary());
        assertTrue   ( issue.getDescription().trim().startsWith( "* List<File> instead of Collection<File>" ));
        assertEquals ( "Mon Jul 19 20:20:38 2010", dateFormat.format( issue.getCreated()));
        assertEquals ( "Mon Jul 23 10:58:18 2012", dateFormat.format( issue.getUpdated()));
        assertEquals ( null, issue.getResolved());
        assertEquals ( "evgenyg", issue.getUpdaterName());
        assertEquals ( "Evgeny Goldin", issue.getUpdaterFullName());
        assertEquals ( "evgenyg", issue.getReporterName());
        assertEquals ( "Evgeny Goldin", issue.getReporterFullName());
        assertEquals ( 2,               ( int ) issue.getCommentsCount());
        assertEquals ( 3,               issue.getComments().size());

        final Comment comment1 = issue.getComment( 0 );
        assertEquals ( "Comment 1. Update.", comment1.getText());
        assertFalse  ( comment1.getDeleted());
        assertEquals ( "Mon Jul 23 10:40:21 2012", dateFormat.format( comment1.getCreatedDate()));
        assertEquals ( "Mon Jul 23 11:28:44 2012", dateFormat.format( comment1.getUpdatedDate()));

        final Comment comment2 = issue.getComment( 1 );
        assertEquals ( "Comment 2", comment2.getText());
        assertTrue   ( comment2.getDeleted());
        assertEquals ( "Mon Jul 23 10:40:26 2012", dateFormat.format( comment2.getCreatedDate()));
        assertEquals ( "Mon Jul 23 11:29:07 2012", dateFormat.format( comment2.getUpdatedDate()));

        final Comment comment3 = issue.getComment( 2 );
        assertEquals ( "Comment 3", comment3.getText());
        assertFalse  ( comment3.getDeleted());
        assertEquals ( "Mon Jul 23 10:58:18 2012", dateFormat.format( comment3.getCreatedDate()));
        assertNull   ( comment3.getUpdatedDate());

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
        assertEquals ( 0, issue.getTags().size());
        assertEquals ( "pl", issue.getProjectShortName());
        assertEquals ( 357,  ( int ) issue.getNumberInProject());
        assertEquals ( "Support GitHub plugin", issue.getSummary());
        assertEquals ( "This is description.", issue.getDescription());
        assertEquals ( "Sat Mar 05 19:16:56 2011", dateFormat.format( issue.getCreated()));
        assertEquals ( "Mon Jul 23 16:26:16 2012", dateFormat.format( issue.getUpdated()));
        assertEquals ( "Sun Apr 29 16:22:07 2012", dateFormat.format( issue.getResolved()));
        assertEquals ( "evgenyg",       issue.getUpdaterName());
        assertEquals ( "Evgeny Goldin", issue.getUpdaterFullName());
        assertEquals ( "evgenyg", issue.getReporterName());
        assertEquals ( "Evgeny Goldin", issue.getReporterFullName());
        assertEquals ( 1, ( int ) issue.getCommentsCount());
        assertEquals ( 1, issue.getComments().size());

        final Comment comment = issue.getComment( 0 );
        assertTrue   ( comment.getText().startsWith( "If [https://wiki.jenkins-ci.org/display/JENKINS/Github+Plugin Jenkins GitHub plugin] is installed" ));
        assertFalse  ( comment.getDeleted());
        assertEquals ( "Sun Apr 29 16:21:59 2012", dateFormat.format( comment.getCreatedDate()));
        assertNull   ( comment.getUpdatedDate());

        assertEquals ( 0,            ( int ) issue.getVotes());
        assertEquals ( customFields, issue.getCustomFields());
        assertEquals ( null,         issue.getPermittedGroup());
    }


    @Test
    public void testRetrieveIssues()
    {
        testRetrieveIssues( restClientsUrl,  "pl",   random.nextInt( 350   ), 10 );
        testRetrieveIssues( evgenyGoldinUrl, "pl",   random.nextInt( 600   ), 10 );
        testRetrieveIssues( evgenyGoldinUrl, "gc",   random.nextInt( 100   ), 10 );
        testRetrieveIssues( jetbrainsUrl,    "JT",   random.nextInt( 15800 ), 10 );
        testRetrieveIssues( jetbrainsUrl,    "IDEA", random.nextInt( 89000 ), 10 );
        testRetrieveIssues( jetbrainsUrl,    "TW",   random.nextInt( 22700 ), 10 );
    }


    /**
     * Reads N issues using the YouTrack instance specified.
     *
     * @param youTrackUrl      YouTrack URL
     * @param projectShortName short name of a project
     * @param issueStart       first issue to read
     * @param numberOfIssues   number of issues to read
     */
    private void testRetrieveIssues( String youTrackUrl, String projectShortName, int issueStart, int numberOfIssues )
    {
        final long     now      = new Date().getTime();
        final YouTrack youTrack = new YouTrack( youTrackUrl );

        for ( int j = issueStart; j <= ( issueStart + numberOfIssues ); j++ )
        {
            String issueId = projectShortName + '-' + j;
            if ( youTrack.issueExists( issueId ))
            {
                final Issue issue;
                try   { issue = youTrack.issue( issueId ); }
                catch ( IssueNotFoundException ignored ) { continue; } // When issue is not visible to the guest user.

                if ( issueId.equals( issue.getId())) // Issue was not moved.
                {
                    assertEquals ( projectShortName, issue.getProjectShortName());
                    assertEquals ( j,                ( int ) issue.getNumberInProject());
                }

                assertFalse( issue.getCustomField( "Type"      ).trim().isEmpty());
                assertFalse( issue.getCustomField( "Subsystem" ).trim().isEmpty());
                assertFalse( issue.getCustomField( "Priority"  ).trim().isEmpty());
                assertFalse( issue.getCustomField( "State"     ).trim().isEmpty());

                if ( issue.getResolved() != null )
                {
                    assertTrue( now > issue.getResolved().getTime());
                }

                assertTrue( issue.getCommentsCount() <= issue.getComments().size());
                for ( int commentIndex = 0; commentIndex < issue.getComments().size(); commentIndex++ )
                {
                    final Comment comment = issue.getComment( commentIndex );
                    assertTrue ( now > comment.getCreatedDate().getTime());
                    assertFalse( comment.getText().trim().isEmpty());
                }
            }
        }
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



    @Test
    public void testPartialIssues()
    {
        final Issue issue1 = yt.partialIssue( "pl-101", Arrays.asList( "id" ));

        assertEquals ( "pl-101", issue1.getId());
        assertNull   ( issue1.getProjectShortName());
        assertNull   ( issue1.getSummary());
        assertNull   ( issue1.getDescription());
        assertTrue   ( issue1.getCustomFields().isEmpty());
        assertNull   ( issue1.getCreated());
        assertNull   ( issue1.getReporterName());

        final Issue issue2 = yt.partialIssue( "pl-101", Arrays.asList( "State", "summary", "description" ));

        assertEquals ( "pl-101", issue1.getId()); // Always returned
        assertEquals ( "Submitted", issue2.getCustomField( "State" ));
        assertEquals ( 1, issue2.getCustomFields().size());
        assertEquals ( "<filter>/<process> enhancements", issue2.getSummary());
        assertTrue   ( issue2.getDescription().trim().startsWith( "* List<File> instead of Collection<File>" ));
        assertNull   ( issue2.getProjectShortName());
        assertNull   ( issue2.getCreated());
        assertNull   ( issue2.getReporterName());
    }
}
