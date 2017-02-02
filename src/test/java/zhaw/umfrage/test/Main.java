package zhaw.umfrage.test;

import java.io.File;
import java.util.ArrayList;

import zhaw.umfrage.*;
import zhaw.umfrage.editor.*;

public class Main {

	public static void main(String[] args) {
		
		/*
		Survey survey = null;
		Survey survey2 = null;
		File f = new File("Vergleichsumfrage.ser");
		try {
			survey = Survey.getFromFile(f);
			survey2 = Survey.getFromFile(f);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println(survey.equals(survey2));
		*/
		
		Survey survey = null;
		File f = new File("Vergleichsumfrage.ser");
		try {
			survey = Survey.getFromFile(f);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		//SurveyDetailPanel p = new SurveyDetailPanel(survey);
	}
}
