/**
 * 
 */
package zhaw.umfrage;

import java.util.ArrayList;
import java.util.Collections;
import java.io.Serializable;

/**
 * @author Darian
 *
 */
public abstract class SurveyTreeAbstract implements Serializable, Comparable<SurveyTreeAbstract> {
	
	private static final long serialVersionUID = 1L;
	private String text;
	protected SurveyTreeAbstract owner;
	protected ArrayList<SurveyTreeAbstract> itemList;
	private int sort = 0;
	private boolean minOwnerScoreRequired;
	private boolean maxOwnerScoreAllowed;
	private int minOwnerScore;
	private int maxOwnerScore;
	protected int minScoreAchieveable = 0;
	protected int maxScoreAchieveable = 0;
	private boolean unreachable;
	protected transient int score = 0;
	private transient int actualItem = 0;
	
	boolean stateChanged;
	
	protected SurveyTreeAbstract (String text, SurveyTreeAbstract owner) {
		this.text = text;
		this.itemList = new ArrayList<SurveyTreeAbstract>();
		if (owner != null) {
			this.owner = owner;
			sort = owner.getMaxSort() + 1;
		}
	}
	
	protected final void addItem(SurveyTreeAbstract item) {
		itemList.add(item);
	}
	
	
	public final void removeItem(SurveyTreeAbstract item) {
		if (!itemList.contains(item)) {
			return;
		}
		itemList.remove(item);
		expose();
	}
	
	@Override
	public final String toString() {
		return text;
	}
	
	@Override
	public final int compareTo(SurveyTreeAbstract o) {
		if (sort > o.sort) {
			return 1;
		}
		if (sort < o.sort) {
			return -1;
		}
		return 0;
	}
	
	public final void setText(String text) {
		this.text = text;
	}
	
	public final String getText() {
		return text;
	}
	
	
	public final SurveyTreeAbstract getOwner() {
		return owner;
	}
	
	public final ArrayList<SurveyTreeAbstract> getItemList() {
		return itemList;
	}
	
	public abstract SurveyTreeAbstract insertItem(String text);
	
	public abstract SurveyTreeAbstract insertItem();


	
	public final int getMaxSort() {
		int maxSort = 0;
		for (SurveyTreeAbstract item : itemList) {
			int sort = item.getSort();
			if (sort > maxSort) {
				maxSort = sort;
			}
		}
		return maxSort;
	}

	public final int getSort() {
		return sort;
	}
	
	private final void setSort(int sort) {
		this.sort = sort;
		reset();
		if (owner != null) {
			owner.notifySortChange();
		}
	}
	
	public final void moveSortUp() {
		if (owner != null) {
			ArrayList<SurveyTreeAbstract> ownerItemList = owner.getItemList();
			int i = 0;
			int thisSort = sort;
			SurveyTreeAbstract lastItem = null;
			for (SurveyTreeAbstract item : ownerItemList) {
				i++;
				if (item == this) {
					if (i == 1) {
						return;
					} else {
						setSort(lastItem.getSort());
						break;
					}
				}
				lastItem = item;
			}
			if (lastItem != null) {
				lastItem.setSort(thisSort);
			}
		}
		expose();
	}
	
	public final void moveSortDown() {
		if (owner != null) {
			ArrayList<SurveyTreeAbstract> ownerItemList = owner.getItemList();
			int i = 0;
			int thisSort = sort;
			boolean takeNext = false;
			for (SurveyTreeAbstract item : ownerItemList) {
				i++;
				if (takeNext) {
					setSort(item.getSort());
					item.setSort(thisSort);
					break;
				}
				if (item == this) {
					if (i == ownerItemList.size()) {
						return;
					} else {
						takeNext = true;
					}
				}
			}
		}
		expose();
	}
	
	protected final void notifySortChange() {
		Collections.sort(itemList);
	}
	
	public final int getMinOwnerScore() {
		return minOwnerScore;
	}
	
	public final void setMinOwnerScoreRequired(boolean minOwnerScoreRequired) {
		this.minOwnerScoreRequired = minOwnerScoreRequired;
		expose();
	}
	
	public final boolean getMinOwnerScoreRequired() {
		return minOwnerScoreRequired;
	}

	public final void setMaxOwnerScoreAllowed(boolean maxOwnerScoreAllowed) {
		this.maxOwnerScoreAllowed = maxOwnerScoreAllowed;
		expose();
	}
	
	public final boolean getMaxOwnerScoreAllowed() {
		return maxOwnerScoreAllowed;
	}
	
	public final void setMinOwnerScore(int minOwnerScore) {
		this.minOwnerScore = minOwnerScore;
		if (getMinOwnerScoreRequired()) {
			expose();
		}
	}

	public final int getMaxOwnerScore() {
		return maxOwnerScore;
	}

	public final void setMaxOwnerScore(int maxOwnerScore) {
		this.maxOwnerScore = maxOwnerScore;
		if (getMaxOwnerScoreAllowed()) {
			expose();
		}
	}

	protected void notifyScoreChange() {
		int newScore = 0;
		for (SurveyTreeAbstract i : itemList) {
			newScore += i.getScore();
		}
		setScore(newScore);
	}
	
	protected void setScore(int score) {
		this.score = score;
		if (owner != null) {
			getOwner().notifyScoreChange();
		}
	}
	
	public final int getScore() {
		return score;
	}
	
	public void reset() {
		actualItem = 0;
		setScore(0);
		if (itemList != null) {
			for (SurveyTreeAbstract item : itemList) {
				item.reset();
			}
		}
	}
	
	public final boolean hasItems() {
		return itemList.size() != 0;
	}
	
	public final boolean isEmpty() {
		return !hasItems();
	}
	
	public boolean isReachable() {
		boolean reachable = false;
		if (!unreachable) {
			if (owner.isReachable()) {
				if (!getMinOwnerScoreRequired() || (getMinOwnerScoreRequired() && owner.getScore() >= getMinOwnerScore())) {
					if (!getMaxOwnerScoreAllowed() || (getMaxOwnerScoreAllowed() && owner.getScore() <= getMaxOwnerScore())) {
						return true;
					}
				}
			}
		}
		return reachable;
	}
	
	protected void calcMinScoreAchieveable() {
		int minScore = 0;
		if (itemList != null) {
			for (SurveyTreeAbstract t : itemList) {
				if (!t.isUnreachable()) {
					minScore += t.getMinScoreAchieveable();
				}
			}
		}
		minScoreAchieveable = minScore;
	}
	
	public final int getMinScoreAchieveable() {
		return minScoreAchieveable;
	}
	
	protected void calcMaxScoreAchieveable() {
		int maxScore = 0;
		if (itemList != null) {
			for (SurveyTreeAbstract t : itemList) {
				if (!t.isUnreachable()) {
					maxScore += t.getMaxScoreAchieveable();
				}
			}
		}
		maxScoreAchieveable = maxScore;
	}
	
	public final int getMaxScoreAchieveable() {
		return maxScoreAchieveable;
	}
	
	
	public boolean isUnreachable() {
		return unreachable;
	}
	
	
	
	public final SurveyTreeAbstract getNextItem() {
		SurveyTreeAbstract nextItem = null;
		while (actualItem < itemList.size()) {
			nextItem = itemList.get(actualItem);
			if (nextItem.isReachable()) {
				actualItem++;
				return nextItem;
			}
			actualItem++;
		}
		actualItem = itemList.size();
		return nextItem;
	}


	public final void recalculate() {
		calculateScoreAchieveable();
		calculateReachability();
	}
	
	protected void calculateReachability() {
		boolean unreachable = false;
		if (owner != null) {
			if (owner.isUnreachable()){
				unreachable = true;
			} else {
				if (getMinOwnerScoreRequired() && getMaxOwnerScoreAllowed() && (getMinOwnerScore() > getMaxOwnerScore())) {
					unreachable = true;
				} else {
					if (getMinOwnerScoreRequired()) {
						int maxScoreLeft = 0;
						for (SurveyTreeAbstract t : owner.itemList) {
							if (t.getSort() < sort) {
								maxScoreLeft += t.getMaxScoreAchieveable();
							}
						}
						unreachable = (maxScoreLeft < getMinOwnerScore());
					}
					if (!unreachable && (getMaxOwnerScoreAllowed())) {
						int minScoreLeft = 0;
						for (SurveyTreeAbstract t : owner.itemList) {
							if (t.getSort() < sort) {
								minScoreLeft += t.getMinScoreAchieveable();
							}
						}
						unreachable = (minScoreLeft > getMaxOwnerScore());
					}
				}
			}
		}
		if (unreachable != this.unreachable) {
			stateChanged = true;
		}
		this.unreachable = unreachable;
	}
	
	protected final void calculateScoreAchieveable() {
		int oldMinScore = minScoreAchieveable;
		int oldMaxScore = maxScoreAchieveable;
		calcMinScoreAchieveable();
		calcMaxScoreAchieveable();
		if (unreachable) {
			minScoreAchieveable = 0;
			maxScoreAchieveable = 0;
		}
		if ((oldMinScore != minScoreAchieveable) || (oldMaxScore != maxScoreAchieveable)) {
			stateChanged = true;
		}
	}
	
	final boolean consumeStateChanged() {
		boolean changed = stateChanged;
		stateChanged = false;
		if (changed) {
			expose();
		}
		return changed;
	}
	
	protected final void expose() {
		recalculate();
		recalculateItems();
		if (owner != null) {
			owner.expose();
		}
	}
	
	protected void recalculateItems() {
		if (!isEmpty()) {
			boolean itemStateChanged = false;
			for (SurveyTreeAbstract t : itemList) {
				t.recalculate();
				if (t.consumeStateChanged()) {
					itemStateChanged = true;
				}
			}
			if (itemStateChanged) {
				expose();
			}
		}
	}
	

}