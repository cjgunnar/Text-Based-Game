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

/**
 * Handles game logic and I/O
 * @author cjgunnar
 *
 */
@SuppressWarnings("unused")
public class Game
{
	public RoomManager manager;
	private GameFrame frame;
	final int STARTING_ROOM_ID = 1;
	public Level level;
	
	public void Start()
	{
		CreateGameFrame();
		
		//output prolog
		Output(level.getProlog());
	}
	
	/** 
	 * Takes in a string from user and performs game logic on it after parsing
	 * @param string from user
	 * @return game logic outputs its own things to UI
	 * */
	public void Input (String input)
	{
		System.out.println("GAME INPUT: " + input);
		
		RunInput(input);
		
		level.checkEndStates();
	}
	
	public void RunInput(String input)
	{
		Command command = CommandInterpreter.InterpretCommand(input);
		
		//if the player is trying to quit the game, close the program
		if(command.getRaw().equalsIgnoreCase("quit") || command.getRaw().equalsIgnoreCase("quit game") || command.getRaw().equalsIgnoreCase("exit") || command.getRaw().equalsIgnoreCase("exit game"))
		{
			frame.dispose();
		}
		
		//try scenario exact phrase matching
		if(level.scenario.hasRequest(command.getRaw()))
		{
			//if it did, stop searching
			return;
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
			
			if(exit != null)
			{
				Command exitCommand = new Command("use", exit.getName(), "use " + exit.getName());
				
				exit.ExecuteCommand(exitCommand);
				
				//end
				return;
			}
			
			//otherwise no exit leading to that room was found, so continue
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
		
		//known verb but unknown verb
		if(command.getBaseObject() != null && manager.getRoom().FindObjectByName(command.getBaseObject()) != null)
		{
			sendErrorMessage("What would you like to do with " + command.getBaseObject() + "?");
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
	
	/**
	 * called from LevelSelectionWindow
	 * @param levelName XML file in src/Levels/ to load
	 */
	public void setLevel(Level level)
	{
		//set level
		this.level = level;
		
		//initialize manager
		manager = new RoomManager();
		
		//initialize scenario game reference
		level.scenario.setGame(this);
		
		//set scenario
		manager.scenario = level.scenario;
		
		//set the starting room
		manager.ChangeRoom(level.FindRoomWithID(STARTING_ROOM_ID));
	}
	
	private void CreateGameFrame ()
	{
		//open up the game panel
		frame = new GameFrame(this);
		frame.setVisible(true);
	}
	

}
