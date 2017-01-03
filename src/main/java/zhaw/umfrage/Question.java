/**
 * 
 */
package zhaw.umfrage;

/**
 * @author Darian
 *
 */
public class Question extends SurveyTreeAbstract {
	
	private static final long serialVersionUID = 1L;
	private int minAnswersToChose = 0;
	private int maxAnswersToChose = 0;
	
	public Question(Questionnaire owner, String text){
		super(text, owner);
	}
	
	public Class getOwnerClass() {
		return Questionnaire.class;
	}
	
	public Class getItemClass() {
		return Answer.class;
	}
	
}
