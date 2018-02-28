package gamelogic;

/** Outputs the description of its parent object */
public class DescriptionAction extends Action
{
	@Override
	public void run()
	{
		_game.Output(parentObject.getDescription());
	}	
}
