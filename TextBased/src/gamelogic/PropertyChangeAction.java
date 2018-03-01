package gamelogic;

import sceneObjects.SceneObject;

public class PropertyChangeAction extends Action
{
	/** Target of the action, represnted as ID */
	private int target;
	
	/** Name of the property of the target to change */
	private String propertyName;
	
	/** New value to set the property of target to */
	private int newValue;
	
	/** Default Constructor */
	public PropertyChangeAction()
	{
		super();
		target = 0;
		propertyName = "unset";
		newValue = 0;
	}
	
	@Override
	public void run()
	{
		runPropertyChange();
	}
	
	/** Execute the property change */
	private void runPropertyChange()
	{
		System.out.println("ACTION: changing property " + propertyName + " to " + newValue);
		
		SceneObject targetObj;
		if(target == 0) //parent
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
		else if(target == Level.SCENARIO_ID) //scenario
		{
			//if it is changing a scenario property, change it then exit method
			_game.level.changeScenarioProperty(propertyName, newValue);
			return;
		}
		else //find the target
		{
			//find the object with the target ID
			targetObj = _game.level.FindObjectWithID(target);
		}
		
		if(targetObj != null)
			//change the property
			targetObj.changeProperty(propertyName, newValue);
		else
			System.err.println("ACTION: ERROR: no target object found with ID: " + target);
	}
	
	/**
	 * Getter for the property name (used only with type property change, otherwise will output error)
	 * @return the propertyName
	 */
	public String getPropertyName()
	{
		return propertyName;
	}
	
	/**
	 * Sets the name of the property to change from the target
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName)
	{
		this.propertyName = propertyName;
	}
	
	/**
	 * Sets the target of the property change
	 * @param target ID of the target
	 */
	public void setTarget(int target)
	{
		this.target = target;
	}

	/**
	 * Returns the target of this property change
	 * @return the ID of the target
	 */
	public int getTarget()
	{
		return target;
	}
	
	/**
	 * Set what the new value will be
	 * @param newValue The new value
	 */
	public void setValue(int newValue)
	{
		this.newValue = newValue;
	}
	
	@Override
	public String toString()
	{
		return "Change property " + propertyName + " from ID#" + target + "to " + newValue;
	}
	
}
