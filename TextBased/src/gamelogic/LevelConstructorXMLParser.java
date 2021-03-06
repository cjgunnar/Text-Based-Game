package gamelogic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import sceneObjects.Exit;
import sceneObjects.Room;
import sceneObjects.SceneObject;
import sceneObjects.SimpleObject;

/**
 * Creates the rooms and objects by reading the XML File
 * @author cjgunnar
 *
 */
public class LevelConstructorXMLParser
{
	static final String LEVEL = "level";
	static final String PROLOG = "prolog";
	static final String SCENARIO = "scenario";
	
	static final String END_STATES = "end-states";
	static final String END_STATE = "end-state";
	static final String TRIGGER = "trigger";
	static final String OPERATOR = "operator";
	
	static final String ID = "id";
	static final String NAME = "name";
	static final String DESCRIPTION = "description";
	static final String OBJECTS = "objects";
	static final String ALIASES = "aliases";
	static final String ALIAS = "alias";
	static final String PROPERTIES = "properties";
	static final String PROPERTY = "property";
	
	static final String ROOM = "room";
	
	static final String SIMPLE_OBJECT = "simple_object";
	
	static final String EXIT = "exit";
	static final String EXITS = "exits";
	static final String ENTRANCE_TO = "entranceTo";
	
	static final String REQUESTS = "requests";
	static final String REQUEST = "request";
	
	static final String EXECUTABLES = "executables";
	
	static final String EXECUTABLE_GROUP = "executable_group";
	static final String MAX_EXECUTED = "max_executed";
	
	static final String CONDITION = "condition";
	static final String PASS = "pass";
	static final String FAIL = "fail";
	static final String VALUE = "value";
	
	//static final String ACTIONS = "actions";
	static final String ACTION = "action";
	
	static final String INPUT = "input";
	
	static final String TYPE = "type";
	static final String OUT = "out";
	static final String PROPERTY_CHANGE = "property_change";
	
	static final String TARGET = "target";
	static final String PROPERTY_NAME = "property_name";
	
	static final String VERB = "verb";
	static final String EXACT = "exact";
	
	static boolean debugMode = true;
	
	String levelFile;

	//the iterator
	XMLEventReader eventReader;

	//the current event the iterator is on
	//placed here so helper methods have access
	XMLEvent event;
	
	/**
	 * From XML file, returns a Level object that can be played
	 * @param levelFile Name of the file (including .xml) to read from src/Levels
	 * @return read Level
	 */
	public Level readLevel(String levelFile)
	{
		//set the file to load
		this.levelFile = levelFile;
	
		//create a room to act as the placeholder to use setters on
		Level level = new Level();
		
		try
		{
			//create the inputfactory to create the event reader
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	
			//create the input stream to create the event reader
			InputStream in = new FileInputStream(levelFile);
	
			//finally create the event reader
			eventReader = inputFactory.createXMLEventReader(in);
	
			//begin reading the XML File
			while(eventReader.hasNext())
			{	
				//set the event of the current iteration
				event = eventReader.nextEvent();
	
				//returns true if this is the start of a new element ex: <room>
				if(event.isStartElement())
				{
					StartElement startElement = event.asStartElement();
					
					String elementName = getStartElementName();
	
					String eventData = getEventData();
	
					if(eventData != null)
						debugLog("LEVEL: element=" + elementName + ", value=" + eventData);
					else
						debugLog("LEVEL: element=" + elementName + ", value=null");
					
					//opening <room> element
					if(elementName.equals(ROOM))
					{
						debugLog("OPENED ROOM");
	
						int id = 0;
						
						//this code will set attributes (the data in the start of the element ex: <room id="1">)
	
						//get all the attributes in the element
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();
	
						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();
	
							debugLog("CURRENT ATTRIBUTE: " + attribute.getName().getLocalPart());
	
							//check for the attributes we are looking for and set them
							if(attribute.getName().getLocalPart().equalsIgnoreCase(ID))
							{
								debugLog("VALUE: " + attribute.getValue());
								try
								{
									id = Integer.parseInt(attribute.getValue());
								}
								catch (NumberFormatException e)
								{
									System.out.println("LEVEL READER ERROR: ROOM: non-int value as ID attribute");
								}
	
							}
							else
							{
								debugLog("ROOM: WARNING: unrecognized attribute: " + attribute.getName().getLocalPart());
							}
						}
						
						Room room = readRoom();
						debugLog("ROOM: id=" + id);
						room.setID(id);
						level.addRoom(room);
					}
					
					//prolog gets outputed at the beginning of the game
					else if (elementName.equals(PROLOG))
					{
						debugLog("LEVEL: add prolog: " + eventData);
						level.setProlog(eventData);
					}
					
					//read scenario (global property and command storage)
					else if(elementName.equals(SCENARIO))
					{
						debugLog("LEVEL: add scenario");
						//read scenario
						Scenario scenario = readScenario();
						level.setScenario(scenario);
					}
					
					//read end-states or endings of the game
					else if(elementName.equals(END_STATES))
					{
						List<EndState> endings = readEndStates();
						for(EndState ending: endings)
							level.addEndState(ending);
					}
					
					else if(elementName.equals(LEVEL)) {debugLog("LEVEL STARTING");}
					else
					{
						System.out.println("LEVEL READER ERROR: LEVEL: unknown elementName " + elementName);
					}
	
				}
	
				//if this is an ending element ex: </room>
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();
	
					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();
	
					//debugLog("CLOSING ELEMENT: " + endElementName);
	
					//closing </level> element at very end of XML document
					if(endElementName.equals(LEVEL))
					{
						//return the rooms
						return level;
					}
				}
	
			}
	
		}
		//If file not found, display error message to console
		catch(FileNotFoundException e)
		{
			System.out.println("LEVEL READER ERROR: Did not find file: " + levelFile);
			return null;
		}
		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
	
		//the rooms should be returned by the ending tag level before this point
		System.out.println("ERROR: reached end of document without closing level element");
		return level;
	}

	private Room readRoom()
	{
		//create a room to act as the placeholder to use setters on
		Room room = new Room();
		
		try
		{
			//begin reading the XML File
			while(eventReader.hasNext())
			{	
				//returns true if this is the start of a new element ex: <room>
				if(event.isStartElement())
				{
					StartElement startElement = event.asStartElement();
					
					String elementName = getStartElementName();

					String eventData = getEventData();

					if(eventData != null && !eventData.equals(""))
						debugLog("ROOM: element=" + elementName + ", value=" + eventData);
					else
						debugLog("ROOM: element=" + elementName + ", value=null");
					
					//opening <room> element
					if(elementName.equals(ROOM))
					{
						debugLog("ROOM: OPENED ROOM");
						room = new Room();

						//this code will set attributes (the data in the start of the element ex: <room id="1">)

						//get all the attributes in the element
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();

						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							debugLog("CURRENT ATTRIBUTE: " + attribute.getName().getLocalPart());

							//check for the attributes we are looking for and set them
							if(attribute.getName().getLocalPart().toString().equals(ID))
							{
								debugLog("VALUE: " + attribute.getValue());
								try
								{
									room.setID(Integer.parseInt(attribute.getValue()));
								}
								catch (NumberFormatException e)
								{
									System.out.println("LEVEL READER ERROR: ROOM: non-int value as ID attribute");
								}

							}
						}
						//go to next iteration
						//continue;
					}

					//opening <objects> element
					else if(elementName.equals(OBJECTS))
					{
						debugLog("OPENED OBJECTS");

						//read objects
						List<SceneObject> objects = readObjects();

						//add objects to current room
						for(SceneObject object: objects)
						{
							room.addObject(object);
						}
					}
					
					//exits
					else if (elementName.equals(EXITS))
					{
						debugLog("OPENED EXITS");
						
						//read exits
						List<Exit> exits = readExits();
						
						//add exits to current room
						for(Exit exit: exits)
						{
							//debugLog("add exit " + exit.getName() + " to room");
							room.addExit(exit);
						}
						
					}
					
					else if(elementName.equals(ALIASES))
					{
						List<String> aliases = readAliases();
						
						//add aliases to current object
						for(String alias : aliases)
						{
							room.addAlias(alias);
						}
					}
					
					//name
					else if(elementName.equals(NAME))
					{
						debugLog("ROOM: setting name");
						debugLog("ROOM: setting name to " + eventData);
						room.setName(eventData);
					}

					//description
					else if(elementName.equals(DESCRIPTION))
					{
						room.setDescription(eventData);
					}

				}

				//if this is an ending element ex: </room>
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					//debugLog("CLOSING ELEMENT: " + endElementName);

					//if it is the end of the entire room, we can add it to the list
					if(endElementName.equals(ROOM))
					{
						debugLog("ROOM: COMPLETED ROOM");
						debugLog("ROOM: COMPLETED ROOM: " + room.toString());

						return room;
					}
				}
				
				//set the event to the current iteration
				event = eventReader.nextEvent();

			}

		}

		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}
		catch (Exception e)
		{
			System.out.println("LEVEL READER ERROR: ROOM: unknown exception");
			e.printStackTrace(System.out);
			e.printStackTrace(System.out);
		}

		//the rooms should be returned by the ending tag level before this point
		System.out.println("LEVEL READER ERROR: ROOM: reached end of document without closing room element");
		return room;
	}
	
	private Scenario readScenario()
	{
		Scenario scenario = new Scenario();
		
		try
		{
			while(eventReader.hasNext())
			{
				event = eventReader.nextEvent();

				if(event.isStartElement())
				{
					//StartElement startElement = event.asStartElement();

					String elementName = getStartElementName();

					String eventData = getEventData();

					if(eventData != null)
						debugLog("LEVEL: element=" + elementName + ", value=" + eventData);
					else
						debugLog("LEVEL: element=" + elementName + ", value=null");
					
					if(elementName.equals(PROPERTIES))
					{
						HashMap<String, Integer> properties = readProperties();
						
						if(properties != null)
						{
							scenario.setProperties(properties);
						}
						else
						{
							System.out.println("LEVEL READER ERROR: SCENARIO: properties HashMap is null");
						}
					}
					else if(elementName.equals(REQUESTS))
					{
						List<Request> requests = readRequests();
						
						for(Request request: requests)
						{
							scenario.addRequest(request);
						}
					}
					else
					{
						debugLog("SCENARIO: ERROR: unknown ending element: " + elementName);
					}
					
				}
				else if(event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();
					
					if(endElementName.equals(SCENARIO))
					{
						return scenario;
					}
					
				}
			}
		}
		catch (XMLStreamException e)
		{
			printXMLStreamException(e);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
		
		System.out.println("ERROR: scenario reader reached end of document");
		return scenario;
	}
	
	private List<EndState> readEndStates()
	{
		List<EndState> endStates = new ArrayList<EndState>();
		
		try
		{
			EndState endState = null;
			
			while(eventReader.hasNext())
			{
				event = eventReader.nextEvent();

				if(event.isStartElement())
				{
					StartElement startElement = event.asStartElement();

					String elementName = getStartElementName();

					String eventData = getEventData();

					if(eventData != null)
						debugLog("END STATES: element=" + elementName + ", value=" + eventData);
					else
						debugLog("END STATES: element=" + elementName + ", value=null");

					//if it is on a new EndState, create one
					if(elementName.equals(END_STATE))
					{
						endState = new EndState();
						
						//get all the attributes in the element
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();

						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							String attributeName = attribute.getName().getLocalPart();
							
							String attributeValue = attribute.getValue();
							
							debugLog("CURRENT ATTRIBUTE: " + attributeName);
							debugLog("VALUE: " + attributeValue);
							
							//check for the attributes we are looking for and set them
							if(attributeName.equals(ID))
							{
								int id = 0;
								try
								{
									id = Integer.parseInt(attributeValue);
								}
								catch(NumberFormatException e)
								{
									System.out.println("LEVEL READER ERROR: END STATE: non-int id");
								}
								
								endState.setID(id);
							}
						}
					}
					
					//if it hits a trigger, add to new EndState
					else if(elementName.equals(TRIGGER))
					{
						EndStateTrigger trigger = new EndStateTrigger();
						
						//go through attributes and set them
						
						//get all the attributes in the element
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();

						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							String attributeName = attribute.getName().getLocalPart();
							
							String attributeValue = attribute.getValue();
							
							debugLog("CURRENT ATTRIBUTE: " + attributeName);
							debugLog("VALUE: " + attributeValue);
							
							//check for the attributes we are looking for and set them
							if(attributeName.equals(TYPE))
							{
								trigger.setType(attributeValue);
							}
							else if(attributeName.equals(PROPERTY_NAME))
							{
								trigger.setProperty_name(attributeValue);
							}
							else if(attributeName.equals(TARGET))
							{
								int target;
								
								//if the target property is global/in scenario
								if(attributeValue.equalsIgnoreCase(SCENARIO) || attributeValue.equalsIgnoreCase("GLOBAL")) //TODO update global keyword at all target Scenario checks
								{
									target = Level.SCENARIO_ID;
								}
								else
								{
									try
									{
										target = Integer.parseInt(attributeValue);
									}
									catch(NumberFormatException e)
									{
										debugLog("END STATES: ERROR: target is not an int or scenario");
										target = 0;
									}
								}
								
								//set the target
								trigger.setTarget(target);
							}
							else if(attributeName.equalsIgnoreCase(OPERATOR))
							{
								trigger.setOperator(attributeValue);
							}
							else
							{
								System.out.println("LEVEL READER ERROR: END STATES: TRIGGER: unknown attribute: " + attributeName);
							}
						}
						
						//done with attributes, now set value
						int value;
						try
						{
							value = Integer.parseInt(eventData);
						}
						catch(NumberFormatException e)
						{
							debugLog("END STATES; ERROR: value of target is not an int");
							//set to default
							value = 0;
						}
						
						//set value
						trigger.setValue(value);
						
						//finally, add trigger to end state
						endState.addTrigger(trigger);
						
					}
					
					//if hits executables list
					else if(elementName.equals(EXECUTABLES))
					{
						//read executables
						List<Executable> executables = readExecutables();
						
						//add executables
						for(Executable executable: executables)
							endState.addExecutable(executable);
					}
					
					else
					{
						debugLog("SCENARIO: ERROR: unknown ending element: " + elementName);
					}

				}
				else if(event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					//return finished list when ending element reached
					if(endElementName.equals(END_STATES))
					{
						return endStates;
					}
					
					//add current EndState
					else if(endElementName.equals(END_STATE))
					{
						endStates.add(endState);
					}

				}
			}
		}
		catch (XMLStreamException e)
		{
			printXMLStreamException(e);
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		System.out.println("ERROR: end-states reader reached end of document");
		return endStates;
	}
	
	private List<SceneObject> readObjects()
	{
		//create a list of objects that will be outputed at the end
		List<SceneObject> objects = new ArrayList<SceneObject>();

		//inside the try block incase error occurs
		try
		{
			//begin to read the XML File
			SceneObject sceneObject = null;

			//while the iterator eventReader has events
			while(eventReader.hasNext())
			{
				//every iteration make a new event
				event = eventReader.nextEvent();

				//returns true if the event is the start of an element ex: <simple_object> or <name>
				if(event.isStartElement())
				{
					//returns the generalized event as a starting event
					//this is done because the XMLEvent type is a interface
					//needed only if attributes need to be read
					StartElement startElement = event.asStartElement();

					String elementName = getStartElementName();

					String eventData = getEventData();

					if(eventData != null)
						debugLog("OBJECTS: element=" + elementName + ", value=" + eventData);
					else
						debugLog("OBJECTS: element=" + elementName + ", value=null");
					
					//if the starting element is a simpleObject, we can create it
					if(elementName.equals(SIMPLE_OBJECT))
					{
						debugLog("OPENED SIMPLE_OBJECT");

						//using the default constructor so that we can add new fields in as
						//they get iterated over
						//This will be done with the getter/setter methods
						sceneObject = new SimpleObject();

						//this code will set attributes (the data in the start of the element ex: <room id="1">)
						
						//get all the attributes in the element
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();

						
						
						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							String attributeName = attribute.getName().getLocalPart();

							String attributeValue = attribute.getValue();

							debugLog("OBJECTS: NEW OBJECT: CURRENT ATTRIBUTE: name=" + attributeName + ", value=" + attributeValue);
							
							if(attributeName.equals(ID))
							{
								//check for the attributes we are looking for and set them
								try
								{
									sceneObject.setID(Integer.parseInt(attribute.getValue()));
								}
								catch (NumberFormatException e)
								{
									System.out.println("LEVEL READER ERROR: SCENE_OBJECT: non-int value as ID attribute");
								}
							}
							else
							{
								System.out.println("OBJECTS: unknown attribute: " + attributeName);
							}
						}

						//go to next iteration
						continue;
					}

					//name element
					else if (elementName.equals(NAME))
					{
						sceneObject.setName(eventData);
						continue;
					}

					//description element
					else if (elementName.equals(DESCRIPTION))
					{
						sceneObject.setDescription(eventData);
						continue;
					}
					
					else if(elementName.equals(ALIASES))
					{
						List<String> aliases = readAliases();
						
						//add aliases to current object
						for(String alias : aliases)
						{
							sceneObject.addAlias(alias);
						}
					}
					else if(elementName.equals(REQUESTS))
					{
						List<Request> requests = readRequests();
						
						//add requests to current object
						for(Request request: requests)
						{
							sceneObject.addRequest(request);
						}
					}
					
					//HashMap of properties
					else if(elementName.equals(PROPERTIES))
					{
						setProperties(sceneObject);
					}
					
					else
					{
						System.out.println("LEVEL READER ERROR: unrecognized object element: " + elementName);
					}
				}

				//if it is the ending element, ex: </room> or </simple_object>
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					//debugLog("CLOSING OBJECT ELEMENT: " + endElementName + ", value: " + getEventData());

					//if it is the end of the entire object, we can add it to the list
					if(endElementName.equals(SIMPLE_OBJECT))
					{
						debugLog("COMPLETE: " + sceneObject.toString());

						objects.add(sceneObject);
						continue;
					}

					else if(endElementName.equals(OBJECTS))
					{
						debugLog("COMPLETE: OBJECTS, len: " + objects.size());

						return objects;
					}
				}
			}


		} 
		//Stream exception thrown by malformed XML document
		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}

		//if an error occurs, print the stack trace
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		System.out.println("ERROR: object reader reached end of document");
		return objects;
	}

	private List<Exit> readExits()
	{
		//create a list of exits that will be outputed at the end
		List<Exit> exits = new ArrayList<Exit>();

		//inside the try block incase error occurs
		try
		{
			//begin to read the XML File
			Exit exit = null;

			//while the iterator eventReader has events
			while(eventReader.hasNext())
			{
				//every iteration make a new event
				event = eventReader.nextEvent();

				//returns true if the event is the start of an element ex: <simple_object> or <name>
				if(event.isStartElement())
				{
					//returns the generalized event as a starting event
					//this is done because the XMLEvent type is a interface
					//only needed if reading attributes
					StartElement startElement = event.asStartElement();

					String startElementName = getStartElementName();

					//the data inside of the event
					String eventData = getEventData();

					if(eventData != null)
						debugLog("EXITS: element=" + startElementName + ", value=" + eventData);
					else
						debugLog("EXITS: element=" + startElementName + ", value=null");
					
					//if the starting element is a simpleObject, we can create it
					if(startElementName.equals(EXIT))
					{
						debugLog("CREATED NEW EXIT");

						//using the default constructor so that we can add new fields in as
						//they get iterated over
						//This will be done with the getter/setter methods
						exit = new Exit();

						//this code will set attributes (the data in the start of the element ex: <room id="1">)
						
						//get all the attributes in the element
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();

						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							debugLog("VALUE: " + attribute.getValue());
							
							//check for the attributes we are looking for and set them
							try
							{
								exit.setID(Integer.parseInt(attribute.getValue()));
							}
							catch (NumberFormatException e)
							{
								System.out.println("EXIT: ERROR: non-int value as ID attribute");
							}
						}
						
						//go to next iteration
						continue;
					}

					//name element
					else if (startElementName.equals(NAME))
					{
						exit.setName(eventData);
						continue;
					}

					//list of aliases
					else if(startElementName.equals(ALIASES))
					{
						List<String> aliases = readAliases();
						
						//add aliases to current object
						for(String alias : aliases)
						{
							exit.addAlias(alias);
						}
					}
					
					//description element
					else if (startElementName.equals(DESCRIPTION))
					{
						exit.setDescription(eventData);
						continue;
					}
					
					//id of the room it is the entrance to
					else if (startElementName.equals(ENTRANCE_TO))
					{
						exit.setEntranceTo(Integer.parseInt(eventData));
					}
					
					//list of requests
					else if(startElementName.equals(REQUESTS))
					{
						List<Request> requests = readRequests();
						
						//add requests to current object
						for(Request request: requests)
						{
							exit.addRequest(request);
						}
					}
					
					//HashMap of properties
					else if(startElementName.equals(PROPERTIES))
					{
						setProperties(exit);
					}
					
					else
					{
						System.out.println("LEVEL READER ERROR: unrecognized exit element: " + startElementName);
					}
					
				}

				//if it is the ending element, ex: </room> or </simple_object>
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					//debugLog("CLOSING EXIT ELEMENT: " + endElementName + ", value: " + getEventData());

					//if it is the end of the entire object, we can add it to the list
					if(endElementName.equalsIgnoreCase(EXIT))
					{
						debugLog("CREATED EXIT: " + exit.toString());

						exits.add(exit);
						continue;
					}

					else if(endElementName.equalsIgnoreCase(EXITS))
					{
						debugLog("CREATED EXITS: len: " + exits.size());

						return exits;
					}
				}
			}


		} 
		//Stream exception thrown by malformed XML document
		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}

		//if an error occurs, print the stack trace
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		System.out.println("LEVEL READER ERROR: exit reader reached end of document");
		return exits;
	}
	
	private List<String> readAliases()
	{
		//create a list of string aliases that will be outputed at the end
		List<String> aliases = new ArrayList<String>();

		debugLog("OPENING ALIASES");
		
		//inside the try block incase error occurs
		try
		{
			//begin to read the XML File
			String alias = "";

			//while the iterator eventReader has events
			while(eventReader.hasNext())
			{
				//every iteration make a new event
				event = eventReader.nextEvent();

				//returns true if the event is the start of an element ex: <simple_object> or <name>
				if(event.isStartElement())
				{
					String elementName = getStartElementName();

					String eventData = getEventData();

					if(eventData != null)
						debugLog("ALIASES: element=" + elementName + ", value=" + eventData);
					else
						debugLog("ALIASES: element=" + elementName + ", value=null");
					
					//if the starting element is a simpleObject, we can create it
					if(elementName.equals(ALIAS))
					{
						debugLog("ADDING ALIAS TO LIST: " + eventData);

						//set the alias to the eventData
						alias = eventData;

						//add the alias to the list
						aliases.add(alias);
						
						//go to next iteration
						continue;
					}
				}

				//if it is the ending element, ex: </room> or </simple_object>
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					//debugLog("CLOSING ALIASES ELEMENT: " + endElementName);

					//if it is the end of the entire list
					if(endElementName.equalsIgnoreCase(ALIASES))
					{
						debugLog("COMPLETED ALIAS LIST: len=" + aliases.size());

						return aliases;
					}
				}
			}


		} 
		//Stream exception thrown by malformed XML document
		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}

		//if an error occurs, print the stack trace
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		System.out.println("LEVEL READER ERROR: alias reader reached end of document");
		return aliases;
	}
	
	private List<Request> readRequests()
	{
		//create a list of requests that will be outputed at the end
		List<Request> requests = new ArrayList<Request>();

		debugLog("OPENING REQUESTS");

		//inside the try block incase error occurs
		try
		{
			//begin to read the XML File
			Request request = new Request();

			//while the iterator eventReader has events
			while(eventReader.hasNext())
			{
				//every iteration make a new event
				event = eventReader.nextEvent();

				//returns true if the event is the start of an element ex: <simple_object> or <name>
				if(event.isStartElement())
				{
					StartElement startElement = event.asStartElement();
					
					String elementName = getStartElementName();

					String eventData = getEventData();

					if(eventData != null)
						debugLog("REQUESTS: element=" + elementName + ", value=" + eventData);
					else
						debugLog("REQUESTS: element=" + elementName + ", value=null");
					
					//if the starting element is a request, read attributes
					if(elementName.equals(REQUEST))
					{
						debugLog("NEW REQUEST");

						//create a new request to reset
						request = new Request();
						
						//no attributes on request
						
						//go to next iteration
						continue;
					}
					else if(elementName.equals(EXECUTABLES))
					{
						//read executables
						List<Executable> executables = readExecutables();
						
						debugLog("REQUEST: adding " + executables.size() + " top-level executables to request");
						
						for(Executable executable: executables)
						{
							request.addExecutable(executable);
						}
					}
					else if(elementName.equals(INPUT))
					{	
						//get all the attributes in the element
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();

						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							String attributeName = attribute.getName().getLocalPart().toString();
							String attributeValue = attribute.getValue();
							
							debugLog("CURRENT ATTRIBUTE: " + attributeName);
							debugLog("VALUE: " + attributeValue);
							
							//check for the attributes we are looking for and set them
							if(attributeName.equals(TYPE))
							{
								if(attributeValue.equals(VERB))
								{
									request.addVerb(eventData);
								}
								else if(attributeValue.equals(EXACT))
								{
									request.addExact(eventData);
								}
							}
						}
						
					}
				}

				//if it is the ending element, ex: </room> or </simple_object>
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					//debugLog("CLOSING REQUEST ELEMENT: " + endElementName);

					if(endElementName.equalsIgnoreCase(REQUEST))
					{
						requests.add(request);
					}
					
					//if it is the end of the entire list
					else if(endElementName.equalsIgnoreCase(REQUESTS))
					{
						debugLog("FINISHED REQUEST LIST: len=" + requests.size());

						return requests;
					}
				}
			}


		} 
		//Stream exception thrown by malformed XML document
		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}

		//if an error occurs, print the stack trace
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		System.out.println("LEVEL READER ERROR: requests reader reached end of document");
		return requests;
	}

	private void setProperties(SceneObject sceneObject)
	{
		//HashMap<String, Integer> properties = new HashMap<String, Integer>();
		
		//inside the try block incase error occurs
		try
		{
			//begin to read the XML File
			
			//create the name and value that will be added as pairs together
			String property_name = "";
			
			int value = SceneObject.defaultValue;

			//while the iterator eventReader has events
			while(eventReader.hasNext())
			{
				//every iteration make a new event
				event = eventReader.nextEvent();

				//returns true if the event is the start of an element ex: <simple_object> or <name>
				if(event.isStartElement())
				{
					StartElement startElement = event.asStartElement();

					String elementName = getStartElementName();

					String eventData = getEventData();

					//if the starting element is a property, read attribute (name)
					if(elementName.equals(PROPERTY))
					{
						debugLog("CREATING NEW PROPERTY");

						//reset the name and value
						property_name = "";
						value = SceneObject.defaultValue;

						//get name attribute of the property
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();
						
						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							String attributeName = attribute.getName().getLocalPart();
							
							//debugLog("CURRENT ATTRIBUTE: " + attributeName);

							String attributeValue = attribute.getValue();
							
							//debugLog("VALUE: " + attributeValue);
							
							//check for the attributes we are looking for and set them
							if(attributeName.equals(NAME))
							{
								property_name = attributeValue;
							}
							
							else
							{
								System.out.println("LEVEL READER ERROR: unrecognized attribute: " + attributeName);
							}
							
						}
						
						//if the value of the property is specified, use it
						//otherwise, use default value
						if(eventData != null)
						{
							try
							{
								value = Integer.parseInt(eventData);
							}
							catch (NumberFormatException e)
							{
								System.out.println("LEVEL READER ERROR: property value is not an integer");
							}
							
						}
						else
						{
							value = SceneObject.defaultValue;
						}
						
						//go to next iteration
						continue;
					}

					else
					{
						System.out.println("PROPERTIES: ERROR: unrecongized element: " + elementName);
					}

				}

				//if it is the ending element, ex: </room> or </simple_object>
				//this is where the property will be set and added
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					//debugLog("CLOSING PROPERTY ELEMENT: " + endElementName);

					//if it is the end of this action
					if(endElementName.equalsIgnoreCase(PROPERTY))
					{
						//add the name and value pair to the HashMap
						if(property_name != null)
						{
							debugLog("PROPERTY: adding property: name=" + property_name + ", value=" + value);
							sceneObject.addProperty(property_name, value);
						}
						else
							System.out.println("LEVEL READER ERROR: null property name");
					}

					//if it is the end of the entire list
					else if(endElementName.equalsIgnoreCase(PROPERTIES))
					{
						debugLog("CLOSED PROPERTIES LIST (HASHMAP)");
						return;
					}
				}
			}


		} 
		//Stream exception thrown by malformed XML document
		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}

		//if an error occurs, print the stack trace
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		System.out.println("LEVEL READER ERROR: properties setter reached end of document");
	}
	
	private HashMap<String, Integer> readProperties()
	{
		HashMap<String, Integer> properties = new HashMap<String, Integer>();

		//inside the try block incase error occurs
		try
		{
			//begin to read the XML File

			//create the name and value that will be added as pairs together
			String property_name = "";

			int value = SceneObject.defaultValue;

			//while the iterator eventReader has events
			while(eventReader.hasNext())
			{
				//every iteration make a new event
				event = eventReader.nextEvent();

				//returns true if the event is the start of an element ex: <simple_object> or <name>
				if(event.isStartElement())
				{
					StartElement startElement = event.asStartElement();

					String elementName = getStartElementName();

					String eventData = getEventData();

					//if the starting element is a property, read attribute (name)
					if(elementName.equals(PROPERTY))
					{
						debugLog("CREATING NEW PROPERTY");

						//reset the name and value
						property_name = "";
						value = SceneObject.defaultValue;

						//get name attribute of the property
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();

						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							String attributeName = attribute.getName().getLocalPart();

							//debugLog("CURRENT ATTRIBUTE: " + attributeName);

							String attributeValue = attribute.getValue();

							//debugLog("VALUE: " + attributeValue);

							//check for the attributes we are looking for and set them
							if(attributeName.equals(NAME))
							{
								property_name = attributeValue;
							}

							else
							{
								System.out.println("LEVEL READER ERROR: unrecognized attribute: " + attributeName);
							}

						}

						//if the value of the property is specified, use it
						//otherwise, use default value
						if(eventData != null)
						{
							try
							{
								value = Integer.parseInt(eventData);
							}
							catch (NumberFormatException e)
							{
								System.out.println("LEVEL READER ERROR: property value is not an integer");
							}

						}
						else
						{
							value = SceneObject.defaultValue;
						}

						//go to next iteration
						continue;
					}

					else
					{
						System.out.println("PROPERTIES: ERROR: unrecongized element: " + elementName);
					}

				}

				//if it is the ending element, ex: </room> or </simple_object>
				//this is where the property will be set and added
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					//debugLog("CLOSING PROPERTY ELEMENT: " + endElementName);

					//if it is the end of this action
					if(endElementName.equalsIgnoreCase(PROPERTY))
					{
						//add the name and value pair to the HashMap
						if(property_name != null)
						{
							debugLog("PROPERTY: adding property: name=" + property_name + ", value=" + value);
							properties.put(property_name, value);
						}
						else
							System.out.println("LEVEL READER ERROR: null property name");
					}

					//if it is the end of the entire list
					else if(endElementName.equalsIgnoreCase(PROPERTIES))
					{
						debugLog("CLOSED PROPERTIES LIST (HASHMAP)");
						return properties;
					}
				}
			}


		} 
		//Stream exception thrown by malformed XML document
		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}

		//if an error occurs, print the stack trace
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		System.out.println("LEVEL READER ERROR: properties reader reached end of document");
		return properties;
	}
	
	private ArrayList<Executable> readExecutables()
	{
		//Create a list of conditions that will be outputed at the end
		ArrayList<Executable> executables = new ArrayList<Executable>();

		debugLog("OPENING EXECUTABLES");

		//inside the try block incase error occurs
		try
		{
			//while the iterator eventReader has events
			while(eventReader.hasNext())
			{
				//every iteration make a new event
				event = eventReader.nextEvent();

				//returns true if the event is the start of an element ex: <simple_object> or <name>
				if(event.isStartElement())
				{
					StartElement startElement = event.asStartElement();

					String elementName = getStartElementName();

					String eventData = getEventData();

					if(eventData != null)
						debugLog("EXECUTABLES: element=" + elementName + ", value=" + eventData);
					else
						debugLog("EXECUTABLES: element=" + elementName + ", value=null");

					if(elementName.equals(EXECUTABLE_GROUP))
					{
						debugLog("CREATING NEW EXECUTABLE GROUP");
						
						ExecutableGroup exeGroup = new ExecutableGroup();
						
						//read attributes
						//get all the attributes in the element
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();

						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							String attributeName = attribute.getName().getLocalPart();
							String attributeValue = attribute.getValue();
							
							debugLog("CURRENT ATTRIBUTE: " + attributeName + ", value=" + attributeValue);

							//check for the attributes we are looking for and set them
							if(attributeName.equals(ID))
							{
								try
								{
									exeGroup.setMaxExecutions(Integer.parseInt(attributeValue));
								}
								catch (NumberFormatException e)
								{
									System.out.println("LEVEL READER ERROR: EXECUTABLES: EXECUTABLE GROUP: non-int value as max executions attribute");
								}
							}
						}
						
						//read executables in the group
						ArrayList<Executable> exes = readExecutables();
						
						//set executables
						for(Executable e: exes)
						{
							exeGroup.addExecutable(e);
						}
						
						executables.add(exeGroup);
					}
					
					else if(elementName.equals(CONDITION))
					{
						debugLog("CREATING NEW CONDITION");

						Condition condition = readCondition();
						executables.add(condition);
					}

					else if(elementName.equals(ACTION))
					{
						debugLog("CREATING NEW ACTION");
						
						Action action = new Action();
						
						//get attributes of actions
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();
						
						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							String attributeName = attribute.getName().getLocalPart();
							
							debugLog("CURRENT ATTRIBUTE: " + attributeName);

							String attributeValue = attribute.getValue();
							
							debugLog("VALUE: " + attributeValue);
							
							//check for the attributes we are looking for and set them
							if(attributeName.equals(TYPE))
							{
								action.setActionType(attributeValue);
							}
							
							//used for type CHANGE_PROPERTY
							else if(attributeName.equals(TARGET))
							{
								try
								{
									if(attributeValue.equalsIgnoreCase(SCENARIO))
									{
										action.setActionTarget(Level.SCENARIO_ID);
									}
									else
									{
										int target = Integer.parseInt(attributeValue);
										
										if(target == Level.SCENARIO_ID)
										{
											System.out.println("LEVEL READER ERROR: specified non scenario ID manually");
										}
										
										action.setActionTarget(target);
									}
								}
								catch (NumberFormatException e)
								{
									System.out.println("EXIT: ERROR: non-int value as target ID attribute");
								}
							}
							
							//used for type CHANGE_PROPERTY
							else if(attributeName.equals(PROPERTY_NAME))
							{
								action.setPropertyName(attributeValue);
							}
							
						}
						
						//set the value of the action
						action.setActionValue(eventData);
						
						executables.add(action);
					}
					
					else if(elementName.equals("RANDOM"))
					{
						//TODO staticize that and create
					}
					
					else if(elementName.equals(PASS) || elementName.equals(FAIL))
					{
						//do nothing until closing but I don't want it outputting unknown element
					}
					
					else
					{
						System.out.println("EXECUTABLES: ERROR: unrecongized element: " + elementName);
					}

				}

				//if it is the ending element, ex: </room> or </simple_object>
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					//if it is the end of the entire list
					if(endElementName.equalsIgnoreCase(EXECUTABLES))
					{
						debugLog("EXECUTABLES: FINISHED EXECUTABLES LIST, len=" + executables.size());

						return executables;
					}
					
					else if(endElementName.equals(EXECUTABLE_GROUP))
					{
						debugLog("EXECUTABLES: FINISHED EXECUTABLE GROUP LIST, len=" + executables.size());
						return executables;
					}
					
					else if(endElementName.equals(PASS))
					{
						debugLog("EXECUTABLES: FINISHED EXECUTABLES LIST FOR PASS, len=" + executables.size());
						return executables;
					}
					
					else if(endElementName.equals(FAIL))
					{
						debugLog("EXECUTABLES: FINISHED EXECUTABLES LIST FOR FAIL, len=" + executables.size());
						return executables;
					}
				}
			}
		}
		//Stream exception thrown by malformed XML document
		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}

		//if an error occurs, print the stack trace
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		System.out.println("LEVEL READER ERROR: executables reader reached end of document");
		return executables;
	}
	
	private Condition readCondition()
	{
		debugLog("OPENING CONDITION");
		
		//inside the try block incase error occurs
		try
		{
			//begin to read the XML File
			Condition condition = new Condition();

			//while the iterator eventReader has events
			while(eventReader.hasNext())
			{
				//every iteration make a new event
				event = eventReader.nextEvent();

				//returns true if the event is the start of an element ex: <simple_object> or <name>
				if(event.isStartElement())
				{
					//StartElement startElement = event.asStartElement();

					String elementName = getStartElementName();

					String eventData = getEventData();

					if(eventData != null)
						debugLog("CONDITION: element=" + elementName + ", value=" + eventData);
					else
						debugLog("CONDITION: element=" + elementName + ", value=null");
					
					//if the starting element is a condition, create a new one to reset
					if(elementName.equals(CONDITION))
					{
						debugLog("CREATING NEW CONDITION");
					}
					
					//conditions have <type>, <target>, <property_name>, <value>, <pass> (group), <fail> (group)
					
					//type is "=", "<", ">"
					else if(elementName.equals(TYPE))
					{
						condition.setOperator(eventData);
					}
					
					else if(elementName.equals(TARGET))
					{
						if(eventData.equalsIgnoreCase(SCENARIO))
						{
							condition.setTarget(Level.SCENARIO_ID);
						}
						else
						{
							condition.setTarget(eventData);
						}
					}
					
					//property_name
					else if(elementName.equals(PROPERTY_NAME))
					{
						condition.setProperty_name(eventData);
					}
					
					else if(elementName.equals(VALUE))
					{
						condition.setValue(eventData);
					}
					
					//pass list contains executables to run if condition passes
					else if(elementName.equals(PASS))
					{
						ArrayList<Executable> exes = readExecutables();
						
						for(Executable e: exes)
						{
							condition.addPassExecutable(e);
						}
					}
					
					//fail list contains executables to run if condition fails
					else if(elementName.equalsIgnoreCase(FAIL))
					{
						ArrayList<Executable> exes = readExecutables();

						for(Executable e: exes)
						{
							condition.addFailExecutable(e);
						}
					}
					
					else
					{
						System.out.println("CONDITION: ERROR: unrecongized element: " + elementName);
					}
					
				}

				//if it is the ending element, ex: </room> or </simple_object>
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					//debugLog("CLOSING CONDITIONS ELEMENT: " + endElementName);

					//if it is the end of this action
					if(endElementName.equalsIgnoreCase(CONDITION))
					{
						return condition;
					}
				}
			}
		} 
		
		//Stream exception thrown by malformed XML document
		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}

		//if an error occurs, print the stack trace
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}

		System.out.println("LEVEL READER ERROR: condition reader reached end of document");
		return null;
	}
	
	private String getStartElementName()
	{
		//returns the generalized event as a starting event
		//this is done because the XMLEvent type is a interface
		StartElement startElement = event.asStartElement();

		//confusing because getName retuns type QName, not a String
		//QName is the full name according to XML standards, which includes other stuff
		//such as the Universal Resource Identifier
		//QName.getLocalPart() returns the "local part" of the QName
		//toString converts it to a string
		//I think that the toString() part isn't neccessary because getLocalPart() returns
		//type string anyways
		String startElementName = startElement.getName().getLocalPart().toString();

		//move to next event [for reasons I don't understand]
		try
		{
			event = eventReader.nextEvent();
		} catch (XMLStreamException e)
		{
			System.out.println("LEVEL READER ERROR: could not get starting element name");
			e.printStackTrace(System.out);
		}
		
		return startElementName;
	}
	
	
	/**
	 * Reads the value of the element, returns null and a ClassCastException if none
	 * @return the value of the element
	 */
	private String getEventData()
	{
		//the data inside of the event
		String eventData = null;
		try
		{
			if(event.isEndElement())
				return eventData;
			
			if(!event.asCharacters().isIgnorableWhiteSpace())
				eventData = event.asCharacters().getData();
		}
		catch (ClassCastException e)
		{
			System.out.println("LEVEL READER ERROR: Could not cast event to character data");
			//e.printStackTrace(System.out);
			System.out.println(e.toString());
		}
		
		return eventData.trim();
	}

	private void printXMLStreamException(Exception e)
	{
		System.out.println("LEVEL READER ERROR: Stream Exception: Possible document error");
		e.printStackTrace(System.out);
	}
	
	private void debugLog(String str)
	{
		if(debugMode)
		{
			System.out.println("LEVEL READER INFO: " + str);
		}
	}

}
