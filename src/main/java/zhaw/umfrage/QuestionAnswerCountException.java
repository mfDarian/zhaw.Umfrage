/**
 * 
 */
package zhaw.umfrage;

/**
 * @author Darian
 *
 */
public class QuestionAnswerCountException extends Exception {

	private static final long serialVersionUID = 1L;
	private int minAnswers;
	private int maxAnswers;

	public QuestionAnswerCountException(Question q) {
		minAnswers = q.getMinAnswersToChose();
		maxAnswers = q.getMaxAnswersToChose();
	}
	
	public String toString() {
		String exceptionText = "Please specify ";
		if (minAnswers == maxAnswers) {
			exceptionText += "exactly ";
			if (minAnswers == 1) {
				exceptionText += "one answer.";
			} else {
				exceptionText += minAnswers + " answers.";
			}		
		} else {
			if (minAnswers == 0) {
				if (maxAnswers == 1) {
					exceptionText +=  "at most one answer.";
				} else {
					exceptionText += "at most " + maxAnswers + " answers.";
				}
				
			} else if (maxAnswers == 0) {
				if (minAnswers == 1) {
					exceptionText +=  "at least one answer.";
				} else {
					exceptionText += "at least " + minAnswers + " answers.";
				}
			} else {
				exceptionText +=  "between " + minAnswers + " and " + maxAnswers +  " answers.";
			}
		}
		return exceptionText;
	}

}