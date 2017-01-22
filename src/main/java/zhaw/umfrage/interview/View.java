/**
 * 
 */
package zhaw.umfrage.interview;

import zhaw.umfrage.Answer;
import zhaw.umfrage.Question;
import zhaw.umfrage.Questionnaire;

/**
 * @author Darian
 *
 */
public interface View {
	
	void interviewStarted();
	
	void interviewAborted();
	
	void interviewFinished();
	
	void showQuestionnaire(Questionnaire questionnaire);
	
	void showQuestion(Question question);
	
	void showAnswer(Answer answer);
	
	void setAnswerChosen(Answer answer, boolean chosen);

}
