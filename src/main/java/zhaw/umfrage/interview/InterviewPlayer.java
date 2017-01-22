package zhaw.umfrage.interview;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import zhaw.umfrage.*;


public class InterviewPlayer extends JPanel implements View, ActionListener {
	
	static String OPEN_COMMAND = "open";
	
	private JFrame frame;
	private Survey survey;
	private File actualFile;
	private InterviewController controller;
	private QuestionPanel questionPanel;
	private AnswerPanel answerPanel;
	private JButton forwardButton;
	private JButton startButton;
	private Interview interview;
	private ArrayList<AnswerBox> answerList;
	
	
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
        controller = new InterviewController();
        controller.addView(this);
        answerList = new ArrayList<AnswerBox>();
        
        //Create the components.
        
        //Main Panel, das die anderen Panels trägt
        JPanel mainPanel = new JPanel(new GridLayout(4,0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        questionPanel = new QuestionPanel();
        mainPanel.add(questionPanel);
        
        answerPanel = new AnswerPanel();
        mainPanel.add(answerPanel);
        
        forwardButton = new JButton("Weiter");
        startButton = new JButton("Start");
        
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
					System.out.println(ex);
				}
			}});
        
        startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (survey != null) {
					controller.setSurvey(survey);
					controller.startInterview("Interviewer", "anonym");
					try {
						controller.proceed();
					} catch (QuestionAnswerCountException ex) {
						System.out.println(ex);
					}
				} else {
					System.out.println("Kein Survey geladen!");
				}
			}});
        
        mainPanel.add(forwardButton);
        mainPanel.add(startButton);
        
        
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
					actualFile = file;
				}
			} catch (Exception ex) {
				ex.printStackTrace(); //TODO
			}
        }
    }

    class QuestionPanel extends JPanel {
    	
    	private JTextArea questionText;
    	private Font f = new Font("Calibri", 48, 48);
    	
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
    	
    	private JScrollPane scrollPane;
    	private Font f = new Font("Arial", 30, 30);
    	
    	AnswerPanel () {
    		this.setLayout(new FlowLayout());
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
    		box.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
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
        
        Insets s = new Insets(2,10,2,10);
        Insets s2 = new Insets(0,-30,0,10);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        menuFile.setMargin(s);
        JMenuItem menuOpen = new JMenuItem("Open");
        menuOpen.setActionCommand(OPEN_COMMAND);
        menuOpen.setMargin(s2);
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
        frame.setSize(100,100);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

	@Override
	public void interviewStarted() {
		// TODO Auto-generated method stub
		System.out.println("Gestartet");
	}

	@Override
	public void interviewAborted() {
		// TODO Auto-generated method stub
		System.out.println("Abgebrochen");
	}

	@Override
	public void interviewFinished() {
		// TODO Auto-generated method stub
		System.out.println("Beendet");
		interview = controller.confirmFinished();
	}

	@Override
	public void showQuestionnaire(Questionnaire questionnaire) {
		// TODO Auto-generated method stub
		System.out.println("Questionnaire anzeigen: " + questionnaire);
		
	}

	@Override
	public void showQuestion(Question question) {
		// TODO Auto-generated method stub
		System.out.println("Question anzeigen: " + question);
		questionPanel.setQuestionText(question.getText());
		answerPanel.clear();
	}

	@Override
	public void showAnswer(Answer answer) {
		// TODO Auto-generated method stub
		System.out.println("Answer anzeigen: " + answer);
		answerPanel.addAnswerBox(new AnswerBox(answer));
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
