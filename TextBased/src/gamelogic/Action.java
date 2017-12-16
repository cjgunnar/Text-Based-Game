package gamelogic;

//actions do things

public class Action
{
	final String OUT = "out";
	
	private String actionType;
	
	private String actionValue;
	
	private Game game;
	
	public void runAction()
	{
		//run action...
		//use game reference to do that
		
		if(actionType.equals(OUT))
		{
			game.Output(actionValue);
		}
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public String getActionType()
	{
		return actionType;
	}
	
	public void setActionType(String actionType)
	{
		this.actionType = actionType;
	}
	
	public String getActionValue()
	{
		return actionValue;
	}
	
	public void setActionValue(String actionValue)
	{
		this.actionValue = actionValue;
	}
}
