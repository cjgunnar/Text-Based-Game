package levelCreator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import gamelogic.Action;
import gamelogic.Executable;

@SuppressWarnings("serial")
public class ExecutableEditor extends JDialog
{
	static final int HEIGHT = 200;
	static final int WIDTH = 400;
	
	public ExecutableEditor()
	{
		super();
		
		setMinimumSize(new Dimension(WIDTH, HEIGHT));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Executable Editor");
		
		CreateComponents();
	}

	private void CreateComponents()
	{
		JPanel main = new JPanel();
		JPanel executableInfo = new JPanel();
		JLabel executableDisplay = new JLabel();
		
		//create root of tree
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");

		//sample leaves for now
		DefaultMutableTreeNode leafA = new DefaultMutableTreeNode("Leaf");
		//leafA.setUserObject(new Action());
		
		root.add(leafA);

		//create tree
		JTree executableTree = new JTree(root);
		//set size
		executableTree.setMinimumSize(new Dimension(150, 300));
		//can only select one at a time
		executableTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		//when tree selection changed
		executableTree.addTreeSelectionListener(new TreeSelectionListener()
		{
			@Override
			public void valueChanged(TreeSelectionEvent e)
			{
				//get the selected node
				DefaultMutableTreeNode node = (DefaultMutableTreeNode)executableTree.getLastSelectedPathComponent();
				
				//check that something is selected
				if(node == null) {return;}
				
				if(node.getUserObject().getClass() == Action.class)
				{
					//use node.getUserObject() to display/edit changes
					Executable selected = (Executable) node.getUserObject();
					executableDisplay.setText(selected + "");
				}
				else
				{
					executableDisplay.setText("Select an Executable");
				}
				
			}
		});
		
		
		//button to create executables, hide when nothing selected
		JButton addExecutable = new JButton("Add Executable");
		
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
	}
}
