package zhaw.umfrage;

public interface DesignView {
	
	public void selectionChanged(SurveyTreeAbstract item);
	
	public void surveySelected(Survey survey);
	
	public void questionnaireSelected(Questionnaire questionnaire);
	
	public void questionSelected(Question question);
	
	public void answerSelected(Answer answer);
	

	public void itemAddable(boolean addable);
	
	public void itemRemoveable(boolean removeable);
	

	public void textChanged(String text);
	

	public void itemCountChanged(int itemCount);


	public void minOwnerScoreRequiredSet(boolean isSet);
	

	public void minOwnerScoreRequiredChanged(int scoreRequired);
	
	public void maxOwnerScoreAllowedSet(boolean isSet);
	
	public void maxOwnerScoreAllowedChanged(boolean scoreAllowed);
	
	public void minAnswersToChoseChanged(int minAnswers);
	
	public void maxAnswersToChoseChanged(int maxAnswers);
	
	public void scoreIfChosenChanged (int scoreIfChosen);
	
	public void structureChanged(Survey root);
	
	public void surveyFrozen();
	
	public void answerChosenChanged(Answer answer, boolean chosen);
	
	public void questionAnsweredChanged(Question question, boolean answered);

}
