/**
 * 
 */
package zhaw.umfrage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Daniel Langhart
 *
 */
public class Question extends SurveyTreeAbstract {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private int minAnswersToChose = 0;
	private int maxAnswersToChose = 0;
	private transient boolean answered;
	
	protected Question(Survey root, Questionnaire owner, String text, int id){
		super(text, owner, root);
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Question)) {
			return false;
		}
		Question q = (Question) o;
		if (minAnswersToChose != q.getMinAnswersToChose()) {
			return false;
		}
		if (maxAnswersToChose != q.getMaxAnswersToChose()) {
			return false;
		}
		if (id != q.getId()) {
			return false;
		}
		return super.equals(o);
	}

	@Override
	public SurveyTreeAbstract insertItem(String text) throws SurveyFrozenException {
		Answer a = new Answer(root, this, text, owner.nextAnswerId());
		super.addItem(a);
		return a;
	}

	@Override
	public SurveyTreeAbstract insertItem() throws SurveyFrozenException {
		return insertItem("New Answer");
	}
	
	@Override
	protected void notifyScoreChange() {
		int score = 0;
		// only answered questions do score, otherwise the score is 0
		if (answered) {
			for (SurveyTreeAbstract t : itemList) {
				Answer a = (Answer) t;
				if (a.isChosen()) {
					score += a.getScoreIfChosen();
				}
			}
		}
		setScore(score);
	}
	

	public final void setAnswered(boolean answered) throws QuestionAnswerCountException {
		int answerCount = 0;
		if (answered) {
			for (SurveyTreeAbstract t : itemList) {
				Answer a = (Answer) t;
				if (a.isChosen()) {
					answerCount++;
				}
			}
			if ((minAnswersToChose > 0 && answerCount < minAnswersToChose) || (maxAnswersToChose > 0 && answerCount > maxAnswersToChose)) {
				throw new QuestionAnswerCountException(this);
			}
		}
		this.answered = answered;
		notifyScoreChange(); // trigger propagation
	}
	

	public final int getMinAnswersToChose() {
		return minAnswersToChose;
	}

	public final int getMaxAnswersToChose() {
		return maxAnswersToChose;
	}

	public final boolean setMinAnswersToChose(int minAnswersToChose) throws SurveyFrozenException {
		if (minAnswersToChose == this.minAnswersToChose) {
			return false;
		}
		checkRootNotFrozen();
		if (minAnswersToChose > itemList.size() || minAnswersToChose < 0) {
			return false;
		}
		this.minAnswersToChose = minAnswersToChose;
		// auto-set maxAnswersToChose if not 0 and below mew minimum
		if (maxAnswersToChose != 0 && maxAnswersToChose < this.minAnswersToChose) {
			setMaxAnswersToChose(this.minAnswersToChose);
		} else {
			expose();
			try {
				setAnswered(false);
			} catch (QuestionAnswerCountException ex) {
				//Nothing to do
			}
		}
		return true;
	}

	public final boolean setMaxAnswersToChose(int maxAnswersToChose) throws SurveyFrozenException {
		if (maxAnswersToChose == this.maxAnswersToChose) {
			return false;
		}
		checkRootNotFrozen();
		if (maxAnswersToChose > itemList.size() || maxAnswersToChose < 0) {
			return false;
		}
		this.maxAnswersToChose = maxAnswersToChose;
		expose();
		
		try {
			setAnswered(false);
		} catch (QuestionAnswerCountException ex) {
			//Nothing to do
		}
		return true;
	}
	
	public final boolean isMandatory() {
		return (minAnswersToChose != 0);
	}
	
	public final boolean isSingleSelect() {
		return (maxAnswersToChose == 1);
	}

	public boolean isAnswered() {
		return answered;
	}

	@Override
	public void reset() throws SurveyFrozenException {
		super.reset();
		try {
			setAnswered(false);
		} catch (QuestionAnswerCountException ex) {
			//Nothing to do
		}
	}
	
	@Override
	public boolean isReachable() {
		// answered questions must stay reachable
		if (answered) {
			return true;
		}
		return super.isReachable();
	}

	@Override
	//TODO zeigen
	protected void calcMinScoreAchieveable() {
		if (itemList == null || itemList.isEmpty()) {
			// first try the cheap way
			minScoreAchieveable = 0;
		} else {
			// we need a list of our answers that is sorted by score (ascending)
			ArrayList<Answer> answersByScoreChosen = new ArrayList<>();
			for (SurveyTreeAbstract t : itemList) {
				answersByScoreChosen.add((Answer)t);
			}
			// sort with special comparator
			Collections.sort(answersByScoreChosen, new AnswersByScoreChosen(false));
			
			// now do simulations of divers answer selections, respecting the settings
			// always taking the ones with lowest score first
			ArrayList<Answer> chosen = new ArrayList<>();
			int simCount = 0;
			int lowestScore = 0;
			int simulatedScore = 0;
			int answerCount = getMinAnswerCount();
			while (answerCount <= getMaxAnswerCount()) {
				chosen.clear();
				simulatedScore = 0;
				for (int i = 0; i < answerCount; i++) {
					chosen.add(answersByScoreChosen.get(i));
				}
				// only reachable answers can be chosen!
				for (Answer a : chosen) {
					if (!a.isUnreachable()) {
						simulatedScore += a.getScoreIfChosen();
					}
				}
				if (simCount == 0) {
					// the first simulation score must always be set as the lowest for comparing later
					lowestScore = simulatedScore;
				}
				// after simulation actualize the lowest score we could achieve
				lowestScore = Math.min(lowestScore, simulatedScore);
				answerCount++;
				simCount++;
			}
		
			minScoreAchieveable = lowestScore;
			
		}
		
	}


	@Override
	protected void calcMaxScoreAchieveable() {
		if (itemList == null || itemList.isEmpty()) {
			maxScoreAchieveable = 0;
		} else {

			ArrayList<Answer> answersByScoreChosen = new ArrayList<>();
			for (SurveyTreeAbstract t : itemList) {
				answersByScoreChosen.add((Answer)t);
			}
			Collections.sort(answersByScoreChosen, new AnswersByScoreChosen(true));

			ArrayList<Answer> chosen = new ArrayList<>();
			int simCount = 0;
			int highestScore = 0;
			int simulatedScore = 0;
			int answerCount = getMinAnswerCount();
			while (answerCount <= getMaxAnswerCount()) {
				chosen.clear();
				simulatedScore = 0;
				for (int i = 0; i < answerCount; i++) {
					chosen.add(answersByScoreChosen.get(i));
				}
				for (Answer a : chosen) {
					if (!a.isUnreachable()) {
						simulatedScore += a.getScoreIfChosen();
					}
				}
				if (simCount == 0) {
					highestScore = simulatedScore;
				}
				highestScore = Math.max(highestScore, simulatedScore);
				answerCount++;
				simCount++;
			}
		
			maxScoreAchieveable = highestScore;
			
		}
	}

	public final int getId() {
		return id;
	}
	
	
	private int getMinAnswerCount() {
		return minAnswersToChose;
	}

	private int getMaxAnswerCount() {
		int maxAnswerCount = maxAnswersToChose;
		if (maxAnswerCount == 0) {
			if (itemList != null) {
				maxAnswerCount = itemList.size();
			}
		}
		return maxAnswerCount;
	}
	
	
	class AnswersByScoreChosen implements Comparator<Answer>{
		
		private boolean descending;
		
		AnswersByScoreChosen(boolean descending) {
			this.descending = descending;
		}

		@Override
		public int compare(Answer a1, Answer a2) {
			if (a1.getScoreIfChosen() < a2.getScoreIfChosen()) {
				if (descending) {
					return 1;
				} else {
					return -1;
				}
			}
			if (a1.getScoreIfChosen() > a2.getScoreIfChosen()) {
				if (descending) {
					return -1;
				} else {
					return 1;
				}
			}
			return 0;
		}
		
	}


}
