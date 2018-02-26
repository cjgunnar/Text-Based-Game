package gamelogic;

import java.util.List;

import sceneObjects.Exit;
import sceneObjects.SceneObject;

//actions do things

public class Action implements Executable
{
	/**
	 * An action of type out displays its value to the log, basically sends text
	 */
	final String OUT = "out";
	
	final String OUT_DESCRIPTION = "out:description";
	
	/**
	 * An action of type property_change changes a property of a target ID to a integer value
	 */
	final String PROPERTY_CHANGE = "property_change";
	
	/**
	 * Can only be used with exits. "uses" the exit to enter destination room and then outputs description of room
	 */
	final String EXIT_CHANGE_ROOM = "exit:change_room";
	
	/** Triggers an ending */
	final String TRIGGER_ENDING = "trigger-ending";
	
	private String actionType;
	private String actionValue;
	
	/**
	 * The owner/parent of the action
	 */
	private SceneObject parentObject;
	
	private int actionTarget;
	private String propertyName;
	
	private Game _game;
	
	public void run()
	{
		//run action...
		//use game reference or parent reference to do that
		if(actionType == null)
		{
			System.err.println("ACTION: ERROR: type is null");
		}
		
		//outputs value to game output (console)
		else if(actionType.equals(OUT))
		{
			_game.Output(actionValue);
		}
		
		//property_change action changes properties of target objects
		else if(actionType.equalsIgnoreCase(PROPERTY_CHANGE))
		{
			runPropertyChange();
		}
		
		//outputs description
		else if(actionType.equalsIgnoreCase(OUT_DESCRIPTION))
		{
			System.out.println("ACTION: outputting parent description: " + parentObject.getName());
			_game.Output(parentObject.getDescription());
		}
		
		//special type for exits only
		//will "use" the exit and change the room, then output the description of the room just entered
		else if(actionType.equalsIgnoreCase(EXIT_CHANGE_ROOM))
		{
			Exit parentExit = (Exit)parentObject;
			parentExit.Built_In_Command_UseDoor();
		}
		
		else if(actionType.equalsIgnoreCase(TRIGGER_ENDING))
		{
			TriggerEnding();
		}
			
		else
		{
			System.err.println("ACTION: ERROR: unrecognized type: " + actionType);
		}
		
	}
	
	/** Triggers the target ending */
	private void TriggerEnding()
	{	
		System.out.println("ACTION: triggering ending: " + actionValue);
		int endingID = 0;
		try
		{
			endingID = Integer.parseInt(actionValue);
		}
		catch(NumberFormatException e)
		{
			System.out.println("ACTION: ERROR: value of id of ending to trigger is not an int");
		}
		
		_game.level.TriggerEndState(endingID);
	}
	
	private void runPropertyChange()
	{
		System.out.println("ACTION: changing property " + propertyName + " to " + actionValue);
		
		SceneObject targetObj;
		if(actionTarget == 0) //parent
		{
			//use parent as default if target is 0
			if(parentObject != null)
				targetObj = parentObject;
			else
			{
				System.err.println("ACTION: ERROR: target is 0 but parent is null");
				return;
			}
		}
		else if(actionTarget == Level.SCENARIO_ID) //scenario
		{
			//if it is changing a scenario property, change it then exit method
			_game.level.changeScenarioProperty(propertyName, Integer.parseInt(actionValue));
			return;
		}
		else //find the target
		{
			//find the object with the target ID
			targetObj = _game.level.FindObjectWithID(actionTarget);
		}
		
		if(targetObj != null)
			//change the property
			targetObj.changeProperty(propertyName, Integer.parseInt(actionValue));
		else
			System.err.println("ACTION: ERROR: no target object found with ID: " + actionTarget);
	}
	
	public void setParentObject(SceneObject parentObject)
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

	@Override
	public void addExecutable(Executable executable)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public List<Executable> getExecutables()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getSuccess()
	{
		//actions run unconditionally
		return true;
	}
	
}
