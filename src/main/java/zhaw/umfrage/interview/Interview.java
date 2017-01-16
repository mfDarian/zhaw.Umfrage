/**
 * 
 */
package zhaw.umfrage.interview;

import java.io.File;
import java.util.ArrayList;

import zhaw.umfrage.Answer;
import zhaw.umfrage.Question;
import zhaw.umfrage.Survey;
import zhaw.umfrage.SurveyTreeAbstract;

/**
 * @author Darian
 *
 */
public class Interview {
	
	transient Survey survey;
	private String interviewer;
	private String interviewee;
	protected ArrayList<Answer> collectanswer; // 16.01 Swen: Ich denke, man muss versuchen eine Sammlung der beantworteten Fragen (Array-List(?)) anzulegen. 
	//Meine Idee ist, dass dann jede beantwortete Frage selber wieder eine ArrayList (oder andere Sammlung(?)) der ausgewählten Antworten hält. Ein Aspekt, bei dem ich noch nicht ganz sicher bin,
	// ist die Art, wie die Objekte der beantworteten Fragen in dieser Sammlung repräsentiert werden soll. Ich meine, man kann das ja nicht in den Ursprünglichen Antworte-Objekten aus dem Survey machen.
	// (der Survey sollte ja unbe
	
	
	
	
	
	
	
	
	
	

	
	public Interview(Survey survey) {
		this.survey = survey;
	}
	
	public Survey getSurvey() {
		return survey;
	}
	
	public void setInterviewer(String interviewer) {
		this.interviewer = interviewer;
	}
	
	public String getInterviewer() {
		return interviewer;
	}
	
	public void setInterviewee(String interviewee) {
		this.interviewee = interviewee;
	}

	public String getInterviewee() {
		return interviewee;
	}

	
 public void proceedInterview()	{ // Swen: 16.01 In dieser Methode (ursprünglich aus der "Main"-Klasse kopiert) möchte ich die Interview-Abfolge-Intelligenz abbilden.
		
		Survey survey = null;
		File f = new File("Ernährungsumfrage.ser"); // Swen: 16.01 Vorerst soll eine fix eingestellte Umfrage-Objekt abgefragt werden-in einem späteren Schritt sollte man die aktiv auswählen können(d.h. als Parameter der Methode übergeben...wie macht man das für ein Objekt?
		try {
			survey = Survey.getFromFile(f);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		while (true) { // Swen: 16.01 ist "true" Teil einer boolean-Variable-welcher? wo definiert?
			SurveyTreeAbstract t = survey.getNextItem(); //Swen: 16.01
			if (t == null) {
				break;
			} else {
				System.out.println(t);
				Question q = (Question) t.getNextItem(); //Swen: 16.01 (Question) ist ja ein expliziter cast- auf ein  Question-Objekt, oder?
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}}
