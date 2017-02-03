package zhaw.umfrage.editor;

import zhaw.umfrage.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

class TreePanel extends JPanel implements DesignView {

	private static final long serialVersionUID = 1L;
	private JFrame messageFrame;
	private DesignController controller;
	private Survey survey;
	private SurveyTreeNode root;
	private SurveyTreeModel treeModel;
	private JScrollPane scrollPane;
	private JTree tree;
	private Border border;
	private ArrayList<SurveyTreeAbstract> collapsedObjects;
	private SurveyTreeAbstract storedSelectedObject;
	private ButtonPanel buttonPanel;
	private boolean showScore, showMinMaxScore;

	TreePanel(JFrame messageFrame) {
		super(new BorderLayout());
		this.messageFrame = messageFrame;
		tree = new JTree();
		tree.setModel(null);
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.setRowHeight(0);
		tree.setCellRenderer(new SurveyTreeCellRenderer());
		tree.addTreeSelectionListener(new SurveyTreeSelectionListener());
		border = BorderFactory.createLineBorder(Color.GRAY, 1, true);
		border = BorderFactory.createTitledBorder(border, " Structure ", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION);
		collapsedObjects = new ArrayList<>();
		scrollPane = new JScrollPane(tree);
		scrollPane.setBorder(border);
		add(scrollPane, BorderLayout.CENTER);
		buttonPanel = new ButtonPanel(this.messageFrame);
		add(buttonPanel, BorderLayout.SOUTH);
		
	}
	
	void setController(DesignController c) {
		if (controller != null) {
			controller.removeView(this);
		}
		controller = c;
		controller.addView(this);
		buttonPanel.setController(c);
	}
	
	void toggleShowScore() {
		showScore = !showScore;
		reload();
	}
	
	void toggleShowMinMaxScore() {
		showMinMaxScore = !showMinMaxScore;
		reload();
	}
	
	void setSurvey(Survey survey) {
		storeCollapseStatus();
		this.survey = survey;
		root = new SurveyTreeNode(this.survey);
		treeModel = new SurveyTreeModel(root);
		tree.setModel(treeModel);
		if (survey.isFrozen()) {
			tree.setBackground(messageFrame.getBackground());
		} else {
			tree.setBackground(Color.WHITE);
		}
		drawTree();
	}
	
	void reload() {
		if (survey != null) {
			storeCollapseStatus();
			treeModel.reload();
			restoreCollapseStatus();
		}
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
	    restoreCollapseStatus();

	}

	private void expandAll() {
	    for (int i = 0; i < tree.getRowCount(); i++) {
	    	tree.expandPath(tree.getPathForRow(i));
	    }		
	}
	
	void selectItem(SurveyTreeAbstract item) {
		for (int i = 0; i < tree.getRowCount(); i++) {
			if (getPathObject(tree.getPathForRow(i)) == item) {
				tree.setSelectionPath(tree.getPathForRow(i));
				tree.scrollPathToVisible(tree.getPathForRow(i));
				return;
			}
		}
		unselect();
	}
	
    void unselect() {
    	tree.setSelectionPath(null);
    }
    
    
    
	private void storeCollapseStatus() {
		collapsedObjects.clear();
		for (int i = 0; i < tree.getRowCount(); i++) {
			TreePath p = tree.getPathForRow(i);
			if (tree.isCollapsed(p) == true) {
				collapsedObjects.add(getPathObject(p));
			}
		}
		storedSelectedObject = getPathObject(tree.getSelectionPath());
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
	}
	
	private void expandActualPath() {
		if (tree.isCollapsed(tree.getSelectionPath())) {
			tree.expandPath(tree.getSelectionPath());
		}
	}
	
	private SurveyTreeAbstract getPathObject(TreePath path) {
		if (path == null) {
			return null;
		}
		SurveyTreeNode node = (SurveyTreeNode) path.getLastPathComponent();
		SurveyTreeAbstract pathObject = (SurveyTreeAbstract) node.getUserObject();
		return pathObject;
	}


	class SurveyTreeNode extends DefaultMutableTreeNode {

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
	

	class SurveyTreeModel extends DefaultTreeModel {

		private static final long serialVersionUID = 1L;

		public SurveyTreeModel(TreeNode root) {
			super(root);
		}		
		
		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {
			String text = newValue.toString();
			if (text != null) {
				storeCollapseStatus();
				//SurveyTreeNode node = (SurveyTreeNode) path.getLastPathComponent();
				//SurveyTreeAbstract s = (SurveyTreeAbstract) node.getUserObject();
				try {
					controller.setText(text);
				} catch (SurveyFrozenException e) {
					System.out.println(e); //TODO
				} finally {
					treeModel.reload();
					restoreCollapseStatus();
				}
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
        	//surveyIcon = new ImageIcon(java.awt.Toolkit.getDefaultToolkit().getClass().getResource("/Resource/survey-icon.png")); //TODO not working
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
            Color bg;
            if (nodeObject.isUnreachable()) {
                bg = new Color(255,185,185);
        	} else if (!nodeObject.isReachable()) {
            	bg = new Color(255,220,170);
        	} else if (survey.isFrozen()) {
        		bg = messageFrame.getBackground();
        	} else {
            	bg = new Color(255,255,255);
            }
        	setBackgroundNonSelectionColor(bg);
            return this;
        }

    }
	
	
    class SurveyTreeSelectionListener implements TreeSelectionListener {
    	
    	public void valueChanged(TreeSelectionEvent e) {
    		SurveyTreeNode node = null;
    		SurveyTreeAbstract userObject = null;
    		if (tree.getSelectionPath() != null) {
    			node = (SurveyTreeNode) tree.getSelectionPath().getLastPathComponent();
    			userObject = (SurveyTreeAbstract) node.getUserObject();
    		}
    		controller.selectItem(userObject);
			

		}
    	
    }

    
    /** Implementation of DesignView **/

	@Override
	public void selectionChanged(SurveyTreeAbstract item) {
		selectItem(item);
		
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
		reload();
		
	}

	@Override
	public void itemCountChanged(int itemCount) {
		setSurvey(survey);
		expandActualPath();
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
		setSurvey(root);
		
	}

	@Override
	public void surveyFrozen(boolean frozen) {
		setSurvey(survey);
		
	}

	@Override
	public void answerChosenChanged(Answer answer, boolean chosen) {
		reload();
		
	}

	@Override
	public void questionAnsweredChanged(Question question, boolean answered) {
		reload();
		
	}
	
	@Override
	public void statesMayHaveChanged() {
		reload();
		
	}

}
