package gamelogic;

import java.util.ArrayList;
import java.util.List;

import sceneObjects.SceneObject;

public class Request
{
	/** all the executables that will be executed when requested */
	List<Executable> executables = new ArrayList<Executable>();
	
	/** verbs that will activate this request */
	List<String> verbs = new ArrayList<String>();
	
	/** exact phrases that will activate this request */
	List<String> exacts = new ArrayList<String>();
	
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
	public void ExecuteActions(Game game, SceneObject parentObject)
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
	 * Adds a condition to the list of conditions this request has
	 * @param condition the condition to add to the list
	 */
	public void addExecutable(Executable executable)
	{
		executables.add(executable);
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
