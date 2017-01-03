package zhaw.umfrage.editor;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import zhaw.umfrage.*;

class SurveyTreePanel extends JPanel {
	
	private SurveyEditor owner;
	private Survey survey;
	private DefaultMutableTreeNode root;
	private DefaultTreeModel treeModel;
	private JTree tree;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	
	protected SurveyTreePanel(SurveyEditor owner) {
		super(new GridLayout(1,0));
		this.owner = owner;
		this.survey = new Survey("survey");
		root = new DefaultMutableTreeNode(survey);
		treeModel = new SurveyTreeModel(root);
		treeModel.addTreeModelListener(new SurveyTreeModelListener());
		tree = new JTree(treeModel);
		tree.setEditable(true);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);
		tree.addTreeSelectionListener(new SurveyTreeSelectionListener());

		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane);
	}
	
	protected Survey getSurvey() {
		return survey;
	}
	
	public void setNewSurvey() {
		setSurvey(new Survey("survey"));
	}
	
	public void setSurvey(Survey survey) {
		this.survey = survey;
		root = new DefaultMutableTreeNode(survey);
		treeModel = new SurveyTreeModel(root);
		tree.setModel(treeModel);
		drawTree();
		treeModel.reload();
		treeModel.addTreeModelListener(new SurveyTreeModelListener());
		tree.repaint();
	}
	

	private void drawTree() {
		DefaultMutableTreeNode questionnaireNode = null;
	    DefaultMutableTreeNode questionNode = null;
	    DefaultMutableTreeNode answerNode = null;
	    
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
	}
	

	public TreePath getSelectedPath() {
		return tree.getSelectionPath();
	}

    /** Remove all nodes except the root node. */
    public void clear() {
        root.removeAllChildren();
        treeModel.reload();
        survey.setItemList(new ArrayList<SurveyTreeAbstract>());
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
    	Class childClass = parent.getItemClass();
		SurveyTreeAbstract child = null;
		if (childClass == Questionnaire.class) {
			child = new Questionnaire((Survey) parent, "New Questionnaire");
		} else if (childClass == Question.class) {
			child = new Question((Questionnaire) parent, "New Question");
		} else if (childClass == Answer.class) {
			child = new Answer((Question) parent, "New Answer");
		}
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
			}
			tree.repaint();
		}
    	
    }
    
    class SurveyTreeSelectionListener implements TreeSelectionListener {

		public void valueChanged(TreeSelectionEvent e) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
			if (node == null) {
				owner.setAddButtonEnabled(false);
				owner.setRemoveButtonEnabled(false);
			} else if (node.getUserObject().getClass() == Answer.class) {
				owner.setAddButtonEnabled(false);
				owner.setRemoveButtonEnabled(true);
			} else if (node.getUserObject().getClass() == Survey.class) {
				owner.setAddButtonEnabled(true);
				owner.setRemoveButtonEnabled(false);
			} else {
				owner.setAddButtonEnabled(true);
				owner.setRemoveButtonEnabled(true);
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
        }
        public void treeNodesRemoved(TreeModelEvent e) {
        }
        public void treeStructureChanged(TreeModelEvent e) {
        }
    }

}
