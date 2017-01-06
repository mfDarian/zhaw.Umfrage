package zhaw.umfrage.editor;

import java.util.ArrayList;
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
	private ArrayList<TreePath> collapsedPaths = new ArrayList<>();
	private TreePath storedSelectedPath;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	private JLabel textLabel, sortLabel, minLabel, maxLabel, chosenLabel, unchosenLabel;
	private JSpinner sortSpinner, minSpinner, maxSpinner, chosenSpinner, unchosenSpinner;
	private JCheckBox minBox, maxBox;
	private JTextField textField;
	private JButton saveButton;
	
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
        
        sortLabel = new JLabel("Set sorting");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        detailPanel.add(sortLabel, gbc);
        
        sortSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
        sortSpinner.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        detailPanel.add(sortSpinner, gbc);
        
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
        gbc.gridx = 1;
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
        gbc.gridx = 1;
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
		this.survey = survey;
		//root = new DefaultMutableTreeNode(survey);
		//treeModel = new SurveyTreeModel(root);
		//tree.setModel(treeModel);
		drawTree();
		//treeModel.reload();
		//treeModel.addTreeModelListener(new SurveyTreeModelListener());
		//tree.repaint();
		//expand();
	}
	

	private void drawTree() {
		DefaultMutableTreeNode questionnaireNode = null;
	    DefaultMutableTreeNode questionNode = null;
	    DefaultMutableTreeNode answerNode = null;
	    
	    Object[] listeners = treeModel.getTreeModelListeners();
	    
	    for (int i = 0; i < listeners.length; i++) {
	    	treeModel.removeTreeModelListener((TreeModelListener)listeners[i]);
	    }
	    
	    root = new DefaultMutableTreeNode(survey);
	    treeModel = new SurveyTreeModel(root);
	    tree.setModel(null);
	    tree.setModel(treeModel);
	    tree.validate();
	    
	    for (SurveyTreeAbstract questionnaire : survey.getItemList()) {
	    	questionnaireNode = new DefaultMutableTreeNode(questionnaire);
	    	//surveyNode.add(questionnaireNode);
	    	treeModel.insertNodeInto(questionnaireNode, root, root.getChildCount());
	    	
	    	for (SurveyTreeAbstract question : questionnaire.getItemList()) {
	    		questionNode = new DefaultMutableTreeNode(question);
	    		//questionnaireNode.add(questionNode);
	    		treeModel.insertNodeInto(questionNode, questionnaireNode, questionnaireNode.getChildCount());

	    		for (SurveyTreeAbstract answer : question.getItemList()) {
	    			answerNode = new DefaultMutableTreeNode(answer);
	    			answerNode.setAllowsChildren(false);
	    			//questionNode.add(answerNode);
	    			treeModel.insertNodeInto(answerNode, questionNode, questionNode.getChildCount());
	    		}
	    	}
	    }
	    
	    treeModel.reload();
	    tree.repaint();
		expand();

		for (int i = 0; i < listeners.length; i++) {
	    	treeModel.addTreeModelListener((TreeModelListener)listeners[i]);
	    }

	}
	
	private void expand() {
	    for (int i = 0; i < tree.getRowCount(); i++) {
	    	tree.expandPath(tree.getPathForRow(i));
	    }		
	}
	
	private void collapse() {
		for (int i = 0; i < tree.getRowCount(); i++) {
			tree.collapsePath(tree.getPathForRow(i));
		}
	}
	
	private void storeCollapseStatus() {
		collapsedPaths.clear();
		for (int i = 0; i < tree.getRowCount(); i++) {
			TreePath p = tree.getPathForRow(i);
			if (tree.isCollapsed(p) == true) {
				collapsedPaths.add(p);
			}
		}
		storedSelectedPath = getSelectedPath();
	}
	
	private void restoreCollapseStatus() {
		for (TreePath p : collapsedPaths) {
			tree.collapsePath(p);
		}
		if (storedSelectedPath != null) {
			boolean rowFound = false;
			for (int i = 0; i < tree.getRowCount(); i++) {
				if (tree.getPathForRow(i) == storedSelectedPath) {
					rowFound = true;
					break;
				}
			}
			if (rowFound) {
				tree.setSelectionPath(storedSelectedPath);
			}
		}
	}
	

	public TreePath getSelectedPath() {
		return tree.getSelectionPath();
	}

    /** Remove all nodes except the root node. */
    public void clear() {
        root.removeAllChildren();
        survey.clear();
        treeModel.reload();
    }

    /** Remove the currently selected node. */
    public void removeCurrentNode() {
        /*
    	TreePath currentSelection = tree.getSelectionPath();
        if (currentSelection != null) {
        	SurveyTreeAbstract currentNode = (SurveyTreeAbstract) currentSelection.getLastPathComponent();
        	SurveyTreeAbstract parent = (SurveyTreeAbstract) currentNode.getOwner();
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                return;
            }
        } 

        // Either there was no selection, or the root was selected.
        toolkit.beep();
        */
    }
    
    /*
    public SurveyTreeAbstract addObject(TreePath parentPath) {
    	if (parentPath != null) {
    		SurveyTreeAbstract parent = (SurveyTreeAbstract) parentPath.getLastPathComponent();
    		return addObject(parent, parentPath);
    	}
    	return null; // TODO
    }
    */
    
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
			sortSpinner.setValue((Integer)t.getSort());
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
			String text = newValue.toString();
			if (text != null) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				SurveyTreeAbstract s = (SurveyTreeAbstract) node.getUserObject();
				s.setText(text);
				textField.setText(text);
			}
			tree.repaint();
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
				t.setSort((int)sortSpinner.getValue());
				t.setMinOwnerScoreRequired((boolean)minBox.isSelected());
				t.setMinOwnerScore((int)minSpinner.getValue());
				t.setMaxOwnerScoreAllowed((boolean)maxBox.isSelected());
				t.setMaxOwnerScore((int)maxSpinner.getValue());
				if (t.getClass() == Answer.class) {
					Answer a = (Answer)t;
					a.setScoreIfChosen((int)chosenSpinner.getValue());
					a.setScoreIfUnchosen((int)unchosenSpinner.getValue());
				}
			
				storeCollapseStatus();
				drawTree();
				restoreCollapseStatus();
			}
			
		}
    	
    }
    
    class SurveyTreeSelectionListener implements TreeSelectionListener {
    	
		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
			if (node == null) {
				owner.setAddButtonEnabled(false);
				owner.setRemoveButtonEnabled(false);
				textField.setEnabled(false);
				sortSpinner.setEnabled(false);
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
				sortSpinner.setEnabled(true);
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
				sortSpinner.setEnabled(false);
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
				sortSpinner.setEnabled(true);
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
				setFieldValues(t);
			}
		}
    	
    }
	
    class SurveyTreeModelListener implements TreeModelListener {
        public void treeNodesChanged(TreeModelEvent e) {
          //  SurveyTreeAbstract node;
         //   node = (SurveyTreeAbstract)(e.getPath().getLastPathComponent());
         //   e.get

            /*
             * If the event lists children, then the changed
             * node is the child of the node we've already
             * gotten.  Otherwise, the changed node and the
             * specified node are the same.
             */

                //int index = e.getChildIndices()[0];
          //      node = (SurveyTreeAbstract)(node.getChildAt(index));

            
            tree.repaint();
            //System.out.println("New value: " + node.getUserObject());
        }
        public void treeNodesInserted(TreeModelEvent e) {
        	System.out.println("treeNodesInserted");
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        	System.out.println("treeNodesRemoved");
        }
        public void treeStructureChanged(TreeModelEvent e) {
        	System.out.println("treeStructureChanged");
        }
    }
    
    
    class SurveyTreeCellRenderer extends DefaultTreeCellRenderer {

		private static final long serialVersionUID = 1L;
		private ImageIcon surveyIcon;
		private ImageIcon questionnaireIcon;
        private ImageIcon questionIcon;
        private ImageIcon answerIcon;

        public SurveyTreeCellRenderer() {
            
        	surveyIcon = new ImageIcon("survey-icon.png");
        	questionnaireIcon = new ImageIcon("questionnaire-icon.png");
            questionIcon = new ImageIcon("question-icon.png");
            answerIcon = new ImageIcon("answer-icon.png");
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
            return this;
        }

    }

}
