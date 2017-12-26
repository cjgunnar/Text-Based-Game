package sceneObjects;
import gamelogic.Command;
import gamelogic.Game;
import gamelogic.Request;

/**
 * Interface for all the objects that can be in the game, including special ones
 * @author cjgunnar
 *
 */
public interface SceneObject 
{
	final static int defaultValue = 0;
	
	//operators
	final static String greaterThan = ">";
	final static String lessThan = "<";
	final static String equalTo = "=";
	
	/**
	 * add a property to a SceneObject (used by xml parser/level reader), properties are a hashmap of String, Integer
	 * @param propName name of the property
	 * @param initValue initial value to set
	 */
	public abstract void addProperty(String propName, int initValue);
	
	/**
	 * add a property with default value of 0
	 * @param propName name of the property
	 */
	public abstract void addProperty(String propName);
	
	/**
	 * used to change properties of scene objects
	 * @param propName name of the property
	 * @param value new value to set to
	 */
	public abstract void changeProperty(String propName, int value);
	
	/**
	 * returns the property value of the property
	 * @param propName name of the property
	 * @return int value of the property
	 */
	public abstract int getProperty(String propName);
	
	/**
	 * checks if the property (propName) is operator (>, <, ==) the value (int)
	 * @param propName name of the property
	 * @param operator check to make
	 * @param value integer value to check against
	 * @return
	 */
	public abstract boolean checkProperty(String propName, String operator, int value);
	
	/**
	 * used to execute and output code when given an output
	 * @param command the command to execute
	 */
	public abstract void ExecuteCommand (Command command);
	
	//getter and setter methods:
	
	/**
	 * sets the id, used by XML parser
	 * @param id
	 */
	public abstract void setID(int id);
	
	/**
	 * returns the id of the SceneObject
	 * @return
	 */
	public abstract int getID();
	
	/**
	 * returns the description
	 * @return
	 */
	public abstract String getDescription();
	
	/**
	 * sets the description
	 * @param description
	 */
	public abstract void setDescription(String description);
	
	/**
	 * returns the name
	 * @return
	 */
	public abstract String getName();
	
	/**
	 * sets the name
	 * @param name
	 */
	public abstract void setName(String name);
	
	/**
	 * sets game reference
	 * @param game
	 */
	public abstract void setGame(Game game);
	
	/**
	 * returns all the things you could call that object
	 * @return
	 */
	public abstract String[] getAliases();
	
	/**
	 * used by XML parser
	 * @param alias alias to add
	 */
	public abstract void addAlias(String alias);

	/**
	 * used by XML parser
	 * @param request request to add
	 */
	public abstract void addRequest(Request request);
}
