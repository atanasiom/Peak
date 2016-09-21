package com.fbla.peak;

public class Answer {
	private String answer;
	private boolean isTrue;

	/**
	 * @param answer
	 *            the string of the answer
	 * @param isTrue
	 *            if the answer is true
	 */
	public Answer(String answer, boolean isTrue) {
		this.answer = answer;
		this.isTrue = isTrue;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public boolean isTrue() {
		return isTrue;
	}

	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}
}