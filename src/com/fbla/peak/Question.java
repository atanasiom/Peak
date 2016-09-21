package com.fbla.peak;

import java.util.ArrayList;

public class Question {
	private String question;
	private Answer answerOne;
	private Answer answerTwo;
	private Answer answerThree;
	private Answer answerFour;
	private ArrayList<Answer> answers = new ArrayList<Answer>();

	/**
	 * @param question
	 * @param answerOne
	 * @param answerTwo
	 * @param answerThree
	 * @param answerFour
	 */
	public Question(String question, Answer answerOne, Answer answerTwo,
			Answer answerThree, Answer answerFour) {
		this.question = question;
		this.answerOne = answerOne;
		this.answerTwo = answerTwo;
		this.answerThree = answerThree;
		this.answerFour = answerFour;
		answers.add(answerOne);
		answers.add(answerTwo);
		answers.add(answerThree);
		answers.add(answerFour);
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Answer getAnswerOne() {
		return answerOne;
	}

	public void setAnswerOne(Answer answerOne) {
		this.answerOne = answerOne;
	}

	public Answer getAnswerTwo() {
		return answerTwo;
	}

	public void setAnswerTwo(Answer answerTwo) {
		this.answerTwo = answerTwo;
	}

	public Answer getAnswerThree() {
		return answerThree;
	}

	public void setAnswerThree(Answer answerThree) {
		this.answerThree = answerThree;
	}

	public Answer getAnswerFour() {
		return answerFour;
	}

	public void setAnswerFour(Answer answerFour) {
		this.answerFour = answerFour;
	}

	public ArrayList<Answer> getAnswers() {
		return answers;
	}
}
