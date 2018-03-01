package gamelogic;

/** Outputs text to the game */
public class OutAction extends Action
{
	/** Text to output */
	private String textOut;
	
	/** Default Constructor */
	public OutAction() 
	{
		super();
		textOut = "Text out";
	}
	
	/**
	 * Constructor with text out specified
	 * @param textOut Text to output when ran
	 */
	public OutAction(String textOut)
	{
		this.textOut = textOut;
	}
	
	/** Outputs text to the game */
	@Override
	public void run()
	{
		if(_game != null)
			_game.Output(textOut);
		else
			System.out.println("OUT ACTION: ERROR: null game reference");
	}
	
	@Override
	public String toString()
	{
		return "OUT: " + textOut;
	}
}
