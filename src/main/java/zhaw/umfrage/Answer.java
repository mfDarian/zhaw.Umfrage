/**
 * 
 */
package zhaw.umfrage;

/**
 * @author Darian
 *
 */
public class Answer extends SurveyTreeAbstract {
	
	//TODO: Wenn die Frage schon beantwortet ist, muss das chose und unchose eine Exception werfen!
	
	private static final long serialVersionUID = 1L;
	private int id;
	private int scoreIfChosen = 0;
	private transient boolean chosen;
	
	public Answer(Question owner, String text, int id) {
		super(text, owner);
		this.id = id;
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
		expose();
	}

	public final boolean isChosen() {
		return chosen;
	}

	public final void setChosen(boolean chosen) {
		this.chosen = chosen;
		if (chosen) {
			setScore(scoreIfChosen);
		} else {
			setScore(0);
		}
	}

	@Override
	public void reset() {
		super.reset();
		chosen = false;
	}
	
	/*
	@Override
	public boolean isReachable() {
		return owner.isReachable();
	}
	*/
	

	@Override
	protected void calcMinScoreAchieveable() {
		minScoreAchieveable = Math.min(scoreIfChosen, 0);
	}

	@Override
	protected void calcMaxScoreAchieveable() {
		maxScoreAchieveable = Math.max(scoreIfChosen, 0);
	}


	public final int getId() {
		return id;
	}

	
}
