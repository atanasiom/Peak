package com.fbla.peak;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LoggedIn extends Activity {
	public static Context context;
	public static Resources res;
	private CharSequence mTitle;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private String[] titles;
	private boolean isOpen = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logged_in);
		context = this;
		res = getResources();
		// This bit of code overrides the built in animation for when
		// application opens to a fade in animation
		overridePendingTransition(R.anim.slide_up_bottom, R.anim.slide_up_top);
		titles = getResources().getStringArray(R.array.title_array);
		titles = getResources().getStringArray(R.array.title_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, titles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		Fragment fragment = new FeedFragment();
		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();
		setTitle(titles[0]);
	}

	@Override
	public void onBackPressed() {
		if (FeedFragment.isUp)
			FeedFragment.closeCompose();
		else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.sign_out:
			MainActivity.editor.putString(MainActivity.EMAIL, "");
			MainActivity.editor.putString(MainActivity.PASSWORD, "");
			MainActivity.editor.commit();
			Intent parentActivityIntent = new Intent(this, UserLogin.class);
			parentActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(parentActivityIntent);
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		switch (position) {
		case 0:
			// Create a new feedFragment
			Fragment feedFragment = new FeedFragment();
			// Insert the fragment by replacing any existing fragment
			FragmentManager feedFragmentManager = getFragmentManager();
			feedFragmentManager.beginTransaction()
					.replace(R.id.content_frame, feedFragment).commit();
			invalidateOptionsMenu();
			break;
		case 1:
			// Create a new feedFragment
			Fragment meFragment = new MeFragment();
			// Insert the fragment by replacing any existing fragment
			FragmentManager meFragmentManager = getFragmentManager();
			meFragmentManager.beginTransaction()
					.replace(R.id.content_frame, meFragment).commit();
			invalidateOptionsMenu();
			break;
		case 2:
			// Create a new feedFragment
			Fragment gameFragment = new GameFragment();
			// Insert the fragment by replacing any existing fragment
			FragmentManager gameFragmentManager = getFragmentManager();
			gameFragmentManager.beginTransaction()
					.replace(R.id.content_frame, gameFragment).commit();
			invalidateOptionsMenu();
			break;
		}
		// Highlight the selected item, update the title, and close the
		// drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(titles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	public void createPost(View view) {
		FeedFragment.createPost();
	}

	public void answerClick(View view) {
		GameFragment.answerClick(view);
	}
}