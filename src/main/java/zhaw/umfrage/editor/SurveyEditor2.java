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


public class SurveyEditor2 extends JFrame implements ActionListener, DesignView {

	private static final long serialVersionUID = 1L;
	
	private static SurveyEditor2 frame;
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
	
	private static String ADD_COMMAND = "add";
    private JButton addButton;
    private static String REMOVE_COMMAND = "remove";
    private JButton removeButton;
    private static String CLEAR_COMMAND = "clear";
    private JButton clearButton;
    private static String FREEZE_COMMAND = "freeze";
    private JButton freezeButton;
    
    private Dimension minSize = new Dimension(1024, 800);

    
    //private SurveyTreePanel treePanel;
    private TreePanel treePanel;
    private DetailPanel detailPanel;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    public SurveyEditor2(String title) {
        super(title);
        setMinimumSize(minSize);
        
        //Get the Controller and register as View
        controller = new DesignController();
        controller.addView(this);
        
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        try {
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        	e.printStackTrace();
        }

        frame = new SurveyEditor2("Survey Editor");
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
		menuFile.add(menuDebug);
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
    	split.add(treePanel);
    	detailPanel = new DetailPanel(this);
    	split.add(detailPanel);
    	mainPanel.add(split, BorderLayout.CENTER);
    }
    
    /** Let the sub panels communicate with the controller **/
    void notifySelectionChange(SurveyTreeAbstract selectedItem) {
    	controller.selectItem(selectedItem);
    }
    
    void notifyTextChange(String text) {
    	try {
			controller.setText(text);
		} catch (SurveyFrozenException e) {
			System.out.println(e);
		}
    }
    
    
    
    
    /** Implementation of DesignView **/

    @Override
	public void selectionChanged(SurveyTreeAbstract item) {
		// TODO Auto-generated method stub
		System.out.println("selection changed");
		treePanel.selectItem(item);
		if (item == null) {
			detailPanel.clear();
		}
	}



	@Override
	public void surveySelected(Survey survey) {
		// TODO Auto-generated method stub
		System.out.println("Survey selected");
		detailPanel.setSubject(survey);
	}



	@Override
	public void questionnaireSelected(Questionnaire questionnaire) {
		// TODO Auto-generated method stub
		detailPanel.setSubject(questionnaire);
	}



	@Override
	public void questionSelected(Question question) {
		// TODO Auto-generated method stub
		detailPanel.setSubject(question);
	}



	@Override
	public void answerSelected(Answer answer) {
		// TODO Auto-generated method stub
		detailPanel.setSubject(answer);
	}



	@Override
	public void itemAddable(boolean addable) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void itemRemoveable(boolean removeable) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void textChanged(String text) {
		// TODO Auto-generated method stub
		System.out.println("Text did Change");
		treePanel.reload();
	}



	@Override
	public void itemCountChanged(int itemCount) {
		// TODO Auto-generated method stub
		
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
	public void maxOwnerScoreAllowedChanged(boolean scoreAllowed) {
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
		System.out.println("Structure Changed");
		treePanel.setSurvey(root);
	}



	@Override
	public void surveyFrozen() {
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



	public void setAddButtonEnabled(boolean aFlag) {
    	addButton.setEnabled(aFlag);
    }
    
    public void setRemoveButtonEnabled(boolean aFlag) {
    	removeButton.setEnabled(aFlag);
    }
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (ADD_COMMAND.equals(command)) {
            //Add button clicked
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
				ex.printStackTrace(); //TODO
			}
        } /* else if (SAVE_COMMAND.equals(command)) {
			if (actualFile == null) {
				JFileChooser fileChoose = new JFileChooser();
				fileChoose.showSaveDialog(frame);
				try {
					File file = fileChoose.getSelectedFile();
					if (file != null) {
						treePanel.getSurvey().saveToFile(file);
						actualFile = file;
					}
				} catch (IOException ex) {
					ex.printStackTrace(); //TODO
				}
			} else {
				try {
					treePanel.getSurvey().saveToFile(actualFile);
				} catch (IOException ex) {
					ex.printStackTrace(); //TODO
				}
			}
        } else if (SAVE_AS_COMMAND.equals(command)) {
			JFileChooser fileChoose = new JFileChooser();
			fileChoose.showSaveDialog(frame);
			try {
				File file = fileChoose.getSelectedFile();
				if (file != null) {
					treePanel.getSurvey().saveToFile(file);
				}
			} catch (IOException ex) {
				ex.printStackTrace(); //TODO
			}
        } */
    }


}
