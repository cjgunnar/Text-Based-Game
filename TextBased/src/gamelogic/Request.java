package gamelogic;

import java.util.ArrayList;
import java.util.List;

public class Request
{
	//all the actions that will be executed when requested
	List<Action> actions = new ArrayList<Action>();
	
	//verbs that will activate this request
	List<String> verbs = new ArrayList<String>();
	
	//exact phrases that will activate this request
	List<String> exacts = new ArrayList<String>();
	
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
	
	public void ExecuteActions(Game game)
	{
		for(Action action: actions)
		{
			action.setGame(game);
			action.runAction();
		}
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
