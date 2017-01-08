package zhaw.umfrage.editor;

import java.io.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.tree.TreePath;
import zhaw.umfrage.*;

// Test Dani
//Test von Swen
public class SurveyEditor extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	private JFrame frame;
	private File actualFile;
	
	static String NEW_COMMAND = "new";
	static String OPEN_COMMAND = "open";
	static String SAVE_COMMAND = "save";
    static String SAVE_AS_COMMAND = "save_as";
    static String DEBUG_COMMAND = "toggle_debug";
	
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
        treePanel = new SurveyTreePanel(this, null);
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

        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.gridwidth = 5;
        buttonPanel.add(addButton, gbc);
        gbc.gridx = 6;
        buttonPanel.add(removeButton, gbc); 
        gbc.gridx = 12;
        buttonPanel.add(clearButton, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.gridx = 5;
        gbc.gridwidth = 1;
        buttonPanel.add(new JLabel(" "), gbc);
        gbc.gridx = 11;
        buttonPanel.add(new JLabel(" "), gbc);
        
        add(buttonPanel, BorderLayout.SOUTH);
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
        } else if (DEBUG_COMMAND.equals(command)) {
        	treePanel.toggleDebug();
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
        try {
        	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        	e.printStackTrace();
        }

        JFrame frame = new JFrame("Survey Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Image img = new ImageIcon("Binary-tree-icon.png").getImage();
        frame.setIconImage(img);
        
        Insets s = new Insets(2,10,2,10);
        Insets s2 = new Insets(0,-30,0,10);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        menuFile.setMargin(s);
        JMenuItem menuNew = new JMenuItem("New");
        menuNew.setActionCommand(NEW_COMMAND);
        menuNew.setMargin(s2);
        JMenuItem menuOpen = new JMenuItem("Open");
        menuOpen.setActionCommand(OPEN_COMMAND);
        menuOpen.setMargin(s2);
        JMenuItem menuSave = new JMenuItem("Save");
        menuSave.setActionCommand(SAVE_COMMAND);
        menuSave.setMargin(s2);;
        JMenuItem menuSaveAs = new JMenuItem("Save as...");
        menuSaveAs.setActionCommand(SAVE_AS_COMMAND);
        menuSaveAs.setMargin(s2);
        
        JMenuItem menuDebug = new JMenuItem("Toggle Debug");
        menuDebug.setActionCommand(DEBUG_COMMAND);
        menuDebug.setMargin(s2);

        menuFile.add(menuNew);
		menuFile.add(menuOpen);
		menuFile.add(menuSave);
		menuFile.add(menuSaveAs);
		menuFile.add(menuDebug);
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
        menuDebug.addActionListener(newContentPane);

        //Display the window.
        frame.setMinimumSize(new Dimension(1000, 1000));
        frame.pack();
        frame.setLocationByPlatform(true);
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
