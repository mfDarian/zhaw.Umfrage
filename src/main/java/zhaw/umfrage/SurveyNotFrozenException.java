package zhaw.umfrage;

public final class SurveyNotFrozenException extends Exception {

	private static final long serialVersionUID = 1L;

	protected SurveyNotFrozenException() {
	}
	
	public String toString() {
		return "Survey must be frozen.";
	}
}
