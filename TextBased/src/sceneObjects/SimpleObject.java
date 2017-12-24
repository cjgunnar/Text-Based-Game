package sceneObjects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gamelogic.Command;
import gamelogic.Game;
import gamelogic.Request;

//implementation of SceneObject
public class SimpleObject implements SceneObject
{
	//ID of the object
	int ID;
	
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
	
	//properties of the object (HashMap), name int pairs
	HashMap<String, Integer> properties = new HashMap<String, Integer>();
	
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
		
		if(command.getTypeOfCommand() != null && command.getTypeOfCommand().equals("look at"))
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
	
	//non-property getters and setters
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
