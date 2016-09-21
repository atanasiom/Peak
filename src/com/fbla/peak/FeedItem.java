package com.fbla.peak;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;

/**
 * This class is for the items that show up on the user's feed.
 * 
 * @author Michael Atanasio
 * 
 */
public class FeedItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4620628876063712497L;
	/**
	 * 
	 */
	private String poster;
	/**
	 * 
	 */
	private String title;
	/**
	 * 
	 */
	private String subtitle;
	/**
	 * 
	 */
	private String postTime;
	/**
	 * 
	 */
	private ArrayList<Comment> comments;
	/**
	 * 
	 */
	private ArrayList<Like> likes;

	private Bitmap postImage;

	/**
	 * @param poster
	 * @param title
	 * @param subtitle
	 * @param postTime
	 * @param comments
	 * @param likes
	 */
	public FeedItem(String poster, String title, String subtitle,
			String postTime, ArrayList<Comment> comments,
			ArrayList<Like> likes, Bitmap postImage) {
		this.poster = poster;
		this.title = title;
		this.subtitle = subtitle;
		this.postTime = postTime;
		this.comments = comments;
		this.likes = likes;
		this.postImage = postImage;
	}

	public FeedItem(String poster, String title, String subtitle,
			String postTime, ArrayList<Comment> comments, ArrayList<Like> likes) {
		this.poster = poster;
		this.title = title;
		this.subtitle = subtitle;
		this.postTime = postTime;
		this.comments = comments;
		this.likes = likes;
	}

	/**
	 * @return
	 */
	public String getPoster() {
		return poster;
	}

	/**
	 * @param poster
	 */
	public void setPoster(String poster) {
		this.poster = poster;
	}

	/**
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return
	 */
	public String getSubtitle() {
		return subtitle;
	}

	/**
	 * @param subtitle
	 */
	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	/**
	 * @return
	 */
	public String getPostTime() {
		return postTime;
	}

	/**
	 * @param postTime
	 */
	public void setPostTime(String postTime) {
		this.postTime = postTime;
	}

	/**
	 * @return
	 */
	public ArrayList<Comment> getComments() {
		return comments;
	}

	/**
	 * @param comments
	 */
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}

	/**
	 * @return
	 */
	public ArrayList<Like> getLikes() {
		return likes;
	}

	/**
	 * @param likes
	 */
	public void setLikes(ArrayList<Like> likes) {
		this.likes = likes;
	}

	public Bitmap getImage() {
		return this.postImage;
	}
}