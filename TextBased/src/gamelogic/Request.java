package gamelogic;

import java.util.ArrayList;
import java.util.List;

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
	 */
	public void ExecuteActions(Game game)
	{
		//run default actions
		for(Action action: actions)
		{
			action.setGame(game);
			action.runAction();
		}
		
		//run conditionals
		for(Condition condition: conditions)
		{
			condition.setGame(game);
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
}
