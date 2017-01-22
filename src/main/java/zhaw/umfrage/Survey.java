/**
 * 
 */
package zhaw.umfrage;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Darian
 *
 */
public class Survey extends SurveyTreeAbstract{
	
	private static final long serialVersionUID = 1L;
	private int questionIdCounter;
	
	public Survey(String text) {
		super(text, null);
		questionIdCounter = 0;
	}
	
	@Override
	public SurveyTreeAbstract insertItem(String text) {
		Questionnaire q = new Questionnaire(this, text);
		super.addItem(q);
		return q;
		
	}
	
	@Override
	public SurveyTreeAbstract insertItem() {
		return insertItem("New Questionnaire");
	}
	

	public void clear() {
		itemList = new ArrayList<>();
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


	protected int nextQuestionId() {
		return ++questionIdCounter;
	}

	@Override
	public boolean isReachable() {
		return true;
	}
	
	

	
}
