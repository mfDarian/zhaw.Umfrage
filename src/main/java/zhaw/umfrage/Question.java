/**
 * 
 */
package zhaw.umfrage;

import javax.swing.JList;

/**
 * @author Darian
 *
 */
public class Question extends SurveyTreeAbstract {
	
	private static final long serialVersionUID = 1L;
	private int minAnswersToChose = 0;
	private int maxAnswersToChose = 0;
	private transient boolean answered;
	private int countanswers;
	
	protected Question(Questionnaire owner, String text){
		super(text, owner);
	}

	@Override
	public SurveyTreeAbstract insertItem(String text) {
		Answer a = new Answer(this, text);
		super.addItem(a);
		return a;
	}

	@Override
	public SurveyTreeAbstract insertItem() {
		return insertItem("New Answer");
	}
	
	
	
	
	
	
	@Override
	protected void notifyScoreChange(SurveyTreeAbstract item) {
		int score = 0;
		for (SurveyTreeAbstract t : itemList) {
			Answer a = (Answer) t;
			if (a.isChosen()) {
				score += a.getScoreIfChosen();
			} else {
				score += a.getScoreIfUnchosen();
			}
		}
		setScore(score);
	}
	
	public final void setAnswered(boolean answered) throws QuestionAnswerCountException { //Swen 18.01 Auf diese Methode muss ich im Interview zugreifen können)
		int answerCount = 0;
		for (SurveyTreeAbstract t : itemList) {
			Answer a = (Answer) t;
			if (a.isChosen()) {
				answerCount++; //19.01 Swen: Ich meine, dieser answer-Count sollte in eineInstanz-Variable geschrieben werden, (inkl einer get();-Methode, welche die InterviewKlasse in ihrer proceedInterview();-Funktion abrufen kann.
			countanswers = answerCount;
			}
		}
		if ((minAnswersToChose > 0 && answerCount < minAnswersToChose) || (maxAnswersToChose > 0 && answerCount > maxAnswersToChose)) {
			throw new QuestionAnswerCountException(this);
		}
		this.answered = answered;
	}
	

	public int getCountanswers() {
		return countanswers;
	}

	public final int getMinAnswersToChose() {
		return minAnswersToChose;
	}

	public final int getMaxAnswersToChose() {
		return maxAnswersToChose;
	}

	public final void setMinAnswersToChose(int minAnswersToChose) {
		this.minAnswersToChose = minAnswersToChose;
		// auto-set maxAnsweresToChose if not 0 and below mew minimum
		if (maxAnswersToChose != 0 && maxAnswersToChose < this.minAnswersToChose) {
			setMaxAnswersToChose(this.minAnswersToChose);
		}
	}

	public final void setMaxAnswersToChose(int maxAnswersToChose) {
		this.maxAnswersToChose = maxAnswersToChose;
	}
	
	public final boolean isSingleSelect() {
		return (maxAnswersToChose == 1);
	}

	public boolean isAnswered() {
		return answered;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		super.reset();
		answered = false;
	}
	
	 
	public void questiongiveAnswers() { // 19.01: Schreibt alle Antworten, welche zu einer Frage gehört in einen STring-Array.
		
		 JList<String> currentanswers = new JList<>(itemList.toArray(new String[0]));
	}
	
	
	
	
	
	
	
	
}
