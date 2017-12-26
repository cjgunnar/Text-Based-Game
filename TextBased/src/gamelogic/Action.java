package gamelogic;

import sceneObjects.SceneObject;

//actions do things

public class Action
{
	//types of actions
	/**
	 * An action of type out displays its value to the log, basically sends text
	 */
	final String OUT = "out";
	
	/**
	 * An action of type property_change changes a property of a target ID to a integer value
	 */
	final String PROPERTY_CHANGE = "property_change";
	
	final String EXIT_CHANGE_ROOM = "exit:change_room";
	
	private String actionType;
	private String actionValue;
	
	/**
	 * The owner/parent of the action
	 */
	private SceneObject parentObject;
	
	private int actionTarget;
	private String propertyName;
	
	private Game _game;
	
	public void runAction()
	{
		//run action...
		//use game reference or parent reference to do that
		if(actionType == null)
		{
			System.err.println("ACTION: ERROR: type is null");
			return;
		}
		
		if(actionType.equals(OUT))
		{
			_game.Output(actionValue);
		}
		else if(actionType.equals(PROPERTY_CHANGE))
		{
			System.out.println("ACTION: changing property " + propertyName + " to " + actionValue);
			
			SceneObject targetObj;
			if(actionTarget == 0)
			{
				//use parent if target is 0
				if(parentObject != null)
					targetObj = parentObject;
				else
				{
					System.err.println("ACTION: ERROR: target is 0 but parent is null");
					return;
				}
			}
			else
			{
				//find the object with the target ID
				targetObj = _game.manager.getRoom().FindObjectByID(actionTarget);
			}
			
			if(targetObj != null)
				//change the property
				targetObj.changeProperty(propertyName, Integer.parseInt(actionValue));
			else
				System.err.println("ACTION: ERROR: no target object found with ID: " + actionTarget);
		}
		else
		{
			System.err.println("ACTION: ERROR: unrecognized type: " + actionType);
		}
		
	}
	
	public void setParentSceneObject(SceneObject parentObject)
	{
		this.parentObject = parentObject;
	}
	
	public void setGame(Game game)
	{
		this._game = game;
	}
	
	public String getActionType()
	{
		return actionType;
	}
	
	public void setActionType(String actionType)
	{
		this.actionType = actionType;
	}
	
	public String getActionValue()
	{
		return actionValue;
	}
	
	public void setActionValue(String actionValue)
	{
		this.actionValue = actionValue;
	}
	
	/**
	 * @return the actionTarget
	 */
	public int getActionTarget()
	{
		return actionTarget;
	}

	/**
	 * @param actionTarget the actionTarget to set
	 */
	public void setActionTarget(int actionTarget)
	{
		this.actionTarget = actionTarget;
	}
	
	public void setActionTarget(String actionTarget)
	{
		if(actionTarget == null)
		{
			System.err.println("ACTION: ERROR: null action target");
			return;
		}
		
		try
		{
			setActionTarget(Integer.parseInt(actionTarget));
		}
		catch (NumberFormatException e)
		{
			System.err.println("ACTION: ERROR: non-int value as target ID attribute: " + actionTarget);
		}
	}

	/**
	 * Getter for the property name (used only with type property change, otherwise will output error)
	 * @return the propertyName
	 */
	public String getPropertyName()
	{
		if(actionType != PROPERTY_CHANGE)
		{
			System.err.println("ACTION: ERROR: trying to set property name for non-Property_Change action");
		}
		
		if(propertyName == null)
		{
			System.err.println("ACTION: ERROR: trying to get null property name");
		}
		
		return propertyName;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName)
	{
		this.propertyName = propertyName;
	}
	
}
