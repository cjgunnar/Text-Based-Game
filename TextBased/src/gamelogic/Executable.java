package gamelogic;

import java.util.List;

import sceneObjects.SceneObject;

public interface Executable
{
	/** Run this executable */
	public void run();
	
	/**
	 * Add a nested executable
	 * @param executable Executable to nest
	 */
	public void addExecutable(Executable executable);
	
	/** Returns a list of the nested executables */
	public List<Executable> getExecutables();
	
	public boolean getSuccess();
	
	public void setParentObject(SceneObject obj);
	
	public void setGame(Game game);
}
