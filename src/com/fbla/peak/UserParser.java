package com.fbla.peak;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;

/**
 * This Class parses a text file containing all of the user's information.
 * 
 * @author Michael Atanasio
 * 
 */
public class UserParser {
	private String url;
	private final String START_USER = "<user>";
	private final String START_EMAIL = "<email>";
	private final String START_FNAME = "<first>";
	private final String START_GRADE = "<grade>";
	private final String START_LNAME = "<last>";
	private final String START_LEVEL = "<level>";
	private final String START_POINTS = "<points>";
	private final String START_USERNAME = "<username>";
	private final String START_PASSWORD = "<password>";
	private final String END_EMAIL = "</email>";
	private final String END_FNAME = "</first>";
	private final String END_GRADE = "</grade>";
	private final String END_LNAME = "</last>";
	private final String END_LEVEL = "</level>";
	private final String END_POINTS = "</points>";
	private final String END_PASSWORD = "</password>";
	private final String END_USERNAME = "</username>";

	/**
	 * Creates a new parser.
	 * 
	 * @param context
	 */
	public UserParser(String url) {
		this.url = url;
	}

	public User getLoggedInUser() throws IOException {
		String data = getData(url);
		String fName = getFName(data);
		String lName = getLName(data);
		String grade = getGrade(data);
		String email = getEmail(data);
		String password = getPassword(data);
		String points = getPoints(data);
		String level = getLevel(data);
		ServiceAPI api = new ServiceAPI(MainActivity.API_KEY,
				MainActivity.SECRET_KEY);
		UploadService uploadService = api.buildUploadService();
		Upload imageUpload = uploadService.getFileByName(email + "_pic.jpg");
		String imageResponse = imageUpload.toString();
		imageResponse = imageResponse.substring(
				imageResponse.indexOf("url\":\"") + "url\":\"".length(),
				imageResponse.indexOf("\",\"tiny"));
		Bitmap image = getImage(imageResponse);
		User user = new User(fName, lName, grade, email, password, points,
				level, image);
		return user;
	}

	public ArrayList<User> getUserList() throws IOException {
		String data = getData(url);
		ArrayList<User> users = new ArrayList<User>();
		while (data.contains(START_USER)) {
			data = data.substring(data.indexOf(START_USER)
					+ START_USER.length());
			String username = getUsername(data);
			String password = getPassword(data);
			User user = new User(username, password);
			users.add(user);
		}
		return users;
	}

	private String getFName(String string) {
		return string.substring(
				string.indexOf(START_FNAME) + START_FNAME.length(),
				string.indexOf(END_FNAME)).trim();
	}

	private String getLName(String string) {
		return string.substring(
				string.indexOf(START_LNAME) + START_LNAME.length(),
				string.indexOf(END_LNAME)).trim();
	}

	private String getGrade(String string) {
		return string.substring(
				string.indexOf(START_GRADE) + START_GRADE.length(),
				string.indexOf(END_GRADE)).trim();
	}

	private String getEmail(String string) {
		return string.substring(
				string.indexOf(START_EMAIL) + START_EMAIL.length(),
				string.indexOf(END_EMAIL)).trim();
	}

	private String getLevel(String string) {
		return string.substring(
				string.indexOf(START_LEVEL) + START_LEVEL.length(),
				string.indexOf(END_LEVEL)).trim();
	}

	private String getPassword(String string) {
		return string.substring(
				string.indexOf(START_PASSWORD) + START_PASSWORD.length(),
				string.indexOf(END_PASSWORD)).trim();
	}

	private String getPoints(String string) {
		return string.substring(
				string.indexOf(START_POINTS) + START_POINTS.length(),
				string.indexOf(END_POINTS)).trim();
	}

	private String getUsername(String string) {
		return string.substring(
				string.indexOf(START_USERNAME) + START_USERNAME.length(),
				string.indexOf(END_USERNAME)).trim();
	}

	private static String getData(String myUrl) throws IOException {
		InputStream is = null;
		try {
			URL url = new URL(myUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			Reader reader = null;
			reader = new InputStreamReader(is, "UTF-8");
			int data = reader.read();
			StringBuilder sb = new StringBuilder();
			while (data != -1) {
				sb.append((char) data);
				data = reader.read();
			}
			return sb.toString();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	private Bitmap getImage(String myUrl) throws IOException {
		InputStream is = null;
		try {
			URL url = new URL(myUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			is = conn.getInputStream();
			Bitmap bmp = BitmapFactory.decodeStream(is);
			return bmp;
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
}