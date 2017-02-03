package zhaw.umfrage.editor;

import zhaw.umfrage.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class DetailPanel extends JPanel implements DesignView, ActionListener, ChangeListener {
	
	private static final long serialVersionUID = 1L;
	private JFrame messageFrame;
	private DesignController controller;
	private SurveyTreeAbstract subject;
	private Border border;
	private JScrollPane scrollPane;
	private JButton freezeButton;
	private JLabel minAnswerLabel, maxAnswerLabel, scoreLabel;
	private JTextField textField;
	private JCheckBox minBox, maxBox, answeredBox, chosenBox;
	private JSpinner minSpinner, maxSpinner, scoreSpinner;
	private JSlider minSlider, maxSlider;

	DetailPanel(JFrame messageFrame) {
		super(new GridLayout(0,1));
		
		this.messageFrame = messageFrame;
		
		freezeButton = new JButton("Freeze Survey");
		freezeButton.addActionListener(this);
		
		textField = new JTextField();
		textField.addActionListener(this);
		
		minBox = new JCheckBox("min. owner score required");
		minBox.addActionListener(this);
		
		maxBox = new JCheckBox("max. owner score allowed");
		maxBox.addActionListener(this);
		
		minSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
		minSpinner.addChangeListener(this);
		
		maxSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
		maxSpinner.addChangeListener(this);
		
		minAnswerLabel = new JLabel("min. answers to chose");
        minSlider = new JSlider();
        minSlider.setLabelTable(minSlider.createStandardLabels(1));
        minSlider.setPaintLabels(true);
        minSlider.addChangeListener(this);

        maxAnswerLabel = new JLabel("max. answers to chose");
        maxSlider = new JSlider();
        maxSlider.setLabelTable(maxSlider.createStandardLabels(1));
        maxSlider.setPaintLabels(true);
        maxSlider.addChangeListener(this);
        
        answeredBox = new JCheckBox("answered");
        answeredBox.addActionListener(this);
        
        scoreLabel = new JLabel("score if chosen");
        scoreSpinner = new JSpinner(new SpinnerNumberModel(0,Integer.MIN_VALUE,null,1));
        scoreSpinner.addChangeListener(this);
        
        chosenBox = new JCheckBox("chosen");
        chosenBox.addActionListener(this);
		
		scrollPane = new JScrollPane();
		scrollPane.add(this);
		setTitle();
	}
	
	void setController(DesignController c) {
		if (controller != null) {
			controller.removeView(this);
		}
		controller = c;
		c.addView(this);
	}
	
	void clear() {
		subject = null;
		setTitle();
		removeAll();
	}
	
	void setSubject(SurveyTreeAbstract subject) {
		clear();
		this.subject = subject;
		setTitle();
		drawContent();
	}
	

	private void setTitle() {
		String borderTitle = " Details: ";
		if (subject != null) {
			borderTitle += subject.getClass().getSimpleName();
			borderTitle += " ";
		}
		border = BorderFactory.createLineBorder(Color.GRAY, 1, true);
		border = BorderFactory.createTitledBorder(border, borderTitle, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION);
	    setBorder(border);
		repaint();
	}
	
	void drawContent() {

		if (subject == null) {
			return;
		}
		
		drawTextField();
		drawBlankLabel();
		
		if (subject instanceof Survey) {
			drawFreezeButton();
		}
		
		if (subject instanceof Questionnaire) {
			drawOwnerScoreDependencies();
		}
		
		if (subject instanceof Question) {
			drawOwnerScoreDependencies();
			drawQuestionSettings();
		}
		
		if (subject instanceof Answer) {
			drawOwnerScoreDependencies();
			drawAnswerSettings();
		}
		

		revalidate();
		validate();

	}
	
	private void drawTextField() {
		textField.setText(subject.getText());
		add(textField);

	}
	
	private void drawFreezeButton() {
		add(freezeButton);
		drawBlankLabel();
	}
	
	
	private void drawOwnerScoreDependencies() {

		minBox.setSelected(subject.getMinOwnerScoreRequired());
		add(minBox);		

		minSpinner.setValue(subject.getMinOwnerScore());
		add(minSpinner);
		
		drawBlankLabel();
		
		maxBox.setSelected(subject.getMaxOwnerScoreAllowed());
		add(maxBox);

		maxSpinner.setValue(subject.getMaxOwnerScore());
		add(maxSpinner);
		
		drawBlankLabel();
		
	}
	
	private void drawQuestionSettings() {
		
		Question q = (Question)subject;
		
		add(minAnswerLabel);
		minSlider.setMaximum(q.getItemList().size());
		minSlider.setValue(q.getMinAnswersToChose());
		add(minSlider);
		
		drawBlankLabel();
		
		add(maxAnswerLabel);
		maxSlider.setMaximum(subject.getItemList().size());
		maxSlider.setValue(q.getMaxAnswersToChose());
		add(maxSlider);
		
		drawBlankLabel();
		
		answeredBox.setSelected(q.isAnswered());
		add(answeredBox);

	}
	
	private void drawAnswerSettings() {
		
		Answer a = (Answer)subject;
		
		add(scoreLabel);

		scoreSpinner.setValue(a.getScoreIfChosen());
		add(scoreSpinner);
		
		drawBlankLabel();
		
		chosenBox.setSelected(a.isChosen());
		add(chosenBox);
	}
	
	
	private void drawBlankLabel() {
		add(new JLabel(""));
	}
	
	
	/** Implementation of ActionListener **/
	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();
		
		if (source == textField) {
			try {
				controller.setText(textField.getText());
			} catch (SurveyFrozenException ex) {
				JOptionPane.showMessageDialog(messageFrame, ex);
			} finally {
				setSubject(subject);
			}
		} else if (source == minBox) {
			try {
				controller.setMinOwnerScoreRequired(minBox.isSelected());
			} catch (SurveyFrozenException ex) {
				JOptionPane.showMessageDialog(messageFrame, ex);
			} finally {
				setSubject(subject);
			}
		} else if (source == maxBox) {
			try {
				controller.setMaxOwnerScoreAllowed(maxBox.isSelected());
			} catch (SurveyFrozenException ex) {
				JOptionPane.showMessageDialog(messageFrame, ex);
			} finally {
				setSubject(subject);
			}
		} else if (source == answeredBox) {
			try {
				controller.setAnswered(answeredBox.isSelected());
			} catch (QuestionAnswerCountException ex) {
				JOptionPane.showMessageDialog(messageFrame, ex);
			} finally {
				setSubject(subject);
			}
		} else if (source == chosenBox) {
			controller.setChosen(chosenBox.isSelected());
		} else if (source == freezeButton) {
		      int answer = JOptionPane.showConfirmDialog(messageFrame, "Are you sure? A frozen Survey is ready for use in Consultations but can not be modified anymore. Freezing can not be undone.",
		              "Confirm Dialog", JOptionPane.YES_NO_CANCEL_OPTION);
		      switch (answer) {
		         case JOptionPane.YES_OPTION:
		            try {
		            	controller.freezeSurvey();
		            } catch (UnreachableItemsException ex) {
		            	JOptionPane.showMessageDialog(messageFrame, ex);
		            } finally {
		            	setSubject(subject);
		            }
		            JOptionPane.showMessageDialog(messageFrame, "Freeze successfull");
		            break;
		         case JOptionPane.NO_OPTION:
		            break;
		         case JOptionPane.CANCEL_OPTION:
		            break;
		      }
		}
	}
	
	
	/** Implementation of ChangeListener **/
	@Override
	public void stateChanged(ChangeEvent e) {
		
		Object source = e.getSource();
		
		if (source == minSpinner) {
			try {
				controller.setMinOwnerScore((Integer)minSpinner.getValue());
			} catch (SurveyFrozenException ex) {
				JOptionPane.showMessageDialog(messageFrame, ex);
			} finally {
				setSubject(subject);
			}
		} else if (source == maxSpinner) {
			try {
				controller.setMaxOwnerScore((Integer)maxSpinner.getValue());
			} catch (SurveyFrozenException ex) {
				JOptionPane.showMessageDialog(messageFrame, ex);
			} finally {
				setSubject(subject);
			}
		} else if (source == minSlider) {
			try {
				controller.setMinAnswersToChose((Integer)minSlider.getValue());
			} catch (SurveyFrozenException ex) {
				JOptionPane.showMessageDialog(messageFrame, ex);
			} finally {
				setSubject(subject);
			}
		} else if (source == maxSlider) {
			try {
				controller.setMaxAnswersToChose((Integer)maxSlider.getValue());
			} catch (SurveyFrozenException ex) {
				JOptionPane.showMessageDialog(messageFrame, ex);
			} finally {
				setSubject(subject);
			}
		} else if (source == scoreSpinner) {
			try {
				controller.setScoreIfChosen((Integer)scoreSpinner.getValue());
			} catch (SurveyFrozenException ex) {
				JOptionPane.showMessageDialog(messageFrame, ex);
			} finally {
				setSubject(subject);
			}
		}
	}
	

	/** Implementation of DesignView **/

	@Override
	public void selectionChanged(SurveyTreeAbstract item) {
		if (item == null) {
			clear();
		}
		
	}

	@Override
	public void surveySelected(Survey survey) {
		setSubject(survey);
		
	}

	@Override
	public void questionnaireSelected(Questionnaire questionnaire) {
		setSubject(questionnaire);
		
	}

	@Override
	public void questionSelected(Question question) {
		setSubject(question);
		
	}

	@Override
	public void answerSelected(Answer answer) {
		setSubject(answer);
		
	}

	@Override
	public void itemAddable(boolean addable) {
		
	}

	@Override
	public void itemRemoveable(boolean removeable) {
		
	}
	
	@Override
	public void sortUpPossible(boolean possible) {
		
	}
	
	@Override
	public void sortDownPossible(boolean possible) {
		
	}

	@Override
	public void textChanged(String text) {
		textField.setText(subject.getText());
		
	}

	@Override
	public void itemCountChanged(int itemCount) {
		
	}

	@Override
	public void minOwnerScoreRequiredSet(boolean isSet) {
		minBox.setSelected(isSet);
		
	}

	@Override
	public void minOwnerScoreRequiredChanged(int scoreRequired) {
		minSpinner.setValue(scoreRequired);
		
	}

	@Override
	public void maxOwnerScoreAllowedSet(boolean isSet) {
		maxBox.setSelected(isSet);
		
	}

	@Override
	public void maxOwnerScoreAllowedChanged(int maxScore) {
		maxSpinner.setValue(maxScore);
		
	}

	@Override
	public void minAnswersToChoseChanged(int minAnswers) {
		minSlider.setValue(minAnswers);
		
	}

	@Override
	public void maxAnswersToChoseChanged(int maxAnswers) {
		maxSlider.setValue(maxAnswers);
		
	}

	@Override
	public void scoreIfChosenChanged(int scoreIfChosen) {
		scoreSpinner.setValue(scoreIfChosen);
		
	}

	@Override
	public void structureChanged(Survey root) {
		
	}

	@Override
	public void surveyFrozen(boolean frozen) {
		freezeButton.setEnabled(!frozen);
		textField.setEnabled(!frozen);
		minBox.setEnabled(!frozen);
		maxBox.setEnabled(!frozen);
		minSpinner.setEnabled(!frozen);
		maxSpinner.setEnabled(!frozen);
		scoreSpinner.setEnabled(!frozen);
		minSlider.setEnabled(!frozen);
		maxSlider.setEnabled(!frozen);
		
	}

	@Override
	public void answerChosenChanged(Answer answer, boolean chosen) {
		chosenBox.setSelected(chosen);
		
	}

	@Override
	public void questionAnsweredChanged(Question question, boolean answered) {
		answeredBox.setSelected(answered);
	}
	
	@Override
	public void statesMayHaveChanged() {
		// TODO Auto-generated method stub
		
	}
	

}
