/**
 * 
 */
package zhaw.umfrage;

/**
 * @author Darian
 *
 */
public class Questionnaire extends SurveyTreeAbstract {
	
	private static final long serialVersionUID = 1L;
	
	public Questionnaire(Survey owner, String text) {
		super(text, owner);
	}
	
	public Class getOwnerClass() {
		return Survey.class;
	}
	
	public Class getItemClass() {
		return Question.class;
	}

}
