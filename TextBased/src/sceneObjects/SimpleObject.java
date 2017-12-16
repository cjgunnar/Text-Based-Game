package sceneObjects;
import java.util.ArrayList;
import java.util.List;

import gamelogic.Command;
import gamelogic.Game;
import gamelogic.Request;

public class SimpleObject implements SceneObject
{
	//name of simple object
	String name;
	
	//description of simple object
	String description;
	
	//reference to the game for game logic/output
	Game game;
	
	//list of similar things to call it
	List<String> aliases = new ArrayList<String>();
	
	//list of "request" or things you can do with this object
	List<Request> requests = new ArrayList<Request>();
	
	//full constructor
	public SimpleObject(String name, String description, Game game) 
	{
		//super(name, description);
		this.name = name;
		this.description = description;
		this.game = game;
		//System.out.println("Created object: " + name);
	}
	
	//default constructor
	public SimpleObject() {}
	
	@Override
	public void ExecuteCommand(Command command)
	{
		if (command == null)
		{
			return;
		}
		
		System.out.println(name.toUpperCase() + ": attempting command, raw: " + command.getRaw());
		
		if(requests.isEmpty())
		{
			System.out.println(name.toUpperCase() + ": no requests available");
		}
		else
		{
			System.out.println(name.toUpperCase() + ": looking through " + requests.size() + " requests...");
		}
		
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
		
		if(command.getTypeOfCommand().equals("look at"))
		{
			game.Output(description);
		}
		
		/*
		
		//System.out.println("Running command with params: " + command.getTypeOfCommand() + " " + name);
		if (command.getTypeOfCommand().equals("look at"))
		{
			game.Output(description);
		}
		else if (command.getTypeOfCommand().equals("use"))
		{
			game.sendErrorMessage("You cannot use the " + name);
		}
		else
		{
			game.sendErrorMessage("You can't " + command.getTypeOfCommand() + " the " + name);
		}
		
		*/
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	@Override
	public String getName()
	{
		return name;
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
		//System.out.println("Simple object: adding alias of " + alias);
	}

	public void addRequest(Request request)
	{
		requests.add(request);
	}

}
