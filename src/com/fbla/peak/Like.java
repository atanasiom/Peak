package com.fbla.peak;

import java.io.Serializable;

public class Like implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6284473863574343363L;
	/**
	 * 
	 */
	private String liker;
	/**
	 * 
	 */
	private String name;

	/**
	 * @param liker
	 * @param name
	 */
	public Like(String liker, String name) {
		this.liker = liker;
		this.name = name;
	}

	/**
	 * @return
	 */
	public String getLiker() {
		return liker;
	}

	/**
	 * @param liker
	 */
	public void setLiker(String liker) {
		this.liker = liker;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
