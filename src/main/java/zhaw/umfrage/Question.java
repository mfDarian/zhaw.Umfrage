/**
 * 
 */
package zhaw.umfrage;

import java.util.ArrayList;

/**
 * @author Darian
 *
 */
public class Question extends SurveyTreeAbstract {
	
	private static final long serialVersionUID = 1L;
	private int id;
	private int minAnswersToChose = 0;
	private int maxAnswersToChose = 0;
	private int answerIdCounter;
	private transient boolean answered;
	
	protected Question(Questionnaire owner, String text, int id){
		super(text, owner);
		this.id = id;
		answerIdCounter = 0;
	}

	@Override
	public SurveyTreeAbstract insertItem(String text) {
		Answer a = new Answer(this, text, nextAnswerId());
		super.addItem(a);
		return a;
	}

	@Override
	public SurveyTreeAbstract insertItem() {
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

	public final void setMinAnswersToChose(int minAnswersToChose) {
		this.minAnswersToChose = Math.min(minAnswersToChose, itemList.size()); //TODO eine Exception wäre vermutlich schöner
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
	}

	public final void setMaxAnswersToChose(int maxAnswersToChose) {
		this.maxAnswersToChose = Math.min(maxAnswersToChose, itemList.size()); //TODO eine Exception wäre vermutlich schöner
		expose();
		try {
			setAnswered(false);
		} catch (QuestionAnswerCountException ex) {
			//Nothing to do
		}
	}
	
	public final boolean isSingleSelect() {
		return (maxAnswersToChose == 1);
	}

	public boolean isAnswered() {
		return answered;
	}

	@Override
	public void reset() {
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

	@SuppressWarnings("unchecked")
	@Override
	protected void calcMinScoreAchieveable() {
		if (itemList == null || itemList.isEmpty()) {
			minScoreAchieveable = 0;
		} else {
			ArrayList<SurveyTreeAbstract> challengerList;
			Answer candidate;
			challengerList = (ArrayList<SurveyTreeAbstract>) itemList.clone();
			candidate = (Answer) itemList.get(0);
			ArrayList<Answer> answersByScoreChosen = new ArrayList<>();
			while (!challengerList.isEmpty()) {
				for (SurveyTreeAbstract t : itemList) {
					if (challengerList.contains(t)) {
						Answer challenger = (Answer)t;
						if (candidate.getScoreIfChosen() > challenger.getScoreIfChosen()) {
							candidate = challenger;
						}
					}
				}
				answersByScoreChosen.add(candidate);
				challengerList.remove(candidate);
				if (!challengerList.isEmpty()) {
					candidate = (Answer) challengerList.get(0);
				}
			}
			
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
				for (Answer a : chosen) {
					if (!a.isUnreachable()) {
						simulatedScore += a.getScoreIfChosen();
					}
				}
				if (simCount == 0) {
					lowestScore = simulatedScore;
				}
				lowestScore = Math.min(lowestScore, simulatedScore);
				answerCount++;
				simCount++;
			}
		
			minScoreAchieveable = lowestScore;
			
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void calcMaxScoreAchieveable() {
		if (itemList == null || itemList.isEmpty()) {
			maxScoreAchieveable = 0;
		} else {
			ArrayList<SurveyTreeAbstract> challengerList;
			Answer candidate;
			challengerList = (ArrayList<SurveyTreeAbstract>) itemList.clone();
			candidate = (Answer) itemList.get(0);
			ArrayList<Answer> answersByScoreChosen = new ArrayList<>();
			while (!challengerList.isEmpty()) {
				for (SurveyTreeAbstract t : itemList) {
					if (challengerList.contains(t)) {
						Answer challenger = (Answer)t;
						if (candidate.getScoreIfChosen() < challenger.getScoreIfChosen()) {
							candidate = challenger;
						}
					}
				}
				answersByScoreChosen.add(candidate);
				challengerList.remove(candidate);
				if (!challengerList.isEmpty()) {
					candidate = (Answer) challengerList.get(0);
				}
			}
			
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
	
	protected int nextAnswerId() {
		return ++answerIdCounter;
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


}
