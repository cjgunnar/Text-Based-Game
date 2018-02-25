package gamelogic;

import java.util.ArrayList;
import java.util.List;

import sceneObjects.SceneObject;

public class Request
{
	/**
	 * all the actions that will be executed when requested, unconditional
	 */
	List<Action> actions = new ArrayList<Action>();
	
	/**
	 * verbs that will activate this request
	 */
	List<String> verbs = new ArrayList<String>();
	
	/**
	 * exact phrases that will activate this request
	 */
	List<String> exacts = new ArrayList<String>();
	
	/**
	 * conditions and their actions
	 */
	List<Condition> conditions = new ArrayList<Condition>();
	
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
		System.out.println("REQUEST: running " + actions.size() + " actions unconditionally");
		
		//run default actions
		for(Action action: actions)
		{
			action.setGame(game);
			action.setParentSceneObject(parentObject);
			action.runAction();
		}
		
		System.out.println("REQUEST: running " + conditions.size() + " conditions");
		
		//run conditionals
		for(Condition condition: conditions)
		{
			condition.setGame(game);
			condition.setParentObject(parentObject);
			condition.runCondition();
		}
	}

	/**
	 * Adds a condition to the list of conditions this request has
	 * @param condition the condition to add to the list
	 */
	public void addCondition(Condition condition)
	{
		if(condition != null)
			conditions.add(condition);
		else
			System.err.println("REQUEST: ERROR: trying to add null condition");
	}
	
	/**
	 * @param action the action to add to the list
	 */
	public void addAction(Action action)
	{
		actions.add(action);
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
		return "Request [actions=" + actions + ", verbs=" + verbs + ", exacts=" + exacts + ", conditions=" + conditions
				+ "]";
	}
}
