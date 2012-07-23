package com.github.goldin.rest.youtrack


/**
 * Throws when issue requested doesn't exist.
 */
class IssueNotFoundException  ( issueId : String ) : RuntimeException( "Issue \"$issueId\" was not found." )


/**
 * Thrown when comment requested doesn't exist.
 */
class CommentNotFoundException( issueId : String, commentIndex : Int ) : RuntimeException( "Issue \"$issueId\" has no comment [$commentIndex]." )
