package zhaw.umfrage.editor;

import java.io.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.tree.TreePath;

import javax.swing.*;
import zhaw.umfrage.*;


public class SurveyEditor extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static SurveyEditor frame;
	private JPanel mainPanel;
	private DesignController controller;
	private File actualFile;
	
	static String NEW_COMMAND = "new";
	static String OPEN_COMMAND = "open";
	static String SAVE_COMMAND = "save";
    static String SAVE_AS_COMMAND = "save_as";
    static String DEBUG_COMMAND = "toggle_debug";
    static String SHOW_SCORE_COMMAND = "show_score";
    static String SHOW_MINMAX_SCORE_COMMAND = "show_minmax_score";
	
    private Dimension minSize = new Dimension(1024, 800);

    
    //private SurveyTreePanel treePanel;
    private TreePanel treePanel;
    private DetailPanel detailPanel;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    public SurveyEditor(String title) {
        super(title);
        setMinimumSize(minSize);
        
        //Get the Controller and register as View
        controller = new DesignController();
        
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        try {
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        	e.printStackTrace();
        }

        frame = new SurveyEditor("Survey Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image img = new ImageIcon("Icons/Binary-tree-icon.png").getImage();
        frame.setIconImage(img);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenuItem menuNew = new JMenuItem("New");
        menuNew.setActionCommand(NEW_COMMAND);
        JMenuItem menuOpen = new JMenuItem("Open");
        menuOpen.setActionCommand(OPEN_COMMAND);
        JMenuItem menuSave = new JMenuItem("Save");
        menuSave.setActionCommand(SAVE_COMMAND);
        JMenuItem menuSaveAs = new JMenuItem("Save as...");
        menuSaveAs.setActionCommand(SAVE_AS_COMMAND);
        JCheckBoxMenuItem menuDebug = new JCheckBoxMenuItem("Debug");
        menuDebug.setSelected(false);
        menuDebug.setActionCommand(DEBUG_COMMAND);
        
        JMenu menuShow = new JMenu("Show");
        JCheckBoxMenuItem showScore = new JCheckBoxMenuItem("Score");
        showScore.setSelected(false);
        showScore.setActionCommand(SHOW_SCORE_COMMAND);
        JCheckBoxMenuItem showMinMaxScore = new JCheckBoxMenuItem("Min/Max Score");
        showMinMaxScore.setSelected(false);
        showMinMaxScore.setActionCommand(SHOW_MINMAX_SCORE_COMMAND);

        menuFile.add(menuNew);
		menuFile.add(menuOpen);
		menuFile.add(menuSave);
		menuFile.add(menuSaveAs);
		//menuFile.add(menuDebug);
		menuBar.add(menuFile);
		menuShow.add(showScore);
		menuShow.add(showMinMaxScore);
		menuBar.add(menuShow);
		
		frame.setJMenuBar(menuBar);

        //Add Listener for Menu Commands
        menuNew.addActionListener(frame);
        menuOpen.addActionListener(frame);
        menuSave.addActionListener(frame);
        menuSaveAs.addActionListener(frame);
        menuDebug.addActionListener(frame);
        showScore.addActionListener(frame);
        showMinMaxScore.addActionListener(frame);
        
        //Prepare UI
        frame.prepareUI();

        //Display the window.
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);

    }
    
    public void prepareUI() {
    	mainPanel = new JPanel();
    	mainPanel.setLayout(new BorderLayout());
    	JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    	getContentPane().add(mainPanel, BorderLayout.CENTER);
    	treePanel = new TreePanel(this);
    	treePanel.setController(controller);
    	split.add(treePanel);
    	detailPanel = new DetailPanel(this);
    	detailPanel.setController(controller);
    	split.add(detailPanel);
    	split.setDividerLocation(550);
    	mainPanel.add(split, BorderLayout.CENTER);
    }
    
    
    /** Implementation of DesignView **/
/*
    @Override
	public void selectionChanged(SurveyTreeAbstract item) {
	}

	@Override
	public void surveySelected(Survey survey) {
	}

	@Override
	public void questionnaireSelected(Questionnaire questionnaire) {
	}

	@Override
	public void questionSelected(Question question) {
	}

	@Override
	public void answerSelected(Answer answer) {
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
	}

	@Override
	public void itemCountChanged(int itemCount) {
	}

	@Override
	public void minOwnerScoreRequiredSet(boolean isSet) {
	}

	@Override
	public void minOwnerScoreRequiredChanged(int scoreRequired) {
	}

	@Override
	public void maxOwnerScoreAllowedSet(boolean isSet) {
	}

	@Override
	public void maxOwnerScoreAllowedChanged(int maxScore) {
	}

	@Override
	public void minAnswersToChoseChanged(int minAnswers) {
	}

	@Override
	public void maxAnswersToChoseChanged(int maxAnswers) {
	}

	@Override
	public void scoreIfChosenChanged(int scoreIfChosen) {
	}

	@Override
	public void structureChanged(Survey root) {
	}

	@Override
	public void surveyFrozen(boolean frozen) {
	}

	@Override
	public void answerChosenChanged(Answer answer, boolean chosen) {
	}

	@Override
	public void questionAnsweredChanged(Question question, boolean answered) {
	}
	
	@Override
	public void statesMayHaveChanged() {
	}
	
	*/


	/** Implementation of ActionListener **/
	

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (NEW_COMMAND.equals(command)) {
        	controller.newSurvey();
     	} else if (OPEN_COMMAND.equals(command)) {
			JFileChooser fileChoose = new JFileChooser();
			fileChoose.showOpenDialog(frame);
			try {
				File file = fileChoose.getSelectedFile();
				if (file != null) {
					Survey survey = Survey.getFromFile(file);
					actualFile = file;
					controller.setSurvey(survey);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "sorry, an unhandeled exception has occured");
			}
        } else if (SHOW_SCORE_COMMAND.equals(command)) {
        	treePanel.toggleShowScore();
        } else if (SHOW_MINMAX_SCORE_COMMAND.equals(command)) {
        	treePanel.toggleShowMinMaxScore();
        } else if (SAVE_COMMAND.equals(command)) {
			if (actualFile == null) {
				JFileChooser fileChoose = new JFileChooser();
				fileChoose.showSaveDialog(frame);
				try {
					File file = fileChoose.getSelectedFile();
					if (file != null) {
						controller.getSurvey().saveToFile(file);
						actualFile = file;
					}
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this, "sorry, an unhandeled exception has occured");
				}
			} else {
				try {
					controller.getSurvey().saveToFile(actualFile);
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(this, "sorry, an unhandeled exception has occured");
				}
			}
        } else if (SAVE_AS_COMMAND.equals(command)) {
			JFileChooser fileChoose = new JFileChooser();
			fileChoose.showSaveDialog(frame);
			try {
				File file = fileChoose.getSelectedFile();
				if (file != null) {
					controller.getSurvey().saveToFile(file);
				}
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(this, "sorry, an unhandeled exception has occured");
			}
        }
    }


}
