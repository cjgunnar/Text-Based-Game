package levelCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import sceneObjects.SceneObject;

/**
 * Allows properties to be edited, has an owner which it sets
 * @author Caden
 *
 */
@SuppressWarnings("serial")
class PropertiesPanel extends JPanel
{
	private JButton saveButton;
	private JButton delButton;
	
	SceneObject owner;
	
	JList<Property> properties;
	
	/**
	 * Creates a new panel, responsbile for owner
	 * @param owner SceneObject to add/remove/update properties
	 */
	public PropertiesPanel(SceneObject owner)
	{
		this.owner = owner;
		
		CreateComponents();
	}
	
	private void CreateComponents()
	{
		JLabel instructions = new JLabel("Add, remove, and save properities for this object using the buttons below.");
		this.add(instructions);
		
		Property[] defaultProperties = {new Property("Prop1", 0), new Property("Prop2", 1)};
		
		properties = new JList<Property>(defaultProperties);
		properties.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.add(properties);
		
		JButton addButton = new JButton("Add Property");
		addButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JTextField propName = new JTextField("Name");
				JTextField propValue = new JTextField("Value");
				
				Object[] message = {"Property Name: ", propName, "Initial Value", propValue};
				
				int option = JOptionPane.showConfirmDialog(null, message, "Title", JOptionPane.OK_CANCEL_OPTION);
				
				if(option == JOptionPane.OK_OPTION)
				{
					
				}
			}
		});
		
		saveButton = new JButton("Save Properties");
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				SaveProperties();
			}
		});
		
		delButton = new JButton("Delete Selected Property");
		delButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//TODO delete property
			}
		});
		
		this.add(addButton);
		this.add(delButton);
		this.add(saveButton);
	}
	
	public void SaveProperties()
	{
		//TODO save properties
	}
	
	class Property
	{
		String name;
		int value;
		
		public Property(String name, int value)
		{
			this.name = name;
			this.value = value;
		}
		
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "Property [name=" + name + ", value=" + value + "]";
		}
	}
	
}
