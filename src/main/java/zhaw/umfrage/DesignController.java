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
			for (DesignView v : views) {
				v.structureChanged(this.survey);
			}
			selectItem(this.survey);
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
				v.itemAddable(true);
				v.itemRemoveable(false);
			}
		} else if (selectedItem instanceof Questionnaire) {
			for (DesignView v : views) {
				v.questionnaireSelected((Questionnaire)selectedItem);
				v.itemAddable(true);
				v.itemRemoveable(true);
			}
		} else if (selectedItem instanceof Question) {
			for (DesignView v : views) {
				v.questionSelected((Question)selectedItem);
				v.itemAddable(true);
				v.itemRemoveable(true);
			}
		} else if (selectedItem instanceof Answer) {
			for (DesignView v : views) {
				v.answerSelected((Answer)selectedItem);
				v.itemAddable(false);
				v.itemRemoveable(true);
			}
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

	
	public void addView(DesignView v) {
		views.add(v);
	}
	
	public void removeView(DesignView v) {
		views.remove(v);
	}
}