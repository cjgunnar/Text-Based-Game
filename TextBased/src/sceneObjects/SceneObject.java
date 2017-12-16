package sceneObjects;
import gamelogic.Command;
import gamelogic.Game;
import gamelogic.Request;

//Caden Gunnarson
public interface SceneObject 
{	
	/*
	private String name;
	private String description;
	*/
	
	/*
	SceneObject(String name, String description)
	{
		this.name = name;
		this.description = description;
	}
	*/
	
	//used to execute and output code when given an output
	public abstract void ExecuteCommand (Command command);
	
	//getter and setter methods:
	
	//returns the description
	public abstract String getDescription();
	
	//sets the description
	public abstract void setDescription(String description);
	
	//returns the name
	public abstract String getName();
	
	//sets the name
	public abstract void setName(String name);
	
	//sets game reference
	public abstract void setGame(Game game);
	
	//returns all the things you could call that object
	public abstract String[] getAliases();
	
	//used by XML parser
	public abstract void addAlias(String alias);

	//used by XML parser
	public abstract void addRequest(Request request);
}