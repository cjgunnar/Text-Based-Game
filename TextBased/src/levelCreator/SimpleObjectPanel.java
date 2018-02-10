package levelCreator;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sceneObjects.SceneObject;

@SuppressWarnings("serial")
public class SimpleObjectPanel extends JPanel
{
	/** Field to display ID of the object */
	JTextField id;
	
	/** Field to edit name of the object */
	JTextField name;
	
	/** Field to edit description of the object */
	JTextArea description;
	
	/** Tabs to edit the object beyond basic properties */
	JTabbedPane objectOptions;
	
	/** Current object viewing/enditing */
	SceneObject currentObject;
	
	/** Contains ID, name, and description */
	JPanel basicProperties;
	
	/** Contains properties of the object we are editing, tabbed */
	PropertiesPanel propertyPanel;
	
	AliasPanel aliasPanel;
	
	/** Creates a panel used to display/edit information about a SceneObject */
	public SimpleObjectPanel()
	{
		CreateComponents();
	}
	
	/** Creates the components of this panel */
	private void CreateComponents()
	{
		//ID, NAME, and DESCRIPTION of object
		id = new JTextField(5);
		id.setEditable(false);
		
		name = new JTextField(15);
		
		description = new JTextArea(4, 20);
		
		//Tabs for object
		objectOptions = new JTabbedPane();
		
		propertyPanel = new PropertiesPanel();
		objectOptions.addTab("Properties", null, propertyPanel, "Open Object Properties");
		
		aliasPanel = new AliasPanel();
		objectOptions.addTab("Alias", null, aliasPanel, "Open Object Alias");
		objectOptions.addTab("Requests", null, new JPanel(), "Open Object Requests");
		
		basicProperties = new JPanel();
		basicProperties.add(id);
		basicProperties.add(name);
		basicProperties.add(description);
		
		add(basicProperties);
		
		add(objectOptions);
		
		//display going down
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	/**
	 * Sets the panel to display info for the object
	 * @param object Object to display info for
	 */
	public void setSelectedObject(SceneObject object)
	{
		currentObject = object;
		
		id.setText(object.getID() + "");
		name.setText(object.getName());
		description.setText(object.getDescription());
		
		propertyPanel.setOwner(object);
		aliasPanel.setOwner(object);
		
		//hide when not in use, reopen here
		objectOptions.setVisible(true);
		basicProperties.setVisible(true);
	}
	
	/**
	 * Clear currently displayed info
	 */
	public void clear()
	{
		id.setText("ID");
		name.setText("Name");
		description.setText("Description");
		
		propertyPanel.clear();
		aliasPanel.clear();
		
		//hide when not in use
		objectOptions.setVisible(false);
		basicProperties.setVisible(false);
	}
	
}
