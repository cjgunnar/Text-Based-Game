package sceneObjects;

import gamelogic.Game;

/**
 * An exit leads to another room, holds destination in destination property. Has action change_room that will make it change the room
 * @author cjgunnar
 *
 */
public class Exit extends SimpleObject
{
	/**
	 * what room this exit leads to
	 */
	Room entranceTo;
	
	//ID of the room it is the entrance to
	int entranceToID;
	
	/**
	 * Exit specific property used to tell which room this exit leads to
	 */
	private String DESTINATION = "destination";
	
	/**
	 * Full constructor for an exit
	 * @param name name of the exit
	 * @param description description of the exit
	 * @param entranceTo room where this exit leads to
	 * @param game reference to game
	 */
	public Exit(String name, String description, Room entranceTo, Game game)
	{
		super(name, description, game);
		addProperty(DESTINATION);
	}
	
	/**
	 * default constructor
	 */
	public Exit() 
	{
		super();
		//create exit specific default properties
		addProperty(DESTINATION);
	}
	
	public void Built_In_Command_UseDoor()
	{
		if(entranceToID == 0)
		{
			System.out.println(name.toUpperCase() + " leads to nowhere");

			//action out should be specified, as internal logic will do nothing
			return;
		}
		else
		{
			game.manager.ChangeRoom(entranceTo);
			game.Output(game.manager.getRoom().getDescription());
		}
	}
	
	public Room getEntranceTo() 
	{
		return entranceTo;
	}

	public void setEntranceTo(Room entranceTo)
	{
		this.entranceTo = entranceTo;
		
	}
	
	//property related methods
	@Override
	public void changeProperty(String propName, int value)
	{
		if(!properties.containsKey(propName))
		{
			System.out.println(name.toUpperCase() + ": does not contain property " + propName + ", creating it with default value...");
			addProperty(propName);
		}
		
		System.out.println(name.toUpperCase() + ": property " + propName + " changed from " + properties.get(propName) + " to " + value);
		properties.put(propName, value);
	
		//exit specific property "destination"
		if(propName.equalsIgnoreCase(DESTINATION))
		{
			System.out.println(name.toUpperCase() + ": changing destination to ID " + value);
			entranceToID = value;
			if(game != null && game.level != null)
				InitializeEntranceToByID();
			else
				System.err.println(name.toUpperCase() + ": ERROR: null game or level reference");
		}
	}
	
	//exit related methods
	public void setEntranceTo(int id)
	{
		entranceToID = id;
		if(game != null && game.level != null)
		{
			//if the game and level have been set up, initialize the new entranceTo reference
			InitializeEntranceToByID();
		}
	}
	
	/**
	 * Using the number ID of the room, find and initialize the Room reference (entranceTo)
	 */
	public void InitializeEntranceToByID()
	{
		//System.out.println("Initializing exit " + name);
		if(entranceToID == 0)
		{
			System.out.println(name.toUpperCase() + ": WARNING: UNSET ID FOR " + name + ", this exit leads nowhere");
			return;
		}
		if(game == null || game.level == null)
			System.out.println("ERROR: null game or game.level reference");
		//System.out.println("Init: looking for room with ID: " + entranceToID);
		//System.out.println("Linking " + name + " to " + game.level.FindRoomWithID(entranceToID).getName());
		entranceTo = game.level.FindRoomWithID(entranceToID);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		final int MAX_DESCRIPTION_LENGTH = 10;
		
		//couldn't help myself had to use a ternary operator
		String modifiedDescription = description != null ? description : "NULL";
		if(description.length() > MAX_DESCRIPTION_LENGTH)
		{
			modifiedDescription = description.substring(0, MAX_DESCRIPTION_LENGTH) + "...";
		}
		
		return "Exit [ID=" + ID + ", name=" + name + ", description=" + modifiedDescription + ", aliases=" + aliases + ", requests=" + requests
				+ ", entranceTo=" + entranceTo + ", entranceToID=" + entranceToID + ", properties="
				+ properties + "]";
	}
	
}
