package gamelogic;

import java.util.List;

import javax.swing.JFrame;

import sceneObjects.*;

/*
import sceneObjects.Exit;
import sceneObjects.Room;
import sceneObjects.SceneObject;
import sceneObjects.SimpleObject;
*/

//Caden Gunnarson
//Handles the game

@SuppressWarnings("unused")
public class Game
{
	public RoomManager manager;
	private GameFrame frame;
	final int STARTING_ROOM_ID = 1;
	public Level level;
	
	public void Start()
	{
		manager = new RoomManager();
		
		CreateLevelSelectionWindow();
		
		/*
		Testing
		//create bedroom room
		Room bedroom = new Room("bedroom", "A neat looking bedroom with a bed, a table, and a desk.", this);
		
		//create and add objects that go in bedroom
		SceneObject desk = new SimpleObject("desk", "A simple wooden desk. Unorganized papers lie on top", this);
		bedroom.addObject(desk);
		
		//create hallway
		Room hallway = new Room("hallway", "A small hallway. Pictures frames are hung on the walls. Stairs at the end lead down to the main level.", this);
		
		//create and add hallway objects
		SceneObject pictureFrames = new SimpleObject("picture frames", "The pictures show Aunt Jenny playing golf, the first day of school, and the cabin on the lake", this);
		hallway.addObject(pictureFrames);
		
		//add exit to hallway back into bedroom
		Exit hallwayBedroomDoor = new Exit("bedroom door", "A simple white door with a silver knob", bedroom, this);
		hallway.addExit(hallwayBedroomDoor);
		
		//create and add exit for bedroom
		Exit bedroomDoor = new Exit("bedroom door", "A simple white door with a silver knob", hallway, this);
		bedroom.addExit(bedroomDoor);
		
		//set current room to bedroom
		manager.ChangeRoom(bedroom);
		
		//System.out.println(manager.getRoom().FindObjectByName("bedroom door").getDescription());
		
		*/
		

		
		/*
		//ha get it testing room because I am testing manually entering the data instead of using the XML parser... funny...
		Room testRoom = new Room("Testing Room", "The testing room is cold and sterile. The white walls have no exits. One chair sits in the middle", this);
		
		SimpleObject chair = new SimpleObject("chair", "A plain chair", this);
		
		Action action = new Action();
		
		action.setActionType("out");
		action.setActionValue("You take a seat. Your legs are still shaking, but you think you can manage.");
		
		Request request = new Request();
		
		request.addVerb("look at");
		request.addAction(action);
		
		chair.addRequest(request);
		
		testRoom.addObject(chair);
		testRoom.setAllObjectsGame(this);
		
		manager.ChangeRoom(testRoom);
		
		*/
		

		
		//Input("enter hallway");
		
		//Command command = new Command("look at", "chair", "look at chair");
		
		//testRoom.FindObjectByName("chair").ExecuteCommand(command);
		//System.out.println(manager.getRoom().getDescription());
		//System.out.println();
		//System.out.println(manager.getRoom().FindObjectByName("posters").getDescription());
		
		//testing
		//Input("use bedroom door");
		
		//Output(manager.getRoom().FindObjectByName("Desk").getDescription());
		
		//Command command1 = CommandInterpreter.InterpretCommand("use desk");
		//Command command1 = new Command("look at", "desk");
		//Command command1 = new Command("grab", "bedroom door");
		//String object1 = command1.baseObject;
		//System.out.println(object1);
		//manager.getRoom().FindObjectByName("bedroom door").ExecuteCommand(new Command("grab", "bedroom door"));
		//desk.ExecuteCommand(command1);
		//manager.getRoom().FindObjectByName(command1.baseObject).ExecuteCommand(command1);
		
		
		
		//Command command2 = new Command("use", "desk");
		//manager.getRoom().FindObjectByName(command2.baseObject).ExecuteCommand(command2);

		//Command command3 = new Command("use", "bedroom door");
		//manager.getRoom().FindObjectByName(command3.baseObject).ExecuteCommand(command3);
		
		/*
		Command command4 = CommandInterpreter.InterpretCommand("look at desk");
		if(manager.getRoom().FindObjectByName(command4.baseObject) != null)
			manager.getRoom().FindObjectByName(command4.baseObject).ExecuteCommand(command4);
		*/
	}
	
	/** 
	 * Takes in a string from user and performs game logic on it after parsing
	 * @param string from user
	 * @return game logic outputs its own things to UI
	 * */
	public void Input (String input)
	{
		System.out.println("GAME INPUT: " + input);
		
		Command command = CommandInterpreter.InterpretCommand(input);
		
		//if the player is trying to quit the game, close the program
		if(command.getRaw().equalsIgnoreCase("quit") || command.getRaw().equalsIgnoreCase("quit game") || command.getRaw().equals("exit") || command.getRaw().equals("exit game"))
		{
			frame.dispose();
		}
		
		//System.out.println(level.hasRoom("DEBUG: " + level.hasRoom(command.getBaseObject())));
		
		//TODO update enter command for multiple exits to the same room
		
		//if "enter kitchen", search for exits that lead to kitchen
		if(command.getTypeOfCommand() != null && command.getTypeOfCommand().equals("enter") && level.hasRoom(command.getBaseObject()))
		{
			//System.out.println("Attempting to enter room: " + command.getBaseObject());
			
			Room destination = level.FindRoomWithName(command.getBaseObject());
			
			//find exit and execute "use" command
			Exit exit = manager.getRoom().FindExitByDestination(destination);
			
			Command exitCommand = new Command("use", exit.getName(), "use " + exit.getName());
			
			exit.ExecuteCommand(exitCommand);
			
			//end
			return;
		}
		
		//known verb-noun pair
		if(command.getBaseObject() != null && command.getTypeOfCommand() != null && manager.getRoom().FindObjectByName(command.getBaseObject()) != null)
		{
			manager.getRoom().FindObjectByName(command.getBaseObject()).ExecuteCommand(command);
			
			//end
			return;
		}
		
		//try to execute on everything in case raw/exact phrase is used
		//manager.getRoom().ExecuteCommandAll(command);
		
		//TODO improve feedback error
		
		System.out.println("GAME: COMMAND: " + command.getTypeOfCommand() + " \"" + command.getBaseObject() + "\"");
		
		//known verb but unknown noun
		if(command.getBaseObject() != null && command.getTypeOfCommand() != null)
		{
			sendErrorMessage("There is no " + command.getBaseObject() + " in the " + manager.getRoom().getName());
			return;
		}
		
		//otherwise say it didn't understand
		sendErrorMessage("I do not understand that");
	}
	
	/** 
	 * Takes in a string to output to user
	 * @param output String to display
	 * */
	public void Output (String output)
	{
		if(frame != null)
		{
			frame.AddEntry(output);
		}
		else
		{
			System.err.println("ERROR: Frame never created");
		}
		
		System.out.println("GAME OUTPUT: " + output);
	}
	
	public void sendErrorMessage(String msg)
	{
		frame.setErrorMessage(msg);
	}
	
	public void LoadLevel(String levelName)
	{
		level = new Level();
		level.LoadLevel("src/Levels/" + levelName, this);
		
		manager.ChangeRoom(level.FindRoomWithID(STARTING_ROOM_ID));
		
		CreateGameFrame();
		
		Output(level.getProlog());
	}
	
	private void CreateLevelSelectionWindow()
	{
		LevelSelectionWindow levelSelect = new LevelSelectionWindow(this);
		levelSelect.setVisible(true);
	}
	
	//@SuppressWarnings("unused")
	private void CreateGameFrame ()
	{
		//open up the game panel
		frame = new GameFrame(this);
		frame.setVisible(true);
	}
	

}
