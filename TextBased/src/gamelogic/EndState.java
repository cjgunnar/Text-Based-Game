package gamelogic;

import java.util.ArrayList;

/** Can trigger the end of the game */
public class EndState
{
	/** List of triggers that can activate this end state */
	ArrayList<EndStateTrigger> triggers = new ArrayList<EndStateTrigger>();
	
	/** Conditions to run when game over */
	ArrayList<Condition> conditions = new ArrayList<Condition>();
	
	/** Actions to run when game over */
	ArrayList<Action> actions = new ArrayList<Action>();
	
	/** Allow communication with game logic to end game */
	Game _game;

	/** Default Constructor */
	public EndState() {};
	
	/**
	 * Checks if one of its triggers has fired, and if it has will end game
	 */
	public void checkEndState()
	{
		for(EndStateTrigger trigger: triggers)
		{
			if(trigger.hasTriggered())
			{
				TriggerEndState();
				return; //only need to find one, don't want to trigger multiple times
			}
		}
	}
	
	/** runs actions/conditions under it and brings up game over screen */
	public void TriggerEndState()
	{
		//run all default actions
		for(Action action: actions)
		{
			action.runAction();
		}
		
		//run conditions
		for(Condition condition: conditions)
		{
			condition.runCondition();
		}
		
		//TODO stop input from player, show EXIT button
		
	}
	
	public void addAction(Action action)
	{
		actions.add(action);
	}
	
	public void addCondition(Condition condition)
	{
		conditions.add(condition);
	}
	
	/**
	 * @param _game the _game to set
	 */
	public void set_game(Game _game)
	{
		this._game = _game;
	}
	
}
