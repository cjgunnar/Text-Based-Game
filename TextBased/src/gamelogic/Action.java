package gamelogic;

import java.util.ArrayList;
import java.util.List;

import sceneObjects.SceneObject;

//actions do things

public abstract class Action implements Executable
{		
	/** The owner/parent of the action */
	protected SceneObject parentObject;
	
	/** reference to the game */
	protected Game _game;
	
	public abstract void run();
	
	public void setParentObject(SceneObject parentObject)
	{
		this.parentObject = parentObject;
	}
	
	public void setGame(Game game)
	{
		this._game = game;
	}

	@Override
	public void addExecutable(Executable executable)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public List<Executable> getExecutables()
	{
		return new ArrayList<Executable>();
	}

	@Override
	public boolean getSuccess()
	{
		//actions run unconditionally
		return true;
	}
	
}
