package zhaw.umfrage;

import java.util.ArrayList;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class SurveyTreeModel implements TreeModel {
	
	private Survey root;
	private ArrayList<TreeModelListener> listeners;
	
	public SurveyTreeModel(Survey root) {
		this.root = root;
		listeners = new ArrayList<TreeModelListener>();
	}

	@Override
	public void addTreeModelListener(TreeModelListener listener) {
		listeners.add(listener);
	}

	@Override
	public Object getChild(Object owner, int index) {
		SurveyTreeAbstract t = (SurveyTreeAbstract) owner;
		return t.itemList.get(index);
	}

	@Override
	public int getChildCount(Object owner) {
		SurveyTreeAbstract t = (SurveyTreeAbstract) owner;
		return t.itemList.size();
	}

	@Override
	public int getIndexOfChild(Object owner, Object item) {
		SurveyTreeAbstract t = (SurveyTreeAbstract) owner;
		return t.itemList.indexOf(item);
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public boolean isLeaf(Object item) {
		SurveyTreeAbstract t = (SurveyTreeAbstract) item;
		return t.itemList.size() == 0;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void valueForPathChanged(TreePath p, Object item) {
		System.out.println("Value Changed");
		
	}

}
