package zhaw.umfrage.test;

import java.io.File;

import zhaw.umfrage.*;

public class Main {

	public static void main(String[] args) {
		
		Survey survey = null;
		File f = new File("Ernährungsumfrage.ser");
		try {
			survey = Survey.getFromFile(f);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		while (true) {
			SurveyTreeAbstract t = survey.getNextItem();
			if (t == null) {
				break;
			} else {
				System.out.println(t);
				Question q = (Question) t.getNextItem();
				if (q == null) {
					break;
				} else {
					System.out.println(q);
				}
			}
		}

		
	}
}
