package gamelogic;
//holds data for commands
public class Command 
{

	private String typeOfCommand;
	private String baseObject;
	private String raw;
	
	Command(String typeOfCommand, String baseObject, String raw)
	{
		this.typeOfCommand = typeOfCommand;
		this.baseObject = baseObject;
		this.raw = raw;
	}

	Command()
	{
		typeOfCommand = "";
		baseObject = "";
		raw ="";
	}
	
	public void setRaw (String raw)
	{
		this.raw = raw;
	}
	
	public String getRaw()
	{
		return raw;
	}
	
	public void setBaseObject(String baseObject)
	{
		this.baseObject = baseObject;
	}
	
	public String getBaseObject()
	{
		return baseObject;
	}
	
	/**
	 * @return the typeOfCommand
	 */
	public String getTypeOfCommand() 
	{
		return typeOfCommand;
	}
	
	public void setTypeOfCommand(String typeOfCommand)
	{
		this.typeOfCommand = typeOfCommand;
	}


}