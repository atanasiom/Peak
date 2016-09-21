package com.fbla.peak;

import android.graphics.Bitmap;

/**
 * This class is an object that stores all of the User's information.
 * 
 * @author Michael Atanasio
 * 
 */
public class User {
	/**
	 * This string stores the User's email address.
	 */
	private String email;
	/**
	 * This string stores the User's first name.
	 */
	private String fName;
	/**
	 * This string stores the User's last name.
	 */
	private String lName;
	/**
	 * This string stores teh User's grade.
	 */
	private String grade;
	private String level;
	/**
	 * This string stores the User's password.
	 */
	private String password;
	/**
	 * This string stores the User's points.
	 */
	private String points;
	/**
	 * This string stores the User's picture url.
	 */
	private String url;
	/**
	 * The actual image of the profile picture
	 */
	private Bitmap profilePic;

	/**
	 * Creates a new User.
	 * 
	 * @param fName
	 *            this User's first name
	 * @param lName
	 *            this User's last name
	 * @param email
	 *            this User's email
	 * @param password
	 *            this User's password
	 * @param points
	 *            this User's number of points
	 * @param bitmap
	 *            the Bitmap of the User's profile picture
	 */
	public User(String fName, String lName, String grade, String email,
			String password, String points, String level, Bitmap bitmap) {
		this.email = email;
		this.fName = fName;
		this.lName = lName;
		this.grade = grade;
		this.password = password;
		this.points = points;
		this.profilePic = bitmap;
		this.level = level;
	}

	/**
	 * @param email
	 *            this User's email
	 * 
	 * @param password
	 *            this User's password
	 */
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/**
	 * Gets the email of this User.
	 * 
	 * @return the email of this User
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the email of this user.
	 * 
	 * @param email
	 *            the new email of this user
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets the first name of this User.
	 * 
	 * @return the first name of this User
	 */
	public String getfName() {
		return fName;
	}

	/**
	 * Sets the first name of this User.
	 * 
	 * @param fName
	 *            the new first name of this User
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}

	/**
	 * Gets the last name of this User.
	 * 
	 * @return the last name of this User
	 */
	public String getlName() {
		return lName;
	}

	/**
	 * Sets the last name of this User.
	 * 
	 * @param lName
	 *            the new last name of this User
	 */
	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * Gets this User's password.
	 * 
	 * @return this User's password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password of this User.
	 * 
	 * @param password
	 *            this User's new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the number of points this User has.
	 * 
	 * @return the number of points this User has
	 */
	public String getPoints() {
		return points;
	}

	/**
	 * Sets the number of points this User has.
	 * 
	 * @param points
	 *            the new number of points this User has
	 */
	public void setPoints(String points) {
		this.points = points;
	}

	/**
	 * Gets the url of this User's profile picture.
	 * 
	 * @return the url of this User's profile picture
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url of this User's profile picture.
	 * 
	 * @param url
	 *            the new url of this User's profile picture
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return
	 */
	public Bitmap getProfilePic() {
		return profilePic;
	}

	/**
	 * @param profilePic
	 */
	public void setProfilePic(Bitmap profilePic) {
		this.profilePic = profilePic;
	}

	/**
	 * Gets the entire name of the user.
	 * 
	 * @return the entire name of the user
	 */
	public String getName() {
		return new StringBuilder().append(fName).append(" ").append(lName)
				.toString();
	}

	@Override
	public String toString() {
		return new StringBuilder().append("Name: ").append(fName).append(" ")
				.append(lName).append("\nEmail: ").append(email)
				.append("\nPassword: ").append(password).append("\nPoints: ")
				.append(points).append("\nPic: ").append(url).toString();
	}
}