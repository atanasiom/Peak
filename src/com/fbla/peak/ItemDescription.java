package com.fbla.peak;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class ItemDescription extends Activity {
	private TextView title_view;
	private TextView subtitle_view;
	private TextView postTime_view;
	private TextView likes_view;
	private TextView comments_view;
	private ListView list;
	private ArrayList<Comment> comments;
	private ArrayList<Like> likes;
	private FeedItem feedItem;
	private CommentAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_description);
		overridePendingTransition(R.anim.slide_in_left, R.anim.do_nothing);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		title_view = (TextView) findViewById(R.id.description_title);
		subtitle_view = (TextView) findViewById(R.id.description_subtitle);
		postTime_view = (TextView) findViewById(R.id.description_post_time);
		likes_view = (TextView) findViewById(R.id.description_likes);
		comments_view = (TextView) findViewById(R.id.description_comments);
		list = (ListView) findViewById(R.id.comment_list);
		feedItem = (FeedItem) getIntent().getSerializableExtra(
				FeedFragment.ITEM);
		comments = feedItem.getComments();
		likes = feedItem.getLikes();
		title_view.setText(feedItem.getTitle());
		subtitle_view.setText(feedItem.getSubtitle());
		postTime_view.setText(feedItem.getPostTime());
		likes_view.setText(likes.size() + " like"
				+ (likes.size() != 1 ? "s" : ""));
		comments_view.setText(comments.size() + " comment"
				+ (comments.size() != 1 ? "s" : ""));
		adapter = new CommentAdapter(this, R.layout.comment_list_item);
		adapter.addAll(feedItem.getComments());
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.item_description, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					R.anim.slide_out_right);
			return true;
		case R.id.share:
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, feedItem.getSubtitle());
			startActivity(Intent.createChooser(shareIntent, "Share..."));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(android.R.anim.fade_in,
				R.anim.slide_out_right);
	}
}