package gamelogic;

import java.util.ArrayList;

import sceneObjects.SceneObject;

/**
 * Will do pass or fail actions based on a property condition
 * @author cjgunnar
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
	 * Allow conditions to have conditions (nested conditions) for AND operations.
	 * This list executed if the condition passes
	 */
	ArrayList<Condition> nestedPassConditions = new ArrayList<Condition>();
	
	/**
	 * Allow conditions to have conditions (nested conditions) for AND operations.
	 * This list executed if the condition fails
	 */
	ArrayList<Condition> nestedFailConditions = new ArrayList<Condition>();
	
	/**
	 * reference to the parent object of this condition
	 */
	SceneObject parentObject;
	
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
	 * Add a nested condition to execute on condition pass
	 * @param condition nested condition to add
	 */
	public void addPassNestedCondition(Condition condition)
	{
		nestedPassConditions.add(condition);
	}
	
	/**
	 * Add a nested condition to execute on condition fail
	 * @param condition nested condition to add
	 */
	public void addFailNestedCondition(Condition condition)
	{
		nestedFailConditions.add(condition);
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
		//execute fail actions
		for(Action action: fail)
		{
			if(game != null && parentObject != null)
			{
				action.setGame(game);
				action.setParentSceneObject(parentObject);
				action.runAction();
			}
			else
				System.err.println("CONDITION: ERROR: null parent object or game reference");
		}
		
		//execute fail nested conditions
		for(Condition condition: nestedFailConditions)
		{
			condition.setGame(game);
			condition.setParentObject(parentObject);
			condition.runCondition();
		}
	}
	
	/**
	 * Runs all actions and nested conditions that are set in the pass group or nestedPassConditions group
	 * @param game reference to game
	 */
	private void executePass(Game game)
	{
		for(Action action: pass)
		{
			if(game != null && parentObject != null)
			{
				action.setGame(game);
				action.setParentSceneObject(parentObject);
				action.runAction();
			}
			else
				System.err.println("CONDITION: ERROR: null parent object or game reference");
		}
		
		//execute pass nested conditions
		for(Condition condition: nestedPassConditions)
		{
			condition.setGame(game);
			condition.setParentObject(parentObject);
			condition.runCondition();
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
	
}
