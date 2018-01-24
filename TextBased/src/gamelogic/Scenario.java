package gamelogic;

import java.util.ArrayList;
import java.util.HashMap;

import sceneObjects.SceneObject;

/**
 * used as a public/global property and command storage 
 * @author cjgunnar
 *
 */
public class Scenario
{
	/** For the error messages */
	private static final String name = "SCENARIO";
	
	/** reference to the logic so it can check properties */
	Game _game;
	
	/**
	 * HashMap of global properites,
	 * In String name, Integer value pairs
	 */
	HashMap<String, Integer> properties = new HashMap<String, Integer>();

	/** List of gloabl requests */
	ArrayList<Request> requests = new ArrayList<Request>();

	/** Default constructor */
	public Scenario() {}
		
	/**
	 * Can only use exact phrases to match since it has no name/place
	 * @param exact Exact phrase to check against (from Command)
	 * @return boolean if it has it and it was successfully ran
	 */
	public boolean hasRequest(String exact)
	{
		//look through each request, see if any match the exact phrase
		for(Request request : requests)
		{
			//if it does...
			if(request.hasExact(exact))
			{
				request.ExecuteActions(_game, null);
				return true;
			}
		}
		
		//default
		return false;
	}
	
	//property related methods
	public void setProperties(HashMap<String, Integer> properties)
	{
		this.properties = properties;
		System.out.println("SCENARIO: adding " + properties.size() + " properties");
	}
	
	public void changeProperty(String propName, int value)
	{
		properties.put(propName, value);
	}

	public int getProperty(String propName)
	{
		int value = properties.get(propName);
		return value;
	}

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

	public void addProperty(String propName, int initValue)
	{
		properties.put(propName, initValue);
	}

	public void addProperty(String propName)
	{
		addProperty(propName, SceneObject.defaultValue);
	}
	
	//request based methods
	public void addRequest(Request request)
	{
		requests.add(request);
	}
	
	/** Sets the game reference */
	public void setGame(Game game)
	{
		this._game = game;
	}

}
