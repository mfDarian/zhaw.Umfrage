package zhaw.umfrage.interview;

import zhaw.umfrage.*;
import java.util.HashSet;

public class Summary {
	
	private Survey survey;
	private HashSet<Interview> interviewList;
	
	public Summary(Survey survey) {
		this.survey = survey;
	}
	
	public void addInterview(Interview interview) {
		//TODO der Survey muss equal sein!
		//TODO Interview muss frozen sein
	}

}
