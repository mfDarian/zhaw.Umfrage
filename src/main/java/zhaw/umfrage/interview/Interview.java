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
	
	
	//Ein Aspekt, bei dem ich noch nicht ganz sicher bin, ist die Art, wie die Objekte der beantworteten Fragen in dieser Sammlung repräsentiert werden sollen. 
	//Ich kann mir hier zwei verschiedene Prinzipien vorstellen;
	
	//Variante A; (unsere Diskussion am Freitag ging in diese Richtung)
	//In der Interview- Sammlung werden nur beantwortete Antworten übertragen.
	//Für dieses System wäre  die Zuweisung einer AntwortID zwingend (Wir haben ja über so einen Zähler-Sammlung geredet: 
	//typ;[NrQuestionnaire/NrFrage/NrAntwort]. Diese ID könnte ja schon im Survey bestimmt werden. Die Interview-Sammlung übernimmt diese dann einfach. 
	//Die Kampagnen-Klasse bräuchte dann eine Writer, welcher die Intelligenz aufweist, für jedes Interview gleich lange Zeilen zu schreiben (d.h. auch mit ausgelassenen Antworten).
	
	//Variante B; (Ist mir selber in den Kopf gekommen)
	//Das Interview bildet das verschachtelte-Sammlungs-System aus dem Survey nach (Survey-hält-Questionnaires-hält-Fragen-hält-Antworten). Die Antwort-Objekte in dieser
	//Interview-Sammlung enthalten noch eine boolean-Variable "chosen" welche zeigt, ob Sie angewählt wurden oder nicht.
	//Beim "submitten" dieser abgeschlossenen Interviews würde die Objekte dieser strukturell- identischen Sammlungs-Systeme (somit bräuchte man dann auch keine explizite Antwort-ID (?)) dann 
	//der Kampagnen-Klasse übertragen. Die Kampagnen-Klasse hat dann auch die kompetenz
	//über einen Writer (welche weniger intelligent sein müsste als bei A) diese aufgezeichneten Interview-Sammlungen auszuschreiben
	
	
	
	
	
	// Ein weiterer Aspekt, ist die Anzahl erlaubter Antworten pro Frage. Wenn mir recht ist, wurde diese Variable noch nicht definiert. 
	//Diese müsste ja in der Frage-Sammlung des Surveys noch gesetzt werden, oder?
	
	
	
	
	
	
	
	
	

	
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

	
 public void proceedInterview()	{ // Swen: 16.01 In dieser Methode (ursprünglich aus der "Main"-Klasse kopiert) möchte ich die Interview-Abfolge-Intelligenz abbilden. Kommentare zeigen, was ich vor habe.
		
		Survey survey = null;
		File f = new File("Ernährungsumfrage.ser"); // Swen: 16.01 Vorerst soll eine fix eingestellte Umfrage-Objekt abgefragt werden-in einem späteren Schritt sollte man die aktiv auswählen können(d.h. als Parameter der Methode übergeben...wie macht man das für ein Objekt?
		try {
			survey = Survey.getFromFile(f);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		while (true) { // Swen: 16.01 ist "true" Teil einer boolean-Variable? Wenn ja welcher, und wo definiert?
			SurveyTreeAbstract t = survey.getNextItem(); //Swen: 16.01
			if (t == null) {
				break;
			} else {
				System.out.println(t);
				Question q = (Question) t.getNextItem(); //Swen: 16.01 (Question) ist ja ein expliziter cast- auf ein  Question-Objekt, oder?
				if (q == null) {
					break;
				} else {
					System.out.println(q); // Swen: 16.01 Diese Zeile zeigt die "aktuelle" Frage an. Aber statt println müsste die Frage im entsprechenden Feld des Field-GUI angezeigt werden, oder?
					//ausserdem müsste hier auch ein Stück Code sein, welche die zur Verfügung stehenden Antworten der "aktuellen" Frageauf die buttons bringt, welche im GUI angezeigt werden.
					if (q.toString().equals("Möchten Sie an einer Umfrage zu Ernährung teilnehmen?")) { //Swen: 16.01 Ich denke,diese Zeile (bis Zeile 112) müsste umgebaut werden in eine Funktion, 
																					//welche auf das submitten der ausgewählten Antwortaus dem GUI-Action-Listener wartet(?)
						ArrayList<SurveyTreeAbstract> al = q.getItemList();			// und diese Antworten dann in die Interview-Sammlung schreibt.
						Answer a = (Answer) al.get(0);
						System.out.println(a);
						a.setChosen(true);
						try {
							q.setAnswered(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
						//System.out.println("Score of Answer: " + a.getScore());       //die folgenden 4 Zeilen braucht es wohl nicht mehr?
						//System.out.println("Score of Question: " + q.getScore());
						//System.out.println("Score of Questionnaire: " + t.getScore());
						//System.out.println("Score of Survey: " + survey.getScore());
					}
				}
			}	
		}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}}
