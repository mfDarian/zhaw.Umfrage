package zhaw.umfrage.editor;

import java.io.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.tree.TreePath;
import zhaw.umfrage.*;



public class SurveyEditor extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private File actualFile;
	
	static String NEW_COMMAND = "new";
	static String OPEN_COMMAND = "open";
	static String SAVE_COMMAND = "save";
    static String SAVE_AS_COMMAND = "save_as";
	
	private static String ADD_COMMAND = "add";
    private JButton addButton;
    private static String REMOVE_COMMAND = "remove";
    private JButton removeButton;
    private static String CLEAR_COMMAND = "clear";
    private JButton clearButton;
    
    private SurveyTreePanel treePanel;

    public SurveyEditor(JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;
        
        //Create the components.
        treePanel = new SurveyTreePanel(this);
        populateTree(treePanel);
        
        addButton = new JButton("Add");
        addButton.setActionCommand(ADD_COMMAND);
        addButton.addActionListener(this);
        
        removeButton = new JButton("Remove");
        removeButton.setActionCommand(REMOVE_COMMAND);
        removeButton.addActionListener(this);
        
        clearButton = new JButton("Clear");
        clearButton.setActionCommand(CLEAR_COMMAND);
        clearButton.addActionListener(this);
        
        setAddButtonEnabled(false);
        setRemoveButtonEnabled(false);

        //Lay everything out.
        treePanel.setPreferredSize(new Dimension(300, 150));
        add(treePanel, BorderLayout.CENTER);

        JPanel panel = new JPanel(new GridLayout(0,3));
        panel.add(addButton);
        panel.add(removeButton); 
        panel.add(clearButton);
        add(panel, BorderLayout.SOUTH);
    }

    public void populateTree(SurveyTreePanel treePanel) {
        //TODO
    }
    
    public void setAddButtonEnabled(boolean aFlag) {
    	addButton.setEnabled(aFlag);
    }
    
    public void setRemoveButtonEnabled(boolean aFlag) {
    	removeButton.setEnabled(aFlag);
    }
    
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        TreePath s = treePanel.getSelectedPath();
        
        if (ADD_COMMAND.equals(command)) {
            //Add button clicked
            treePanel.addObject(s);
        } else if (REMOVE_COMMAND.equals(command)) {
            //Remove button clicked
            treePanel.removeObject(s);
        } else if (CLEAR_COMMAND.equals(command)) {
            //Clear button clicked.
        	treePanel.unselect();
            treePanel.clear();
        } else if (NEW_COMMAND.equals(command)) {
        	actualFile = null;
        	treePanel.setNewSurvey();
        } else if (OPEN_COMMAND.equals(command)) {
			JFileChooser fileChoose = new JFileChooser();
			fileChoose.showOpenDialog(frame);
			try {
				File file = fileChoose.getSelectedFile();
				if (file != null) {
					Survey survey = Survey.getFromFile(file);
					actualFile = file;
					treePanel.setSurvey(survey);
				}
			} catch (Exception ex) {
				ex.printStackTrace(); //TODO
			}
        } else if (SAVE_COMMAND.equals(command)) {
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
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Survey Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

        menuFile.add(menuNew);
		menuFile.add(menuOpen);
		menuFile.add(menuSave);
		menuFile.add(menuSaveAs);
		menuBar.add(menuFile);
		frame.setJMenuBar(menuBar);

        //Create and set up the content pane.
        SurveyEditor newContentPane = new SurveyEditor(frame);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        
        //Add Listener for Menu Commands
        menuNew.addActionListener(newContentPane);
        menuOpen.addActionListener(newContentPane);
        menuSave.addActionListener(newContentPane);
        menuSaveAs.addActionListener(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);


    
    
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }


}
