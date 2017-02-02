package zhaw.umfrage;

import zhaw.umfrage.*;
import java.util.ArrayList;

public class Summary {
	
	private Survey survey;
	private ArrayList<Interview> interviewList;
	
	public Summary(Survey survey) {
		this.survey = survey;
		interviewList = new ArrayList<Interview>();
	}
	
	public Survey getSurvey() {
		return survey;
	}
	
	public Integer getInterviewCount() {
		return interviewList.size();
	}
	
	public void addInterview(Interview interview) {
		//TODO der Survey muss equal sein!
		//TODO Interview muss frozen sein!
		interviewList.add(interview);
		System.out.println("Summary now has " + interviewList.size() + " Interviews");
	}
	
	public void mergeInto(Summary target) {
		for (Interview i : interviewList) {
			target.addInterview(i);
		}
	}

}