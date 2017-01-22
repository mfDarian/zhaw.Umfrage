package zhaw.umfrage.interview;

import java.util.HashSet;
import zhaw.umfrage.*;

public class InterviewController {
	
	private HashSet<View> views;
	private Survey survey;
	private Summary summary;
	private Interview interview;
	private Questionnaire actualQuestionnaire;
	private Question actualQuestion;
	private boolean running;
	
	public InterviewController() {
		views = new HashSet<View>();
	}
	
	
	public void setSurvey(Survey survey) {
		this.survey = survey;
		summary = new Summary(survey);
		survey.reset();
	}
	
	public void setSummary(Summary summary) {
		this.summary = summary;
	}
	
	public void startInterview(String interviewer, String interviewee) {
		interview = new Interview(survey, interviewer, interviewee);
		survey.reset();
		running = true;
		for (View v : views) {
			v.interviewStarted();
		}
	}
	
	public void proceed() throws QuestionAnswerCountException {
		
		if (!running) {
			System.out.println("NOT RUNNING!");
			return; //TODO exception
		}
		
		if (actualQuestion != null && !actualQuestion.isAnswered()) {
			actualQuestion.setAnswered(true);
		}
		
		if (running && actualQuestionnaire == null) {
			actualQuestionnaire = (Questionnaire) survey.getNextItem();
			for (View v : views) {
				v.showQuestionnaire(actualQuestionnaire);
			}
		}
		
		
		Question nextQuestion = (Question) actualQuestionnaire.getNextItem();
		if (nextQuestion == null) {
			actualQuestionnaire = (Questionnaire) survey.getNextItem();
			if (actualQuestionnaire != null) {
				for (View v : views) {
					v.showQuestionnaire(actualQuestionnaire);
				}
				nextQuestion = (Question) actualQuestionnaire.getNextItem();
			}
		}
		if (nextQuestion == null) {
			for (View v : views) {
				v.interviewFinished();
				running = false;
			}
		} else {
			actualQuestion = nextQuestion;
			for (View v : views) {
				v.showQuestion(nextQuestion);
				for (SurveyTreeAbstract a : actualQuestion.getItemList()) {
					v.showAnswer((Answer) a);
				}
			}
		}
		
	}
	
	public void setAnswerChosen(Answer answer, boolean chosen) {
		if (actualQuestion != null) {
			for (SurveyTreeAbstract a : actualQuestion.getItemList()) {
				if (a == answer) {
					if (chosen != ((Answer)a).isChosen()) {
						((Answer)a).setChosen(chosen);
						for (View v : views) {
							v.setAnswerChosen(answer, chosen);
						}
					}
				}
			}
		}
	}
	
	public void abortInterview() {
		interview = null;
		survey.reset();
		for (View v : views) {
			v.interviewAborted();
		}
		running = false;
	}
	
	public Interview confirmFinished() {
		survey.reset();
		running = false;
		interview.freeze();
		return interview;
	}
	
	public void addView(View v) {
		views.add(v);
		// Bring the new view up to date if needed
		if (running) {
			if (actualQuestionnaire != null) {
				v.showQuestionnaire(actualQuestionnaire);
				if (actualQuestion != null) {
					v.showQuestion(actualQuestion);
					for (SurveyTreeAbstract a : actualQuestion.getItemList()) {
						v.showAnswer((Answer) a);
					}
				}
			}
		}

	}
	
	public void removeView(View v) {
		views.remove(v);
	}
	
	public boolean isRunning() {
		return running;
	}
	
}
