package levelCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sceneObjects.SceneObject;

/**
 * Allows properties to be edited, has an owner which it sets
 * @author cjgunnar
 *
 */
@SuppressWarnings("serial")
class PropertiesPanel extends JPanel
{	
	SceneObject owner;
	
	JList<Property> propertyList;
	DefaultListModel<Property> propertyModel;
	
	/**
	 * Creates a new panel, responsbile for owner
	 */
	public PropertiesPanel()
	{
		CreateComponents();
	}
	
	private void CreateComponents()
	{
		JLabel instructions = new JLabel("Add, remove, and save properities for this object using the buttons below.");
		this.add(instructions);
		
		propertyModel = new DefaultListModel<Property>();
		
		propertyList = new JList<Property>(propertyModel);
		propertyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.add(propertyList);
		
		//DELTE PROPERTY BUTTON
		JButton delButton = new JButton("Delete Property");
		delButton.setEnabled(false); //disabled by default
		delButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Property selectedProp = propertyList.getSelectedValue();
				if(selectedProp == null)
					return;
				
				owner.removeProperty(selectedProp.name);
				propertyModel.removeElement(propertyList.getSelectedValue());
			}
		});
		
		//disable/enable delete button depending on if something is selected
		propertyList.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent event)
			{
				if(propertyList.getSelectedIndex() != -1) //nothing selected
					delButton.setEnabled(true); //disable button
				else
					delButton.setEnabled(false); //enable button
			}	
		});
		
		JPanel root = this; //anonymous inner class ActionListener screws this up
		
		JButton addButton = new JButton("Add Property");
		addButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				JTextField propName = new JTextField("Name");
				JTextField propValue = new JTextField(10);
				
				Object[] message = {"Property Name: ", propName, "Initial Value", propValue};
				
				//request name and initial value
				int option = JOptionPane.showConfirmDialog(null, message, "Add Property", JOptionPane.OK_CANCEL_OPTION);
				
				if(option == JOptionPane.OK_OPTION)
				{
					String newPropName = propName.getText();
					if(newPropName.trim().equals(""))
					{
						//display error message
						JOptionPane.showMessageDialog(root, 
								"Enter a valid name", 
								"Error", JOptionPane.WARNING_MESSAGE, 
								null);
						//end the add property operation
						return;
					}
					
					int propIntValue;
					
					try
					{
						if(propValue.getText().equalsIgnoreCase("true") || propValue.getText().equalsIgnoreCase("yes"))
							propIntValue = 1;
						else if(propValue.getText().equalsIgnoreCase("false") || propValue.getText().equalsIgnoreCase("no"))
							propIntValue = 0;
						else if(propValue.getText().trim().equals(""))
							propIntValue = SceneObject.defaultValue;
						else
							propIntValue = Integer.parseInt(propValue.getText());
					}
					catch(NumberFormatException e)
					{
						//display error if number is not entered
						JOptionPane.showMessageDialog(root, 
								"Enter a valid initial value, enter true/false, or leave it blank for default\n" + 
								"It must be an integer", 
								"Error", JOptionPane.WARNING_MESSAGE, 
								null);
						return;
					}
					
					//add the property to the property model
					Property newProp = new Property(newPropName, propIntValue);
					propertyModel.addElement(newProp);
					
					//add to owner
					owner.addProperty(newPropName, propIntValue);
				}
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(delButton);
		
		this.add(buttonPanel);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void setOwner(SceneObject owner)
	{
		clear();
		
		this.owner = owner;
		
		Map<String, Integer> ownerProps = owner.getProperties();
		Object[] ownerPropValues = ownerProps.values().toArray();
		Object[] ownerPropNames = ownerProps.keySet().toArray();
		
		for(int i = 0; i < ownerProps.size(); i++)
		{
			Property prop = new Property((String)ownerPropNames[i], (int)ownerPropValues[i]);
			propertyModel.addElement(prop);
		}
	}
	
	public void clear()
	{
		propertyModel.clear();
	}
	
	class Property
	{
		public String name;
		public int value;
		
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
			return name + " : " + value;
		}
	}
	
}
