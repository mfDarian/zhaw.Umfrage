package zhaw.umfrage.consultation;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import zhaw.umfrage.*;


public class InterviewPlayer extends JPanel implements ConsultationView, ActionListener {
	
	private static final long serialVersionUID = 1L;

	static String OPEN_COMMAND = "open";
	
	private JFrame frame;
	private Survey survey;
	private File actualFile;
	private ConsultationController controller;
	private QuestionnairePanel questionnairePanel;
	private QuestionPanel questionPanel;
	private AnswerPanel answerPanel;
	private JButton forwardButton;
	private JButton startButton;
	private Interview interview;
	private ArrayList<AnswerBox> answerList;
	private JLabel interviewCount;
	private Summary summary;
	
	
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
	
    public InterviewPlayer(JFrame frame) {
        super();
        this.frame = frame;
        controller = new ConsultationController();
        answerList = new ArrayList<AnswerBox>();
        
        //Create the components.
        
        //Main Panel, das die anderen Panels trägt
        JPanel mainPanel = new JPanel(new GridLayout(7,0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        questionnairePanel = new QuestionnairePanel();
        mainPanel.add(questionnairePanel);
        
        questionPanel = new QuestionPanel();
        mainPanel.add(questionPanel);
        
        answerPanel = new AnswerPanel();
        mainPanel.add(answerPanel);
        
        forwardButton = new JButton("Proceed");
        forwardButton.setEnabled(false);
        startButton = new JButton("Start");
        startButton.setEnabled(false);
        
        forwardButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//Prüfen der Antwortanzahl
				//Melden an Interview, was angewählt ist
				//Nächste Frage anfordern
				try {
					controller.proceed();
					answerPanel.repaint();
				} catch (QuestionAnswerCountException ex) {
					JOptionPane.showMessageDialog(frame, ex);
				}
			}});
        
        startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (survey != null) {
					controller.startInterview("Interviewer", "anonym");
					try {
						controller.proceed();
					} catch (QuestionAnswerCountException ex) {
						System.out.println(ex);
					}
				} else {
					JOptionPane.showMessageDialog(frame, "no Survey File is loaded");
				}
			}});
        
        mainPanel.add(forwardButton);
        mainPanel.add(startButton);
        
        interviewCount = new JLabel("Interviews in Summary: 0");
        mainPanel.add(interviewCount);
        
        controller.addView(this);
        
        add(mainPanel);
    
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (OPEN_COMMAND.equals(command)) {
			JFileChooser fileChoose = new JFileChooser();
			fileChoose.showOpenDialog(frame);
			try {
				File file = fileChoose.getSelectedFile();
				if (file != null) {
					survey = Survey.getFromFile(file);
					controller.setSurvey(survey);
					actualFile = file;
				}
			} catch (Exception ex) {
				ex.printStackTrace(); //TODO
			}
        }
    }

    class QuestionnairePanel extends JPanel {
    	
		private static final long serialVersionUID = 1L;
		private JTextArea questionnaireText;
    	private Font f = new Font("Calibri", 30, 30);
    	
    	QuestionnairePanel() {
    		setFont(f);
    		questionnaireText = new JTextArea();
    		questionnaireText.setFont(f);
    		add(questionnaireText);
    	}
    	
    	void setQuestionnaireText(String text) {
    		questionnaireText.setText(text);
    	}
    }
    
    class QuestionPanel extends JPanel {
    	
		private static final long serialVersionUID = 1L;
		private JTextArea questionText;
    	private Font f = new Font("Calibri", 24, 24);
    	
    	QuestionPanel() {
    		setFont(f);
    		questionText = new JTextArea();
    		questionText.setFont(f);
    		add(questionText);
    	}
    	
    	void setQuestionText(String text) {
    		questionText.setText(text);
    	}
    }
    
    class AnswerPanel extends JPanel {
    	
		private static final long serialVersionUID = 1L;
    	private Font f = new Font("Calibri", 20, 20);
    	
    	AnswerPanel () {
    		this.setLayout(new GridLayout(0,1));
    		//scrollPane = new JScrollPane();
    		//add(scrollPane);
    	}
    	
    	void addAnswerBox(AnswerBox ab) {
    		//scrollPane.add(new JCheckBox(answerText));
    		JCheckBox box = ab.getBox();
    		box.setFont(f);
    		add(box);
    		answerList.add(ab);
    	}
    	
    	void clear() {
    		//System.out.println("clear: we have " + scrollPane.getComponentCount() + " Boxes");
    		//scrollPane.removeAll();
    		removeAll();
    		answerList.clear();
    	}
    }
    
    class AnswerBox {
    	
    	private Answer answer;
    	private JCheckBox box;
    	
    	AnswerBox(Answer answer) {
    		this.answer = answer;
    		box = new JCheckBox(answer.getText());
    		box.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent ev) {
					controller.setAnswerChosen(answer, box.isSelected());
				}});
    	}
    	
    	Answer getAnswer() {
    		return answer;
    	}
    	
    	JCheckBox getBox() {
    		return box;
    	}
    }
    
    private static void createAndShowGUI() {
        //Create and set up the window.
        try {
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        	e.printStackTrace();
        }

        JFrame frame = new JFrame("Interview");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        


        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");

        JMenuItem menuOpen = new JMenuItem("Open");
        menuOpen.setActionCommand(OPEN_COMMAND);
		menuFile.add(menuOpen);
		menuBar.add(menuFile);
		
		frame.setJMenuBar(menuBar);

        //Create and set up the content pane.
        InterviewPlayer newContentPane = new InterviewPlayer(frame);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        
        //Add Listener for Menu Commands
        menuOpen.addActionListener(newContentPane);

        //Display the window.
        //frame.setMinimumSize(new Dimension(1000, 1000));
        frame.setMinimumSize(new Dimension(500,350));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

	@Override
	public void interviewStarted() {
		// TODO Auto-generated method stub
		forwardButton.setEnabled(true);
		startButton.setEnabled(false);
	}

	@Override
	public void interviewAborted() {
		// TODO Auto-generated method stub
		startButton.setEnabled(true);
	}

	@Override
	public void interviewFinished() {
		// TODO Auto-generated method stub
		forwardButton.setEnabled(false);
		startButton.setEnabled(true);
		interview = controller.confirmFinished();
	}
	
	@Override
	public void summaryLoaded(Summary summary) {
		// TODO Auto-generated method stub
		this.summary = summary;
		startButton.setEnabled(true);
		//summaryUpdated();
	}
	
	@Override
	public void summaryUpdated() {
		// TODO Auto-generated method stub
		interviewCount.setText("Interviews in Summary: " +summary.getInterviewCount().toString());
	}

	@Override
	public void showQuestionnaire(Questionnaire questionnaire) {
		// TODO Auto-generated method stub
		questionnairePanel.setQuestionnaireText(questionnaire.getText());
	}

	@Override
	public void showQuestion(Question question) {
		// TODO Auto-generated method stub
		questionPanel.setQuestionText(question.getText());
		answerPanel.clear();
	}

	@Override
	public void showAnswer(Answer answer) {
		// TODO Auto-generated method stub
		answerPanel.addAnswerBox(new AnswerBox(answer));
		frame.pack();
	}

	@Override
	public void setAnswerChosen(Answer answer, boolean chosen) {
		for (AnswerBox ab : answerList) {
			if (answer == ab.getAnswer()) {
				ab.getBox().setSelected(chosen);
				break;
			}
		}
	}
}
