package com.fbla.peak;

import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.TextView;

import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;

/**
 * This Activity is only shown if the user has not already logged in or if they
 * have signed out. It allows them to login in to their existing accounts, or
 * create a new one if they do not have one.
 * 
 * @author Michael Atanasio
 * 
 */
public class UserLogin extends Activity {
	/**
	 * This variable is true if the username and password match, otherwise it's
	 * false.
	 */
	private boolean correctInfo;
	/**
	 * This is where the user types their email.
	 */
	private EditText editEmail;
	/**
	 * This is where the user types their password.
	 */
	private EditText editPassword;

	/**
	 * This is the TextView that displays if the user enters wrong login info.
	 */
	private TextView wrongView;
	/**
	 * This String holds whatever email the user types in.
	 */
	private String email;
	/**
	 * This String holds whatever password the user types in.
	 */
	private String password;
	/**
	 * This is the user that one is logged in as.
	 */
	public static User user;

	/*
	 * This method is run when the application is first created, hence onCreate.
	 * Currently I am overriding the built in onCreate method so I can do all of
	 * the necessary UI changes before the user sees it.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);
		// This bit of code overrides the built in animation for when
		// application opens to a fade in animation.
		// These next three lines get instances of the views.
		wrongView = (TextView) findViewById(R.id.main_wrong);
		editEmail = (EditText) findViewById(R.id.main_email);
		editPassword = (EditText) findViewById(R.id.main_password);
	}

	/**
	 * This method is triggered when the user hits the login button. This method
	 * starts the logging in process.
	 * 
	 * @param view
	 */
	public void login(View view) {
		login();
		if (correctInfo) {
			finishLogin();
		} else {
			// Creates a new animation that will fade from fully transparent to
			// fully visible.
			AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
			// This sets the duration to 1000 milliseconds (one second).
			fadeIn.setDuration(1000);
			// This makes the end result persistent. This means that when the
			// text is filled in, it will stay that way.
			fadeIn.setFillAfter(true);
			// This starts the animation on the view.
			wrongView.startAnimation(fadeIn);
		}

	}

	/**
	 * This method is triggered when the user hits the create new account
	 * button. This method starts the create new account activity.
	 * 
	 * @param view
	 */
	public void createAccount(View view) {
		Intent intent = new Intent(this, CreateAccount.class);
		startActivity(intent);

	}

	/**
	 * This method checks whether or not the username and password match. If
	 * they do, correctInfo becomes true, otherwise it becomes false.
	 */
	private void login() {
		new LogIn().execute();
		email = editEmail.getText().toString();
		password = editPassword.getText().toString();
		for (User user : MainActivity.users)
			if (user.getEmail().equals(email)
					&& user.getPassword().equals(password)) {
				correctInfo = true;
				new Thread() {
					public void run() {
						try {
							ServiceAPI api = new ServiceAPI(
									MainActivity.API_KEY,
									MainActivity.SECRET_KEY);
							UploadService uploadService = api
									.buildUploadService();
							Upload upload = uploadService.getFileByName(email
									+ "_profile.txt");
							String response = upload.toString();
							response = response.substring(
									response.indexOf("url\":\"")
											+ "url\":\"".length(),
									response.indexOf("\",\"tiny"));
							UserLogin.user = new UserParser(response)
									.getLoggedInUser();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}.start();
				return;
			}
		correctInfo = false;
	}

	/**
	 * This method finishes logging the user in. This method only runs when
	 * correctInfo is true.
	 */
	private void finishLogin() {
		MainActivity.editor.putString(MainActivity.EMAIL, email);
		MainActivity.editor.putString(MainActivity.PASSWORD, password);
		MainActivity.editor.commit();
		Intent intent = new Intent(UserLogin.this, LoggedIn.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.slide_up_bottom, R.anim.slide_up_top);
	}

	private class LogIn extends AsyncTask<User, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(UserLogin.this);

		@Override
		public void onPreExecute() {
			dialog.setMessage("Logging in");
			dialog.show();
		}

		@Override
		protected Void doInBackground(User... params) {
			try {
				MainActivity.users = new UserParser(MainActivity.USER_LIST_URL)
						.getUserList();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (dialog.isShowing())
				dialog.dismiss();
		}
	}
}