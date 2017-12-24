package sceneObjects;
import gamelogic.Command;
import gamelogic.Game;
import gamelogic.Request;

//Caden Gunnarson
public interface SceneObject 
{
	final static int defaultValue = 0;
	
	//operators
	final static String greaterThan = ">";
	final static String lessThan = "<";
	final static String equalTo = "=";
	
	//add a property to a SceneObject (used by xml parser/level reader)
	//properties are a hashmap of String, Integer
	public abstract void addProperty(String propName, int initValue);
	
	//add a property with default value of 0
	public abstract void addProperty(String propName);
	
	//used to change properties of scene objects
	public abstract void changeProperty(String propName, int value);
	
	//returns the property value of the property
	public abstract int getProperty(String propName);
	
	//checks if the property (propName) is operator (>, <, ==) the value (int)
	public abstract boolean checkProperty(String propName, String operator, int value);
	
	//used to execute and output code when given an output
	public abstract void ExecuteCommand (Command command);
	
	//getter and setter methods:
	
	//sets the id, used by XML parser
	public abstract void setID(int id);
	
	//returns the id
	public abstract int getID();
	
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