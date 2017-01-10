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
	protected transient int score = 0;
	private transient int actualItem = 0;
	
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

	public final void removeItem(SurveyTreeAbstract item) {
		if (!itemList.contains(item)) {
			return;
		}
		itemList.remove(item);
	}
	
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
	
	public final void setSort(int sort) {
		this.sort = sort;
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
			lastItem.setSort(thisSort);
		}
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
	}
	
	protected final void notifySortChange() {
		Collections.sort(itemList);
	}
	
	public final int getMinOwnerScore() {
		return minOwnerScore;
	}
	
	public final void setMinOwnerScoreRequired(boolean minOwnerScoreRequired) {
		this.minOwnerScoreRequired = minOwnerScoreRequired;
	}
	
	public final boolean getMinOwnerScoreRequired() {
		return minOwnerScoreRequired;
	}

	public final void setMaxOwnerScoreAllowed(boolean maxOwnerScoreAllowed) {
		this.maxOwnerScoreAllowed = maxOwnerScoreAllowed;
	}
	
	public final boolean getMaxOwnerScoreAllowed() {
		return maxOwnerScoreAllowed;
	}
	
	public final void setMinOwnerScore(int minOwnerScore) {
		this.minOwnerScore = minOwnerScore;
	}

	public final int getMaxOwnerScore() {
		return maxOwnerScore;
	}

	public final void setMaxOwnerScore(int maxOwnerScore) {
		this.maxOwnerScore = maxOwnerScore;
	}

	protected void notifyScoreChange(SurveyTreeAbstract item) {
		int score = this.score + item.getScore();
		this.score = score;
	}
	
	protected final void setScore(int score) {
		this.score = score;
		if (owner != null) {
			getOwner().notifyScoreChange(this);
		}
	}
	
	public final int getScore() {
		return score;
	}
	
	public void reset() {
		actualItem = 0;
		score = 0;
		if (itemList != null) {
			for (SurveyTreeAbstract item : itemList) {
				item.reset();
			}
		}
	}
	
	public final SurveyTreeAbstract getNextItem() {
		SurveyTreeAbstract nextItem = null;
		while (actualItem < itemList.size()) {
			nextItem = itemList.get(actualItem);
			if (!nextItem.getMinOwnerScoreRequired() || (nextItem.getMinOwnerScoreRequired() && score >= nextItem.getMinOwnerScore())) {
				if (!nextItem.getMaxOwnerScoreAllowed() || (nextItem.getMaxOwnerScoreAllowed() && score <= nextItem.getMaxOwnerScore())) {
					actualItem++;
					return nextItem;
				}
			}
			actualItem++;
		}
		actualItem = itemList.size();
		return nextItem;
	}

}
