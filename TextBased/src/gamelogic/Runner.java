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
		PlayFromLevelSelector();
	}
	
	/**
	 * Opens the level selection window, then plays the level the use selects
	 */
	public static void PlayFromLevelSelector()
	{
		//display the level selector
		CreateLevelSelectionWindow();
	}
	
	/**
	 * Performs neccesary operations to create level and play game
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
	
	/** Opens a level selection window */
	private static void CreateLevelSelectionWindow()
	{
		LevelSelectionWindow levelSelect = new LevelSelectionWindow();
		levelSelect.setVisible(true);
	}
	
	/**
	 * Creates level from levelFile, then assigns it to game
	 * @param levelFile The name of the XML file in src/Levels/
	 * @param game The game to set the created level reference to
	 * @return the created level (unused)
	 */
	public static Level LoadLevel(String levelFile, Game game)
	{
		boolean debugMode = promptEnableDebugMode();
		
    	//maybe that name can be shortened, and be static-ized
    	LevelConstructorXMLParser reader = new LevelConstructorXMLParser(debugMode);
    	
    	//create the level from the XML file
    	Level level = reader.readLevel(LEVEL_LOCATIONS + levelFile);
    	
		//set game level
		game.setLevel(level);
    	
    	level.InitializeLevel(game);
    	
    	return level;
	}
	
	/**
	 * Shows a Option Dialog about wheter to enable debug mode or not
	 * @return True if debug mode, false if no debug mode
	 */
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
