package gamelogic;

import sceneObjects.SceneObject;

/** Can Trigger EndState */
public class EndStateTrigger
{
	String type; //property
	
	//for property change
	final String PROPERTY = "property";
	String property_name;
	int target;
	int value;
	String operator;
	
	Game _game;
	
	/** @return True or false, if the trigger has triggered */
	public boolean hasTriggered()
	{
		//safety null check
		if(type == null)
		{
			System.out.println("FAIL STATE TRIGGER: ERROR: null type");
			return false;
		}
		
		if(type.equalsIgnoreCase(PROPERTY))
		{
			return checkProperty();
		}
		else
		{
			System.out.println("FAIL STATE TRIGGER: ERROR: unknown type: " + type);
		}
		
		//default
		System.out.println("FAIL STATE TRIGGER: ERROR: uncaught error");
		return false;
	}
	
	private boolean checkProperty()
	{
		if(operator == null)
		{
			System.err.println("FAIL STATE TRIGGER: ERROR: null operator");
			return false;
		}
		else if(property_name == null)
		{
			System.err.println("FAIL STATE TRIGGER: ERROR: property_name is null");
			return false;
		}
		
		//find the target object
		SceneObject targetObject;
		if(target == Level.SCENARIO_ID) //special case scenario property
		{
			//return the scenario check
			return _game.level.checkScenarioProperty(property_name, operator, value);
		}
		else //find object
		{
			targetObject = _game.manager.getRoom().FindObjectByID(target);
		}
		
		if(targetObject != null)
		{
			if(targetObject.checkProperty(property_name, operator, value))
			{
				System.out.println("END STATE TRIGGER: PASSED: " + property_name + " is " + operator + " " + value);
				return true;
			}
			else
			{
				System.out.println("END STATE TRIGGER: FAILED: " + property_name + " is NOT " + operator + " " + value + ", target is " + target);
				return false;
			}
		}
		else
		{
			System.err.println("END STATE TRIGGER: ERROR: no object found with target ID: " + target);
			return false;
		}
	}

	
	//GETTERS AND SETTERS
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type)
	{
		this.type = type;
	}

	/**
	 * @param property_name the property_name to set
	 */
	public void setProperty_name(String property_name)
	{
		this.property_name = property_name;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(int target)
	{
		this.target = target;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value)
	{
		this.value = value;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator)
	{
		this.operator = operator;
	}

	/**
	 * @param _game the _game to set
	 */
	public void set_game(Game _game)
	{
		this._game = _game;
	}
	
}
