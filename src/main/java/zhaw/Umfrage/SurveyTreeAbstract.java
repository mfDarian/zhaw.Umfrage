/**
 * 
 */
package zhaw.Umfrage;

import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

/**
 * @author Darian
 *
 */
public abstract class SurveyTreeAbstract implements Serializable, Comparable<SurveyTreeAbstract> {
	
	private String text;
	private SurveyTreeAbstract owner;
	private ArrayList<SurveyTreeAbstract> slaveList;
	private int sort = 1;
	private boolean useMinOwnerScoreToBeReleased;
	private boolean useMaxOwnerScoreToBeReleased;
	private int minOwnerScoreToBeReleased;
	private int maxOwnerScoreToBeReleased;
	private transient int score = 0;
	private transient int actualSlave = -1;
	
	public SurveyTreeAbstract (String text, SurveyTreeAbstract owner) {
		this.text = text;
		this.slaveList = new ArrayList<SurveyTreeAbstract>();
		if (owner != null) {
			this.owner = owner;
			owner.addSlave(this);
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

	public final void setSlaveList(ArrayList<SurveyTreeAbstract> slaveList) {
		this.slaveList = slaveList;
		//TODO Exception, um falsche Zuordnungen zu vermeiden?
	}
	
	public final ArrayList<SurveyTreeAbstract> getSlaveList() {
		return slaveList;
	}

	public final void addSlave(SurveyTreeAbstract slave) {
		if (slaveList.contains(slave)) {
			return;
		}
		slaveList.add(slave);
		//TODO Exception, um falsche Zuordnungen zu vermeiden?
	}
	
	public final int getMaxSort() {
		int maxSort = 0;
		for (SurveyTreeAbstract slave : slaveList) {
			int sort = slave.getSort();
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
	
	protected final void notifySortChange() {
		Collections.sort(slaveList);
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

	protected void notifyScoreChange(SurveyTreeAbstract slave) {
		int score = this.score + slave.getScore();
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
	
	public final SurveyTreeAbstract getNextSlave() {
		SurveyTreeAbstract nextSlave = null;
		int c = actualSlave + 1;
		if (slaveList.size() >= c) {
			nextSlave = slaveList.get(c);
			if (nextSlave.useMinOwnerScoreToBeReleased || nextSlave.useMaxOwnerScoreToBeReleased) {
				while(c <= slaveList.size()) {
					if (!nextSlave.useMinOwnerScoreToBeReleased || (nextSlave.useMinOwnerScoreToBeReleased && score >= nextSlave.minOwnerScoreToBeReleased)) {
						if (!nextSlave.useMaxOwnerScoreToBeReleased || (nextSlave.useMaxOwnerScoreToBeReleased && score <= nextSlave.maxOwnerScoreToBeReleased)) {
							return nextSlave;
						}
					}
					c++;
					nextSlave = slaveList.get(c);
				}
			}
		}
		return nextSlave;
	}

}
