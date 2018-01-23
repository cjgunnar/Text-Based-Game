package levelCreator;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import gamelogic.Level;
import sceneObjects.Room;
import sceneObjects.SceneObject;
import sceneObjects.SimpleObject;

/**
 * GUI to create new levels
 * @author cjgunnar
 *
 */
public class LevelCreator extends JFrame
{
	/** Serializable ID or something */
	private static final long serialVersionUID = 1L;

	//frame window config
	boolean fullScreen = false;
	final int FRAME_WIDTH = 1500;
	final int FRAME_HEIGHT = 500;

	//level storage, used with game as well
	Level level;

	//current room
	Room selectedRoom;

	//current selected object
	SceneObject selectedObject;

	//room selector combo box
	@SuppressWarnings({ "rawtypes" })
	JComboBox roomSelector;

	//room selector options
	JTextField roomIdField;
	JTextField roomNameField;
	JTextArea roomDescriptionField;

	//object list
	JList<SceneObject> objectsList;

	//object fields
	JTextField objectIdField;
	JTextField objectNameField;
	JTextArea objectDescriptionField;

	/** Creates/configures settings */
	public LevelCreator()
	{	
		if(fullScreen)
		{
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else
		{
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
		}

		setMinimumSize(new Dimension(900, 500));
		setTitle("Level Creator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args)
	{
		LevelCreator levelCreatorGUI = new LevelCreator();

		levelCreatorGUI.CreateComponents();

		levelCreatorGUI.Display();
	}

	/** Creates components for GUI */
	@SuppressWarnings("unchecked")
	public void CreateComponents()
	{
		//init level
		level = new Level();

		//add an example room so there is something to see on creation
		Room exampleRoom = new Room();
		exampleRoom.setID(0);
		exampleRoom.setName("Example Room");
		exampleRoom.setDescription("This room provides a template/example for your first room");

		SceneObject exampleObj1 = new SimpleObject();
		exampleObj1.setName("Bed");
		exampleObj1.setDescription("A bare bed sits in the corner");

		exampleRoom.addObject(exampleObj1);

		level.addRoom(exampleRoom);

		//create the final panel
		JPanel finalPanel = new JPanel();
		finalPanel.setLayout(new BoxLayout(finalPanel, BoxLayout.Y_AXIS));

		//INSTRUCTIONS
		//add instructions area
		JPanel instructionsPanel = new JPanel();
		JTextArea instructionsTextArea = new JTextArea("Select a room. Edit it's name and description. Use the tabs to edit subfields of the room, such as contained objects, exits, properties, and aliases.");
		instructionsTextArea.setEditable(false);
		instructionsPanel.add(instructionsTextArea);

		//ROOM SELECTION
		//add room selection and editing area
		JPanel roomSelectionPanel = new JPanel();

		roomSelector= new JComboBox();
		roomSelector.addItem(exampleRoom);

		//add actionlistiner to combobox
		roomSelector.addActionListener(new ActionListener()
		{
			@Override
			/** Listens to the combo box. */
			public void actionPerformed(ActionEvent e) 
			{
				JComboBox cb = (JComboBox)e.getSource();
				Room selectedRoom = (Room)cb.getSelectedItem();

				SaveRoomData();
				updateSelectedRoom(selectedRoom);
			}
		});

		//SELECTED ROOM NAME, DESCRIPTION, ID VIEW AND EDIT
		//create name and description editors
		roomIdField = new JTextField(4);
		roomIdField.setEditable(false);
		roomNameField = new JTextField(14);
		roomDescriptionField = new JTextArea(5, 10);

		//NEW ROOM CREATION
		//add "new room" button
		JButton newRoomButton = new JButton("Add a room");

		//add action create new room to new room button
		newRoomButton.addActionListener(new ActionListener()
		{
			@Override
			/** Listens to new room button, and creates a new room when clicked */
			public void actionPerformed(ActionEvent e)
			{
				CreateNewRoom();
			}
		});

		//add components to roomSelectionPanel
		roomSelectionPanel.add(roomSelector);
		roomSelectionPanel.add(newRoomButton);
		roomSelectionPanel.add(roomIdField);
		roomSelectionPanel.add(roomNameField);
		roomSelectionPanel.add(roomDescriptionField);

		//ROOM OPTIONS TABBED PANEL
		JTabbedPane roomOptions = new JTabbedPane();
		
		//ROOM OPTIONS: OBJECTS
		//objects panel
		JPanel objectBasicSelectionPanel = new JPanel();
		objectBasicSelectionPanel.setLayout(new BoxLayout(objectBasicSelectionPanel, BoxLayout.Y_AXIS));

		//objects list
		objectsList = new JList<SceneObject>(exampleRoom.getObjects());
		objectsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//OBJECT BUTTONS
		JPanel objectButtons = new JPanel();
		
		//create new object button
		JButton newObjButton = new JButton("Create new Object");
		newObjButton.addActionListener(new ActionListener() //when clicked, create new object
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				CreateNewObject();
			}
		});

		//edit object button
		JButton editObjButton = new JButton("Edit Selected Object");
		editObjButton.addActionListener(new ActionListener() //when clicked, update the object fields
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{			
				SceneObject selectedObject = (SceneObject)objectsList.getSelectedValue();

				updateSelectedObject(selectedObject);
			}
		});

		//save object button
		JButton saveObjButton = new JButton("Save Object");
		saveObjButton.addActionListener(new ActionListener() //when clicked, update object and list
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				SceneObject selectedObject = (SceneObject)objectsList.getSelectedValue();

				if(selectedObject != null) //TODO add feedback saying "no object selected"
				{
					SaveSelectedObjectData(selectedObject);

					updateObjects(selectedRoom);
				}
			}
		});

		//delete object button
		JButton delObjButton = new JButton("Delete Selected Object");
		delObjButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//not sure how to delete yet, as IDs will get messed up
				System.out.println("LEVEL CREATOR: ERROR: REMOVE OBJECT not implemented yet");
			}
		});
		
		//add buttons to button panel
		objectButtons.add(newObjButton);
		objectButtons.add(editObjButton);
		objectButtons.add(saveObjButton);
		objectButtons.add(delObjButton);
		
		//OBJECTS VIEW AND EDIT DESCRIPTION, NAME, ID
		//fields for object
		JLabel objectIdLabel = new JLabel("ID:");
		
		objectIdField = new JTextField(4);
		objectIdField.setEditable(false);

		JLabel objectNameLabel = new JLabel("Name:");
		objectNameField = new JTextField(14);
		
		JLabel objectDescriptionLabel = new JLabel("Description: ");
		objectDescriptionField = new JTextArea(3, 10);

		//add components to objects panel
		objectBasicSelectionPanel.add(objectsList);
		objectBasicSelectionPanel.add(objectButtons);

		JPanel objectIdPanel = new JPanel();
		objectIdPanel.add(objectIdLabel);
		objectIdPanel.add(objectIdField);
		objectBasicSelectionPanel.add(objectIdPanel);
		
		JPanel objectNamePanel = new JPanel();
		objectNamePanel.add(objectNameLabel);
		objectNamePanel.add(objectNameField);
		objectBasicSelectionPanel.add(objectNamePanel);
		
		JPanel objectDescriptionPanel = new JPanel();
		objectDescriptionPanel.add(objectDescriptionLabel);
		objectDescriptionPanel.add(objectDescriptionField);
		objectBasicSelectionPanel.add(objectDescriptionPanel);

		//OBJECT OPTIONS TABBED PANEL
		//object options
		JTabbedPane objectOptions = new JTabbedPane();
		
		objectOptions.addTab("Aliases", null, null, "Open Object Aliases Panel");
		objectOptions.addTab("Properties", null, null, "Open Object Properties Panel");
		objectOptions.addTab("Requests", null, null, "Open Object Requests Panel");
		
		//OBJECTS ROOM PANEL FINAL
		JPanel objectPanel = new JPanel();
		objectPanel.setLayout(new BoxLayout(objectPanel, BoxLayout.Y_AXIS));
		
		objectPanel.add(objectBasicSelectionPanel);
		objectPanel.add(objectOptions);
		
		//exits
		JButton newExitButton = new JButton("Create new Exit");
		JButton delExitButton = new JButton("Delete Selected Exit");
		
		//ROOM TABBED PANEL FINAL
		//add panels to room tabbed panel
		roomOptions.addTab("Aliases", null, null, "Open Aliases Panel"); //TODO create Aliases panel
		roomOptions.addTab("Properties", null, null, "Open Properties Panel"); //TODO create properties panel
		roomOptions.addTab("Objects", null, objectPanel, "Open Object Panel");
		roomOptions.addTab("Exits", null, null, "Open Exits Panel"); //TODO create exits panel
		
		//FINAL PANEL
		//add all panels and subpanels
		finalPanel.add(instructionsPanel);
		finalPanel.add(roomSelectionPanel);
		finalPanel.add(roomOptions);

		//add final panel to frame
		add(finalPanel);

		//set selection panel to the Example Room to start
		updateSelectedRoom(exampleRoom);
	}

	/**
	 * Creates a list of Strings that is the Rooms names
	 * @return List of names for the List of rooms passed in
	 */
	public String[] getRoomsNames(List<Room> rooms)
	{		
		if(rooms == null)
		{
			System.out.println("CREATOR: ERROR: rooms is null");
		}

		String[] output = new String[rooms.size()];

		for(int i = 0; i < rooms.size(); i++)
		{
			output[i] = rooms.get(i).getName();
		}

		return output;
	}

	/**
	 * Sets the frame to be visible or not
	 * @param display Visibility
	 */
	public void Display(boolean display)
	{
		setVisible(display);
	}

	/**
	 * Sets the frame to be visible
	 */
	public void Display()
	{
		setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public void CreateNewRoom()
	{
		//create a room with some defaults
		Room newRoom = new Room();
		newRoom.setID(level.getRooms().size());
		System.out.println("Setting new room ID to " + level.getRooms().size());
		newRoom.setName("New Room");

		//add it to the list of rooms
		level.addRoom(newRoom);

		//add option to room selector
		roomSelector.addItem(newRoom);

		//save the current room data
		SaveRoomData();

		updateSelectedRoom(newRoom);

		//update the selector to select the new room
		roomSelector.setSelectedIndex(level.getRooms().size() - 1);
	}

	/** Create and add a new object with defaults to the current room */
	private void CreateNewObject()
	{
		//create a new SceneObject with some defaults
		SceneObject newObj = new SimpleObject();

		newObj.setName("New Object");
		//room ids are 1, 2, 3, 4, 5, etc
		//object ids are the current room (ex 4) and it starts over ex: 400, 401, 402, 403 etc
		int id = selectedRoom.getID() * 100 + selectedRoom.getObjects().length;
		System.out.println("Creating new object in room " + selectedRoom + " with id " + id);
		newObj.setID(id);

		SceneObject[] currentObjs = selectedRoom.getObjects();
		SceneObject[] objs = new SceneObject[selectedRoom.getObjects().length + 1];

		//copy current array into new array
		for(int i = 0; i < selectedRoom.getObjects().length; i++)
		{
			objs[i] = currentObjs[i];
		}
		//add new element to empty last spot in new array
		objs[objs.length - 1] = newObj;

		objectsList.setListData(objs);

		selectedRoom.addObject(newObj);
	}

	private void SaveRoomData()
	{
		selectedRoom.setID(Integer.parseInt(roomIdField.getText()));
		selectedRoom.setName(roomNameField.getText());
		selectedRoom.setDescription(roomDescriptionField.getText());
	}

	private void SaveSelectedObjectData(SceneObject object)
	{
		System.out.println("Saving object " + object.getName());
		object.setID(Integer.parseInt(objectIdField.getText()));
		object.setName(objectNameField.getText());
		object.setDescription(objectDescriptionField.getText());
	}

	protected void updateSelectedRoom(Room room) 
	{
		System.out.println("ComboBox changed to " + room);

		selectedRoom = room;

		if(selectedRoom == null)
		{
			System.out.println("CREATOR: ERROR: null selected room can't be updated");
		}

		roomIdField.setText(selectedRoom.getID() + "");
		roomNameField.setText(selectedRoom.getName());
		roomDescriptionField.setText(selectedRoom.getDescription());

		//update objects
		updateObjects(room);

	}

	private void updateObjects(Room room)
	{
		objectsList.setListData(room.getObjects());

		if(room.getObjects().length == 0)
		{
			clearSelectedObject();
		}
	}

	private void clearSelectedObject()
	{
		objectIdField.setText("ID");
		objectNameField.setText("Name");
		objectDescriptionField.setText("Description");
	}

	private void updateSelectedObject(SceneObject obj)
	{
		objectIdField.setText(obj.getID() + "");
		objectNameField.setText(obj.getName());
		objectDescriptionField.setText(obj.getDescription());
	}

}
