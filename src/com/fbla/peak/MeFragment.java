package com.fbla.peak;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MeFragment extends Fragment {
	private Bitmap profilePic;
	private ImageView profilePicView;
	private TextView nameView;
	private TextView pointsView;
	private TextView gradeView;
	private TextView levelView;
	private User user;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_me, container, false);
		user = UserLogin.user;
		profilePic = user.getProfilePic();
		nameView = (TextView) view.findViewById(R.id.me_name);
		profilePicView = (ImageView) view.findViewById(R.id.me_profile_pic);
		pointsView = (TextView) view.findViewById(R.id.me_points);
		gradeView = (TextView) view.findViewById(R.id.me_grade);
		levelView = (TextView) view.findViewById(R.id.me_level);
		nameView.setText(user.getName());
		pointsView.setText(user.getPoints() + " point"
				+ (Integer.parseInt(user.getPoints()) != 1 ? "s" : ""));
		int grade = Integer.parseInt(user.getGrade());
		gradeView.setText(user.getGrade()
				+ (grade == 1 ? "st" : grade == 1 ? "nd" : grade == 3 ? "rd"
						: grade > 4 ? "th" : "") + " grade");
		profilePic = user.getProfilePic();
		profilePicView.setImageBitmap(profilePic);
		String level = null;
		if (user.getLevel().equals("0"))
			level = "I'm an IIT expert and I want to help people";
		else if (user.getLevel().equals("1"))
			level = "I'm doing okay and I don't need help, but I'm not confident enough to help others";
		else if (user.getLevel().equals("2"))
			level = "I need help";
		else if (user.getLevel().equals("3"))
			level = "I need a tutor because I just can't get the hang of IIT";
		levelView.setText(level);
		return view;
	}
}