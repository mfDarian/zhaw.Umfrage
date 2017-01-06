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

	public int getMinAnswersToChose() {
		return minAnswersToChose;
	}

	public void setMinAnswersToChose(int minAnswersToChose) {
		this.minAnswersToChose = minAnswersToChose;
	}

	public int getMaxAnswersToChose() {
		return maxAnswersToChose;
	}

	public void setMaxAnswersToChose(int maxAnswersToChose) {
		this.maxAnswersToChose = maxAnswersToChose;
	}
	
}
