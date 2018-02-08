package levelCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sceneObjects.Room;

@SuppressWarnings("serial")
public class RoomSelectorPanel extends JPanel
{
	RoomPanel selectedRoomInfo;
	
	DefaultListModel<Room> roomsList;
	JList<Room> roomsUI;
	
	public RoomSelectorPanel()
	{
		CreateComponents();
	}
	
	private void CreateComponents()
	{
		//LIST OF ROOMS
		roomsList = new DefaultListModel<Room>();
		roomsUI = new JList<Room>(roomsList);
		roomsUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		selectedRoomInfo = new RoomPanel();
		
		//DELETE BUTTON
		JButton delRoom = new JButton("Delete Room");
		delRoom.setEnabled(false);
		
		//REFRESH SELECTED ROOM
		roomsUI.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent event)
			{
				//System.out.println("Change to rooms list");
				//System.out.println(event.toString());
				
				if(roomsUI.getSelectedIndex() != -1)
				{
					delRoom.setEnabled(true);
					
					Room selectedRoom = roomsList.elementAt(roomsUI.getSelectedIndex());
					if(selectedRoom != null)
						selectedRoomInfo.setSelectedRoom(selectedRoom);
				}
				else
					delRoom.setEnabled(false);
			}	
		});
		
		//CREATE ROOM
		JButton newRoom = new JButton("Create Room");
		newRoom.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				//TODO create room in level
				
				//create pop-up with basic info, such as name and description of room
				JTextField roomName = new JTextField("Name");
				JTextArea roomDescription = new JTextArea(3, 20);
				roomDescription.setText("Description");
				roomDescription.setWrapStyleWord(true);
				
				Object[] message = {"Room Name: ", roomName, "Description: ", roomDescription};
				
				int option = JOptionPane.showConfirmDialog(null, message, "Create New Room", JOptionPane.OK_CANCEL_OPTION);
				
				if(option == JOptionPane.OK_OPTION)
				{
					roomsList.addElement(new Room(roomName.getText(), roomDescription.getText(), null));
				}
			}
		});
		
		//DELTE ROOM
		delRoom.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(roomsUI.getSelectedIndex() != -1)
				{
					Room selectedRoom = roomsList.elementAt(roomsUI.getSelectedIndex());
					if(selectedRoom != null)
					{
						//TODO remove from level
						
						//remove from list
						roomsList.remove(roomsUI.getSelectedIndex());
						
						//clear selected room panel
						selectedRoomInfo.clear();
					}
				}
				else
				{
					delRoom.setEnabled(false);
				}
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(newRoom);
		buttonPanel.add(delRoom);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		add(roomsUI);
		add(buttonPanel);
		add(selectedRoomInfo);
	}
	
}
