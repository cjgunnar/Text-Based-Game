package levelCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sceneObjects.Room;
import sceneObjects.SceneObject;
import sceneObjects.SimpleObject;

@SuppressWarnings("serial")
public class ObjectSelectorPanel extends JPanel
{
	Room room;
	
	DefaultListModel<SceneObject> objList;
	JList<SceneObject> objsUI;
	
	SimpleObjectPanel selectedObjectPanel;
	
	public ObjectSelectorPanel()
	{
		CreateComponents();
	}
	
	private void CreateComponents()
	{
		room = new Room();
		
		//MODEL LIST OF OBJECTS
		objList= new DefaultListModel<SceneObject>();
		
		//LIST OF OBJECTS
		objsUI = new JList<SceneObject>(objList);
		objsUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//DELTE OBJECT BUTTON
		JButton delObj = new JButton("Delete Selected Object");
		delObj.setEnabled(false);
		
		//REFRESH SELECTED ROOM
		objsUI.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent event)
			{
				if(objsUI.getSelectedIndex() != -1)
				{
					delObj.setEnabled(true);

					SceneObject selectedObj = objList.elementAt(objsUI.getSelectedIndex());
					if(selectedObj != null)
						selectedObjectPanel.setSelectedObject(selectedObj);
				}
				else
					delObj.setEnabled(false);
			}	
		});
		
		//ADD OBJECT BUTTON
		JButton addObj = new JButton("Create new Object");
		addObj.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String name = JOptionPane.showInputDialog("Name of the object");
				if(name != null)
				{
					SceneObject newObj = new SimpleObject();
					newObj.setName(name);
					
					int id = room.getID() * 100 + objList.size();
					newObj.setID(id);
					
					objList.addElement(newObj);
					
					room.addObject(newObj);
				}
			}
		});
		
		//DELTE OBJECT BUTTON ON-CLICK
		delObj.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				
			}
		});
		
		//Panel to display the object
		selectedObjectPanel = new SimpleObjectPanel();
		
		if(room == null)
			System.out.println("LEVEL CREATOR: OBJECT SELECTOR PANEL: ERROR: null room");
		
		//add all elements from room at start
		for(SceneObject obj : room.getObjects())
		{
			objList.addElement(obj);
		}
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addObj);
		buttonPanel.add(delObj);
		
		add(objsUI);
		add(buttonPanel);
		add(selectedObjectPanel);
		
		//set vertically
		//buttons will be alligned horizontall though
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void setSelectedRoom(Room room)
	{
		clear();
		
		this.room = room;
		
		for(SceneObject obj : room.getObjects())
		{
			objList.addElement(obj);
		}
		
		selectedObjectPanel.clear();
	}
	
	public void clear()
	{
		objList.clear();
	}
	
}
