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
	
	public Survey(String text) {
		super(text, null);
	}
	
	public Class getOwnerClass() {
		return null;
	}
	
	public Class getItemClass() {
		return Questionnaire.class;
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
	
}
