package zhaw.umfrage;

import java.util.ArrayList;
import zhaw.umfrage.*;

public class ConsultationController {
	
	private ArrayList<ConsultationView> consultationViews;
	private Survey survey;
	private Summary summary;
	private Interview interview;
	private Questionnaire actualQuestionnaire;
	private Question actualQuestion;
	private boolean running;
	
	public ConsultationController() {
		consultationViews = new ArrayList<ConsultationView>();
	}
	
	
	public void setSurvey(Survey survey) {
		if (survey != null) {
			this.survey = survey;
			summary = new Summary(survey);
			try {
				survey.reset();
			} catch (SurveyFrozenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (ConsultationView v : consultationViews) {
				v.summaryLoaded(this.summary);
			}
		}
	}
	
	public void setSummary(Summary summary) {
		if (summary != null) {
			if (!survey.equals(summary.getSurvey())) {
				 //TODO survey not equal exception
			}
			this.summary = summary;
		}
	}
	
	public void startInterview(String interviewer, String interviewee) {
		interview = new Interview(survey, interviewer, interviewee);
		try {
			survey.reset();
		} catch (SurveyFrozenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		running = true;
		for (ConsultationView v : consultationViews) {
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
			interview.addAnswers(actualQuestion);
		}
		
		if (running && actualQuestionnaire == null) {
			actualQuestionnaire = (Questionnaire) survey.getNextItem();
			for (ConsultationView v : consultationViews) {
				v.showQuestionnaire(actualQuestionnaire);
			}
		}
		
		Question nextQuestion = (Question) actualQuestionnaire.getNextItem();
		if (nextQuestion == null) {
			actualQuestionnaire = (Questionnaire) survey.getNextItem();
			if (actualQuestionnaire != null) {
				for (ConsultationView v : consultationViews) {
					v.showQuestionnaire(actualQuestionnaire);
				}
				nextQuestion = (Question) actualQuestionnaire.getNextItem();
			}
		}
		if (nextQuestion == null) {
			running = false;
			interview.freeze();
			summary.addInterview(interview);
			for (ConsultationView v : consultationViews) {
				v.interviewFinished();
				v.summaryUpdated();
			}
			try {
				survey.reset();
			} catch (SurveyFrozenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			actualQuestion = nextQuestion;
			interview.addQuestion(actualQuestion);
			for (ConsultationView v : consultationViews) {
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
						for (ConsultationView v : consultationViews) {
							v.setAnswerChosen(answer, chosen);
						}
					}
				}
			}
		}
	}
	
	public void abortInterview() {
		if (running) {
			interview = null;
			try {
				survey.reset();
			} catch (SurveyFrozenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (ConsultationView v : consultationViews) {
				v.interviewAborted();
			}
			running = false;
		}
	}
	
	public Interview confirmFinished() {
		//TODO running exception wenn noch am laufen
		return interview;
	}
	
	public void addView(ConsultationView v) {
		consultationViews.add(v);
		// Bring the new view up to date if needed
		v.summaryLoaded(summary);
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
	
	public void removeView(ConsultationView v) {
		consultationViews.remove(v);
	}
	
	public boolean isRunning() {
		return running;
	}
	
}
