package com.fbla.peak;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadFileType;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;

/**
 * @author Michael Atanasio
 * 
 */
public class FeedFragment extends Fragment {
	public static boolean isUp;
	public static String ITEM = "item";
	public static ArrayList<FeedItem> feedItems;
	private static EditText composeEdit;
	private static FeedAdapter adapter;
	private static ListView list;
	private static RelativeLayout compose;
	private static RadioButton requestHelp;
	private static RadioButton provideHelp;
	private static RadioButton shareTip;
	private static RadioButton shareResource;
	private static RadioButton remindEveryone;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_feed, container, false);
		compose = (RelativeLayout) view.findViewById(R.id.compose_layout);
		composeEdit = (EditText) view.findViewById(R.id.edit_compose);
		requestHelp = (RadioButton) view.findViewById(R.id.radio_one);
		provideHelp = (RadioButton) view.findViewById(R.id.radio_two);
		shareTip = (RadioButton) view.findViewById(R.id.radio_three);
		shareResource = (RadioButton) view.findViewById(R.id.radio_four);
		remindEveryone = (RadioButton) view.findViewById(R.id.radio_five);
		setHasOptionsMenu(true);
		adapter = new FeedAdapter(LoggedIn.context, R.layout.feed_list_item);
		createFeed();
		list = (ListView) view.findViewById(R.id.feed_list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(clickListener);
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.logged_in, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.compose:
			if (isUp) {
				closeCompose(true);
			} else
				openCompose();
			return true;
		case R.id.refresh:
			new LoadFeed().execute();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * When any list item is clicked, anything that is in the onItemClick method
	 * will run.
	 */
	private OnItemClickListener clickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (!isUp) {
				Intent intent = new Intent(LoggedIn.context,
						ItemDescription.class);
				FeedItem item = adapter.getItem(position);
				FeedItem newItem = new FeedItem(item.getPoster(),
						item.getTitle(), item.getSubtitle(),
						item.getPostTime(), item.getComments(), item.getLikes());
				intent.putExtra(ITEM, newItem);
				startActivity(intent);
			}
		}
	};

	/**
	 * Opens the compose post area.
	 */
	private void openCompose() {
		composeEdit.setText("");
		compose.setVisibility(RelativeLayout.VISIBLE);
		Animation animation = AnimationUtils.loadAnimation(LoggedIn.context,
				R.anim.compose_slide_up);
		compose.startAnimation(animation);
		isUp = true;
	}

	/**
	 * Closes the compose post area.
	 */
	public static void closeCompose(boolean options) {
		Animation animation = AnimationUtils.loadAnimation(LoggedIn.context,
				R.anim.compose_slide_down);
		compose.startAnimation(animation);
		compose.setVisibility(RelativeLayout.GONE);
		isUp = false;
		if (composeEdit.getText().toString().equals("") || options)
			postCancelled();
	}

	public static void closeCompose() {
		closeCompose(false);
	}

	/**
	 * Creates the feed.
	 */
	private void createFeed() {
		new LoadFeed().execute();
	}

	private static void postCancelled() {
		Toast.makeText(LoggedIn.context, "Post cancelled", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * Code that allows you to create a new feed post.
	 */
	public static void createPost() {
		String text = composeEdit.getText().toString();
		if (text.equals(""))
			Toast.makeText(LoggedIn.context, "Please enter something to post",
					Toast.LENGTH_SHORT).show();
		else {
			String poster = UserLogin.user.getEmail();
			String type = null;
			if (requestHelp.isChecked())
				type = " needs help";
			else if (provideHelp.isChecked())
				type = " is providing help";
			else if (shareTip.isChecked())
				type = " is sharing a tip";
			else if (shareResource.isChecked())
				type = " is sharing a resource";
			else if (remindEveryone.isChecked())
				type = " is reminding everyone";
			String title = UserLogin.user.getName() + type;
			Date date = new Date(System.currentTimeMillis());
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a",
					Locale.US);
			FeedItem item = new FeedItem(poster, title, text, df.format(date),
					new ArrayList<Comment>(), new ArrayList<Like>(),
					UserLogin.user.getProfilePic());
			FeedFragment f = new FeedFragment();
			f.new PostItem().execute(item);
		}
	}

	private class PostItem extends AsyncTask<FeedItem, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(LoggedIn.context);

		@Override
		protected void onPreExecute() {
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("Posting..");
			dialog.show();
		}

		@Override
		protected Void doInBackground(FeedItem... item) {
			try {
				ServiceAPI api = new ServiceAPI(MainActivity.API_KEY,
						MainActivity.SECRET_KEY);
				UploadService uploadService = api.buildUploadService();
				Upload feedUpload = uploadService.getFileByName("feedposts");
				String feedResponse = feedUpload.toString();
				feedResponse = feedResponse.substring(
						feedResponse.indexOf("url\":\"") + "url\":\"".length(),
						feedResponse.indexOf("\",\"tiny"));
				String feedList = getData(feedResponse);
				feedList = feedList
						+ "<post><poster>"
						+ UserLogin.user.getEmail()
						+ "</poster><title>"
						+ item[0].getTitle()
						+ "</title><subtitle>"
						+ item[0].getSubtitle()
						+ "</subtitle><posttime>"
						+ item[0].getPostTime()
						+ "</posttime><comments></comments><likes></likes></post>";
				FileOutputStream fos;
				fos = LoggedIn.context.openFileOutput("feedposts",
						Context.MODE_PRIVATE);
				fos.write(feedList.getBytes());
				fos.close();
				File userListFile = new File(LoggedIn.context.getFilesDir(),
						"feedposts");
				uploadService.removeFileByName("feedposts");
				Upload upload = uploadService.uploadFile("feedposts",
						userListFile.getCanonicalPath(), UploadFileType.TXT,
						"Feed posts");
				String newFeedResponse = upload.toString();
				newFeedResponse = newFeedResponse.substring(
						newFeedResponse.indexOf("url\":\"")
								+ "url\":\"".length(),
						newFeedResponse.indexOf("\",\"tiny"));
				MainActivity.FEED_URL = newFeedResponse;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			if (dialog.isShowing())
				dialog.dismiss();
			new LoadFeed().execute();
			closeCompose();
		}
	}

	private class LoadFeed extends AsyncTask<Void, Void, Void> {
		ProgressDialog dialog = new ProgressDialog(LoggedIn.context);
		private ArrayList<FeedItem> items;

		@Override
		protected void onPreExecute() {
			dialog.setCanceledOnTouchOutside(false);
			dialog.setMessage("Loading feed...");
			dialog.show();
			adapter.clear();
		}

		@Override
		protected Void doInBackground(Void... unused) {
			try {
				ServiceAPI api = new ServiceAPI(MainActivity.API_KEY,
						MainActivity.SECRET_KEY);
				UploadService uploadService = api.buildUploadService();
				Upload feedUpload = uploadService.getFileByName("feedposts");
				String newFeedResponse = feedUpload.toString();
				newFeedResponse = newFeedResponse.substring(
						newFeedResponse.indexOf("url\":\"")
								+ "url\":\"".length(),
						newFeedResponse.indexOf("\",\"tiny"));
				MainActivity.FEED_URL = newFeedResponse;
				items = new FeedParser(MainActivity.FEED_URL).getFeed();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			Collections.reverse(items);
			adapter.addAll(items);
			list.invalidate();
			if (dialog.isShowing())
				dialog.dismiss();
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