package zhaw.umfrage.test;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import zhaw.umfrage.*;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("Start Test");
		
		// Create a survey with two Questionnaires and some Questions with answers inside
		Survey survey = new Survey("Test Befragung");
		Questionnaire q1 = new Questionnaire(survey, "Allgemeine Fragen");
		Question q1q1 = new Question(q1, "Guten Tag, wie geht es Ihnen heute?");
		Answer q1q1a1 = new Answer(q1q1, "Danke, es geht so.");
		Answer q1q1a2 = new Answer(q1q1, "Bestens, ich kann wirklich nicht klagen.");
		Answer q1q1a3 = new Answer(q1q1, "Heute läuft aber auch alles schief, und jetzt das noch...");
		Question q1q2 = new Question(q1, "Möchten Sie an einer Umfrage teilnehmen? Es geht um Politik.");
		Answer q1q2a1 = new Answer(q1q2, "Ja, gerne.");
		Answer q1q2a2 = new Answer(q1q2, "Nein, danke.");
		Questionnaire q2 = new Questionnaire(survey, "US Wahlen");
		Question q2q1 = new Question(q2, "Amerika hat gewählt. Hat Sie das Ergebnis überrascht?");
		Answer q2q1a1 = new Answer(q2q1, "Ja, sehr.");
		Answer q2q1a2 = new Answer(q2q1, "Ein wenig.");
		Answer q2q1a3 = new Answer(q2q1, "Nein, gar nicht.");
		Question q2q2 = new Question(q2, "Wen hätten Sie gewählt?");
		Answer q2q2a1 = new Answer(q2q2, "Hillary Clinton");
		Answer q2q2a2 = new Answer(q2q2, "Donald Trump");
		Questionnaire q3 = new Questionnaire(survey, "Danke");
		Question q3q1 = new Question(q3, "Vielen Dank für Ihre Teilnahme an der Umfrage");
		
		survey.printChain();
		
		// Save it to a file
		String path = "testumfrage.ser";
		survey.serialize(path);
		
		// Load the file
		Survey geladen = null;
		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(path));
			geladen = (Survey) is.readObject();
			is.close();
		} catch (Exception ex) {
			ex.printStackTrace(); //TODO
		}
		
		geladen.printChain();
		
		System.out.println("Ende Test");
		
	}
}
