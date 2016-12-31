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
	
	private String text;
	private SurveyTreeAbstract owner;
	private ArrayList<SurveyTreeAbstract> itemList;
	private int sort = 1;
	private boolean useMinOwnerScoreToBeReleased;
	private boolean useMaxOwnerScoreToBeReleased;
	private int minOwnerScoreToBeReleased;
	private int maxOwnerScoreToBeReleased;
	private transient int score = 0;
	private transient int actualItem = -1;
	
	public SurveyTreeAbstract (String text, SurveyTreeAbstract owner) {
		this.text = text;
		this.itemList = new ArrayList<SurveyTreeAbstract>();
		if (owner != null) {
			this.owner = owner;
			owner.addItem(this);
			sort = owner.getMaxSort() + 1;
		}
	}
	
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
	
	protected final void setOwner(SurveyTreeAbstract owner) {
		this.owner = owner;
		//TODO Exception, um falsche Zuordnungen zu vermeiden?
	}
	
	public final SurveyTreeAbstract getOwner() {
		return owner;
	}

	public final void setItemList(ArrayList<SurveyTreeAbstract> itemList) {
		this.itemList = itemList;
		//TODO Exception, um falsche Zuordnungen zu vermeiden?
	}
	
	public final ArrayList<SurveyTreeAbstract> getItemList() {
		return itemList;
	}

	public final void addItem(SurveyTreeAbstract item) {
		if (itemList.contains(item)) {
			return;
		}
		itemList.add(item);
		//TODO Exception, um falsche Zuordnungen zu vermeiden?
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
	
	public final int getMinOwnerScoreToBeReleased() {
		return minOwnerScoreToBeReleased;
	}
	
	public final void setUseMinOwnerScoreToBeReleased(boolean useMinOwnerScoreToBeReleased) {
		this.useMinOwnerScoreToBeReleased = useMinOwnerScoreToBeReleased;
	}
	
	public final boolean getUseMinOwnerScoreToBeReleased() {
		return useMinOwnerScoreToBeReleased;
	}

	public final void setUseMaxOwnerScoreToBeReleased(boolean useMaxOwnerScoreToBeReleased) {
		this.useMaxOwnerScoreToBeReleased = useMaxOwnerScoreToBeReleased;
	}
	
	public final boolean getUseMaxOwnerScoreToBeReleased() {
		return useMaxOwnerScoreToBeReleased;
	}
	
	public final void setMinOwnerScoreToBeReleased(int minOwnerScoreToBeReleased) {
		this.minOwnerScoreToBeReleased = minOwnerScoreToBeReleased;
	}

	public final int getMaxOwnerScoreToBeReleased() {
		return maxOwnerScoreToBeReleased;
	}

	public final void setMaxOwnerScoreToBeReleased(int maxOwnerScoreToBeReleased) {
		this.maxOwnerScoreToBeReleased = maxOwnerScoreToBeReleased;
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
	
	public final SurveyTreeAbstract getNextItem() {
		SurveyTreeAbstract nextItem = null;
		int c = actualItem + 1;
		if (itemList.size() > c) {
			nextItem = itemList.get(c);
			if (nextItem.getUseMinOwnerScoreToBeReleased() || nextItem.getUseMaxOwnerScoreToBeReleased()) {
				while(c < itemList.size()) {
					if (!nextItem.getUseMinOwnerScoreToBeReleased() || (nextItem.getUseMinOwnerScoreToBeReleased() && score >= nextItem.getMinOwnerScoreToBeReleased())) {
						if (!nextItem.getUseMaxOwnerScoreToBeReleased() || (nextItem.getUseMaxOwnerScoreToBeReleased() && score <= nextItem.getMaxOwnerScoreToBeReleased())) {
							actualItem = c;
							return nextItem;
						}
					}
					c++;
					nextItem = itemList.get(c);
				}
			}
		}
		actualItem = -1; // TODO sinnvolles verhalten?
		return nextItem;
	}

}
