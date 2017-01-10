/**
 * 
 */
package zhaw.umfrage;

/**
 * @author Darian
 *
 */
public class Question extends SurveyTreeAbstract {
	
	private static final long serialVersionUID = 1L;
	private int minAnswersToChose = 0;
	private int maxAnswersToChose = 0;
	private transient boolean answered;
	
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
		score = 0;
		for (SurveyTreeAbstract t : itemList) {
			Answer a = (Answer) t;
			if (a.isChosen()) {
				score += a.getScoreIfChosen();
			} else {
				score += a.getScoreIfUnchosen();
			}
		}
	}
	
	public final void setAnswered(boolean answered) throws QuestionAnswerCountException {
		int answerCount = 0;
		for (SurveyTreeAbstract t : itemList) {
			Answer a = (Answer) t;
			if (a.isChosen()) {
				answerCount++;
			}
		}
		if ((minAnswersToChose > 0 && answerCount < minAnswersToChose) || (maxAnswersToChose > 0 && answerCount > maxAnswersToChose)) {
			throw new QuestionAnswerCountException(this);
		}
		this.answered = answered;
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
}
