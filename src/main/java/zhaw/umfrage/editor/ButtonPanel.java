package zhaw.umfrage.editor;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import zhaw.umfrage.*;

class ButtonPanel extends JPanel implements DesignView, ActionListener {
	
	private static final long serialVersionUID = 1L;
	private static String ADD_COMMAND = "add";
	private static String REMOVE_COMMAND = "remove";
	private static String SORT_UP_COMMAND = "sortup";
	private static String SORT_DOWN_COMMAND = "sortdown";
	private JFrame messageFrame;
	private DesignController controller;
	private JButton addButton, removeButton, sortUpButton, sortDownButton;
	
	ButtonPanel(JFrame messageFrame) {
		super(new GridLayout(1,0));
		
		this.messageFrame = messageFrame;
		
		addButton = new JButton("Add item");
		addButton.setActionCommand(ADD_COMMAND);
		addButton.setEnabled(false);
		addButton.addActionListener(this);
		add(addButton);
		
		removeButton = new JButton ("Remove");
		removeButton.setActionCommand(REMOVE_COMMAND);
		removeButton.setEnabled(false);
		removeButton.addActionListener(this);
		add(removeButton);
		
        sortUpButton = new JButton("Sort up");
        sortUpButton.setActionCommand(SORT_UP_COMMAND);
        sortUpButton.setIcon(new ImageIcon("Icons/sort-up.png"));
        sortUpButton.setIconTextGap(10);
        sortUpButton.setEnabled(false);
        sortUpButton.addActionListener(this);
        add(sortUpButton);
        
        sortDownButton = new JButton("Sort down");
        sortDownButton.setActionCommand(SORT_DOWN_COMMAND);
        sortDownButton.setIcon(new ImageIcon("Icons/sort-down.png"));
        sortDownButton.setIconTextGap(10);
        sortDownButton.setEnabled(false);
        sortDownButton.addActionListener(this);
        add(sortDownButton);
        		
	}
	
	void setController(DesignController c) {
		if (controller != null) {
			controller.removeView(this);
		}
		controller = c;
		c.addView(this);
	}
	

	/** Implementation of ActionListener **/

	public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (ADD_COMMAND.equals(command)) {
        	try {
        		controller.addItem();
        	} catch (SurveyFrozenException ex) {
        		JOptionPane.showMessageDialog(messageFrame, ex);
        	}
        } else if (REMOVE_COMMAND.equals(command)) {
        	try {
        		controller.removeItem();
        	} catch (SurveyFrozenException ex) {
        		JOptionPane.showMessageDialog(messageFrame, ex);
        	}
        } else if (SORT_UP_COMMAND.equals(command)) {
        	try {
        		controller.moveSortUp();
        	} catch (SurveyFrozenException ex) {
        		JOptionPane.showMessageDialog(messageFrame, ex);
        	}
        } else if (SORT_DOWN_COMMAND.equals(command)) {
        	try {
        		controller.moveSortDown();
        	} catch (SurveyFrozenException ex) {
        		JOptionPane.showMessageDialog(messageFrame, ex);
        	}
        }
    }
	

	/** Implementation of DesignView **/

	@Override
	public void selectionChanged(SurveyTreeAbstract item) {
				
	}

	@Override
	public void surveySelected(Survey survey) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void questionnaireSelected(Questionnaire questionnaire) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void questionSelected(Question question) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void answerSelected(Answer answer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemAddable(boolean addable) {
		addButton.setEnabled(addable);
		
	}

	@Override
	public void itemRemoveable(boolean removeable) {
		removeButton.setEnabled(removeable);
		
	}
	
	@Override
	public void sortUpPossible(boolean possible) {
		sortUpButton.setEnabled(possible);
	}
	
	@Override
	public void sortDownPossible(boolean possible) {
		sortDownButton.setEnabled(possible);
	}

	@Override
	public void textChanged(String text) {
		// TODO Auto-generated method stub
	}

	@Override
	public void itemCountChanged(int itemCount) {

	}

	@Override
	public void minOwnerScoreRequiredSet(boolean isSet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void minOwnerScoreRequiredChanged(int scoreRequired) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void maxOwnerScoreAllowedSet(boolean isSet) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void maxOwnerScoreAllowedChanged(int maxScore) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void minAnswersToChoseChanged(int minAnswers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void maxAnswersToChoseChanged(int maxAnswers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scoreIfChosenChanged(int scoreIfChosen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void structureChanged(Survey root) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surveyFrozen(boolean frozen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void answerChosenChanged(Answer answer, boolean chosen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void questionAnsweredChanged(Question question, boolean answered) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void statesMayHaveChanged() {
		// TODO Auto-generated method stub
		
	}

}
