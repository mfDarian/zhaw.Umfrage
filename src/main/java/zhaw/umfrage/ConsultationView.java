/**
 * 
 */
package zhaw.umfrage;

/**
 * @author Darian
 *
 */
public interface ConsultationView {
	
	void interviewStarted();
	
	void interviewAborted();
	
	void interviewFinished();
	
	void summaryLoaded(Summary summary);
	
	void summaryUpdated();
	
	void showQuestionnaire(Questionnaire questionnaire);
	
	void showQuestion(Question question);
	
	void showAnswer(Answer answer);
	
	void setAnswerChosen(Answer answer, boolean chosen);

}
