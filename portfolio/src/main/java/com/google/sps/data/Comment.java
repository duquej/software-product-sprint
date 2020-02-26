package com.google.sps.data;

import java.util.Date;

/**
 * Class representing a comment.
 */
public class Comment {

  /* The comment left. */
  private String comment;

  /* The name of who left the comment. */
  private String commenter;
 
  /* The time and date when the comment was submitted. */
  private String timeSubmitted;

  /** 
   * Creates a new comment.
   * @param comment the comment left by the user.
   * @param commenter the user who left the comment.
   * @param timeSubmitted the time when a comment was submitted. 
   */
  public Comment(String comment, String commenter, String timeSubmitted){
    this.comment = comment;
    this.commenter = commenter;
    this.timeSubmitted = timeSubmitted;  
  }
  
  /**
   * Returns the comment left by the user.
   */
  public String getComment(){
    return comment;
  }
  
  /** 
   * Returns the commenter of the comment.
   */
  public String getCommenter(){
    return commenter;
  }

  /** 
   * Returns the date/time that the comment was submitted.
   */
  public String getTimeSubmitted(){
    return timeSubmitted;
  }

  /** 
   * Sets the comment to {@code comment}.
   * @param comment the comment that the user left.
   */
  public void setComment(String comment){
    this.comment = comment;
  }
   
  /** 
   * Sets the commenter to {@code commenter}.
   * @param commenter the name of the person who left the comment.
   */
  public void setCommenter(String commenter){
    this.commenter = commenter; 
  }

  /** 
   * Sets the time submitted to [timeSubmitted].
   * @param commenter the time/date that the comment was left.
   */
  public void setTimeSubmitted(String timeSubmitted){
    this.timeSubmitted = timeSubmitted;
  }




}
