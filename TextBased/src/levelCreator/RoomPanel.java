package levelCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import sceneObjects.Room;

@SuppressWarnings("serial")
public class RoomPanel extends JPanel
{
	JTextField id;
	JTextField name;
	JTextArea description;
	
	JTabbedPane roomOptions;
	
	Room selectedRoom;
	
	JPanel basicProperties;
	
	ObjectSelectorPanel objectSelector;
	
	public RoomPanel()
	{
		CreateComponents();
	}
	
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
		roomOptions.addTab("Properties", null, new PropertiesPanel(selectedRoom), "Open Room Properties Panel");
		roomOptions.addTab("Aliases", null, new AliasPanel(selectedRoom), "Open Room Alias Panel");
		
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
	
	public void setSelectedRoom(Room room)
	{
		//selectedRoom = room;
		
		id.setText(room.getID() + "");
		name.setText(room.getName());
		description.setText(room.getDescription());
		
		objectSelector.setSelectedRoom(room);
		
		basicProperties.setVisible(true);
		roomOptions.setVisible(true);
	}
	
	public void clear()
	{
		id.setText("ID");
		name.setText("Name");
		description.setText("Description");
		
		objectSelector.clear();
		
		basicProperties.setVisible(false);
		roomOptions.setVisible(false);
	}
}
