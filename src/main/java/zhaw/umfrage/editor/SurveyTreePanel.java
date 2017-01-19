ackage zhaw.umfrage.editor;

import java.util.ArrayList;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import zhaw.umfrage.*;

class SurveyTreePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private SurveyEditor owner;
	private Survey survey;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private JTree tree;
	private ArrayList<SurveyTreeAbstract> collapsedObjects = new ArrayList<>();
	private SurveyTreeAbstract storedSelectedObject;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private boolean debug;
	
	private JLabel textLabel, minLabel, maxLabel, chosenLabel, unchosenLabel;
	private JSpinner minSpinner, maxSpinner, chosenSpinner, unchosenSpinner;
	private JCheckBox minBox, maxBox;
	private JTextField textField;
	private JButton sortUpButton, sortDownButton, saveButton;
	
	protected SurveyTreePanel(SurveyEditor owner, Survey survey) {
		super(new GridLayout(1,2));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.owner = owner;
		if (survey == null) {
			this.survey = new Survey("New Survey");
		} else {
			this.survey = survey;
		}
		root = new DefaultMutableTreeNode(this.survey);
		treeModel = new SurveyTreeModel(root);
		treeModel.addTreeModelListener(new SurveyTreeModelListener());
		tree = new JTree(treeModel);
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.setRowHeight(0);
		tree.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		tree.setCellRenderer(new SurveyTreeCellRenderer());
		tree.addTreeSelectionListener(new SurveyTreeSelectionListener());
		JScrollPane scrollPane = new JScrollPane(tree);
		
		add(scrollPane);
		
		//Setup Detail Panel
        JPanel detailPanel = new JPanel(new GridBagLayout());
        detailPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;

        textLabel = new JLabel("Text");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        detailPanel.add(textLabel, gbc);
        
        textField = new JTextField();
        textField.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        detailPanel.add(textField, gbc);
        
        sortUpButton = new JButton("Sort up");
        sortUpButton.setIcon(new ImageIcon("Icons/sort-up.png"));
        sortUpButton.setIconTextGap(10);
        sortUpButton.setEnabled(false);
        sortUpButton.addActionListener(new SortUpListener());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        detailPanel.add(sortUpButton, gbc);
        
        sortDownButton = new JButton("Sort down");
        sortDownButton.setIcon(new ImageIcon("Icons/sort-down.png"));
        sortDownButton.setIconTextGap(10);
        sortDownButton.setEnabled(false);
        sortDownButton.addActionListener(new SortDownListener());
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        detailPanel.add(sortDownButton, gbc);
        
        minLabel = new JLabel("Set min. owner score to be released");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        detailPanel.add(minLabel, gbc);
        
        minBox = new JCheckBox();
        minBox.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        detailPanel.add(minBox, gbc);
        
        minSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
        minSpinner.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        detailPanel.add(minSpinner, gbc);
        
        maxLabel = new JLabel("Set max. owner score to be released");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        detailPanel.add(maxLabel, gbc);
        
        maxBox = new JCheckBox();
        maxBox.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        detailPanel.add(maxBox, gbc);
        
        maxSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
        maxSpinner.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        detailPanel.add(maxSpinner, gbc);

        chosenLabel = new JLabel("Score if chosen");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        detailPanel.add(chosenLabel, gbc);
        
        chosenSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
        chosenSpinner.setEnabled(false);
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        detailPanel.add(chosenSpinner, gbc);
        
        unchosenLabel = new JLabel("Score if unchosen");
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        detailPanel.add(unchosenLabel, gbc);
        
        unchosenSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
        unchosenSpinner.setEnabled(false);
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        detailPanel.add(unchosenSpinner, gbc);
        
        saveButton = new JButton("Save details");
        saveButton.addActionListener(new DetailChangeListener());
        saveButton.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 3;
        detailPanel.add(saveButton, gbc);
        
        add(detailPanel);
	
	}
	
	protected Survey getSurvey() {
		return survey;
	}
	
	public void setNewSurvey() {
		setSurvey(new Survey("survey"));
	}
	
	public void setSurvey(Survey survey) {
		storeCollapseStatus();
		this.survey = survey;
		root.removeAllChildren();
		root.setUserObject(survey);
		treeModel.setRoot(root);
		drawTree();
		restoreCollapseStatus();
	}
	

	private void drawTree() {
		DefaultMutableTreeNode questionnaireNode = null;
	    DefaultMutableTreeNode questionNode = null;
	    DefaultMutableTreeNode answerNode = null;
	    
	    for (SurveyTreeAbstract questionnaire : survey.getItemList()) {
	    	questionnaireNode = new DefaultMutableTreeNode(questionnaire);
	    	treeModel.insertNodeInto(questionnaireNode, root, root.getChildCount());
	    	
	    	for (SurveyTreeAbstract question : questionnaire.getItemList()) {
	    		questionNode = new DefaultMutableTreeNode(question);
	    		treeModel.insertNodeInto(questionNode, questionnaireNode, questionnaireNode.getChildCount());

	    		for (SurveyTreeAbstract answer : question.getItemList()) {
	    			answerNode = new DefaultMutableTreeNode(answer);
	    			answerNode.setAllowsChildren(false);
	    			treeModel.insertNodeInto(answerNode, questionNode, questionNode.getChildCount());
	    		}
	    	}
	    }
	    
	    treeModel.reload();
	    tree.repaint();
	    expandAll();

	}
	
	private void expandAll() {
	    for (int i = 0; i < tree.getRowCount(); i++) {
	    	tree.expandPath(tree.getPathForRow(i));
	    }		
	}
	
	private void collapseAll() {
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.collapsePath(tree.getPathForRow(i));
		}
	}
	
	private void storeCollapseStatus() {
		collapsedObjects.clear();
		for (int i = 0; i < tree.getRowCount(); i++) {
			TreePath p = tree.getPathForRow(i);
			if (tree.isCollapsed(p) == true) {
				collapsedObjects.add(getPathObject(p));
			}
		}
		storedSelectedObject = getPathObject(getSelectedPath());
	}
	
	private void restoreCollapseStatus() {
		expandAll();
		
		for (int i = 0; i < tree.getRowCount(); i++) {
			TreePath p = tree.getPathForRow(i);
			SurveyTreeAbstract o = getPathObject(p);
			for (SurveyTreeAbstract t : collapsedObjects) {
				if (t == o) {
					tree.collapseRow(i);
					break;
				}
			}
			if (o == storedSelectedObject) {
				tree.setSelectionPath(p);
			}
		}
		tree.requestFocus();
	}
	

	public TreePath getSelectedPath() {
		return tree.getSelectionPath();
	}
	
	private SurveyTreeAbstract getPathObject(TreePath path) {
		if (path == null) {
			return null;
		}
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		SurveyTreeAbstract pathObject = (SurveyTreeAbstract) node.getUserObject();
		return pathObject;
	}

    /** Remove all nodes except the root node. */
    public void clear() {
        root.removeAllChildren();
        survey.clear();
        treeModel.reload();
    }


    public void unselect() {
    	tree.setSelectionPath(null);
    }
    
    public void addObject(TreePath parentPath) {
    	DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
		SurveyTreeAbstract parent = (SurveyTreeAbstract) parentNode.getUserObject();
		SurveyTreeAbstract child = parent.insertItem();
		if (child != null) {
			DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
			treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
    }
    
    public void removeObject(TreePath path) {
        if (path != null) {
        	DefaultMutableTreeNode  currentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        	DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) currentNode.getParent();
        	MutableTreeNode parent = (MutableTreeNode) parentNode;
        	SurveyTreeAbstract currentObject = (SurveyTreeAbstract) currentNode.getUserObject();
        	SurveyTreeAbstract parentObject = (SurveyTreeAbstract) parentNode.getUserObject();
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                parentObject.removeItem(currentObject);
                return;
            }
        } 

        toolkit.beep();
    }
    
    
	private void setFieldValues(SurveyTreeAbstract t) {
		if (t != null) {
			textField.setText(t.getText());
			minBox.setSelected(t.getMinOwnerScoreRequired());
			minSpinner.setValue(t.getMinOwnerScore());
			maxBox.setSelected(t.getMaxOwnerScoreAllowed());
			maxSpinner.setValue(t.getMaxOwnerScore());
			if (t.getClass() == Answer.class) {
				Answer a = (Answer)t;
				chosenSpinner.setValue((Integer)a.getScoreIfChosen());
				unchosenSpinner.setValue((Integer)a.getScoreIfUnchosen());
			} else {
				chosenSpinner.setValue((Integer)0);
				unchosenSpinner.setValue((Integer)0);
			}
		}
	}
	
 
    class SurveyTreeModel extends DefaultTreeModel {

		private static final long serialVersionUID = 1L;

		public SurveyTreeModel(TreeNode root) {
			super(root);
		}		
		
		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {
        	if (debug) {
        		System.out.println("valueForPathChanged: " + path.toString());
        	}
			String text = newValue.toString();
			if (text != null) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				SurveyTreeAbstract s = (SurveyTreeAbstract) node.getUserObject();
				s.setText(text);
				textField.setText(text);
			}
			//tree.repaint(); TODO hack
			setSurvey(survey);
		}
    	
    }
    
    class DetailChangeListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			TreePath p = getSelectedPath();
			if (p != null) {
				DefaultMutableTreeNode n = (DefaultMutableTreeNode)p.getLastPathComponent();
				SurveyTreeAbstract t = (SurveyTreeAbstract)n.getUserObject();
				t.setText(textField.getText());
				t.setMinOwnerScoreRequired((boolean)minBox.isSelected());
				t.setMinOwnerScore((int)minSpinner.getValue());
				t.setMaxOwnerScoreAllowed((boolean)maxBox.isSelected());
				t.setMaxOwnerScore((int)maxSpinner.getValue());
				if (t.getClass() == Answer.class) {
					Answer a = (Answer)t;
					a.setScoreIfChosen((int)chosenSpinner.getValue());
					a.setScoreIfUnchosen((int)unchosenSpinner.getValue());
				}
			
				//tree.repaint(); TODO hack
				setSurvey(survey);
			}
			
		}
    	
    }
    
    class SurveyTreeSelectionListener implements TreeSelectionListener {
    	
		private void switchSortButtons(SurveyTreeAbstract userObject) {
			if (userObject.getClass() == Survey.class) {
				sortUpButton.setEnabled(false);
				sortDownButton.setEnabled(false);
			} else {
				if (userObject.getSort() > 1) {
					sortUpButton.setEnabled(true);
				} else {
					sortUpButton.setEnabled(false);
				}
				if (userObject.getSort() < userObject.getOwner().getMaxSort()) {
					sortDownButton.setEnabled(true);
				} else {
					sortDownButton.setEnabled(false);
				}
			}
		}
    	
    	public void valueChanged(TreeSelectionEvent e) {
			//DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
    		DefaultMutableTreeNode node = null;
    		if (getSelectedPath() != null) {
    			node = (DefaultMutableTreeNode) getSelectedPath().getLastPathComponent();
    		}
			if (node == null) {
				owner.setAddButtonEnabled(false);
				owner.setRemoveButtonEnabled(false);
				textField.setEnabled(false);
				sortUpButton.setEnabled(false);
				sortDownButton.setEnabled(false);
				minBox.setEnabled(false);
				minSpinner.setEnabled(false);
				maxBox.setEnabled(false);
				maxSpinner.setEnabled(false);
				chosenSpinner.setEnabled(false);
				unchosenSpinner.setEnabled(false);
				saveButton.setEnabled(false);
			} else if (node.getUserObject().getClass() == Answer.class) {
				owner.setAddButtonEnabled(false);
				owner.setRemoveButtonEnabled(true);
				textField.setEnabled(true);
				minBox.setEnabled(true);
				minSpinner.setEnabled(true);
				maxBox.setEnabled(true);
				maxSpinner.setEnabled(true);
				chosenSpinner.setEnabled(true);
				unchosenSpinner.setEnabled(true);
				saveButton.setEnabled(true);
			} else if (node.getUserObject().getClass() == Survey.class) {
				owner.setAddButtonEnabled(true);
				owner.setRemoveButtonEnabled(false);
				textField.setEnabled(true);
				minBox.setEnabled(false);
				minSpinner.setEnabled(false);
				maxBox.setEnabled(false);
				maxSpinner.setEnabled(false);
				chosenSpinner.setEnabled(false);
				unchosenSpinner.setEnabled(false);
				saveButton.setEnabled(true);
			} else {
				owner.setAddButtonEnabled(true);
				owner.setRemoveButtonEnabled(true);
				textField.setEnabled(true);
				minBox.setEnabled(true);
				minSpinner.setEnabled(true);
				maxBox.setEnabled(true);
				maxSpinner.setEnabled(true);
				chosenSpinner.setEnabled(false);
				unchosenSpinner.setEnabled(false);
				saveButton.setEnabled(true);
			}
			
			if (node != null) {
				SurveyTreeAbstract t = (SurveyTreeAbstract) node.getUserObject();
				switchSortButtons(t);
				setFieldValues(t);
			} else {
				sortUpButton.setEnabled(false);
				sortDownButton.setEnabled(false);
			}
		}
    	
    }
	
    class SurveyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
        	if (debug) {
        		System.out.println("treeNodesChanged: " + e.getTreePath());
        	}
        }
        
        public void treeNodesInserted(TreeModelEvent e) {
        	if (debug) {
        		System.out.println("treeNodesInserted: " + e.getTreePath());
        	}
        }
        
        public void treeNodesRemoved(TreeModelEvent e) {
        	if (debug) {
        		System.out.println("treeNodesRemoved: " + e.getTreePath());
        	}
        	
        }
        
        public void treeStructureChanged(TreeModelEvent e) {
        	if (debug) {
        		System.out.println("treeStructureChanged: " + e.getTreePath());
        	}
        	
        }
    }
    
    
    class SurveyTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;
		private ImageIcon surveyIcon;
		private ImageIcon questionnaireIcon;
        private ImageIcon questionIcon;
        private ImageIcon answerIcon;

        public SurveyTreeCellRenderer() {
            
        	surveyIcon = new ImageIcon("Icons/survey-icon.png");
        	questionnaireIcon = new ImageIcon("Icons/questionnaire-icon.png");
            questionIcon = new ImageIcon("Icons/question-icon.png");
            answerIcon = new ImageIcon("Icons/answer-icon.png");
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            setToolTipText(null); //no tool tip
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            SurveyTreeAbstract nodeObject = (SurveyTreeAbstract) (node.getUserObject());
            if (nodeObject.getClass() == Survey.class) {
            	setIcon(surveyIcon);
            } else if (nodeObject.getClass() == Questionnaire.class) {
                setIcon(questionnaireIcon);
            } else if (nodeObject.getClass() == Question.class) {
                setIcon(questionIcon);
            } else if (nodeObject.getClass() == Answer.class) {
            	setIcon(answerIcon);
            }
            // TODO test hintergrundfarbe (für allfällige Simulation)
            Color bg = new Color(210,60,120);
            setBackgroundNonSelectionColor(bg);
            setBackgroundSelectionColor(bg);
            return this;
        }

    }
    
    class SortUpListener implements ActionListener {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
			TreePath p = getSelectedPath();
			DefaultMutableTreeNode n = (DefaultMutableTreeNode)p.getLastPathComponent();
			SurveyTreeAbstract t = (SurveyTreeAbstract)n.getUserObject();
			t.moveSortUp();
			setSurvey(survey); // TODO hack bzw. besser kapseln
		}
    }
    
    class SortDownListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			TreePath p = getSelectedPath();
			DefaultMutableTreeNode n = (DefaultMutableTreeNode)p.getLastPathComponent();
			SurveyTreeAbstract t = (SurveyTreeAbstract)n.getUserObject();
			t.moveSortDown();
			setSurvey(survey); // TODO hack bzw. besser kapseln
		}
    }
    
	public void setDebug(boolean debug) {
		this.debug = debug;
		if (debug) {
			System.out.println("Debugging activated");
		} else {
			System.out.println("Debugging deactivated");
		}
	}
	
	public boolean isDebug() {
		return debug;
	}
	
	public void toggleDebug() {
		if (debug) {
			setDebug(false);
		} else {
			setDebug(true);
		}
	}

}
