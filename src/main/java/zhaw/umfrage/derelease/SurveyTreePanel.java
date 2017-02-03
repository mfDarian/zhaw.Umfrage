package zhaw.umfrage.derelease;

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
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
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
	private SurveyTreeNode root;
	private DefaultTreeModel treeModel;
	private SurveyTreeModel tmodel;
	private JTree tree;
	private ArrayList<SurveyTreeAbstract> collapsedObjects = new ArrayList<>();
	private SurveyTreeAbstract storedSelectedObject;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private boolean debug, showScore, showMinMaxScore;
	
	private JLabel textLabel, minLabel, maxLabel, minAnswerLabel, maxAnswerLabel, chosenLabel;
	private JSpinner minSpinner, maxSpinner, minAnswerSpinner, maxAnswerSpinner, chosenSpinner;
	private JSlider minSlider;
	private JCheckBox minBox, maxBox, setBox, answeredBox;
	private JTextField textField;
	private JButton sortUpButton, sortDownButton, saveButton;
	
	private JPopupMenu popUp;
	
	protected SurveyTreePanel(SurveyEditor owner, Survey survey) {
		super(new GridLayout(1,2));
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		this.owner = owner;
		if (survey == null) {
			this.survey = new Survey("New Survey");
		} else {
			this.survey = survey;
		}
		root = new SurveyTreeNode(this.survey);
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
		popUp = new JPopupMenu("CTX");
		JMenuItem p1 = new JMenuItem("Hallo");
		JMenuItem p2 = new JMenuItem("Adie");
		popUp.add(p1);
		popUp.add(p2);
		tree.setComponentPopupMenu(popUp);
		JScrollPane scrollPane = new JScrollPane(tree);
		
		add(scrollPane);
		
		//Setup Detail Panel
        JPanel detailPanel = new JPanel(new GridBagLayout());
        
        Border bo = BorderFactory.createEmptyBorder(0, 20, 20, 20);
        //Border bo2 = BorderFactory.createTitledBorder(bo, "Textli", 1, 1, getFont()); //TODO
        detailPanel.setBorder(bo);
        

        
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
        gbc.gridwidth = 2;
        detailPanel.add(sortUpButton, gbc);
        
        
        
        
        sortDownButton = new JButton("Sort down");
        sortDownButton.setIcon(new ImageIcon("Icons/sort-down.png"));
        sortDownButton.setIconTextGap(10);
        sortDownButton.setEnabled(false);
        sortDownButton.addActionListener(new SortDownListener());
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
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
        

        minSlider = new JSlider();
        minSlider.setMaximum(5);
        
        minSlider.setLabelTable(minSlider.createStandardLabels(1));
        minSlider.setPaintLabels(true);
        //detailPanel.add(minSlider, gbc); TODO

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
        
        minAnswerLabel = new JLabel("Min Anwers");
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        detailPanel.add(minAnswerLabel, gbc);
        
        minAnswerSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
        minAnswerSpinner.setEnabled(false);
        gbc.gridx = 2;
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        detailPanel.add(minAnswerSpinner, gbc);
        
        maxAnswerLabel = new JLabel("Max Answers");
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        detailPanel.add(maxAnswerLabel, gbc);
        
        maxAnswerSpinner = new JSpinner(new SpinnerNumberModel(0,0,null,1));
        maxAnswerSpinner.setEnabled(false);
        gbc.gridx = 2;
        gbc.gridy = 9;
        gbc.gridwidth = 1;
        detailPanel.add(maxAnswerSpinner, gbc);

        chosenLabel = new JLabel("Score if chosen");
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        detailPanel.add(chosenLabel, gbc);
        
        chosenSpinner = new JSpinner(new SpinnerNumberModel(0,Integer.MIN_VALUE,null,1));
        chosenSpinner.setEnabled(false);
        gbc.gridx = 2;
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        detailPanel.add(chosenSpinner, gbc);
        
        answeredBox = new JCheckBox("Answered");
        answeredBox.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        detailPanel.add(answeredBox, gbc);
        
        setBox = new JCheckBox("Chosen");
        setBox.setEnabled(false);
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        detailPanel.add(setBox, gbc);
        
        saveButton = new JButton("Save details");
        saveButton.addActionListener(new DetailChangeListener());
        saveButton.setEnabled(false);
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 3;
        gbc.weighty = 10;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        detailPanel.add(saveButton, gbc);
        
        add(detailPanel);
	
	}
	
	protected void freeze() {
		try {
			survey.freeze();
		} catch (UnreachableItemsException e) {
			System.out.println(e); //TODO Fehlermeldung
		}
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
		SurveyTreeNode questionnaireNode = null;
	    SurveyTreeNode questionNode = null;
	    SurveyTreeNode answerNode = null;
	    
	    for (SurveyTreeAbstract questionnaire : survey.getItemList()) {
	    	questionnaireNode = new SurveyTreeNode(questionnaire);
	    	treeModel.insertNodeInto(questionnaireNode, root, root.getChildCount());
	    	
	    	for (SurveyTreeAbstract question : questionnaire.getItemList()) {
	    		questionNode = new SurveyTreeNode(question);
	    		treeModel.insertNodeInto(questionNode, questionnaireNode, questionnaireNode.getChildCount());

	    		for (SurveyTreeAbstract answer : question.getItemList()) {
	    			answerNode = new SurveyTreeNode(answer);
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
		SurveyTreeNode node = (SurveyTreeNode) path.getLastPathComponent();
		SurveyTreeAbstract pathObject = (SurveyTreeAbstract) node.getUserObject();
		return pathObject;
	}

    /** Remove all nodes except the root node. */
    public void clear() {
        root.removeAllChildren();
        try {
			survey.clear();
		} catch (SurveyFrozenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        treeModel.reload();
    }


    public void unselect() {
    	tree.setSelectionPath(null);
    }
    
    public void addObject(TreePath parentPath) {
    	SurveyTreeNode parentNode = (SurveyTreeNode) parentPath.getLastPathComponent();
		SurveyTreeAbstract parent = (SurveyTreeAbstract) parentNode.getUserObject();
		SurveyTreeAbstract child;
		try {
			child = parent.insertItem();
			if (child != null) {
				SurveyTreeNode childNode = new SurveyTreeNode(child);
				treeModel.insertNodeInto(childNode, parentNode, parentNode.getChildCount());
				tree.scrollPathToVisible(new TreePath(childNode.getPath()));
			}
		} catch (SurveyFrozenException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    public void removeObject(TreePath path) {
        if (path != null) {
        	SurveyTreeNode  currentNode = (SurveyTreeNode) path.getLastPathComponent();
        	SurveyTreeNode parentNode = (SurveyTreeNode) currentNode.getParent();
        	MutableTreeNode parent = (MutableTreeNode) parentNode;
        	SurveyTreeAbstract currentObject = (SurveyTreeAbstract) currentNode.getUserObject();
        	SurveyTreeAbstract parentObject = (SurveyTreeAbstract) parentNode.getUserObject();
            if (parent != null) {
                treeModel.removeNodeFromParent(currentNode);
                try {
					parentObject.removeItem(currentObject);
				} catch (SurveyFrozenException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				minAnswerSpinner.setValue((Integer)0);
				maxAnswerSpinner.setValue((Integer)0);
				chosenSpinner.setValue((Integer)a.getScoreIfChosen());
				setBox.setSelected((boolean)a.isChosen());
				answeredBox.setSelected(false);
			} else if (t.getClass() == Question.class) {
				Question q = (Question) t;
				minAnswerSpinner.setValue(q.getMinAnswersToChose());
				maxAnswerSpinner.setValue(q.getMaxAnswersToChose());
				chosenSpinner.setValue((Integer)0);
				setBox.setSelected(false);
				answeredBox.setSelected((boolean)q.isAnswered());
			} else {
				minAnswerSpinner.setValue((Integer)0);
				maxAnswerSpinner.setValue((Integer)0);
				chosenSpinner.setValue((Integer)0);
				setBox.setSelected(false);
				answeredBox.setSelected(false);
			}
		}
	}
	

	private class SurveyTreeNode extends DefaultMutableTreeNode {

		private static final long serialVersionUID = 1L;
		
		public SurveyTreeNode(Object userObject) {
			super(userObject);
		}

		@Override
		public String toString() {
			String s;
			if (userObject == null) {
				s = "empty Node";
			} else {
				SurveyTreeAbstract t = (SurveyTreeAbstract) userObject;
				s = t.toString();
				if (showScore && showMinMaxScore) {
					s += " [" + t.getMinScoreAchieveable() + " <= " + t.getScore() + " <= " + t.getMaxScoreAchieveable() + "]";
				} else if (showScore) {
					s += " [" + t.getScore() + "]";
				} else if (showMinMaxScore) {
					s += " [" + t.getMinScoreAchieveable() + " / " + t.getMaxScoreAchieveable() + "]";
				}
			}
			return s;
		}

	}

	
	private class SurveyTreeModel extends DefaultTreeModel {

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
				SurveyTreeNode node = (SurveyTreeNode) path.getLastPathComponent();
				SurveyTreeAbstract s = (SurveyTreeAbstract) node.getUserObject();
				try {
					s.setText(text);
				} catch (SurveyFrozenException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
				SurveyTreeNode n = (SurveyTreeNode)p.getLastPathComponent();
				SurveyTreeAbstract t = (SurveyTreeAbstract)n.getUserObject();
				try {
					t.setText(textField.getText());
				} catch (SurveyFrozenException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					t.setMinOwnerScoreRequired((boolean)minBox.isSelected());
				} catch (SurveyFrozenException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					t.setMinOwnerScore((int)minSpinner.getValue());
				} catch (SurveyFrozenException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					t.setMaxOwnerScoreAllowed((boolean)maxBox.isSelected());
				} catch (SurveyFrozenException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					t.setMaxOwnerScore((int)maxSpinner.getValue());
				} catch (SurveyFrozenException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (t.getClass() == Answer.class) {
					Answer a = (Answer)t;
					try {
						a.setScoreIfChosen((int)chosenSpinner.getValue());
					} catch (SurveyFrozenException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					a.setChosen((boolean)setBox.isSelected());
				} else if (t.getClass() == Question.class) {
					Question q = (Question)t;
					try {
						q.setMinAnswersToChose((int)minAnswerSpinner.getValue());
					} catch (SurveyFrozenException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						q.setMaxAnswersToChose((int)maxAnswerSpinner.getValue());
					} catch (SurveyFrozenException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						q.setAnswered((boolean)answeredBox.isSelected());
					} catch (QuestionAnswerCountException ex) {
						System.out.println(ex); // TODO
					}
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
			//SurveyTreeNode node = (SurveyTreeNode) e.getPath().getLastPathComponent();
    		SurveyTreeNode node = null;
    		if (getSelectedPath() != null) {
    			node = (SurveyTreeNode) getSelectedPath().getLastPathComponent();
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
				minAnswerSpinner.setEnabled(false);
				maxAnswerSpinner.setEnabled(false);
				chosenSpinner.setEnabled(false);
				setBox.setEnabled(false);
				answeredBox.setEnabled(false);
				saveButton.setEnabled(false);
			} else if (node.getUserObject().getClass() == Answer.class) {
				owner.setAddButtonEnabled(false);
				owner.setRemoveButtonEnabled(true);
				textField.setEnabled(true);
				minBox.setEnabled(true);
				minSpinner.setEnabled(true);
				maxBox.setEnabled(true);
				maxSpinner.setEnabled(true);
				minAnswerSpinner.setEnabled(false);
				maxAnswerSpinner.setEnabled(false);
				chosenSpinner.setEnabled(true);
				setBox.setEnabled(true);
				answeredBox.setEnabled(false);
				saveButton.setEnabled(true);
			} else if (node.getUserObject().getClass() == Survey.class) {
				owner.setAddButtonEnabled(true);
				owner.setRemoveButtonEnabled(false);
				textField.setEnabled(true);
				minBox.setEnabled(false);
				minSpinner.setEnabled(false);
				maxBox.setEnabled(false);
				maxSpinner.setEnabled(false);
				minAnswerSpinner.setEnabled(false);
				maxAnswerSpinner.setEnabled(false);
				chosenSpinner.setEnabled(false);
				setBox.setEnabled(false);
				answeredBox.setEnabled(false);
				saveButton.setEnabled(true);
			} else if (node.getUserObject().getClass() == Question.class) {
				owner.setAddButtonEnabled(true);
				owner.setRemoveButtonEnabled(true);
				textField.setEnabled(true);
				minBox.setEnabled(true);
				minSpinner.setEnabled(true);
				maxBox.setEnabled(true);
				maxSpinner.setEnabled(true);
				minAnswerSpinner.setEnabled(true);
				maxAnswerSpinner.setEnabled(true);
				chosenSpinner.setEnabled(false);
				setBox.setEnabled(false);
				answeredBox.setEnabled(true);
				saveButton.setEnabled(true);
			} else {
				owner.setAddButtonEnabled(true);
				owner.setRemoveButtonEnabled(true);
				textField.setEnabled(true);
				minBox.setEnabled(true);
				minSpinner.setEnabled(true);
				maxBox.setEnabled(true);
				maxSpinner.setEnabled(true);
				minAnswerSpinner.setEnabled(false);
				maxAnswerSpinner.setEnabled(false);
				chosenSpinner.setEnabled(false);
				setBox.setEnabled(false);
				answeredBox.setEnabled(false);
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
        private ImageIcon questionUnansweredIcon, questionAnsweredIcon;
        private ImageIcon answerUnchosenIcon, answerChosenIcon;

        public SurveyTreeCellRenderer() {
            
        	surveyIcon = new ImageIcon("Icons/survey-icon.png");
        	questionnaireIcon = new ImageIcon("Icons/questionnaire-icon.png");
        	questionUnansweredIcon = new ImageIcon("Icons/question-unanswered-icon.png");
        	questionAnsweredIcon = new ImageIcon("Icons/question-answered-icon.png");
            answerUnchosenIcon = new ImageIcon("Icons/answer-unchosen-icon.png");
            answerChosenIcon = new ImageIcon("Icons/answer-chosen-icon.png");
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            setToolTipText(null); //no tool tip
            SurveyTreeNode node = (SurveyTreeNode) value;
            SurveyTreeAbstract nodeObject = (SurveyTreeAbstract) (node.getUserObject());
            if (nodeObject.getClass() == Survey.class) {
            	setIcon(surveyIcon);
            } else if (nodeObject.getClass() == Questionnaire.class) {
                setIcon(questionnaireIcon);
            } else if (nodeObject.getClass() == Question.class) {
                if (((Question)nodeObject).isAnswered()) {
                	setIcon(questionAnsweredIcon);
                } else {
                	setIcon(questionUnansweredIcon);
                }
            } else if (nodeObject.getClass() == Answer.class) {
            	if (((Answer)nodeObject).isChosen()) {
            		setIcon(answerChosenIcon);
            	} else {
            		setIcon(answerUnchosenIcon);
            	}
            }
            // TODO test hintergrundfarbe (für allfällige Simulation)
            Color bg;
            if (nodeObject.isUnreachable()) {
                bg = new Color(255,185,185);
        	} else if (!nodeObject.isReachable()) {
            	bg = new Color(255,220,170);
            } else {
            	bg = new Color(255,255,255);
            }
        	setBackgroundNonSelectionColor(bg);
        	//setBackgroundSelectionColor(bg);
            return this;
        }

    }
    
    class SortUpListener implements ActionListener {
    	@Override
    	public void actionPerformed(ActionEvent arg0) {
			TreePath p = getSelectedPath();
			SurveyTreeNode n = (SurveyTreeNode)p.getLastPathComponent();
			SurveyTreeAbstract t = (SurveyTreeAbstract)n.getUserObject();
			try {
				t.moveSortUp();
			} catch (SurveyFrozenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setSurvey(survey); // TODO hack bzw. besser kapseln
		}
    }
    
    class SortDownListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			TreePath p = getSelectedPath();
			SurveyTreeNode n = (SurveyTreeNode)p.getLastPathComponent();
			SurveyTreeAbstract t = (SurveyTreeAbstract)n.getUserObject();
			try {
				t.moveSortDown();
			} catch (SurveyFrozenException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setSurvey(survey); // TODO hack bzw. besser kapseln
		}
    }
    
	private void setShowScore(boolean showScore) {
		this.showScore = showScore;
		setSurvey(survey);
	}
	
	protected boolean isShowScore() {
		return showScore;
	}
	
	protected void toggleShowScore() {
		setShowScore(!showScore);
	}
	
	private void setShowMinMaxScore(boolean showMinMaxScore) {
		this.showMinMaxScore = showMinMaxScore;
		setSurvey(survey);
	}
	
	protected boolean isShowMinMaxScore() {
		return showMinMaxScore;
	}
    
	protected void toggleShowMinMaxScore() {
		setShowMinMaxScore(!showMinMaxScore);
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
		setDebug(!debug);
	}

}
