package com.fbla.peak;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Michael Atanasio
 * 
 */
public class FeedAdapter extends ArrayAdapter<FeedItem> {

	/**
	 * The ID of the row
	 */
	private int layoutResourceId;

	/**
	 * Creates a new ListAdapter for the Feed.
	 * 
	 * @param context
	 *            this applications context
	 * @param layoutResourceId
	 *            the ID of the row
	 */
	public FeedAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		convertView = inflater.inflate(layoutResourceId, parent, false);
		TextView title = (TextView) convertView.findViewById(R.id.item_title);
		TextView subtitle = (TextView) convertView
				.findViewById(R.id.item_subtitle);
		ImageView postImage = (ImageView) convertView
				.findViewById(R.id.item_picture);
		// Set the text to the name of the user who posted
		title.setText(getItem(position).getTitle());
		subtitle.setText(getItem(position).getSubtitle());
		postImage.setImageBitmap(getItem(position).getImage());
		// Returns the item
		return convertView;
	}
}