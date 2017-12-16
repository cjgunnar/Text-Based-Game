package gamelogic;
//Caden Gunnarson

public class CommandInterpreter 
{
	public static Command InterpretCommand(String commandString)
	{
		//change to lower case
		commandString = commandString.toLowerCase();
		
		//split string into words
		String[] inputs = commandString.split("\\s");
		
		Command command = new Command(null, null, commandString);
		
		//check for single word commands
		/*
		switch (inputs[0])
		{
		case "use":
			return InterpretUse(inputs);
		default:
			return null;
		}
		*/
		
		if (inputs[0].equals("use"))
		{
			command = InterpretVerbName(command, inputs, "use");
		}
		else if (inputs[0].equals("grab"))
		{
			command = InterpretVerbName(command, inputs, "grab");
		}
		else if (inputs[0].equals("enter"))
		{
			command = InterpretVerbName(command, inputs, "enter");
		}
		else if (inputs[0].equals("look") && inputs[1].equals("at"))
		{
			if(inputs.length == 3)
			{
				command.setBaseObject(inputs[2]);
				command.setTypeOfCommand("look at");
			}
			
			String object = "";
			
			for(int i = 2; i < inputs.length; i++) //start at position 2
			{
				object += inputs[i];
				if(i < inputs.length - 1)
				{
					object += " ";
				}
			}
			
			//System.out.println("Created \"look at\" command of " + object);
			command.setBaseObject(object);
			command.setTypeOfCommand("look at");
		}

		return command;
		
		//System.out.print("\"" + inputs[1] + "\"");
	}
	
	//parse verb-name command
	private static Command InterpretVerbName(Command command, String[] inputs, String commandType)
	{
		command.setTypeOfCommand(commandType);
		
		if (inputs.length == 2)
		{
			command.setBaseObject(inputs[1]);
		}
		else
		{
			String object = "";
			for(int i = 1; i < inputs.length; i++)
			{
				object += inputs[i];
				if (i < inputs.length - 1)
				{
					object += " ";
				}
			}
			
			command.setBaseObject(object);
		}

		return command;
	}

}
