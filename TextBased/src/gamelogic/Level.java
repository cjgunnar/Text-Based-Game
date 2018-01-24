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
	/** List of rooms in the level */
	List<Room> rooms = new ArrayList<Room>();
	
	String _prolog;
	
	Scenario scenario;
	
	/** Endings of the game that can be triggered by Actions */
	ArrayList<EndState> endings = new ArrayList<EndState>();
	
	public static final int SCENARIO_ID = 100000;
	
	public void setProlog(String prolog)
	{
		this._prolog = prolog;
	}
	
	public String getProlog()
	{
		return this._prolog;
	}
	
	/**
	 * Add a room to the level
	 * @param room The new room to add
	 */
	public void addRoom(Room room)
	{
		if(room == null)
			System.out.println("LEVEL: ERROR: trying to add null room");
		rooms.add(room);
	}
	
	public void InitializeRooms(Game game)
	{
		for(Room room: rooms)
		{
			//probably could of been done at time of construction
			room.setGame(game);
			room.setAllObjectsGame(game);
			room.setAllExitsGame(game);
			
			room.InitializeAllExits();
		}
	}
	
	public void checkEndStates()
	{
		for(EndState ending: endings)
		{
			ending.checkEndState();
		}
	}
	
	/**
	 * Add an ending to the level
	 * @param ending The EndState ending to add
	 */
	public void addEndState(EndState ending)
	{
		if(ending == null)
			System.out.println("LEVEL: ERROR: trying to add null ending");
		endings.add(ending);
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
	
	public Room FindRoomWithName(String roomName)
	{
		for(Room room: rooms)
		{
			System.out.println("Looking in " + room.getName());
			
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
	
	public void outputLevelSummaryData()
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
