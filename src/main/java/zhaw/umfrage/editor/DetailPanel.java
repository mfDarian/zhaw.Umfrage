package zhaw.umfrage.editor;

import zhaw.umfrage.*;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

class DetailPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private SurveyEditor2 editor;
	private SurveyTreeAbstract subject;
	private GridBagConstraints gbc;
	private Border border;
	private JScrollPane scrollPane;

	DetailPanel(SurveyEditor2 editor) {
		super(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
		this.editor = editor;
		scrollPane = new JScrollPane();
		scrollPane.add(this);
		setTitle();
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
		gbc.gridx = 0;
		gbc.gridy = 0;
		drawTextField();
		
		if (subject instanceof Questionnaire) {
			drawOwnerScoreDependencies();
		}

		revalidate();
		validate();
	}
	
	private void drawTextField() {
		JTextField textField = new JTextField();
		add(textField, gbc);
		textField.setText(subject.getText());
		textField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editor.notifyTextChange(textField.getText());
			}});
	}
	
	private void drawOwnerScoreDependencies() {
		gbc.gridy = 1;
		JCheckBox minBox = new JCheckBox("min. owner score required");
		add(minBox, gbc);
		gbc.gridx = 1;
		JSpinner minSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
		add(minSpinner, gbc);
	}
	

}
