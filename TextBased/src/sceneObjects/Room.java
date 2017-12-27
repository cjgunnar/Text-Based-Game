package sceneObjects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gamelogic.Command;
import gamelogic.Game;
import gamelogic.Request;

//Caden Gunnarson

public class Room implements SceneObject
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
			if(exit.getEntranceTo().getName().equals(destination.getName()))
			{
				return exit;
			}
		}
		
		//default
		return null;
	}
	
	public SceneObject FindObjectByID(int id)
	{
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
		
		//default
		return null;
	}
	
	public SceneObject FindObjectByName(String name)
	{
		if (name == null || name.equals("") || name.equals(" "))
		{
			System.out.println("ERROR: null name entered");
			return null;
		}
		
		if (objects.isEmpty() && exits.isEmpty())
		{
			System.out.println("ERROR: no objects or exits in room");
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
					System.out.println("ERROR: null alias");
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
		System.out.println("ERROR: did not find a " + name + " in the current room: " + this.name);
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
	
	public SceneObject[] getObjects()
	{
		SceneObject[] sceneObjectsArray = new SceneObject[objects.size()];
		for (int i = 0; i < objects.size(); i++)
		{
			sceneObjectsArray[i] = objects.get(i);
		}
		return sceneObjectsArray;
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
	public void addRequest(Request request)
	{
		// TODO Auto-generated method stub
		System.err.println("ERROR: not yet implemented requests in ROOMS");
	}
	
}
