package com.fbla.peak;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadFileType;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;

/**
 * This is the Activity the user sees if they pressed the "Create new account"
 * button.
 * 
 * @author Michael Atanasio
 * 
 */
public class CreateAccount extends Activity {
	/**
	 * Number used to identify the photo selection results.
	 */
	private static final int SELECT_PHOTO = 0;
	/**
	 * Number used to identify the crop photo results.
	 */
	private final int PIC_CROP = 1;
	/**
	 * The application's ActionBar
	 */
	private ActionBar ab;
	/**
	 * This stores the image the user selects when they click on the
	 * profileImageView.
	 */
	private Bitmap profileImage;
	/**
	 * The editText that the user types their email into.
	 */
	private EditText emailText;
	private EditText firstNameText;
	private EditText lastNameText;
	private EditText gradeText;
	private EditText passwordText;
	private EditText confirmText;
	private TextView wrongView;
	private TextView expertiseText;
	private String email;
	private String firstName;
	private String lastName;
	private String grade;
	private String expertise = "-1";
	private String password;
	private String confirm;
	/**
	 * This View holds the user's profile image.
	 */
	private ImageView profileImageView;
	private AlertDialog alert;
	private ExpertiseAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Call the super class constructor
		super.onCreate(savedInstanceState);
		// Set the view to the activity_create_account file
		setContentView(R.layout.activity_create_account);
		// This code allows the user to press the button in the ActionBar and go
		// to the previous screen
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
		// Get a copy of the application's Action Bar
		ab = getActionBar();
		// Set the app icon as an up button
		ab.setDisplayHomeAsUpEnabled(true);
		// Get a copy of the email field
		emailText = (EditText) findViewById(R.id.create_email);
		// Get a copy of the profile picture field
		profileImageView = (ImageView) findViewById(R.id.create_profile_pic);
		firstNameText = (EditText) findViewById(R.id.create_first_name);
		lastNameText = (EditText) findViewById(R.id.create_last_name);
		gradeText = (EditText) findViewById(R.id.create_grade);
		passwordText = (EditText) findViewById(R.id.create_password);
		confirmText = (EditText) findViewById(R.id.create_confirm_password);
		wrongView = (TextView) findViewById(R.id.create_wrong);
		expertiseText = (TextView) findViewById(R.id.create_expertise);
		expertiseText.setOnClickListener(expertiseListener);
	}

	private OnClickListener expertiseListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// 1. Instantiate an AlertDialog.Builder with its constructor
			AlertDialog.Builder builder = new AlertDialog.Builder(
					CreateAccount.this);
			// 2. Chain together various setter methods to set the dialog
			// characteristics
			builder.setTitle("Select expertise level");
			// 3. Get the AlertDialog from create()
			ListView list = new ListView(CreateAccount.this);
			adapter = new ExpertiseAdapter(CreateAccount.this,
					R.layout.expertise_dialog);
			adapter.add("I'm an IIT expert and I want to help people");
			adapter.add("I'm doing okay and I don't need help, but I'm not confident enough to help others");
			adapter.add("I need help");
			adapter.add("I need a tutor because I just can't get the hang of IIT");
			list.setAdapter(adapter);
			list.setOnItemClickListener(listListener);
			builder.setView(list);
			alert = builder.create();
			alert.show();
		}
	};
	private OnItemClickListener listListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapterView, View view,
				int position, long itemId) {
			expertiseText.setText(adapter.getItem(position));
			expertise = Integer.toString(position);
			if (alert.isShowing())
				alert.dismiss();
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parentActivityIntent = new Intent(this, UserLogin.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SELECT_PHOTO:
			if (resultCode == RESULT_OK) {
				Uri selectedImage = data.getData();
				performCrop(selectedImage);
			}
			break;
		case PIC_CROP:
			if (data != null) {
				// Get the returned data
				Bundle extras = data.getExtras();
				// Get the cropped bitmap
				Bitmap selectedBitmap = extras.getParcelable("data");
				profileImage = selectedBitmap;
				profileImageView.setImageBitmap(selectedBitmap);
			}
		}
	}

	private void performCrop(Uri picUri) {
		try {

			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// Indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// Set crop properties
			cropIntent.putExtra("crop", "true");
			// Indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// Indicate output X and Y
			cropIntent.putExtra("outputX", 125);
			cropIntent.putExtra("outputY", 125);
			// Retrieve data on return
			cropIntent.putExtra("return-data", true);
			// Start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		}
		// Respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			// Display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	/**
	 * This method is triggered when the user click the create account button.
	 * 
	 * @param view
	 */
	public void createNewAccount(View view) {
		firstName = firstNameText.getText().toString();
		lastName = lastNameText.getText().toString();
		grade = gradeText.getText().toString();
		email = emailText.getText().toString();
		password = passwordText.getText().toString();
		confirm = confirmText.getText().toString();
		expertise = expertiseText.getText().toString();
		// Checks for empty fields
		if (firstName.equals("") || lastName.equals("") || grade.equals("")
				|| email.equals("") || password.equals("")
				|| confirm.equals("") || expertise.contains("Click")
				|| profileImage == null) {
			wrongView.setText("You must complete all fields");
			animate();
		} else if (!password.equals(confirm)) {
			wrongView.setText("Passwords must match");
			animate();
		} else
			new UploadAccount().execute();

	}

	private void animate() {
		// Creates a new animation that will fade from fully
		// transparent
		// to
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

	/**
	 * @param view
	 */
	public void selectImage(View view) {
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_PHOTO);
	}

	/**
	 * @param email
	 */
	private void createDialog(boolean worked) {
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setTitle(worked ? "Account created!" : "OOPS!");
		dialog.setMessage(worked ? "Congratulations!  Your account has been created. Welcome to Peak!"
				: "Something went wrong! Please try again later. Sorry for any inconvenience.");
		dialog.setButton(DialogInterface.BUTTON_POSITIVE,
				worked ? "Start climbing!" : "OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		dialog.show();
	}

	private class UploadAccount extends AsyncTask<Void, Void, Void> {
		private String response;
		ProgressDialog dialog = new ProgressDialog(CreateAccount.this);

		@Override
		protected void onPreExecute() {
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("Creating account");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... unused) {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("<first>").append(firstName)
						.append("</first>\n<last>").append(lastName)
						.append("</last>\n<grade>").append(grade)
						.append("</grade>\n<level>").append(expertise)
						.append("</level>\n<email>").append(email)
						.append("</email>\n<password>").append(password)
						.append("</password>\n<points>0</points>");
				String newUserFileName = email + "_profile.txt";
				FileOutputStream fos1;
				fos1 = openFileOutput(newUserFileName, Context.MODE_PRIVATE);
				fos1.write(sb.toString().getBytes());
				fos1.close();
				File newUserFile = new File(getFilesDir(), newUserFileName);
				String description = "Profile for " + email;
				ServiceAPI api = new ServiceAPI(MainActivity.API_KEY,
						MainActivity.SECRET_KEY);
				UploadService uploadService = api.buildUploadService();
				Upload newUserUpload = uploadService.uploadFile(
						newUserFileName, newUserFile.getCanonicalPath(),
						UploadFileType.TXT, description);
				response = newUserUpload.toString();
				String imageFileName = email + "_pic.jpg";
				File imagePath = new File(getFilesDir(), email + "_pic.jpg");
				FileOutputStream fos2 = new FileOutputStream(imagePath);
				// Use the compress method on the BitMap object to write image
				// to the OutputStream
				profileImage.compress(Bitmap.CompressFormat.JPEG, 100, fos2);
				fos2.close();
				File imageFile = new File(getFilesDir(), imageFileName);
				String imageDescription = "Profile picture for " + email;
				uploadService.uploadFile(imageFileName,
						imageFile.getCanonicalPath(), UploadFileType.IMAGE,
						imageDescription);
				Upload usersUpload = uploadService
						.getFileByName("userlist.txt");
				String usersResponse = usersUpload.toString();
				usersResponse = usersResponse
						.substring(usersResponse.indexOf("url\":\"")
								+ "url\":\"".length(),
								usersResponse.indexOf("\",\"tiny"));
				String userList = getData(usersResponse);
				userList = userList + "<user><username>" + email
						+ "</username>\n<password>" + password
						+ "</password></user>";
				FileOutputStream fos3;
				fos3 = openFileOutput("userlist.txt", Context.MODE_PRIVATE);
				fos3.write(userList.getBytes());
				fos3.close();
				File userListFile = new File(getFilesDir(), "userlist.txt");
				uploadService.removeFileByName("userlist.txt");
				uploadService.uploadFile("userlist.txt",
						userListFile.getCanonicalPath(), UploadFileType.TXT,
						"List of users");
				MainActivity.USER_LIST_URL = userList;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (dialog.isShowing())
				dialog.dismiss();
			createDialog(response.contains("success"));
		}
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
}