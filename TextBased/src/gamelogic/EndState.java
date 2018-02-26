package gamelogic;

import java.util.ArrayList;

/** Can trigger the end of the game */
public class EndState
{
	/** ID used to specify this ending (EndState) */
	int ID;
	
	/** List of triggers that can activate this end state */
	ArrayList<EndStateTrigger> triggers = new ArrayList<EndStateTrigger>();
	
	/** Executables to run when game over */
	ArrayList<Executable> executables = new ArrayList<Executable>();
	
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
	
	/** runs actions, then conditions under it and brings up game over screen */
	public void TriggerEndState()
	{
		//run all executables actions
		for(Executable executable: executables)
		{
			executable.run();
		}
		
		System.out.println("END STATE: triggered!");
		_game.Output("");
		_game.Output("THE END");
		
		_game.EndGame();
	}
	
	/**
	 * Initializes Triggers, Conditions, and Actions with a game reference
	 * @param game The game reference to set
	 */
	public void Initialize(Game game)
	{
		for(EndStateTrigger trigger: triggers)
		{
			trigger.set_game(game);
		}
		
		for(Executable executable: executables)
		{
			executable.setGame(game);
		}
	}
	
	/**
	 * Adds a trigger to ending, used by level constructor
	 * @param trigger
	 */
	public void addTrigger(EndStateTrigger trigger)
	{
		triggers.add(trigger);
	}
	
	/**
	 * Adds executable to this ending to be executed
	 * @param executable The executable to add
	 */
	public void addExecutable(Executable executable)
	{
		executables.add(executable);
	}
	
	public int getID()
	{
		return ID;
	}
	
	/**
	 * Set the ID of this ending
	 * @param id The id to set
	 */
	public void setID(int id)
	{
		ID = id;
	}
	
	/**
	 * @param _game the _game to set
	 */
	public void set_game(Game _game)
	{
		this._game = _game;
	}
	
}
