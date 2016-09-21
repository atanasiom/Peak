package com.fbla.peak;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CommentAdapter extends ArrayAdapter<Comment> {

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
	public CommentAdapter(Context context, int layoutResourceId) {
		super(context, layoutResourceId);
		this.layoutResourceId = layoutResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		convertView = inflater.inflate(layoutResourceId, parent, false);
		TextView commenter = (TextView) convertView
				.findViewById(R.id.comment_commenter);
		TextView comment = (TextView) convertView
				.findViewById(R.id.comment_comment);
		commenter.setText(getItem(position).getName());
		comment.setText(getItem(position).getContent());
		// Returns the item
		return convertView;
	}
}
