package gamelogic;

import javax.swing.JOptionPane;

//starts the game

/**
 * Runner for TextBased game
 * @author cjgunnar
 *
 */
public class Runner 
{
	static final String LEVEL_LOCATIONS = "src/Levels/";
	
	public static void main(String[] args)
	{
		//display the level selector
		CreateLevelSelectionWindow();
	}
	
	/**
	 * 
	 * @param levelPath
	 */
	public static void PlayLevel(String levelPath)
	{
		//create game
		Game game = new Game();
		
		//create level from XML
		LoadLevel(levelPath, game);
		
		//start game
		game.Start();
	}
	
	private static void CreateLevelSelectionWindow()
	{
		LevelSelectionWindow levelSelect = new LevelSelectionWindow();
		levelSelect.setVisible(true);
	}
	
	public static Level LoadLevel(String levelFile, Game game)
	{
		boolean debugMode = promptEnableDebugMode();
		
    	//maybe that name can be shortened, and be static-ized
    	LevelConstructorXMLParser reader = new LevelConstructorXMLParser(debugMode);
    	
    	//create the level from the XML file
    	Level level = reader.readLevel(LEVEL_LOCATIONS + levelFile);
    	
		//set game level
		game.setLevel(level);
    	
    	level.InitializeRooms(game);
    	
    	return level;
	}
	
	public static boolean promptEnableDebugMode()
	{
		int result = JOptionPane.showOptionDialog(null, "Enable Debug-Mode?", "Debug Mode", 
												  JOptionPane.YES_NO_OPTION,
												  JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(result == JOptionPane.OK_OPTION)
			return true;
		else
			return false;
	}
	
}
