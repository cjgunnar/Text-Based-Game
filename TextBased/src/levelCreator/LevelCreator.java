package levelCreator;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
		
		CreateComponents();
	}

	private void CreateComponents()
	{
		//create a bit of an example room
		level = new Level();
		Room roomStart = new Room();
		roomStart.setID(1);
		roomStart.setName("Start Room");
		roomStart.setDescription("The Starting Room and an Example");
		
		SceneObject obj1 = new SimpleObject();
		obj1.setName("Chair");
		obj1.setDescription("A chair in the middle of the room.");
		
		roomStart.addObject(obj1);
		
		level.addRoom(roomStart);
		
		JTabbedPane mainTabs = new JTabbedPane();
		
		RoomSelectorPanel roomSelector = new RoomSelectorPanel();
		roomSelector.LoadRooms(level);
		
		mainTabs.addTab("Rooms", roomSelector);
		mainTabs.addTab("Scenario", new JPanel()); //TODO create Scenario tab
		
		add(mainTabs);
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

}
