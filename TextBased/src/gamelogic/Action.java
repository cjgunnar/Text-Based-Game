package gamelogic;

import sceneObjects.SceneObject;

//actions do things

public class Action
{
	final String OUT = "out";
	final String PROPERTY_CHANGE = "property_change";
	
	private String actionType;
	private String actionValue;
	
	private int actionTarget;
	private String propertyName;
	
	private Game _game;
	
	public void runAction()
	{
		//run action...
		//use game reference to do that
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
			
			//find the object with the target ID
			SceneObject targetObj = _game.manager.getRoom().FindObjectByID(actionTarget);
			
			//change the property
			targetObj.changeProperty(propertyName, Integer.parseInt(actionValue));
		}
		else
		{
			System.err.println("ACTION: ERROR: unrecognized type: " + actionType);
		}
		
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
