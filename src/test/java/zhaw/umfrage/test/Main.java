package zhaw.umfrage.test;

import java.io.File;
import java.util.ArrayList;

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
					if (q.toString().equals("Möchten Sie an einer Umfrage zu Ernährung teilnehmen?")) {
						ArrayList<SurveyTreeAbstract> al = q.getItemList();
						Answer a = (Answer) al.get(0);
						System.out.println(a);
						a.setChosen(true);
						try {
							q.setAnswered(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						System.out.println("Score of Answer: " + a.getScore());
						System.out.println("Score of Question: " + q.getScore());
						System.out.println("Score of Questionnaire: " + t.getScore());
						System.out.println("Score of Survey: " + survey.getScore());
					}
				}
			}
		}

		
	}
}
