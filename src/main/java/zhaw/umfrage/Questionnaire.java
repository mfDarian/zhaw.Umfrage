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
		Survey o = (Survey) owner;
		Question q = new Question(this, text, o.nextQuestionId());
		super.addItem(q);
		return q;
	}

	@Override
	public SurveyTreeAbstract insertItem() {
		return insertItem("New Question");
	}

	@Override
	public boolean isReachable() {
		// questionnaires containing answered questions must stay reachable
		for (SurveyTreeAbstract i : itemList) {
			Question q = (Question) i;
			if (q.isAnswered()) {
				return true;
			}
		}
		return super.isReachable();
	}
	
	
}
