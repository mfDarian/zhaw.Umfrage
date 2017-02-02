package zhaw.umfrage;

public final class SurveyFrozenException extends Exception {

	private static final long serialVersionUID = 1L;
	
	protected SurveyFrozenException() {
	}
	
	public String toString() {
		return "Survey is frozen.";
	}

}
