/**
 * 
 */
package zhaw.Umfrage;

import java.io.*;

/**
 * @author Darian
 *
 */
public class Survey extends SurveyTreeAbstract{
	
	public Survey(String text) {
		super(text, null);
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
	
	
	public void printChain() {
		System.out.println(getText());
		for (SurveyTreeAbstract questionnaire : getSlaveList()) {
			System.out.println("_ " + questionnaire.getText());
			for (SurveyTreeAbstract question : questionnaire.getSlaveList()) {
				System.out.println(" - " + question.getText());
				for (SurveyTreeAbstract answer : question.getSlaveList()) {
					System.out.println("  + " + answer.getText());
				}
			}
		}
	}
	
}
