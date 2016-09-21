package com.fbla.peak;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FeedParser {
	private String url;
	private static final String START_COMMENT = "<comment>";
	private static final String START_COMMENTER = "<commenter>";
	private static final String START_COMMENTS = "<comments>";
	private static final String START_CONTENT = "<content>";
	private static final String START_LIKER = "<liker>";
	private static final String START_LIKE = "<like>";
	private static final String START_NAME = "<name>";
	private static final String START_POST = "<post>";
	private static final String START_POSTER = "<poster>";
	private static final String START_POSTTIME = "<posttime>";
	private static final String START_TIME = "<time>";
	private static final String START_SUBTITLE = "<subtitle>";
	private static final String START_TITLE = "<title>";
	private static final String END_COMMENTER = "</commenter>";
	private static final String END_COMMENTS = "</comments>";
	private static final String END_CONTENT = "</content>";
	private static final String END_LIKER = "</liker>";
	private static final String END_NAME = "</name>";
	private static final String END_POSTER = "</poster>";
	private static final String END_POSTTIME = "</posttime>";
	private static final String END_SUBTITLE = "</subtitle>";
	private static final String END_TIME = "</time>";
	private static final String END_TITLE = "</title>";

	public FeedParser(String url) {
		this.url = url;
	}

	public ArrayList<FeedItem> getFeed() throws IOException {
		String data = getData(url);
		ArrayList<FeedItem> items = new ArrayList<FeedItem>();
		while (data.contains(START_POST)) {
			data = data.substring(data.indexOf(START_POST)
					+ START_POST.length());
			String poster = getPoster(data);
			String title = getTitle(data);
			String subtitle = getSubtitle(data);
			String postTime = getPostTime(data);
			ArrayList<Comment> comments = getComments(data);
			ArrayList<Like> likes = getLikes(data);
			ServiceAPI api = new ServiceAPI(MainActivity.API_KEY,
					MainActivity.SECRET_KEY);
			UploadService uploadService = api.buildUploadService();
			Upload imageUpload = uploadService
					.getFileByName(poster + "_pic.jpg");
			String imageResponse = imageUpload.toString();
			imageResponse = imageResponse.substring(
					imageResponse.indexOf("url\":\"") + "url\":\"".length(),
					imageResponse.indexOf("\",\"tiny"));
			Bitmap image = getImage(imageResponse);
			FeedItem item = new FeedItem(poster, title, subtitle, postTime,
					comments, likes, image);
			items.add(item);
		}
		return items;
	}

	private String getPoster(String string) {
		return string.substring(
				string.indexOf(START_POSTER) + START_POSTER.length(),
				string.indexOf(END_POSTER)).trim();
	}

	private String getTitle(String string) {
		return string.substring(
				string.indexOf(START_TITLE) + START_TITLE.length(),
				string.indexOf(END_TITLE)).trim();
	}

	private String getSubtitle(String string) {
		return string.substring(
				string.indexOf(START_SUBTITLE) + START_SUBTITLE.length(),
				string.indexOf(END_SUBTITLE)).trim();
	}

	private String getPostTime(String string) {
		return string.substring(
				string.indexOf(START_POSTTIME) + START_POSTTIME.length(),
				string.indexOf(END_POSTTIME)).trim();
	}

	private ArrayList<Comment> getComments(String string) {
		ArrayList<Comment> comments = new ArrayList<Comment>();
		string = string.substring(string.indexOf(START_COMMENTS)
				+ START_COMMENTS.length(), string.indexOf(END_COMMENTS));
		while (string.contains(START_COMMENT)) {
			String commenter = string.substring(
					string.indexOf(START_COMMENTER) + START_COMMENTER.length(),
					string.indexOf(END_COMMENTER)).trim();
			String name = string.substring(
					string.indexOf(START_NAME) + START_NAME.length(),
					string.indexOf(END_NAME)).trim();
			String content = string.substring(
					string.indexOf(START_CONTENT) + START_CONTENT.length(),
					string.indexOf(END_CONTENT)).trim();
			String time = string.substring(
					string.indexOf(START_TIME) + START_TIME.length(),
					string.indexOf(END_TIME)).trim();
			Comment comment = new Comment(commenter, name, content, time);
			comments.add(comment);
			string = string.substring(string.indexOf(START_COMMENT)
					+ START_COMMENT.length());
		}
		return comments;
	}

	private ArrayList<Like> getLikes(String string) {
		ArrayList<Like> likes = new ArrayList<Like>();
		while (string.contains(START_LIKE)) {
			string = string.substring(string.indexOf(START_LIKE)
					+ START_LIKE.length());
			String liker = string.substring(
					string.indexOf(START_LIKER) + START_LIKER.length(),
					string.indexOf(END_LIKER)).trim();
			String name = string.substring(
					string.indexOf(START_NAME) + START_NAME.length(),
					string.indexOf(END_NAME)).trim();
			Like like = new Like(liker, name);
			likes.add(like);
		}
		return likes;
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