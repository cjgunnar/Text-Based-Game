package sceneObjects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gamelogic.Command;
import gamelogic.Game;
import gamelogic.Request;

/**
 * An exit leads to another room, holds destination in destination property. Has action change_room that will make it change the room
 * @author cjgunnar
 *
 */
public class Exit implements SceneObject
{
	//name and description of the object
	String name;
	String description;
	
	//aliases
	List<String> aliases = new ArrayList<String>();
	
	//requests
	List<Request> requests = new ArrayList<Request>();
	
	/**
	 * what room this exit leads to
	 */
	Room entranceTo;
	
	//ID of the room it is the entrance to
	int entranceToID;
	
	//reference to game so it can change rooms
	Game game;
	
	/**
	 * properties of the object (HashMap), name int pairs. Contains destination of the exit
	 */
	HashMap<String, Integer> properties = new HashMap<String, Integer>();
	
	/**
	 * Exit specific property used to tell which room this exit leads to
	 */
	private String DESTINATION = "destination";
	
	//id of the object
	int ID;
	
	/**
	 * Full constructor for an exit
	 * @param name name of the exit
	 * @param description description of the exit
	 * @param entranceTo room where this exit leads to
	 * @param game reference to game
	 */
	public Exit(String name, String description, Room entranceTo, Game game)
	{
		//super(name, description);
		this.name = name;
		this.description = description;
		this.entranceTo = entranceTo;
		this.game = game;
		//System.out.println("new exit constructed named " + name);
	}
	
	/**
	 * default constructor
	 */
	public Exit() 
	{
		//create exit specific default properties
		addProperty(DESTINATION);
	}
	
	/*
	 * @see SceneObject#ExecuteCommand(Command)
	 * @return boolean returns true if command found and executed
	 */
	
	public void ExecuteCommand(Command command)
	{
		if (command == null)
		{
			return;
		}
		
		if(requests.isEmpty())
		{
			System.out.println(name.toUpperCase() + ": no requests available");
		}
		else
		{
			//System.out.println(name.toUpperCase() + ": attempting command, raw: " + command.getRaw());
			//System.out.println(name.toUpperCase() + ": looking through " + requests.size() + " requests...");
			
			for(Request request: requests)
			{
				if(request.hasExact(command.getRaw()))
				{
					//System.out.println(name.toUpperCase() + ": MATCH with raw: " + command.getRaw());
					request.ExecuteActions(game, this);
				}
				
				if(request.hasVerb(command.getTypeOfCommand()))
				{
					//System.out.println(name.toUpperCase() + ": MATCH with verb: " + command.getTypeOfCommand());
					request.ExecuteActions(game, this);
				}
				/*
				else
				{
					System.out.println(name.toUpperCase() + ": current request does not have verb");
				}
				*/
			}
		}
		
		if(command.getTypeOfCommand() != null && command.getTypeOfCommand().equalsIgnoreCase("use"))
		{
			if(entranceToID == 0)
			{
				System.out.println(name.toUpperCase() + " leads to nowhere");
				
				//"use" action should be specified, as internal logic will do nothing
				return;
			}
			else
			{
				game.manager.ChangeRoom(entranceTo);
				game.Output(game.manager.getRoom().getDescription());
			}
		}
		
		if(command.getTypeOfCommand() != null && command.getTypeOfCommand().equalsIgnoreCase("look at"))
		{
			game.Output(description);
		}
		
		/*
		switch (command.getTypeOfCommand())
		{
		case "look at":
			System.out.println(description);
			break;
		case "use":
			System.out.println("You twist the knob and enter.");
			game.manager.ChangeRoom(entranceTo);
			System.out.println(game.manager.getRoom().getDescription());
			//System.out.println("You cannot use the " + name);
			break;
		case "grab":
			System.out.println("You can't or don't see any reason to pick up the " + name);
			break;
		default:
			System.out.println("You can't or don't see a reason to " + command.getTypeOfCommand() + " the " + name);
			break;
		}
		*/
	}
	
	public Room getEntranceTo() 
	{
		return entranceTo;
	}

	public void setEntranceTo(Room entranceTo)
	{
		this.entranceTo = entranceTo;
		
	}
	
	//property related methods
	@Override
	public void changeProperty(String propName, int value)
	{
		if(!properties.containsKey(propName))
		{
			System.out.println(name.toUpperCase() + ": does not contain property " + propName + ", creating it with default value...");
			addProperty(propName);
		}
		
		System.out.println(name.toUpperCase() + ": property " + propName + " changed from " + properties.get(propName) + " to " + value);
		properties.put(propName, value);
	
		//exit specific property "destination"
		if(propName.equalsIgnoreCase(DESTINATION))
		{
			System.out.println(name.toUpperCase() + ": changing destination to ID " + value);
			entranceToID = value;
			if(game != null && game.level != null)
				InitializeEntranceToByID();
			else
				System.err.println(name.toUpperCase() + ": ERROR: null game or level reference");
		}
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
		if(!properties.containsKey(propName))
		{
			System.err.println(name.toUpperCase() + ": ERROR: can not check property \"" + propName + "\", as it does not exist");
			return false;
		}
		
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
		//exit specific property "destination"
		
		
		properties.put(propName, initValue);
	}

	@Override
	public void addProperty(String propName)
	{
		addProperty(propName, SceneObject.defaultValue);
	}
	
	//exit related methods
	public void setEntranceTo(int id)
	{
		entranceToID = id;
		if(game != null && game.level != null)
		{
			//if the game and level have been set up, initialize the new entranceTo reference
			InitializeEntranceToByID();
		}
	}
	
	/**
	 * Using the number ID of the room, find and initialize the Room reference (entranceTo)
	 */
	public void InitializeEntranceToByID()
	{
		//System.out.println("Initializing exit " + name);
		if(entranceToID == 0)
		{
			System.err.println(name.toUpperCase() + ": WARNING: UNSET ID FOR " + name + ", this exit leads nowhere");
			return;
		}
		if(game == null || game.level == null)
			System.err.println("ERROR: null game or game.level reference");
		//System.out.println("Init: looking for room with ID: " + entranceToID);
		//System.out.println("Linking " + name + " to " + game.level.FindRoomWithID(entranceToID).getName());
		entranceTo = game.level.FindRoomWithID(entranceToID);
	}
	
	//non-exit methods
	@Override
	public int getID()
	{
		return ID;
	}
	
	@Override
	public void setID(int ID)
	{
		this.ID = ID;
	}
	
	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void setDescription(String description)
	{
		this.description = description;
		
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
		
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
		//System.out.println("Add alias: " + alias + " to " + name);
		aliases.add(alias);
	}

	@Override
	public void addRequest(Request request)
	{
		requests.add(request);
	}
	
}
