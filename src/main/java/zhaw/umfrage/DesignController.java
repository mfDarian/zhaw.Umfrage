package zhaw.umfrage;

import java.util.ArrayList;

public class DesignController {
	
	private ArrayList<DesignView> views;
	private Survey survey;
	private SurveyTreeAbstract selectedItem;
	
	public DesignController() {
		views = new ArrayList<DesignView>();
	}
	
	public void setSurvey(Survey survey) {
		if (survey != null) {
			this.survey = survey;
			notifyStructureChange();
			selectItem(this.survey);
			for (DesignView v : views) {
				v.surveyFrozen(survey.isFrozen());
			}
		}
	}
	
	public Survey getSurvey() {
		return survey;
	}
	
	public void newSurvey() {
		Survey s = new Survey("New Survey");
		setSurvey(s);
	}
	
	public void freezeSurvey() throws UnreachableItemsException {
		if (survey != null) {
			survey.freeze();
			for (DesignView v : views) {
				v.surveyFrozen(survey.isFrozen());
			}
		}
	}
	
	public void selectItem(SurveyTreeAbstract item) {
		selectedItem = item;
		for (DesignView v : views) {
			v.selectionChanged(selectedItem);
		}
		if (selectedItem == null) {
			for (DesignView v : views) {
				v.itemAddable(false);
				v.itemRemoveable(false);
			}
		} else if (selectedItem instanceof Survey) {
			for (DesignView v : views) {
				v.surveySelected((Survey)selectedItem);
				v.itemAddable(true && (!survey.isFrozen()));
				v.itemRemoveable(false);
			}
		} else if (selectedItem instanceof Questionnaire) {
			for (DesignView v : views) {
				v.questionnaireSelected((Questionnaire)selectedItem);
				v.itemAddable(true && (!survey.isFrozen()));
				v.itemRemoveable(true && (!survey.isFrozen()));
			}
		} else if (selectedItem instanceof Question) {
			for (DesignView v : views) {
				v.questionSelected((Question)selectedItem);
				v.itemAddable(true && (!survey.isFrozen()));
				v.itemRemoveable(true && (!survey.isFrozen()));
			}
		} else if (selectedItem instanceof Answer) {
			for (DesignView v : views) {
				v.answerSelected((Answer)selectedItem);
				v.itemAddable(false);
				v.itemRemoveable(true && (!survey.isFrozen()));
			}
		}
		notifySortPossibilities();
	}
	
	private void notifySortPossibilities() {
		boolean upPossible = false;
		boolean downPossible = false;
		if (selectedItem != null && !(survey.isFrozen())) {
			if (!(selectedItem instanceof Survey)) {
				if (selectedItem.getSort() > 1) {
					upPossible = true;
				}
				if (selectedItem.getSort() < selectedItem.getOwner().getMaxSort()) {
					downPossible = true;
				}
			}
		}
		for (DesignView v : views) {
			v.sortUpPossible(upPossible);
			v.sortDownPossible(downPossible);
		}
	}
	
	private void notifyStructureChange() {
		for (DesignView v : views) {
			v.structureChanged(survey);
		}
	}
	
	private void notifyStateChange() {
		for (DesignView v : views) {
			v.statesMayHaveChanged();
		}
	}

	public SurveyTreeAbstract getSelectedItem() {
		return selectedItem;
	}
	
	
	public void setText(String text) throws SurveyFrozenException {
		if (selectedItem != null) {
			selectedItem.setText(text);
			for (DesignView v : views) {
				v.textChanged(text);
			}
		}
	}
	
	public void addItem() throws SurveyFrozenException {
		if (selectedItem == null) {
			return;
		} else if (!(selectedItem instanceof Answer)) {
			SurveyTreeAbstract item = selectedItem;
			selectedItem.insertItem();
			int newCount = selectedItem.itemList.size();
			for (DesignView v : views) {
				v.itemCountChanged(newCount);
			}
			selectItem(item);
		}
	}
	
	public void removeItem() throws SurveyFrozenException  {
		if (selectedItem != null) {
			SurveyTreeAbstract owner = selectedItem.getOwner();
			if (owner != null) {
				owner.removeItem(selectedItem);
				for (DesignView v : views) {
					v.itemCountChanged(owner.itemList.size());
				}
			}
		}
	}
	
	public void moveSortUp() throws SurveyFrozenException {
		SurveyTreeAbstract item = selectedItem;
		selectedItem.moveSortUp();
		notifyStructureChange();
		selectItem(item);
	}
	
	public void moveSortDown() throws SurveyFrozenException {
		SurveyTreeAbstract item = selectedItem;
		selectedItem.moveSortDown();
		notifyStructureChange();
		selectItem(item);
	}
	
	public void setMinOwnerScoreRequired(boolean required) throws SurveyFrozenException {
		selectedItem.setMinOwnerScoreRequired(required);
		for (DesignView v : views) {
			v.minOwnerScoreRequiredSet(required);
		}
		notifyStateChange();
	}
	
	public void setMaxOwnerScoreAllowed(boolean allowed) throws SurveyFrozenException {
		selectedItem.setMaxOwnerScoreAllowed(allowed);
		for (DesignView v : views) {
			v.maxOwnerScoreAllowedSet(allowed);
		}
		notifyStateChange();
	}
	
	public void setMinOwnerScore(int minScore) throws SurveyFrozenException {
		selectedItem.setMinOwnerScore(minScore);
		for (DesignView v : views) {
			v.minOwnerScoreRequiredChanged(minScore);
		}
		notifyStateChange();
	}
	
	public void setMaxOwnerScore(int maxScore) throws SurveyFrozenException {
		selectedItem.setMaxOwnerScore(maxScore);
		for (DesignView v : views) {
			v.maxOwnerScoreAllowedChanged(maxScore);
		}
		notifyStateChange();
	}
	
	public void setMinAnswersToChose(int minAnswers) throws SurveyFrozenException {
		if (selectedItem instanceof Question) {
			Question q = (Question)selectedItem;
			int actMinAnswers = q.getMinAnswersToChose();
			int actMaxAnswers = q.getMaxAnswersToChose();
			boolean stateChange = false;
			q.setMinAnswersToChose(minAnswers);
			if (q.getMinAnswersToChose() != actMinAnswers) {
				stateChange = true;
				for (DesignView v : views) {
					v.minAnswersToChoseChanged(q.getMinAnswersToChose());
				}
			}
			if (q.getMaxAnswersToChose() != actMaxAnswers) {
				stateChange = true;
				for (DesignView v : views) {
					v.maxAnswersToChoseChanged(q.getMaxAnswersToChose());
				}
			}
			if (stateChange) {
				notifyStateChange();
			}
		}
	}
	
	public void setMaxAnswersToChose(int maxAnswers) throws SurveyFrozenException {
		if (selectedItem instanceof Question) {
			Question q = (Question)selectedItem;
			int actMaxAnswers = q.getMaxAnswersToChose();
			boolean stateChange = false;
			q.setMaxAnswersToChose(maxAnswers);
			if (q.getMaxAnswersToChose() != actMaxAnswers) {
				stateChange = true;
				for (DesignView v : views) {
					v.maxAnswersToChoseChanged(q.getMaxAnswersToChose());
				}
			}
			if (stateChange) {
				notifyStateChange();
			}
		}
	}
	
	public void setAnswered(boolean answered) throws QuestionAnswerCountException {
		if (selectedItem instanceof Question) {
			Question q = (Question)selectedItem;
			boolean isAnswered = q.isAnswered();
			q.setAnswered(answered);
			if (q.isAnswered() != isAnswered) {
				for (DesignView v : views) {
					v.questionAnsweredChanged(q, q.isAnswered());
				}
			}
		}
	}
	
	public void setScoreIfChosen(int score) throws SurveyFrozenException {
		if (selectedItem instanceof Answer) {
			Answer a = (Answer)selectedItem;
			int oldScore = a.getScoreIfChosen();
			a.setScoreIfChosen(score);
			boolean changed = (a.getScoreIfChosen() != oldScore);
			if (changed) {
				for (DesignView v : views) {
					v.scoreIfChosenChanged(a.getScoreIfChosen());
				}
				notifyStateChange();
			}
		}
	}
	
	public void setChosen(boolean chosen) {
		if (selectedItem instanceof Answer) {
			Answer a = (Answer)selectedItem;
			boolean isChosen = a.isChosen();
			a.setChosen(chosen);
			if (a.isChosen() != isChosen) {
				for (DesignView v : views) {
					v.answerChosenChanged(a, a.isChosen());
				}
			}
		}
	}

	
	public void addView(DesignView v) {
		views.add(v);
	}
	
	public void removeView(DesignView v) {
		views.remove(v);
	}
}