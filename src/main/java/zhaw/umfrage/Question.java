/**
 * 
 */
package zhaw.umfrage;

/**
 * @author Darian
 *
 */
public class Question extends SurveyTreeAbstract {
	
	private int minAnswersToChose = 0;
	private int maxAnswersToChose = 0;
	
	public Question(Questionnaire owner, String text){
		super(text, owner);
	}
	
}
