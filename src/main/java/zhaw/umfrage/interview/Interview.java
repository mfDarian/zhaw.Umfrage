/**
 * 
 */
package zhaw.umfrage.interview;

import zhaw.umfrage.Survey;

/**
 * @author Darian
 *
 */
public class Interview {
	
	transient Survey survey;
	private String interviewer;
	private String interviewee;
	
	
	public Interview(Survey survey) {
		this.survey = survey;
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

}
