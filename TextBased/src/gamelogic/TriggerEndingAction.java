package gamelogic;

/** Triggers an ending */
public class TriggerEndingAction extends Action
{
	/** ID of the ending to trigger */
	int endingID;
	
	@Override
	public void run()
	{
		TriggerEnding();
	}
	
	/** Triggers the target ending */
	private void TriggerEnding()
	{	
		System.out.println("ACTION: triggering ending: " + endingID);
		_game.level.TriggerEndState(endingID);
	}
}
