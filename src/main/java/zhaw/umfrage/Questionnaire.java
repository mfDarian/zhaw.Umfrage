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
	private int id;
	
	protected Questionnaire(Survey owner, String text, int id) {
		super(text, owner, owner);
		this.id = id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Questionnaire)) {
			return false;
		}
		Questionnaire q = (Questionnaire) o;
		if (id != q.getId()) {
			return false;
		}
		return super.equals(o);
	}

	@Override
	public SurveyTreeAbstract insertItem(String text) throws SurveyFrozenException  {
		Survey o = (Survey) owner;
		Question q = new Question(root, this, text, o.nextQuestionId());
		super.addItem(q);
		return q;
	}

	@Override
	public SurveyTreeAbstract insertItem() throws SurveyFrozenException {
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
	
	public int getId() {
		return id;
	}
	
	
}
