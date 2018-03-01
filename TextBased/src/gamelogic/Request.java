package gamelogic;

import java.util.ArrayList;
import java.util.List;

import sceneObjects.SceneObject;

public class Request
{
	/** all the executables that will be executed when requested */
	List<Executable> executables;
	
	/** verbs that will activate this request */
	List<String> verbs;
	
	/** exact phrases that will activate this request */
	List<String> exacts;
	
	/** Parent of this request */
	SceneObject parentObject;
	
	/** Reference to the I/O and control class game */
	Game game;
	
	/** Default Constructor */
	public Request()
	{
		executables = new ArrayList<Executable>();
		verbs = new ArrayList<String>();
		exacts = new ArrayList<String>();
		parentObject = null;
	}
	
	/**
	 * Returns true if the request has a verb matching the input
	 * @param inputVerb verb to check
	 * @return boolean if it contains it
	 */
	public boolean hasVerb(String inputVerb)
	{
		for(String verb: verbs)
		{
			if(verb.equalsIgnoreCase(inputVerb))
			{
				return true;
			}
		}
		
		//default
		return false;
	}
	
	/**
	 * Returns true if the request has an exact specified in input
	 * @param inputExact exact phrase to check
	 * @return boolean if it contains it
	 */
	public boolean hasExact(String inputExact)
	{
		for(String exact: exacts)
		{
			if(exact.equalsIgnoreCase(inputExact))
			{
				return true;
			}
		}
		
		//default
		return false;
	}
	
	/**
	 * Executes all the actions it has, and process conditionals
	 * @param game reference to the game
	 * @param parentObject reference to the owner of this request
	 */
	public void ExecuteActions()
	{
		System.out.println("REQUEST: running " + executables.size() + " executables");
		
		//run executables
		for(Executable executable: executables)
		{
			executable.setGame(game);
			executable.setParentObject(parentObject);
			executable.run();
		}
	}

	/**
	 * Sets the parent object of the request
	 * @param parentObject the parent/owner of this object
	 */
	public void setParentObject(SceneObject parentObject)
	{
		this.parentObject = parentObject;
	}
	
	/**
	 * Gets the parent object of the request
	 * @return the parent object
	 */
	public SceneObject getParentObject()
	{
		return parentObject;
	}
	
	/**
	 * Sets a reference to the game class
	 * @param game the game to set a reference to
	 */
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	/**
	 * Returns a reference to the game
	 * @param game The game
	 */
	public Game getGame()
	{
		return game;
	}
	
	/**
	 * Adds a condition to the list of conditions this request has
	 * @param condition the condition to add to the list
	 */
	public void addExecutable(Executable executable)
	{
		executables.add(executable);
	}

	/**
	 * Returns the list of verbs this request will activate to
	 * @return The List of verbs
	 */
	public List<String> getVerbs()
	{
		return verbs;
	}
	
	/**
	 * Returns the list of exact phrases this request will activate to
	 * @return the List of exact phrases
	 */
	public List<String> getExacts()
	{
		return exacts;
	}
	
	/**
	 * @param verb the verb to add to the list
	 */
	public void addVerb(String verb)
	{
		verbs.add(verb);
	}

	/**
	 * @param exact the exact string to add to the list
	 */
	public void addExact(String exact)
	{
		exacts.add(exact);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Request [verbs=" + verbs + ", exacts=" + exacts + ", executables=" + executables
				+ "]";
	}
}
