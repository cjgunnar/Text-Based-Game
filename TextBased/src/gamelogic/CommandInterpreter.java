package gamelogic;

/**
 * Reads and interprets strings as commands on objects
 * @author cjgunnar
 *
 */
public class CommandInterpreter
{
	//all the verbs the game recognizes you can do, and their synonyms
	
	static String[] examine = {"examine", "look", "see", "observe", "watch", "look at"};
	
	static String[] eat = {"eat", "consume", "swallow", "drink", "ingest", "bite"};
	
	static String[] take = {"take", "grab", "collect", "steal", "pick up"};
	
	static String[] attack = {"attack", "hurt", "stab", "hit", "beat", "strike", "damage", "break"};
	
	static String[] sell = {"sell", "barter", "trade"};
	
	static String[] cover = {"cover", "cover up", "conceal", "hide", "protect", "hide behind"};
	
	static String[] uncover = {"uncover", "show", "get out from behind"};
	
	static String[] search = {"search", "go through", "look through", "search through", "look for"};
	
	static String[] use = {"use", "go out", "go down", "enable"};
	
	static String[] enter = {"enter", "go", "walk through", "walk into", "go to", "walk into", "go into", "walk to"};
	
	static String[] exit = {"exit", "leave", "go out", "go out of", "get out of"};
	
	static String[] equip = {"equip", "put on", "hold", "wear"};
	
	static String[] unequip = {"unequip", "take off", "put down", "put away"};
	
	static String[] talk = {"talk", "talk to", "interrogate", "chat with", "talk with"};
	
	static String[] disconnect = {"disconnect", "unplug"};
	
	static String[] connect = {"connect", "plug in", "hook up"};
	
	static String[] lock = {"lock", "latch", "bolt", "seal", "secure"};
	
	static String[] unlock = {"unlock", "unlatch", "unbolt", "unseal"};
	
	static String[][] allVerbs = {examine, eat, take, attack, sell, cover, uncover, 
			search, use, enter, exit, equip, unequip, talk, disconnect, connect, lock, unlock};
	
	/**
	 * Attempts to parse String input as a verb and an object
	 * @param commandString String input
	 * @return A command (verb, noun, raw)
	 */
	public static Command InterpretCommand(String commandString)
	{
		//change to lower case
		commandString = commandString.toLowerCase();
		
		//remove " the " and replace with a space
		if(commandString.contains(" the "))
		{
			//System.out.println("COMMAND INTERPRETER: removing 'the' from: " + commandString);
			commandString = commandString.replace(" the ", " ");
			//System.out.println("COMMAND INTERPRETER: string is now: " + commandString);
		}
		
		//split string into words
		String[] inputs = commandString.split("\\s");
		
		Command command = new Command(null, null, commandString);
		
		String identifiedVerb = null;
		
		String actualVerb = null;
		
		//if it is only one word, that word is likely a verb
		//ex "run" is likely, while "chair" is not
		if(inputs.length == 1)
		{
			command.setTypeOfCommand(inputs[0]);
			return command;
		}
		
		//check if it is a known verb
		for(String[] verbList : allVerbs)
		{
			for(String verb : verbList)
			{
				int verbWordCount = countWords(verb);
				
				//System.out.println("COMMAND INTERPRETER: current verb=" + verb + ", word count=" + verbWordCount);
				
				if(inputs.length > verbWordCount)
				{
					String constructVerb = "";
					for(int i = 0; i < verbWordCount; i++)
					{
						constructVerb += inputs[i];
						if(i < verbWordCount - 1)
						{
							constructVerb += " ";
						}
					}
					
					//System.out.println("COMMAND INTERPRETER: constructVerb=" + constructVerb);
					
					if(constructVerb.equals(verb))
					{
						identifiedVerb = verbList[0];
						actualVerb = verb;
						System.out.println("COMMAND INTERPRETER: recognized verb: " + verb + ", actual: " + actualVerb);
					}
				}
				
				/*
				//try one word match
				if(inputs[0].equalsIgnoreCase(verb))
				{
					//the 0 (first) one is the main one, the rest are synonyms
					identifiedVerb = verbList[0];
					actualVerb = verb;
					System.out.println("COMMAND INTERPRETER: recognized verb: " + verb + ", actual: " + actualVerb);
				}
				
				//try two word match (must be at least 3 words)
				else if(inputs.length >= 3)
				{
					String twoWord = inputs[0] + " " + inputs[1];
					if(twoWord.equalsIgnoreCase(verb))
					{
						actualVerb = twoWord;
						identifiedVerb = verbList[0];
						System.out.println("COMMAND INTERPRETER: recognized verb: " + identifiedVerb + ", actual: " + actualVerb);
					}
				}
				*/
			}
		}
		
		if(identifiedVerb != null)
		{
			//find out how many words the verb is, ex: "look at" is 2 and "observe" is 1
			int actualVerbLen = countWords(actualVerb);
			
			System.out.println("COMMAND INTERPRETER: actual verb has a word length of " + actualVerbLen);
			
			String directObject = "";
			
			for(int i = actualVerbLen; i < inputs.length; i++)
			{
				directObject += inputs[i];
				
				//add space to the end if it is not the last word
				if(i < inputs.length - 1)
				{
					directObject += " ";
				}
			}
			
			//now we can set and return the command
			command.setTypeOfCommand(identifiedVerb);
			
			System.out.println("COMMAND INTERPRETER: base object set to: " + directObject);
			command.setBaseObject(directObject);
			
			return command;
		}
		else
		{
			System.out.println("COMMAND INTERPRETER: WARNING: unrecongnized verb, guessing it is: " + inputs[0]);
			
			//take a guess that it is a one word command on an object
			command.setTypeOfCommand(inputs[0]);
			
			String baseObject = "";
			
			for(int i = 1; i < inputs.length; i++)
			{
				baseObject += inputs[i];
				if(i < inputs.length - 1)
					baseObject += " ";
			}
			
			//set the guess of a base object
			command.setBaseObject(baseObject);
			
			return command;
		}
		
		/*
		if (inputs[0].equals("use"))
		{
			command = InterpretVerbName(command, inputs, "use");
		}
		else if (inputs[0].equals("close"))
		{
			command = InterpretVerbName(command, inputs, "close");
		}
		else if (inputs[0].equals("open"))
		{
			command = InterpretVerbName(command, inputs, "open");
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
		}*/

		//return command
		
		//System.out.print("\"" + inputs[1] + "\"");
	}
	
	private static int countWords(String input)
	{
		int count = 0;
		
		//count the words by counting the spaces
		for(int i = 0; i < input.length(); i++)
		{
			char currentCharacter = input.charAt(i);
			
			if(currentCharacter == ' ')
			{
				count++;
			}
		}
		
		//number of spaces + 1 to get words
		count++;
		
		return count;
	}
	
	//parse verb-name command
	/*private static Command InterpretVerbName(Command command, String[] inputs, String commandType)
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
	}*/

}
