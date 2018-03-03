package gamelogic;

import java.util.ArrayList;
import java.util.List;

import sceneObjects.SceneObject;

/**
 * Executable that will do pass or fail actions based on a property condition of a target object
 * @author cjgunnar
 *
 */
public class Condition implements Executable
{
	/** Can be SceneObject.greaterThan, SceneObject.lessThan, or SceneObject.equalTo */
	String operator;
	
	/** Target of the condition, 0 will use parent */
	int target;
	
	/** the value that will be evaluated with the target property value */
	int value;
	
	/** name of the target property to get the value */
	String property_name;

	/** actions to execute if action passes */
	ExecutableGroup pass;
	
	/** actions to execute if action fails */
	ExecutableGroup fail;
	
	/** reference to the parent object of this condition */
	SceneObject parentObject;
	
	/** reference to game */
	Game _game;
	
	/** Keeps track of if the pass or fail condition ran */
	boolean success;
	
	/** Default condition constructor */
	public Condition() 
	{
		target = 0;
		property_name = "Unset";
		operator = "=";
		value = 0;
		
		pass = new ExecutableGroup(ExecutableGroup.PASS);
		fail = new ExecutableGroup(ExecutableGroup.FAIL);
		success = false;
	}
	
	/**
	 * Main Constructor
	 * @param target the ID of the SceneObject with the property to get
	 * @param property_name the name of the property to get from the target
	 * @param operator how to compare the property with the value
	 * @param value the value to test against
	 */
	public Condition(int target, String property_name, String operator, int value)
	{
		this.target = target;
		this.property_name = property_name;
		this.operator = operator;
		this.value = value;
		
		success = false;
		pass = new ExecutableGroup(ExecutableGroup.PASS);
		fail = new ExecutableGroup(ExecutableGroup.FAIL);
	}

	@Override
	public void run()
	{
		if(checkCondition())
		{
			//run pass
			executePass(_game);
			success = true;
		}
		else
		{
			//run fail
			executeFail(_game);
			success = false;
		}
	}

	@Override
	public void addExecutable(Executable executable)
	{
		//default add to pass group
		addPassExecutable(executable);
	}

	@Override
	public List<Executable> getExecutables()
	{
		ArrayList<Executable> total = new ArrayList<Executable>();
		total.add(pass);
		total.add(fail);
		
		return total;
	}
	
	/**
	 * Add action to execute on condition pass
	 * @param executable the executable to add
	 */
	public void addPassExecutable(Executable executable)
	{
		pass.addExecutable(executable);
	}
	
	/**
	 * Add action to execute on condition fail
	 * @param action the action to add
	 */
	public void addFailExecutable(Executable executable)
	{
		fail.addExecutable(executable);
	}
	
	/**
	 * Helper method for condition, checks and returns boolean
	 * @return true or false if condition pass/fail
	 */
	public boolean checkCondition()
	{
		if(operator == null)
		{
			System.err.println("CONDITION: ERROR: null operator");
			return false;
		}
		else if(property_name == null)
		{
			System.err.println("CONDITION: ERROR: property_name is null");
			return false;
		}
		
		
		//find the target object, if target is 0 use parent
		SceneObject targetObject;
		if(target == 0) //parent object
		{
			//safety null check
			if(parentObject == null)
			{
				System.err.println("CONDITION: ERROR: target is 0 but parent is null");
				return false;
			}
			
			targetObject = parentObject;
		}
		else if(target == Level.SCENARIO_ID) //special case scenario property
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
				System.out.println("CONDITION: PASSED: " + property_name + " is " + operator + " " + value);
				return true;
			}
			else
			{
				System.out.println("CONDITION: FAILED: " + property_name + " is NOT " + operator + " " + value + ", target is " + target);
				return false;
			}
		}
		else
		{
			System.err.println("CONDITION: ERROR: no object found with target ID: " + target);
			return false;
		}
	}
	
	/**
	 * Runs all actions and nested conditions that are set in the fail group or nestedFailConditinos group
	 * @param game reference to game
	 */
	private void executeFail(Game game)
	{
		for(Executable executable: fail.getExecutables())
		{
			if(game != null)
				executable.setGame(game);
			else
				System.out.println("CONDITION: ERROR: game reference");
				
			if(parentObject != null)
			{
				executable.setParentObject(parentObject);
			}
			else
			{
				System.out.println("CONDITION: WARNING: null parent object");
			}
			
			executable.run();
		}
	}
	
	/**
	 * Runs all actions and nested conditions that are set in the pass group or nestedPassConditions group
	 * @param game reference to game
	 */
	private void executePass(Game game)
	{
		for(Executable executable: pass.getExecutables())
		{
			if(game != null)
				executable.setGame(game);
			else
				System.out.println("CONDITION: ERROR: game reference");
				
			if(parentObject != null)
			{
				executable.setParentObject(parentObject);
			}
			else
			{
				System.out.println("CONDITION: WARNING: null parent object");
			}
				
			executable.run();
		}
	}
	
	/**
	 * @param _game the _game to set
	 */
	public void setGame(Game game)
	{
		this._game = game;
	}

	/**
	 * Sets the parent object of the condition
	 * @param parentObject the parentObject to set
	 */
	public void setParentObject(SceneObject parentObject)
	{
		this.parentObject = parentObject;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(String operator)
	{
		if(operator == null)
		{
			System.err.println("CONDITION: ERROR: trying to set null operator");
			return;
		}
		
		else if(!operator.equals(SceneObject.greaterThan) && !operator.equals(SceneObject.lessThan) && !operator.equals(SceneObject.equalTo))
		{
			System.err.println("CONDITION: ERROR: unknown operator: " + operator);
			return;
		}
		
		this.operator = operator;
	}

	/**
	 * Sets the target ID of the condition parameter check
	 * @param target the target to set
	 */
	public void setTarget(int target)
	{
		this.target = target;
	}

	public void setTarget(String target)
	{
		try
		{
			setTarget(Integer.parseInt(target));
		}
		catch (NumberFormatException e)
		{
			System.err.println("CONDTION: ERROR: target ID cannot be turned into an integer");
		}
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(int value)
	{
		this.value = value;
	}

	/**
	 * xml parser reads String, this will turn it into a int
	 * @param value String to turn into int and then set
	 */
	public void setValue(String value)
	{
		try
		{
			setValue(Integer.parseInt(value));
		}
		catch (NumberFormatException e)
		{
			System.err.println("CONDTION: ERROR: value cannot be turned into an integer");
		}
	}
	
	/**
	 * @param property_name the property_name to set
	 */
	public void setProperty_name(String property_name)
	{
		this.property_name = property_name;
	}

	@Override
	public boolean getSuccess()
	{
		return success;
	}
	
	@Override
	public String toString()
	{
		return "CONDITION: IF ID#" + target + "'s " + property_name + " IS " + operator + " " + value + " THEN";
	}
}
