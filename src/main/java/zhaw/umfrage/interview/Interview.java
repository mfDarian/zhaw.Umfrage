/**
 * 
 */
package zhaw.umfrage.interview;

import java.io.Serializable;

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
	private boolean frozen;

	
	public Interview(Survey survey, String interviewer, String interviewee) {
		this.survey = survey;
		this.interviewer = interviewer;
		this.interviewee = interviewee;
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
	
	public void freeze() {
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
