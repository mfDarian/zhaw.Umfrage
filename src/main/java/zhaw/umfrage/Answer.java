/**
 * 
 */
package zhaw.umfrage;

/**
 * @author Darian
 *
 */
public class Answer extends SurveyTreeAbstract {
	
	private static final long serialVersionUID = 1L;
	private int scoreIfChosen = 0;
	private int scoreIfUnchosen = 0;
	private transient boolean chosen;
	
	public Answer(Question owner, String text) {
		super(text, owner);
	}
	
	@Override
	public SurveyTreeAbstract insertItem(String text) {
		return null;
	}

	@Override
	public SurveyTreeAbstract insertItem() {
		return null;
	}

	public final int getScoreIfChosen() {
		return scoreIfChosen;
	}

	public final void setScoreIfChosen(int scoreIfChosen) {
		this.scoreIfChosen = scoreIfChosen;
	}

	public final int getScoreIfUnchosen() {
		return scoreIfUnchosen;
	}

	public final void setScoreIfUnchosen(int scoreIfUnchosen) {
		this.scoreIfUnchosen = scoreIfUnchosen;
	}

	public final boolean isChosen() {
		return chosen;
	}

	public final void setChosen(boolean chosen) {
		if (chosen) {
			setScore(getScore() + scoreIfChosen);
		} else {
			setScore(getScore() + scoreIfUnchosen);
		}
		this.chosen = chosen;
	}





}
