/**
 * 
 */
package zhaw.umfrage;

import java.io.*;


/**
 * @author Darian
 *
 */
public class Survey extends SurveyTreeAbstract{
	
	private static final long serialVersionUID = 1L;
	private int nextAnswerId;
	private int nextQuestionId;
	private int nextQuestionnaireId;
	private boolean frozen;
	
	public Survey(String text) {
		super(text, null, null);
		nextQuestionnaireId = 0;
		nextQuestionId = 0;
		nextAnswerId = 0;
	}
	
	@Override
	public SurveyTreeAbstract insertItem(String text) throws SurveyFrozenException  {
		Questionnaire q = new Questionnaire(this, text, nextQuestionnaireId());
		super.addItem(q);
		return q;
		
	}
	
	
	@Override
	public SurveyTreeAbstract insertItem() throws SurveyFrozenException  {
		return insertItem("New Questionnaire");
	}
	

	public void clear() throws SurveyFrozenException {
		if (frozen) {
			throw new SurveyFrozenException();
		}
		for (SurveyTreeAbstract t : itemList) {
			t.owner = null;
		}
		itemList.clear();
		expose();
	}
	
	// Temporär hier
	public void serialize(String path) {
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(path));
			os.writeObject(this);
			os.close();
		} catch (IOException ex) {
			ex.printStackTrace(); //TODO
		}
	}
	
	public void saveToFile(File file) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
		os.writeObject(this);
		os.close();
	}
	
	public static Survey getFromFile(File file) throws Exception {
		ObjectInputStream is = new ObjectInputStream(new FileInputStream(file));
		Survey survey = (Survey) is.readObject();
		is.close();
		return survey;
	}
	
	
	public void freeze() throws UnreachableItemsException {
		if (frozen) {
			return;
		}
		int unreachableCount = 0;
		for (SurveyTreeAbstract questionnaire : getItemList()) {
			if (questionnaire.isUnreachable()) {
				unreachableCount++;
			}
			for (SurveyTreeAbstract question : questionnaire.getItemList()) {
				if (question.isUnreachable()) {
					unreachableCount++;
				}
				for (SurveyTreeAbstract answer : question.getItemList()) {
					if (answer.isUnreachable()) {
						unreachableCount++;
					}
				}
			}
		}
		if (unreachableCount != 0) {
			throw new UnreachableItemsException(this, unreachableCount);
		} else {
			frozen = true;
		}
		
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public void printChain() {
		System.out.println(getText());
		for (SurveyTreeAbstract questionnaire : getItemList()) {
			System.out.println("_ " + questionnaire.getText());
			for (SurveyTreeAbstract question : questionnaire.getItemList()) {
				System.out.println(" - " + question.getText());
				for (SurveyTreeAbstract answer : question.getItemList()) {
					System.out.println("  + " + answer.getText());
				}
			}
		}
	}

	protected int nextQuestionnaireId() {
		return ++nextQuestionnaireId;
	}
	
	protected int nextQuestionId() {
		return ++nextQuestionId;
	}
	
	@Override
	protected int nextAnswerId() {
		return ++nextAnswerId;
	}

	@Override
	public boolean isReachable() {
		return true;
	}
	
	

	
}
