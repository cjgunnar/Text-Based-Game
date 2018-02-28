package gamelogic;

import sceneObjects.Exit;

public class ExitAction extends Action
{

	@Override
	public void run()
	{
		// TODO have it do it itself
		Exit parentExit = (Exit)parentObject;
		parentExit.Built_In_Command_UseDoor();
	}

}
