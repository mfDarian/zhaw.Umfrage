/**
 * 
 */
package zhaw.umfrage;

import java.io.Serializable;
import java.util.HashSet;

import zhaw.umfrage.*;

/**
 * @author Darian
 *
 */
public class Interview implements Serializable {
	
	private static final long serialVersionUID = 1L;
	transient private Survey survey;
	private String interviewer;
	private String interviewee;
	private HashSet<Integer> questionsAsked;
	private HashSet<Integer> answersChosen;
	private int score;
	private boolean frozen;

	
	public Interview(Survey survey, String interviewer, String interviewee) {
		this.survey = survey;
		this.interviewer = interviewer;
		this.interviewee = interviewee;
		questionsAsked = new HashSet<Integer>();
		answersChosen = new HashSet<Integer>();
	}
	
	public Survey getSurvey() {
		return survey;
	}
	
	public void setInterviewer(String interviewer) {
		this.interviewer = interviewer;
	}
	
	public String getInterviewer() {
		return interviewer;
	}
	
	public void setInterviewee(String interviewee) {
		this.interviewee = interviewee;
	}

	public String getInterviewee() {
		return interviewee;
	}
	
	public void addQuestion(Question q) {
		questionsAsked.add(q.getId());
	}
	
	public void addAnswers(Question q) {
		for (SurveyTreeAbstract t : q.getItemList()) {
			Answer a = (Answer) t;
			if (a.isChosen()) {
				answersChosen.add(a.getId());
				System.out.println("Answer added!");
			}
		}
	}
	
	public int getScore() {
		return score;
	}
	
	public void freeze() {
		score = survey.getScore();
		System.out.println("Interview eingefroren");
		frozen = true;
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public void printAll() {
		survey.printChain();
	}
}
