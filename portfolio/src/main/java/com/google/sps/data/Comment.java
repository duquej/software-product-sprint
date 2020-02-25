package com.google.sps.data;

import java.util.Date;

/**
 *  Class representing a comment.
 */
public class Comment {

  /*The comment left. */
  private String comment;

  /*The name of who left the comment. */
  private String commenter;
 
  /*The time and date when the comment was submitted.*/
  private Date timeSubmitted;

  /** 
   *  Creates a new comment.
   *  @param comment the comment left by the user.
   *  @param commenter the user who left the comment.
   *  @param timeSubmitted the time when a comment was submitted. 
   */
  public Comment(String comment, String commenter, Date timeSubmitted){
    this.comment = comment;
    this.commenter = commenter;
    this.timeSubmitted = timeSubmitted;  
  }
  
  /** Returns the comment of this object.
   * @return the comment submitted
   */
  public String getComment(){
    return comment;
  }


}
