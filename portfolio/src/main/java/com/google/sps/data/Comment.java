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
  private String formattedTimeSubmitted;

  /* Millisecond time representation when the comment was submitted. */
  private long timeSubmitted;

  /* The email address associated with the user who submitted the comment. */
  private String email;

  /** 
   * Creates a new comment.
   * @param comment the comment left by the user.
   * @param commenter the user who left the comment.
   * @param timeSubmitted the time when a comment was submitted.
   * @param sysTime the time in milliseconds when the comment was submitted.
   * @param email the email address of the user who submitted the comment. 
   */
  public Comment(String comment, String commenter, String timeSubmitted, long sysTime, String email){
    this.comment = comment;
    this.commenter = commenter;
    this.formattedTimeSubmitted = timeSubmitted;  
    this.timeSubmitted = sysTime;
    this.email = email;
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
  public String getFormattedTimeSubmitted(){
    return formattedTimeSubmitted;
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
  public void setFormattedTimeSubmitted(String formattedTimeSubmitted){
    this.formattedTimeSubmitted = formattedTimeSubmitted;
  }

  /**
   * Sets the email the comment is associated with.
   */
  public void setEmail(String email){
    this.email = email;
  }

  /**
   * Gets the email the comment is associated with.
   */
  public String getEmail(){
    return email;
  }

  /** 
   * Gets the time in milliseconds when the comment was submitted. 
   */
  public long getTimeSubmitted(){
    return timeSubmitted;
  }

  /**
   * Sets the time in milliseconds of the comment.
   */
  public void setTimeSubmitted(long timeSubmitted){
    this.timeSubmitted = timeSubmitted;
  }




}
