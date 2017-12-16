package sceneObjects;
import java.util.ArrayList;
import java.util.List;

import gamelogic.Command;
import gamelogic.Game;
import gamelogic.Request;

//all the exits to a room
public class Exit implements SceneObject
{
	//name and description of the object
	String name;
	String description;
	
	//aliases
	List<String> aliases = new ArrayList<String>();
	
	//requests
	List<Request> requests = new ArrayList<Request>();
	
	//what room does this exit lead to
	Room entranceTo;
	
	//ID of the room it is the entrance to
	int entranceToID;
	
	//reference to game so it can change rooms
	Game game;
	
	//full constructor
	public Exit(String name, String description, Room entranceTo, Game game)
	{
		//super(name, description);
		this.name = name;
		this.description = description;
		this.entranceTo = entranceTo;
		this.game = game;
		//System.out.println("new exit constructed named " + name);
	}
	
	//default constructor
	public Exit() {}
	
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
			System.out.println(name.toUpperCase() + ": attempting command, raw: " + command.getRaw());
			System.out.println(name.toUpperCase() + ": looking through " + requests.size() + " requests...");
			
			for(Request request: requests)
			{
				if(request.hasExact(command.getRaw()))
				{
					System.out.println(name.toUpperCase() + ": MATCH with raw: " + command.getRaw());
					request.ExecuteActions(game);
				}
				
				if(request.hasVerb(command.getTypeOfCommand()))
				{
					System.out.println(name.toUpperCase() + ": MATCH with verb: " + command.getTypeOfCommand());
					request.ExecuteActions(game);
				}
				else
				{
					System.out.println(name.toUpperCase() + ": current request does not have verb");
				}
			}
		}
		
		if(command.getTypeOfCommand() != null && command.getTypeOfCommand().equalsIgnoreCase("use"))
		{
			game.manager.ChangeRoom(entranceTo);
			game.Output(game.manager.getRoom().getDescription());
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
	
	public void setEntranceTo(int id)
	{
		entranceToID = id;
		//entranceTo = game.level.FindRoomWithID(id);
	}
	
	public void InitializeEntranceToByID()
	{
		//System.out.println("Initializing exit " + name);
		if(entranceToID == 0)
		{
			System.out.println("ERROR: UNSET ID FOR " + name);
		}
		if(game == null || game.level == null)
			System.err.println("ERROR: null game or game.level reference");
		//System.out.println("Init: looking for room with ID: " + entranceToID);
		//System.out.println("Linking " + name + " to " + game.level.FindRoomWithID(entranceToID).getName());
		entranceTo = game.level.FindRoomWithID(entranceToID);
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
		aliases.add(alias);
	}

	@Override
	public void addRequest(Request request)
	{
		requests.add(request);
	}
	
}