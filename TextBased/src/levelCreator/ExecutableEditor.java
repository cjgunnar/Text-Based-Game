package levelCreator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import gamelogic.Condition;
import gamelogic.DescriptionAction;
import gamelogic.Executable;
import gamelogic.ExecutableGroup;
import gamelogic.OutAction;
import gamelogic.Request;
import sceneObjects.SceneObject;

@SuppressWarnings("serial")
public class ExecutableEditor extends JDialog
{
	/** Height of the editor */
	static final int HEIGHT = 200;
	/** Widht of the editor */
	static final int WIDTH = 400;
	
	/** Parent of the request Executables being displayed */
	SceneObject parent;
	/** Reference to the owner of the executables */
	Request request;
	
	/** Visible tree heiarchy containing Executables */
	JTree executableTree;
	
	/** Model for tree */
	DefaultTreeModel treeModel;
	
	/** Root of the tree model */
	DefaultMutableTreeNode root;
	
	/** Default Constructor */
	public ExecutableEditor()
	{
		super();
		
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Executable Editor");
		
		CreateComponents();
	}
	
	public ExecutableEditor(Request request)
	{
		super();
		
		this.request = request;
		
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Executable Editor");
		
		CreateComponents();
	}

	/** Create all the components */
	private void CreateComponents()
	{
		JPanel main = new JPanel();
		/** Displays info about the currently selected Executable */
		JPanel executableInfo = new JPanel();
		JLabel executableDisplay = new JLabel();
		
		//create root of tree
		root = new DefaultMutableTreeNode("Root");

		//sample leaves for now
		DefaultMutableTreeNode leafA = new DefaultMutableTreeNode("Leaf");
		leafA.setUserObject(new OutAction("Well it is quite lovely out today if you ignore the worms."));
		
		root.add(leafA);

		//init tree
		executableTree = new JTree(root);
		//set size
		executableTree.setMinimumSize(new Dimension(150, 300));
		//can only select one at a time
		executableTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		//init tree model
		treeModel = (DefaultTreeModel)executableTree.getModel();
		
		//button to create executables, hide when nothing selected
		JButton addExecutable = new JButton("Add Executable");
		addExecutable.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String[] options = {"Out", "Out Description", "Condition", "Property Change"};
				
				//bring up menu asking what type of action to create
				String choice = (String)JOptionPane.showInputDialog(rootPane, "", "New Executable", JOptionPane.QUESTION_MESSAGE, null, options, null);
				
				if(choice == null) {return;}
				
				//how to make each type of Executable
				if(choice.equals("Out"))
				{
					//get creation output
					JLabel outLabel = new JLabel("Enter text to output.");
					JTextField outText = new JTextField("output text");
					JOptionPane.showMessageDialog(rootPane, new Object[]{outLabel, outText}, "Create an Out Action", JOptionPane.PLAIN_MESSAGE, null);
					if(!outText.getText().trim().equals(""))
					{
						//add to selected node
						addNodeAndUserObjectToSelectedNode(new OutAction(outText.getText()));
					}
					
					//TODO actually add to request
				}
				else if(choice.equals("Out Description"))
				{
					addNodeAndUserObjectToSelectedNode(new DescriptionAction());
					//TODO actually add to request
				}
				else if(choice.equals("Property Change"))
				{
					//TODO property change
				}
				else if(choice.equals("Condition"))
				{
					//create fields to get input from
					JTextField target = new JTextField(10);
					JTextField property_name = new JTextField("Property Name");
					JComboBox<String> operator = new JComboBox<String>(new String[]{SceneObject.equalTo, SceneObject.greaterThan, SceneObject.lessThan});
					JTextField value = new JTextField("Value to test against");
					
					//create pop-up to get info
					JOptionPane.showMessageDialog(rootPane, 
							new Object[]{new JLabel("Enter the target, property name, operator, and value to check."), target, property_name, operator, value}, 
							"Create Condition", 
							JOptionPane.QUESTION_MESSAGE,
							null);
					
					//turn non-int fields into ints
					int targetInt = 0;
					int valueInt = 0;
					
					try
					{
						targetInt = Integer.parseInt(target.getText());
						valueInt = Integer.parseInt(value.getText());
					}
					catch(NumberFormatException error)
					{
						JOptionPane.showMessageDialog(rootPane, "Target ID and value must be integers", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					//create the condition
					Condition condition = new Condition(targetInt, property_name.getText(), (String)operator.getSelectedItem(), valueInt);
					
					//add pass group
					ExecutableGroup passGroup = new ExecutableGroup(ExecutableGroup.PASS);
					condition.addPassExecutable(passGroup);
					
					ExecutableGroup failGroup = new ExecutableGroup(ExecutableGroup.FAIL);
					condition.addFailExecutable(failGroup);
					
					//create main condition node with children passNode and failNode
					DefaultMutableTreeNode conditionNode = new DefaultMutableTreeNode();
					
					//create pass Node and add group
					DefaultMutableTreeNode passNode = new DefaultMutableTreeNode("Pass");
					passNode.setUserObject(passGroup);
					
					//create failNode and add group
					DefaultMutableTreeNode failNode = new DefaultMutableTreeNode("Fail");
					failNode.setUserObject(failGroup);
					
					//add children
					conditionNode.add(passNode);
					conditionNode.add(failNode);
					
					//add condition to condition node
					conditionNode.setUserObject(condition);
					
					//get selected node
					DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)executableTree.getLastSelectedPathComponent();
					
					//safety check
					if(selectedNode == null) {return;}
					
					//add the whole thing to the tree Model
					treeModel.insertNodeInto(conditionNode, selectedNode, selectedNode.getChildCount());
					
					//TODO actually add to request
				}
			}
		});
		
		//disabled until selection made
		addExecutable.setEnabled(false);
		
		//when tree selection changed (down here so access to create button)
		executableTree.addTreeSelectionListener(new TreeSelectionListener()
		{
			@Override
			public void valueChanged(TreeSelectionEvent e)
			{
				//get the selected node
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)executableTree.getLastSelectedPathComponent();
				
				//if nothing selected (just deselcted), hide create button
				if(node == null)
				{
					addExecutable.setEnabled(false);
				}
				
				//if something is selected, reenable create button
				addExecutable.setEnabled(true);
				
				if(node.getUserObject() != null && node.getUserObject().getClass() == OutAction.class)
				{
					//use node.getUserObject() to display/edit changes
					Executable selected = (Executable) node.getUserObject();
					executableDisplay.setText(selected + "");
				}
				else
				{
					executableDisplay.setText("Select an Executable, " + node.getUserObject().getClass());
				}
				
			}
		});
		
		JButton okBtn = new JButton("OK");
		okBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				//close window when OK button pressed
				dispose();
			}
		});
		
		main.add(new JLabel("Instructions"));
		main.add(new JScrollPane(executableTree));
		
		executableInfo.add(executableDisplay);
		main.add(executableInfo);
		main.add(addExecutable);
		
		main.add(okBtn);
		
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		add(main);
		
		//if a request was set, display it
		if(request != null)
		{
			setRequest(request);
		}
	}
	
	public void setRequest(Request request)
	{
		
		root = new DefaultMutableTreeNode("Root");
		
		treeModel.setRoot(root);
		
		//System.out.println("There are " + request.getExecutables().size() + " to add");
		for(Executable exe : request.getExecutables())
		{
			addExecutableToModel(root, exe);
		}
		
	}
	
	/**
	 * Recursively adds a executable and all of its children into the treeModel, first call should use root
	 * @param node Recursive node to add, start with root
	 * @param executable Recursive executable to add, start with all in request
	 */
	private void addExecutableToModel(DefaultMutableTreeNode node, Executable executable)
	{
		if(executable == null)
		{
			System.out.println("Error: null executable");
			return;
		}
		else if(node == null)
		{
			System.out.println("Error: null node");
			return;
		}
		
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
		newNode.setUserObject(executable);
		
		treeModel.insertNodeInto(newNode, node, node.getChildCount());
		
		//System.out.println(executable + ":" + executable.getExecutables());
		for(Executable exe : executable.getExecutables())
		{
			addExecutableToModel(newNode, exe);
		}
		
	}
	
	/**
	 * Creates and adds a node to the selected node in the JTree with userObject, correctly using TreeModel
	 * @param userObject the object to add to the new node
	 */
	private void addNodeAndUserObjectToSelectedNode(Object userObject)
	{
		//get selected node
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)executableTree.getLastSelectedPathComponent();
		
		//safety check
		if(selectedNode == null) {return;}
		
		//create the new node
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode();
		
		//add the user object
		newNode.setUserObject(userObject);
		
		//add the new node to the end of the childeNode list
		treeModel.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());
	}
}
