package sceneObjects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gamelogic.Command;
import gamelogic.Game;
import gamelogic.Request;

//Caden Gunnarson

public class Room extends SimpleObject
{
	//name used to identify it
	String name;
	
	//aliases of the room
	List<String> aliases = new ArrayList<String>();
	
	//description of the room
	String description;
	
	//id of the room
	int ID;
	
	//reference to game to send messages
	Game game;
	
	//list of all the objects in the room
	List<SceneObject> objects = new ArrayList<SceneObject>();

	//list of exits to room
	List<Exit> exits = new ArrayList<Exit>();
	
	//properties of the object (HashMap), name int pairs
	HashMap<String, Integer> properties = new HashMap<String, Integer>();
	
	//full constructor
	public Room(String name, String description, Game game) 
	{
		this.name = name;
		this.description = description;
		this.game = game;
	}
	
	//default constructor
	public Room() {};
	
	/**
	 * Execute command on every object in room
	 * Ensures exact phrases will be matched
	 * @param command to execute
	 */
	public void ExecuteCommandAll(Command command)
	{
		for(SceneObject object: objects)
		{
			object.ExecuteCommand(command);
		}
		
		for(Exit exit: exits)
		{
			exit.ExecuteCommand(command);
		}
	}
	
	//property related methods
	@Override
	public void changeProperty(String propName, int value)
	{
		properties.put(propName, value);
	}

	@Override
	public int getProperty(String propName)
	{
		int value = properties.get(propName);
		return value;
	}

	@Override
	public boolean checkProperty(String propName, String operator, int value)
	{
		int propValue = properties.get(propName);

		if(operator == null)
		{
			System.out.println(name.toUpperCase() + " ERROR: null operator");
			return false;
		}

		if(operator.equals(SceneObject.greaterThan))
		{
			//check if the property is greater than value
			if(propValue > value)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(operator.equals(SceneObject.lessThan))
		{
			//check if the property is less than value
			if(propValue < value)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else if(operator.equals(SceneObject.equalTo))
		{
			//checkk if the property is equal to the value
			if(propValue == value)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			System.out.println(name.toUpperCase() + " ERROR: operator " + operator + " not supported");
			return false;
		}

	}

	@Override
	public void addProperty(String propName, int initValue)
	{
		properties.put(propName, initValue);
	}

	@Override
	public void addProperty(String propName)
	{
		addProperty(propName, SceneObject.defaultValue);
	}
	
	@Override
	public void removeProperty(String propName)
	{
		properties.remove(propName);
	}
	
	@Override
	public Map<String, Integer> getProperties()
	{
		return this.properties;
	}
	
	//initialize exits
	public void InitializeAllExits()
	{
		for(int i = 0; i < exits.size(); i++)
		{
			exits.get(i).InitializeEntranceToByID();
		}
	}
	
	//room related finding SceneObjects and exits
	public Exit FindExitByDestination(Room destination)
	{
		for(Exit exit: exits)
		{
			if(exit.getEntranceTo() == null)
				//door leads nowhere
				continue;
			
			String exitDestinationName = exit.getEntranceTo().getName();
			
			if(exitDestinationName == null)
				//door leads to a nameless room
				continue;
			
			if(exitDestinationName.equals(destination.getName()))
			{
				return exit;
			}
		}
		
		//default
		return null;
	}
	
	/**
	 * Searches through exits and objects, find the SceneObject with the ID or returns null
	 * @param id ID to search fcr
	 * @return the SceneObject or null if nothing found
	 */
	public SceneObject FindObjectByID(int id)
	{
		if(id == 0)
		{
			System.err.println(name.toUpperCase() + ": ERROR: cannot find anything with special ID 0");
		}
		
		//check objects
		for(SceneObject object: objects)
		{
			int objectID = object.getID();
			
			if(objectID == id)
			{
				return object;
			}
		}
		
		//check exits
		for(Exit exit: exits)
		{
			int exitID = exit.getID();
			
			if(exitID == id)
			{
				return exit;
			}
		}
		
		System.err.println(name.toUpperCase() + ": ERROR: no objects/exits found with ID: " + id);
		
		//default
		return null;
	}
	
	public SceneObject FindObjectByName(String name)
	{
		if (name == null || name.equals("") || name.equals(" "))
		{
			System.out.println(name.toUpperCase() + ": ERROR: null name entered");
			return null;
		}
		
		if (objects.isEmpty() && exits.isEmpty())
		{
			System.out.println(name.toUpperCase() + ": ERROR: no objects or exits in room");
			return null;
		}
		
		//System.out.println("Searching for name: " + name);
		
		//search through each object in room for object with name
		for(SceneObject object : objects)
		{
			//System.out.println("Searching for: " + name);
			
			if (object.getName() != null && object.getName().equalsIgnoreCase(name))
			{
				//return the object with matching name
				return object;
			}
			
			for(String alias : object.getAliases())
			{
				if(alias == null)
				{
					System.out.println(name.toUpperCase() + ": ERROR: null alias");
				}
				
				//System.out.println("Current search alias: " + alias);
				else if(alias.equalsIgnoreCase(name))
				{
					return object;
				}
			}
		}
		
		//System.out.println("Searching for " + name + " in " + exits.size() + " exits");
		
		//search through each exits in room for matching name
		for(Exit exit : exits)
		{
			//System.out.println("CURRENT EXIT: " + exit.getName());
			
			if (exit.getName() == null)
			{
				//System.err.println("ERROR: this exit has no name");
				
				//move to next exit
				continue;
			}
			
			if (exit.getName().equalsIgnoreCase(name))
			{
				//return the object with matching name
				return exit;
			}
			
			//System.out.println("Looking in " + exit.getAliases().length);
			
			//search through each exits aliases as well
			for(String alias : exit.getAliases())
			{
				//System.out.println(exit.getName().toUpperCase() + ": Searching in alias: " + alias);
				if(alias != null && alias.equalsIgnoreCase(name))
				{
					return exit;
				}
			}
		}
		
		//if no object found
		game.sendErrorMessage("There is no " + name + " in the current room: " + this.name);
		System.out.println(name.toUpperCase() + ": ERROR: did not find a " + name + " in the current room: " + this.name);
		return null;
	}

	
	//command related
	@Override
	public void ExecuteCommand(Command command)
	{
		System.out.println("ERROR: unsupported: execute command on room");
		
	}

	public void Built_In_Command_OutputDescription()
	{
		if(description != null)
			game.Output(description);
	}
	
	//getters and setters
	public String getDescription() 
	{
		return description;
	}
	
	public void addObject (SceneObject object)
	{
		if(object == null)
		{
			System.err.println(name.toUpperCase() + ": ERROR: attempt to add null object");
		}
		else
		{
			//System.out.println(name.toUpperCase() + ": Added object: " + object.getName() + " to " + this.name);
			objects.add(object);
		}
	}
	
	/**
	 * Gets array of all the objects in the room, has to convert from list to array
	 * @return array of all the objects in the room
	 */
	public SceneObject[] getObjects()
	{
		SceneObject[] sceneObjectsArray = new SceneObject[objects.size()];
		for (int i = 0; i < objects.size(); i++)
		{
			sceneObjectsArray[i] = objects.get(i);
		}
		return sceneObjectsArray;
	}
	
	/**
	 * Gets array of all the exits in the room, has to convert from list to array
	 * @return array of all the exits in the room
	 */
	public Exit[] getExits()
	{
		Exit[] exitsArray = new Exit[exits.size()];
		for(int i = 0; i < exits.size(); i++)
		{
			exitsArray[i] = exits.get(i);
		}
		return exitsArray;
	}
	
	public void addExit (Exit exit)
	{
		exits.add(exit);
	}
	
	public void setID(int id)
	{
		//System.out.println("ID set to: " + id);
		this.ID = id;
	}
	
	public int getID()
	{
		
		return ID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	
	public void setAllObjectsGame(Game game)
	{
		for(int i = 0; i < objects.size(); i++)
		{
			objects.get(i).setGame(game);
		}
	}
	
	public void setAllExitsGame(Game game)
	{
		if(game == null)
		{
			System.err.println("ERROR: Null reference for game trying to be set in setAllExitsGame for room " + name);
			return;
		}
		
		for(int i = 0; i < exits.size(); i++)
		{
			exits.get(i).setGame(game);
		}
	}
	
	@Override
	public void setDescription(String description)
	{
		this.description = description;
		
	}

	@Override
	public void setGame(Game game)
	{
		this.game = game;
		
	}

	@Override
	public String[] getAliases()
	{
		String[] aliasesArray = new String[aliases.size()];
		for(int i = 0; i < aliases.size(); i++)
		{
			aliasesArray[i] = aliases.get(i);
		}
		return aliasesArray;
	}

	@Override
	public void addAlias(String alias)
	{
		//adds this alias to the list of aliases
		aliases.add(alias);
	}

	@Override
	public void removeAlias(String alias)
	{
		aliases.remove(alias);
	}
	
	@Override
	public void addRequest(Request request)
	{
		// TODO Auto-generated method stub
		System.err.println("ERROR: not yet implemented requests in ROOMS");
	}
	
	//toString override
	@Override
	public String toString()
	{
		String output = name != null ? name : "UNNAMED ROOM";
		return output;
		
		/*
		final int MAX_DESCRIPTION_LENGTH = 10;
		
		String objectsStr = "";
		if(objects.size() == 0)
		{
			objectsStr = "none";
		}
		else
		{
			for(int i = 0; i < objects.size(); i++)
			{
				objectsStr += objects.get(i).getName();
				if(i < objects.size() - 1)
				{
					objectsStr += ", ";
				}
			}
		}
		
		String exitsStr = "";
		if(exits.size() == 0)
		{
			exitsStr = "none";
		}
		else
		{
			for(int i = 0; i < exits.size(); i++)
			{
				exitsStr += exits.get(i).getName();
				if(i < exits.size() - 1)
				{
					exitsStr += ", ";
				}
			}
		}
		
		String modifiedDescription = description;
		if(description.length() > MAX_DESCRIPTION_LENGTH)
		{
			modifiedDescription = description.substring(0, MAX_DESCRIPTION_LENGTH) + "...";
		}
		
		String output = "ROOM: [ID: {" + ID + "}, name: {" + name + "}, description: {" + modifiedDescription + "}, objects: {" + objectsStr + "}, exits: {" + exitsStr + "}]";
		
		return output;
		*/
	}
	
}
