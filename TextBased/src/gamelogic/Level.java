package gamelogic;

import java.util.ArrayList;
import java.util.List;

import sceneObjects.Room;
//import sceneObjects.SceneObject;

/**
 * Creates and stores all the rooms in the level
 * @author Caden
 *
 */
public class Level
{
	List<Room> rooms = new ArrayList<Room>();
	
	String _prolog;
	
	public void setProlog(String prolog)
	{
		this._prolog = prolog;
	}
	
	public String getProlog()
	{
		return this._prolog;
	}
	
	public void LoadLevel(String levelFile, Game game)
	{		
    	//maybe that name can be shortened
    	LevelConstructorXMLParser reader = new LevelConstructorXMLParser(game);
    	
    	//create the list from the XML file
    	List<Room> rooms = reader.readLevel(levelFile);
    	
    	//set own list to this
    	this.rooms = rooms;
    	
    	//set references for all rooms, objects, and exits
    	//now that we know everything is loaded
    	for (Room room : rooms)
    	{
    		room.setGame(game);
    		room.setAllObjectsGame(game);
    		room.setAllExitsGame(game);
    		
    		room.InitializeAllExits();
    	}
    	
    	outputLevelSummaryData();
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
	
	private void outputLevelSummaryData()
	{
    	System.out.println("LOADED LEVEL SUMMARY DATA");
    	System.out.println("-----------------");
		
    	if(rooms.size() == 0)
    	{
    		System.out.println("NO ROOMS LOADED");
    	}
    	
		for(Room room: rooms)
		{
    		System.out.println("ID: " + room.getID());
    		System.out.println("NAME: " + room.getName());
    		System.out.println("DESCRIPTION: " + room.getDescription());
    		System.out.print("\n");
		}
		
    	System.out.println("# OF ROOMS LOADED: " + rooms.size());
    	
    	System.out.println("---------------");
	}
}
