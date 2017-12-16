package sceneObjects;
import java.util.ArrayList;
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
	
	//description of the object
	String description;
	
	//id of the room
	int ID;
	
	//reference to game to send messages
	Game game;
	
	//list of all the objects in the room
	List<SceneObject> objects = new ArrayList<SceneObject>();

	//list of exits to room
	List<Exit> exits = new ArrayList<Exit>();
	
	//full constructor
	public Room(String name, String description, Game game) 
	{
		this.name = name;
		this.description = description;
		this.game = game;
	}
	
	//default constructor
	public Room() {};
	
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
	
	public void InitializeAllExits()
	{
		for(int i = 0; i < exits.size(); i++)
		{
			exits.get(i).InitializeEntranceToByID();
		}
	}
	
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
	
	public SceneObject FindObjectByName(String name)
	{
		if (name == null || name.equals("") || name.equals(" "))
		{
			System.out.println("ERROR: null name entered");
			return null;
		}
		
		if (objects.isEmpty())
		{
			System.out.println("ERROR: no objects in room");
			return null;
		}
		
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
		
		//search through each exits in room for matching name
		for(Exit exit : exits)
		{
			if (exit.getName() != null && exit.getName().equalsIgnoreCase(name))
			{
				//return the object with matching name
				return exit;
			}
			
			//search through each exits aliases as well
			for(String alias : exit.getAliases())
			{
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

	@Override
	public void ExecuteCommand(Command command)
	{
		System.out.println("ERROR: unsupported execute command on room");
		
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