package zhaw.umfrage;

public final class UnreachableItemsException extends Exception {

	private static final long serialVersionUID = 1L;
	private Survey originator;
	private int unreachableCount;

	protected UnreachableItemsException(Survey s, int unreachableCount) {
		originator = s;
		this.unreachableCount = unreachableCount;
	}
	
	public String toString() {
		return originator.getText() + " contains " + unreachableCount + " unreachable items";
	}

}
