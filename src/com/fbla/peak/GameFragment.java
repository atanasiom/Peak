package com.fbla.peak;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GameFragment extends Fragment {
	private static int score = 0;
	private static int currentQuestion = 0;
	private static Button answerOne;
	private static Button answerTwo;
	private static Button answerThree;
	private static Button answerFour;
	private static TextView question;
	private static ArrayList<Question> questions = new ArrayList<Question>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_game, container, false);
		question = (TextView) view.findViewById(R.id.question_text);
		answerOne = (Button) view.findViewById(R.id.answer_1);
		answerTwo = (Button) view.findViewById(R.id.answer_2);
		answerThree = (Button) view.findViewById(R.id.answer_3);
		answerFour = (Button) view.findViewById(R.id.answer_4);
		createQuestions();
		startGame();
		return view;
	}

	public void onResume() {
		super.onResume();
		score = 0;
		currentQuestion = 0;
		createQuestions();
	}

	/**
	 * What happens when a user clicks on an answer. If the user gets the
	 * question right, it adds one to the score and goes on to the next
	 * question.
	 * 
	 * @param view
	 */
	public static void answerClick(View view) {
		if (currentQuestion != 20) {
			if (questions.get(currentQuestion).getAnswers()
					.get(Integer.parseInt((String) view.getTag())).isTrue())
				score++;
			nextQuestion();
		} else {
			AlertDialog dialog = new AlertDialog.Builder(LoggedIn.context)
					.create();
			dialog.setTitle("Congratulations!");
			dialog.setMessage("You got " + score + "/20 correct!");
			dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog.show();
		}
	}

	/**
	 * Goes to the next question
	 */
	private static void nextQuestion() {
		currentQuestion++;
		if (currentQuestion == 20)
			return;
		Question q = questions.get(currentQuestion);
		question.setText("#" + (currentQuestion + 1) + ". " + q.getQuestion());
		answerOne.setText(q.getAnswers().get(0).getAnswer());
		answerTwo.setText(q.getAnswers().get(1).getAnswer());
		answerThree.setText(q.getAnswers().get(2).getAnswer());
		answerFour.setText(q.getAnswers().get(3).getAnswer());
	}

	/**
	 * Initializes the first questions.
	 */
	private void startGame() {
		Question q = questions.get(currentQuestion);
		question.setText("#" + (currentQuestion + 1) + ". " + q.getQuestion());
		answerOne.setText(q.getAnswers().get(0).getAnswer());
		answerTwo.setText(q.getAnswers().get(1).getAnswer());
		answerThree.setText(q.getAnswers().get(2).getAnswer());
		answerFour.setText(q.getAnswers().get(3).getAnswer());
	}

	/**
	 * Creates the question list. At the end of the method, it shuffles the
	 * answers in each question and shuffles all of the questions.
	 */
	private void createQuestions() {
		questions.add(new Question("The Kernel is the core of a computer ___.",
				new Answer("Operating system", true), new Answer("Hard drive",
						false), new Answer("Motherboard", false), new Answer(
						"Application", false)));
		questions
				.add(new Question(
						"The start-up of a computer from a powered-down state is called a ___.",
						new Answer("Cold boot", true), new Answer("Warm boot",
								false), new Answer("Soft boot", false),
						new Answer("Reboot", false)));
		questions
				.add(new Question(
						"Software that restricts access to data on a network is called a(n) ___.",
						new Answer("Firewall", true), new Answer("Ad blocker",
								false), new Answer("Antivirus program", false),
						new Answer("VPN", false)));
		questions.add(new Question("ASCII is a 7-bit or 8-bit ___ system.",
				new Answer("Encoding", true), new Answer("Decoding", false),
				new Answer("Programming", false), new Answer("Gaming", false)));
		questions.add(new Question(
				"An intranet is a local or ___ communications network.",
				new Answer("Restricted", true), new Answer("Unrestricted",
						false), new Answer("Useful", false), new Answer(
						"Secure", false)));
		questions
				.add(new Question(
						"A ___ is a clickable object that links you to a file or web page.",
						new Answer("Hyperlink", true), new Answer("Superlink",
								false), new Answer("Linkbox", false),
						new Answer("Checkbox", false)));
		questions
				.add(new Question(
						"Which of the following is the name of an Intel microprocessor?",
						new Answer("Pentium", true), new Answer("Phenom II",
								false), new Answer("Tegra 4", false),
						new Answer("Snapdragon", false)));
		questions.add(new Question("There are ___ bits in one byte.",
				new Answer("8", true), new Answer("16", false), new Answer("2",
						false), new Answer("4", false)));
		questions.add(new Question("What does RAM stand for?", new Answer(
				"Random-Access Memory", true), new Answer(
				"Recurring-Access Memory", false), new Answer(
				"Random-Accurate Memory", false), new Answer(
				"Recurring-Accurate Memory", false)));
		questions.add(new Question(
				"A CD-ROM can store up to ___ megabytes of data.", new Answer(
						"700", true), new Answer("200", false), new Answer(
						"500", false), new Answer("800", false)));
		questions.add(new Question(
				"A ___ is a system of computers linked together.", new Answer(
						"Network", true), new Answer("Website", false),
				new Answer("Server", false), new Answer("Mainframe", false)));
		questions.add(new Question("What does URL stand for?", new Answer(
				"Universal Resourse Locator", true), new Answer(
				"Universal Resourse Limiter", false), new Answer(
				"United Resourse Locator", false), new Answer(
				"United Resourse Limiter", false)));
		questions.add(new Question("What does HTTP stand for?", new Answer(
				"Hyper-Text Transfer Protocol", true), new Answer(
				"Hyper-Text Transfer Property", false), new Answer(
				"Hyper-Text Thread Protocol", false), new Answer(
				"Hyper-Text Thread Property", false)));
		questions.add(new Question("The ___ is the location of the CPU.",
				new Answer("Motherboard", true),
				new Answer("Hard drive", false), new Answer("Power supply",
						false), new Answer("RAM", false)));
		questions.add(new Question("What type of interface does DOS use?",
				new Answer("Command line", true),
				new Answer("Graphical", false), new Answer("Point-and-click",
						false), new Answer("Touch", false)));
		questions.add(new Question("What does HTML stand for?", new Answer(
				"Hyper-Text Markup Language", true), new Answer(
				"Hyper-Text Markup Library", false), new Answer(
				"Hyper-Text Media Language", false), new Answer(
				"Hyper-Text Media Library", false)));
		questions.add(new Question(
				"The ___ datatype stores a sequence of characters.",
				new Answer("String", true), new Answer("Character", false),
				new Answer("Boolean", false), new Answer("Integer", false)));
		questions.add(new Question(
				"The ___ layout is the standard keyboard layout.", new Answer(
						"QWERTY", true), new Answer("QWERTZ", false),
				new Answer("AZERTY", false), new Answer("QZERTY", false)));
		questions
				.add(new Question(
						"A small picture of an image or page layout is called a(n) ___.",
						new Answer("Thumbnail", true),
						new Answer("Icon", false), new Answer("Logo", false),
						new Answer("Toenail", false)));
		questions.add(new Question("What does LAN stand for?", new Answer(
				"Local Area Network", true), new Answer("Limited Area Network",
				false), new Answer("Local Access Network", false), new Answer(
				"Limited Access Network", false)));
		for (Question q : questions)
			Collections.shuffle(q.getAnswers());
		Collections.shuffle(questions);
	}
}