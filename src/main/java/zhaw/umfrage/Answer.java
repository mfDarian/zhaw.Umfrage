/**
 * 
 */
package zhaw.umfrage;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
		this.chosen = chosen;
		if (chosen) {
			setScore(scoreIfChosen);
		} else {
			setScore(scoreIfUnchosen);
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		super.reset();
		chosen = false;
	}
 

}
