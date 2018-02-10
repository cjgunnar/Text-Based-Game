package levelCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sceneObjects.Room;

/**
 * Displays and allows editing of a room
 * @author cjgunnar
 */
@SuppressWarnings("serial")
public class RoomPanel extends JPanel
{
	/** Field to view ID */
	JTextField id;
	
	/** Field to view/edit na,e */
	JTextField name;
	
	/** Field to view/edit description */
	JTextArea description;
	
	/** TabbedPane with the options of the room (properties, aliases, objects) */
	JTabbedPane roomOptions;
	
	/** Contains id, name, description for formatting view */
	JPanel basicProperties;
	
	/** Tab that allows selection of objects */
	ObjectSelectorPanel objectSelector;
	
	/** Tab that allows viewing/editing properties */
	PropertiesPanel propertiesPanel;
	
	/** Tab that allows viewing/editing aliases */
	AliasPanel aliasPanel;
	
	/** Create a new RoomPanel */
	public RoomPanel()
	{
		CreateComponents();
	}
	
	/** Create neccessary components */
	private void CreateComponents()
	{
		id = new JTextField(5);
		id.setEditable(false);
					
		name = new JTextField(10);
		
		name.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				// TODO Auto-generated method stub
				System.out.println("Event in name field!");
				System.out.println(event.getID());
			}
		});
		
		description = new JTextArea(2, 20);
		
		roomOptions = new JTabbedPane();
		
		propertiesPanel = new PropertiesPanel();
		roomOptions.addTab("Properties", null, propertiesPanel, "Open Room Properties Panel");
		
		aliasPanel = new AliasPanel();
		roomOptions.addTab("Aliases", null, aliasPanel, "Open Room Alias Panel");
		
		objectSelector = new ObjectSelectorPanel();
		roomOptions.addTab("Objects", null, objectSelector, "Open Room Objects Panel");
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		basicProperties = new JPanel();
		//basicProperties.setLayout(new BoxLayout(basicProperties, BoxLayout.Y_AXIS));
		
		basicProperties.add(id);
		basicProperties.add(name);
		basicProperties.add(description);
		
		add(basicProperties);
		add(roomOptions);
		
		clear();
	}
	
	/**
	 * Set the room to display
	 * @param room Room to display
	 */
	public void setSelectedRoom(Room room)
	{
		//selectedRoom = room;
		
		id.setText(room.getID() + "");
		name.setText(room.getName());
		description.setText(room.getDescription());
		
		objectSelector.setSelectedRoom(room);
		propertiesPanel.setOwner(room);
		aliasPanel.setOwner(room);
		
		basicProperties.setVisible(true);
		roomOptions.setVisible(true);
	}
	
	/** Clear the currently selected room */
	public void clear()
	{
		id.setText("ID");
		name.setText("Name");
		description.setText("Description");
		
		objectSelector.clear();
		propertiesPanel.clear();
		aliasPanel.clear();
		
		basicProperties.setVisible(false);
		roomOptions.setVisible(false);
	}
}
