/**
 * 
 */
package zhaw.umfrage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.io.Serializable;

/**
 * @author Darian
 *
 */
public abstract class SurveyTreeAbstract implements Serializable, Comparable<SurveyTreeAbstract> {
	
	private static final long serialVersionUID = 1L;
	protected Survey root;
	protected SurveyTreeAbstract owner;
	protected final ArrayList<SurveyTreeAbstract> itemList;
	private String text;
	private int sort = 0;
	private boolean minOwnerScoreRequired;
	private boolean maxOwnerScoreAllowed;
	private int minOwnerScore;
	private int maxOwnerScore;
	protected int minScoreAchieveable = 0;
	protected int maxScoreAchieveable = 0;
	private boolean unreachable;
	boolean stateChanged;
	protected transient int score = 0;
	private transient int actualItem = 0;
	

	protected SurveyTreeAbstract (String text, SurveyTreeAbstract owner, Survey root) {
		this.text = text;
		this.itemList = new ArrayList<SurveyTreeAbstract>();
		if (owner != null) {
			this.owner = owner;
			sort = owner.getMaxSort() + 1;
		}
		if (root != null) {
			this.root = root;
		}
	}
	
	protected final void checkRootNotFrozen() throws SurveyFrozenException {
		if (root != null && root.isFrozen()) {
			throw new SurveyFrozenException();
		}
	}
	
	protected final void checkRootFrozen() throws SurveyNotFrozenException {
		if (root != null && !root.isFrozen()) {
			throw new SurveyNotFrozenException();
		}
	}
	
	protected final void addItem(SurveyTreeAbstract item) throws SurveyFrozenException {
		checkRootNotFrozen();
		itemList.add(item);
		item.expose();
	}
	
	
	public final void removeItem(SurveyTreeAbstract item) throws SurveyFrozenException {
		checkRootNotFrozen();
		if (!itemList.contains(item)) {
			return;
		}
		itemList.remove(item);
		expose();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SurveyTreeAbstract)) {
			return false;
		}
		SurveyTreeAbstract s = (SurveyTreeAbstract) o;
		// Do not check if owners are equal, since item check would trigger endless loop (referencing back to owner)
		if (!text.equals(s.getText())) {
			return false;
		}
		if (owner == null && s.getOwner() != null) {
			return false;
		}
		if (owner != null && s.getOwner() == null) {
			return false;
		}
		if (sort != s.getSort()) {
			return false;
		}
		if (minOwnerScoreRequired != s.getMinOwnerScoreRequired()) {
			return false;
		}
		if (maxOwnerScoreAllowed != s.getMaxOwnerScoreAllowed()) {
			return false;
		}
		if (minOwnerScore != s.getMinOwnerScore()) {
			return false;
		}
		if (maxOwnerScore != s.getMaxOwnerScore()) {
			return false;
		}
		if (itemList.size() != s.itemList.size()) {
			return false;
		}
		for (SurveyTreeAbstract t : itemList) {
			if (!(t.equals(s.itemList.get(itemList.indexOf(t))))) {
				return false;
			}
		}
		return true;
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
	
	public final void setText(String text) throws SurveyFrozenException {
		checkRootNotFrozen();
		this.text = text;
	}
	
	public final String getText() {
		return text;
	}
	
	
	public final SurveyTreeAbstract getOwner() {
		return owner;
	}
	
	public final Collection<SurveyTreeAbstract> getItemList() {
		return Collections.unmodifiableList(itemList);
		
	}
	
	public abstract SurveyTreeAbstract insertItem(String text) throws SurveyFrozenException;
	
	public abstract SurveyTreeAbstract insertItem() throws SurveyFrozenException;

	protected int nextAnswerId() {
		return owner.nextAnswerId();
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
	
	private final void setSort(int sort) throws SurveyFrozenException {
		checkRootNotFrozen();
		this.sort = sort;
		reset();
		if (owner != null) {
			owner.notifySortChange();
		}
	}
	
	public final void moveSortUp() throws SurveyFrozenException {
		if (owner != null) {
			int i = 0;
			int thisSort = sort;
			SurveyTreeAbstract lastItem = null;
			for (SurveyTreeAbstract item : owner.itemList) {
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
	
	public final void moveSortDown() throws SurveyFrozenException {
		if (owner != null) {
			int i = 0;
			int thisSort = sort;
			boolean takeNext = false;
			for (SurveyTreeAbstract item : owner.itemList) {
				i++;
				if (takeNext) {
					setSort(item.getSort());
					item.setSort(thisSort);
					break;
				}
				if (item == this) {
					if (i == owner.itemList.size()) {
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
	
	public final void setMinOwnerScoreRequired(boolean minOwnerScoreRequired) throws SurveyFrozenException {
		checkRootNotFrozen();
		this.minOwnerScoreRequired = minOwnerScoreRequired;
		expose();
	}
	
	public final boolean getMinOwnerScoreRequired() {
		return minOwnerScoreRequired;
	}

	public final void setMaxOwnerScoreAllowed(boolean maxOwnerScoreAllowed) throws SurveyFrozenException {
		checkRootNotFrozen();
		this.maxOwnerScoreAllowed = maxOwnerScoreAllowed;
		expose();
	}
	
	public final boolean getMaxOwnerScoreAllowed() {
		return maxOwnerScoreAllowed;
	}
	
	public final void setMinOwnerScore(int minOwnerScore) throws SurveyFrozenException {
		checkRootNotFrozen();
		this.minOwnerScore = minOwnerScore;
		if (getMinOwnerScoreRequired()) {
			expose();
		}
	}

	public final int getMaxOwnerScore() {
		return maxOwnerScore;
	}

	public final void setMaxOwnerScore(int maxOwnerScore) throws SurveyFrozenException {
		checkRootNotFrozen();
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
	
	public void reset() throws SurveyFrozenException  {
		checkRootNotFrozen();
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


	public final void recalculate() { //TODO muss der public sein?
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