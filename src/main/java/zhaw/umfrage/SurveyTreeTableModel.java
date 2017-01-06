/**
 * 
 */
package zhaw.umfrage;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.HashSet;
import java.util.Vector;

/**
 * @author Darian
 *
 */
public class SurveyTreeTableModel implements TableModel{
	
	private HashSet<TableModelListener> listeners;
	private Vector<SurveyTreeAbstract> items;
	private SurveyTreeAbstract owner;
	
	
	public SurveyTreeTableModel(SurveyTreeAbstract owner) {
		this.owner = owner;
		listeners = new HashSet<TableModelListener>();
		items = new Vector<SurveyTreeAbstract>();
		//TODO initialisieren hier ok?
		for (SurveyTreeAbstract item : owner.getItemList()) {
			addItem(item);
		}
	}
	
	private void notifyListeners(TableModelEvent e) {
		for(TableModelListener listener : listeners){
			listener.tableChanged(e);
		}
	}
	
	private void addItem(SurveyTreeAbstract item) {
		int index = items.size();
		items.add(item);
		TableModelEvent e = new TableModelEvent(this, index, index,	TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT );
		notifyListeners(e);
	}
	
	public void addNewItem(String text) {
		SurveyTreeAbstract item = null;
		if (owner.getClass() == Survey.class) {
			item = new Questionnaire((Survey) owner, text);
		} else {
			if (owner.getClass() == Questionnaire.class) {
				item = new Question((Questionnaire) owner, text);
			} else {
				if (owner.getClass() == Question.class) {
					item = new Answer((Question) owner, text);
				}
			}
		}
		if (item != null) {
			owner.addItem(item);
			addItem(item);
		}
	}
	
	public void removeItem(SurveyTreeAbstract item) {
		int index = items.indexOf(item);
		owner.removeItem(item);
		items.remove(item);
		TableModelEvent e = new TableModelEvent(this, index, index,	TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE );
		notifyListeners(e);
	}
	
	public Object[] getColumnValues(int columnIndex) {
		Object[] values = new Object[items.size()];
		int i = -1;
		for (SurveyTreeAbstract t : items) {
			i++;
			values[i] = getValueAt(i, columnIndex);
		}
		
		return values;
	}


	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}


	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0: return String.class;
		case 1: return Integer.class;
		case 2: return Boolean.class;
		case 3: return Integer.class;
		case 4: return Boolean.class;
		case 5: return Integer.class;
		default: return null;
		}
	}


	@Override
	public int getColumnCount() {
		return 6;
	}


	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0: return "Text";
		case 1: return "Sort";
		case 2: return "Use Min Owner Score";
		case 3: return "Min Owner Score";
		case 4: return "Use Max Owner Score";
		case 5: return "Max Owner Score";
		default: return null;
		}
	}


	@Override
	public int getRowCount() {
		return items.size();
	}


	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		SurveyTreeAbstract t = (SurveyTreeAbstract)items.get(rowIndex);
		 
		switch(columnIndex) {
		case 0: return t.getText();
		case 1: return new Integer(t.getSort());
		case 2: return t.getMinOwnerScoreRequired() ? Boolean.TRUE : Boolean.FALSE;
		case 3: return new Integer(t.getMinOwnerScore());
		case 4: return t.getMaxOwnerScoreAllowed() ? Boolean.TRUE : Boolean.FALSE;
		case 5: return new Integer(t.getMaxOwnerScore());
		default: return null;
		}
	}


	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}


	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}


	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	

}
