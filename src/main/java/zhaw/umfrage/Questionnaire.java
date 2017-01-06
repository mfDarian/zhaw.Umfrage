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
	
	protected Questionnaire(Survey owner, String text) {
		super(text, owner);
	}

	@Override
	public SurveyTreeAbstract insertItem(String text) {
		Question q = new Question(this, text);
		super.addItem(q);
		return q;
	}

	@Override
	public SurveyTreeAbstract insertItem() {
		return insertItem("New Question");
	}
	


}
