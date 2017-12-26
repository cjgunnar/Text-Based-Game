package gamelogic;

import java.util.ArrayList;

import sceneObjects.SceneObject;

/**
 * Will do pass or fail actions based on a property condition
 * @author Caden
 *
 */
public class Condition
{
	/**
	 * Can be SceneObject.greaterThan, SceneObject.lessThan, or SceneObject.equalTo
	 */
	String operator;
	
	/**
	 * id of the target, 0 will return an error
	 */
	int target;
	
	/**
	 * the value that will be evaluated with the target property value
	 */
	int value;
	
	/**
	 * name of the target property to get the value
	 */
	String property_name;

	/**
	 * actions to execute if action passes
	 */
	ArrayList<Action> pass = new ArrayList<Action>();
	
	/**
	 * actions to execute if action fails
	 */
	ArrayList<Action> fail = new ArrayList<Action>();
	
	/**
	 * reference to game
	 */
	Game _game;
	
	//default condition constructor
	public Condition() {}

	/**
	 * Add action to execute on condition pass
	 * @param action the action to add
	 */
	public void addPassAction(Action action)
	{
		pass.add(action);
	}
	
	/**
	 * Add action to execute on condition fail
	 * @param action the action to add
	 */
	public void addFailAction(Action action)
	{
		fail.add(action);
	}
	
	/**
	 * Runs this condition. If the condition is true, runs pass list, if false, runs fail list
	 */
	public void runCondition()
	{
		if(checkCondition())
		{
			//run pass
			executePass(_game);
		}
		else
		{
			//run fail
			executeFail(_game);
		}
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
		else if(target == 0)
		{
			System.err.println("CONDITION: ERROR: target is 0");
			return false;
		}
		else if(property_name == null)
		{
			System.err.println("CONDITION: ERROR: property_name is null");
			return false;
		}
		
		//find the target object
		SceneObject targetObject = _game.manager.getRoom().FindObjectByID(target);
		
		if(targetObject != null)
		{
			if(targetObject.checkProperty(property_name, operator, value))
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
			System.err.println("CONDITION: ERROR: no object found with target ID: " + target);
			return false;
		}
	}
	
	/**
	 * Runs all actions that are set in the fail group
	 * @param game reference to game
	 */
	private void executeFail(Game game)
	{
		for(Action action: fail)
		{
			action.setGame(game);
			action.runAction();
		}
	}
	
	/**
	 * Runs all actions that are set in the pass group
	 * @param game reference to game
	 */
	private void executePass(Game game)
	{
		for(Action action: pass)
		{
			action.setGame(game);
			action.runAction();
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
	
}
