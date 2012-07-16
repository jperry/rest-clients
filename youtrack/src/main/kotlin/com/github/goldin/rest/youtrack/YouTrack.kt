package com.github.goldin.rest.youtrack

//import com.github.goldin.rest.common.HTTP


class YouTrack ( val url : String )
{
//    final val http : HTTP = HTTP()


    /**
     * Checks that an issue specified exists.
     */
    fun issueExists( issueId: String ): Boolean
    {
        return true
/*
        checkNotNull( issueId, "'issueId' is null" )

        // http://confluence.jetbrains.net/display/YTD4/Check+that+an+Issue+Exists
        val    statusCode = http.getResponse( "${ url }/rest/issue/${ issueId }/exists" )!!.getStatusCode()
        return statusCode == 200
*/
    }
}