package sceneObjects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gamelogic.Command;
import gamelogic.Game;
import gamelogic.Request;

/**
 * Basic implementation of SceneObject
 * @author cjgunnar
 *
 */
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
				request.ExecuteActions(game, this);
			}
			
			if(request.hasVerb(command.getTypeOfCommand()))
			{
				System.out.println(name.toUpperCase() + ": MATCH with verb: " + command.getTypeOfCommand());
				request.ExecuteActions(game, this);
			}
			/*
			else
			{
				System.out.println(name.toUpperCase() + ": current request does not have verb");
			}
			*/
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
	
	public void Built_In_Command_OutputDescription()
	{
		if(description != null)
			game.Output(description);
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
		if(propName == null)
		{
			System.err.println(name.toUpperCase() + ": ERROR: can't check null property name as input");
			return false;
		}
		
		if(properties == null)
		{
			System.err.println(name.toUpperCase() + ": ERROR: properties is null");
			return false;
		}
		
		if(!properties.containsKey(propName))
		{
			System.err.println(name.toUpperCase() + ": ERROR: can not check property " + propName + ", as it does not exist");
			return false;
		}
		
		int propValue;
		if(properties.get(propName) != null)
		{
			propValue = properties.get(propName);
		}
		else
		{
			System.err.println(name.toUpperCase() + ": ERROR: value of property: " + propName + " is null");
			return false;
		}
		
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final int MAX_DESCRIPTION_LENGTH = 10;
		
		String modifiedDescription = description;
		if(description.length() > MAX_DESCRIPTION_LENGTH)
		{
			modifiedDescription = description.substring(0, MAX_DESCRIPTION_LENGTH) + "...";
		}
		
		return "SimpleObject [ID=" + ID + ", name=" + name + ", description=" + modifiedDescription + ", aliases=" 
		+ aliases.toString() + ", requests=" + requests + ", properties=" + properties.toString() + "]";
	}

}
