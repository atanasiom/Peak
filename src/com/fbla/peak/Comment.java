package com.fbla.peak;

import java.io.Serializable;

/**
 * This class is a Comment. Comments are the replies to posts (FeedItems). They
 * hold the content, the name of the user who posted it, and the time it was
 * posted.
 * 
 * @author Michael Atanasio
 * 
 */
public class Comment implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -863013713317435026L;
	/**
	 * This Comment's commenter.
	 */
	private String commenter;
	/**
	 * The name of the User who posted this Comment.
	 */
	private String name;
	/**
	 * This Comment's content.
	 */
	private String content;
	/**
	 * This Comment's post time.
	 */
	private String time;

	/**
	 * Creates a new Comment based on the given information.
	 * 
	 * @param commenter
	 *            this Comment's commenter
	 * @param name
	 *            the name of the User who commented
	 * @param content
	 *            this Comment's content
	 * @param time
	 *            the time this Comment was posted
	 */
	public Comment(String commenter, String name, String content, String time) {
		this.commenter = commenter;
		this.name = name;
		this.content = content;
		this.time = time;
	}

	/**
	 * Gets this Comment's commenter.
	 * 
	 * @return this Comment's commenter.
	 */
	public String getCommenter() {
		return commenter;
	}

	/**
	 * Sets this Comment's commenter.
	 * 
	 * @param commenter
	 *            this Comment's new commenter
	 */
	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}

	/**
	 * Gets the name of the poster of this Comment.
	 * 
	 * @return the name of the poster of this Comment
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the poster of this Comment.
	 * 
	 * @param name
	 *            the new name of the poster of this Comment
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets this Comment's content.
	 * 
	 * @return this Comment's new content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets this Comment's content.
	 * 
	 * @param content
	 *            this Comment's content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Gets the time this Comment was posted.
	 * 
	 * @return the time this Comment was posted
	 */
	public String getTime() {
		return time;
	}

	/**
	 * Sets this time at which this Comment was posted.
	 * 
	 * @param time
	 *            the new time this Comment was posted
	 */
	public void setTime(String time) {
		this.time = time;
	}
}