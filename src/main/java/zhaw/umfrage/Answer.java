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
	
	protected Answer(Survey root, Question owner, String text, int id) {
		super(text, owner, root);
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Answer)) {
			return false;
		}
		Answer a = (Answer) o;
		if (scoreIfChosen != a.getScoreIfChosen()) {
			return false;
		}
		if (id != a.getId()) {
			return false;
		}
		return super.equals(o);
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

	public final void setScoreIfChosen(int scoreIfChosen) throws SurveyFrozenException {
		checkRootNotFrozen();
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
	public void reset() throws SurveyFrozenException {
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
