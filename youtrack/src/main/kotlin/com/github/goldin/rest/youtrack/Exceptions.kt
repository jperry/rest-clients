package com.github.goldin.rest.youtrack


class IssueNotFoundException( issueId : String ) : RuntimeException( "Issue \"$issueId\" was not found." )
