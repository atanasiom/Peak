package com.fbla.peak;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;

import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;

/**
 * This Activity is always seen by the user. It serves as the application's
 * splash screen. It loads any necessary resources so that they don't have to be
 * loaded during use of the app. This activity also logs the user in if they
 * have logged in before.
 * 
 * @author Michael Atanasio
 * 
 */
public class MainActivity extends Activity {
	public static final String PREFS_NAME = "loginInformation";
	public static final String EMAIL = "email";
	public static final String PASSWORD = "password";
	public static final String API_KEY = "4eabe705de29cc2b2057bc9d1a8b29049d93e3ab2e56fa76012ca69cfb290c90";
	public static final String SECRET_KEY = "61c9289c8f70f01c0a200e482397c73b5cf0777c49b9e7f722cc3204492306be";
	public static String FEED_URL = "";
	public static String USER_LIST_URL = "";
	private boolean correctInfo;
	/**
	 * This ArrayList holds all of the users.
	 */
	public static ArrayList<User> users;
	public static SharedPreferences sp;
	public static SharedPreferences.Editor editor;
	private String email;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Call the super class constructor
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Set the view to the activity_create_account file
		setContentView(R.layout.activity_main);
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		sp = getSharedPreferences(PREFS_NAME, 0);
		editor = sp.edit();
		email = sp.getString(EMAIL, "");
		password = sp.getString(PASSWORD, "");
		new GetUrls().execute();
	}

	private void login() {
		for (User user : users)
			if (user.getEmail().equals(email)
					&& user.getPassword().equals(password)) {
				correctInfo = true;
				new Thread() {
					public void run() {
						try {
							ServiceAPI api = new ServiceAPI(
									MainActivity.API_KEY,
									MainActivity.SECRET_KEY);
							UploadService uploadService = api.buildUploadService();
							Upload upload = uploadService.getFileByName(email + "_profile.txt");
							String response = upload.toString();
							response = response.substring(
									response.indexOf("url\":\"") + "url\":\"".length(),
									response.indexOf("\",\"tiny"));
							UserLogin.user = new UserParser(response).getLoggedInUser();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
				return;
			}
		correctInfo = false;
	}

	private void check() {
		if (correctInfo) {
			Intent intent = new Intent(this, LoggedIn.class);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(this, UserLogin.class);
			startActivity(intent);
			finish();
		}
	}

	private class GetUrls extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... unused) {
			try {
				ServiceAPI api = new ServiceAPI(API_KEY, SECRET_KEY);
				UploadService uploadService = api.buildUploadService();
				Upload feedUpload = uploadService.getFileByName("feedposts");
				String feedResponse = feedUpload.toString();
				feedResponse = feedResponse.substring(
						feedResponse.indexOf("url\":\"") + "url\":\"".length(),
						feedResponse.indexOf("\",\"tiny"));
				MainActivity.FEED_URL = feedResponse;
				Upload usersUpload = uploadService
						.getFileByName("userlist.txt");
				String usersResponse = usersUpload.toString();
				usersResponse = usersResponse
						.substring(usersResponse.indexOf("url\":\"")
								+ "url\":\"".length(),
								usersResponse.indexOf("\",\"tiny"));
				MainActivity.USER_LIST_URL = usersResponse;
				ArrayList<User> users = new UserParser(usersResponse)
						.getUserList();
				MainActivity.users = users;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			login();
			check();
		}
	}
}