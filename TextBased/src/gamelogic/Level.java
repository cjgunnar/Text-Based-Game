package gamelogic;

import java.util.ArrayList;
import java.util.List;

import sceneObjects.Exit;
import sceneObjects.Room;
import sceneObjects.SceneObject;
//import sceneObjects.SceneObject;

/**
 * Creates and stores all the rooms in the level
 * @author cjgunnar
 *
 */
public class Level
{
	ArrayList<Room> rooms = new ArrayList<Room>();
	
	String _prolog;
	
	Scenario scenario;
	
	public static final int SCENARIO_ID = 100000;
	
	/**
	 * Sets prolog text
	 * @param prolog Text to set
	 */
	public void setProlog(String prolog)
	{
		this._prolog = prolog;
	}
	
	/**
	 * Returns prolog text
	 * @return prolog text returned
	 */
	public String getProlog()
	{
		return this._prolog;
	}
	
	/**
	 * Adds a room to the rooms list
	 * @param room Room to add
	 */
	public void addRoom(Room room)
	{
		if(room ==  null)
		{
			System.out.println("LEVEL: ERROR: Can't add null room to LEVEL rooms");
			return;
		}
		
		rooms.add(room);
	}
	
	/**
	 * Gets the list of rooms
	 * @return The ArrayList of rooms
	 */
	public ArrayList<Room> getRooms()
	{
		if(rooms.isEmpty())
		{
			System.out.println("LEVEL: WARNING: getting list of empty rooms");
		}
		return rooms;
	}
	
	/**
	 * Sets its list of filled rooms based on name of level in /Levels
	 * @param levelFile Name of level is src/Levels/
	 * @param game Reference to game to pass on to rooms, objects, etc
	 */
	public void LoadLevel(String levelFile, Game game)
	{		
    	//maybe that name can be shortened
    	LevelConstructorXMLParser reader = new LevelConstructorXMLParser(game);
    	
    	//create the list from the XML file
    	ArrayList<Room> rooms = (ArrayList<Room>) reader.readLevel(levelFile);
    	
    	//set own list to this
    	this.rooms = rooms;
    	
    	//set references for all rooms, objects, and exits
    	//now that we know everything is loaded
    	for (Room room : rooms)
    	{
    		//probably could of been done at time of construction
    		room.setGame(game);
    		room.setAllObjectsGame(game);
    		room.setAllExitsGame(game);
    		
    		room.InitializeAllExits();
    	}
    	
    	outputLevelSummaryData();
	}
	
	/**
	 * Setter for scenario
	 * @param scenario Finished Scenario to set
	 */
	public void setScenario(Scenario scenario)
	{
		this.scenario = scenario;
	}
	
	/**
	 * Change scenario property
	 * @param name Name of property to change
	 * @param value Value to change property to
	 */
	public void changeScenarioProperty(String name, int value)
	{
		scenario.changeProperty(name, value);
	}
	
	/**
	 * Checks the scenario property using input
	 * @param name Name of the property to compare
	 * @param operator How to compare (=, >, <)
	 * @param value Value to compare against
	 * @return true or false
	 */
	public boolean checkScenarioProperty(String name, String operator, int value)
	{
		return scenario.checkProperty(name, operator, value);
	}
	
	/**
	 * Searches for room with roomName
	 * @param roomName Name to look for
	 * @return true or false
	 */
	public boolean hasRoom(String roomName)
	{
		if(FindRoomWithName(roomName) != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Searches for room with name, returns it if found, otherwise null
	 * @param roomName Name of room to look for
	 * @return Room with name or null
	 */
	public Room FindRoomWithName(String roomName)
	{
		for(Room room: rooms)
		{
			//System.out.println("Looking in " + room.getName());
			
			if(room.getName().equalsIgnoreCase(roomName))
			{
				return room;
			}
			
			String[] aliases = room.getAliases();
			
			for(String alias: aliases)
			{
				if(alias.equalsIgnoreCase(roomName))
				{
					return room;
				}
			}
		}
		
		//default
		return null;
	}
	
	/**
	 * Returns a SceneObject (Room, SimpleObject, Exit) with ID or null
	 * @param ID ID to search for
	 * @return SceneObject or null
	 */
	public SceneObject FindObjectWithID(int ID)
	{		
		for(Room room: rooms)
		{
			//check if the room itself is the object
			if(room.getID() == ID)
			{
				return room;
			}
			
			SceneObject objectForRoom = room.FindObjectByID(ID);
			if(objectForRoom != null)
			{
				return objectForRoom;
			}
			else
			{
				//go to next room
				continue;
			}
		}
		
		//default
		return null;
	}
	
	/**
	 * Returns a room found with ID or null
	 * @param ID ID to look for
	 * @return Room found with ID or null
	 */
	public Room FindRoomWithID(int ID)
	{
		//System.out.println("Looking in " + rooms.size() + " rooms");
		
		for(Room room: rooms)
		{
			//System.out.println("Current Room: " + room.getName() + " with ID: " + room.getID());
			
			if(room.getID() == ID)
			{
				return room;
			}
		}
		
		System.err.println("ERROR: no room with ID of " + ID);
		return null;
	}
	
	/**
	 * Prints a summary of the room loaded and their objects
	 */
	private void outputLevelSummaryData()
	{
    	System.out.println("\nLOADED LEVEL SUMMARY DATA");
    	System.out.println("-----------------");
		
    	if(rooms.size() == 0)
    	{
    		System.out.println("NO ROOMS LOADED");
    	}
    	
		for(Room room: rooms)
		{
    		System.out.println("ID: " + room.getID());
    		
    		System.out.println(room.toString());
    		
    		System.out.println("OBJECTS:");
    		SceneObject[] objects = room.getObjects();
    		for(SceneObject object: objects)
    		{
    			System.out.println("\t" + object.toString());
    		}
    		
    		System.out.println("EXITS:");
    		Exit[] exits = room.getExits();
    		for(Exit exit: exits)
    		{
    			System.out.println("\t" + exit.toString());
    		}
    		
		}
		
    	System.out.println("# OF ROOMS LOADED: " + rooms.size());
    	
    	System.out.println("---------------");
	}
}
