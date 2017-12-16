package gamelogic;

import sceneObjects.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

//@SuppressWarnings("unused")
/**
 * Creates the rooms and objects by reading the XML File
 * @author Caden
 *
 */
public class LevelConstructorXMLParser
{
	/*
    static final String DATE = "date";
    static final String ITEM = "item";
    static final String MODE = "mode";
    static final String UNIT = "unit";
    static final String CURRENT = "current";
    static final String INTERACTIVE = "interactive";
	 */

	static final String SIMPLE_OBJECT = "simple_object";
	static final String NAME = "name";
	static final String DESCRIPTION = "description";
	static final String ROOM = "room";
	static final String EXIT = "exit";
	static final String EXITS = "exits";
	static final String ENTRANCE_TO = "entranceTo";
	static final String OBJECTS = "objects";
	static final String LEVEL = "level";
	
	static final String ALIASES = "aliases";
	static final String ALIAS = "alias";

	static final String REQUESTS = "requests";
	static final String REQUEST = "request";
	static final String ACTIONS = "actions";
	static final String ACTION = "action";
	
	static final String INPUT = "input";
	static final String TYPE = "type";
	static final String OUT = "out";
	static final String VERB = "verb";
	static final String EXACT = "exact";
	
	boolean debugMode = false;
	
	String levelFile;

	//the iterator
	XMLEventReader eventReader;

	//the current event the iterator is on
	//placed here so helper methods have access
	XMLEvent event;

	public List<Room> readLevel(String levelFile)
	{
		//set the file to load
		this.levelFile = levelFile;

		//create the list of rooms that will outputed at the end
		List<Room> rooms = new ArrayList<Room>();

		try
		{
			//create the inputfactory to create the event reader
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();

			//create the input stream to create the event reader
			InputStream in = new FileInputStream(levelFile);

			//finally create the event reader
			eventReader = inputFactory.createXMLEventReader(in);

			//create a room to act as the placeholder to use setters on
			Room room = null;

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

					//opening <room> element
					if(elementName.equals(ROOM))
					{
						debugLog("OPENED ROOM");
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
							if(attribute.getName().getLocalPart().toString().equals("id"))
							{
								debugLog("VALUE: " + attribute.getValue());
								try
								{
									room.setID(Integer.parseInt(attribute.getValue()));
								}
								catch (NumberFormatException e)
								{
									System.err.println("ERROR: non-int value as ID attribute");
								}

							}
						}
						//go to next iteration
						continue;
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

					debugLog("CLOSING ELEMENT: " + endElementName);

					//if it is the end of the entire room, we can add it to the list
					if(endElementName.equals(ROOM))
					{
						debugLog("CLOSED ROOM NAMED: " + room.getName());

						rooms.add(room);
						continue;
					}

					//closing </level> element at very end of XML document
					else if(endElementName.equals(LEVEL))
					{
						//return the rooms
						return rooms;
					}
				}

			}

		}
		//If file not found, display error message to console
		catch(FileNotFoundException e)
		{
			System.err.println("LEVEL READER ERROR: Did not find file: " + levelFile);
		}
		catch(XMLStreamException e)
		{
			printXMLStreamException(e);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		//the rooms should be returned by the ending tag level before this point
		System.err.println("ERROR: reached end of document without closing level element");
		return rooms;
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
					//StartElement startElement = event.asStartElement();

					String elementName = getStartElementName();

					String eventData = getEventData();

					//if the starting element is a simpleObject, we can create it
					if(elementName.equals(SIMPLE_OBJECT))
					{
						debugLog("OPENED SIMPLE_OBJECT");

						//using the default constructor so that we can add new fields in as
						//they get iterated over
						//This will be done with the getter/setter methods
						sceneObject = new SimpleObject();

						//this code will set attributes (the data in the start of the element ex: <room id="1">)
						/*
						//get all the attributes in the element
						Iterator<Attribute> attributes = startElement.getAttributes();

						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							//check for the attributes we are looking for and set them
							if(attribute.getName().getLocalPart().toString().equals("attribute name"))
							{
								simpleObject.setAttribute(attribute.getValue());
							}
						}

						 */

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
							debugLog("Setting alias list, current alias: " + alias);
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
				}

				//if it is the ending element, ex: </room> or </simple_object>
				else if (event.isEndElement())
				{
					//create object of type end element
					EndElement endElement = event.asEndElement();

					//get the name of the ending element as we did before with the start element
					String endElementName = endElement.getName().getLocalPart().toString();

					debugLog("CLOSING OBJECT ELEMENT: " + endElementName);

					//if it is the end of the entire object, we can add it to the list
					if(endElementName.equals(SIMPLE_OBJECT))
					{
						debugLog("CLOSED SIMPLE_OBJECT NAMED: " + sceneObject.getName());

						objects.add(sceneObject);
						continue;
					}

					else if(endElementName.equals(OBJECTS))
					{
						debugLog("CLOSED OBJECTS ELEMENT");

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
			e.printStackTrace();
		}

		System.err.println("ERROR: object reader reached end of document");
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
					//StartElement startElement = event.asStartElement();

					String startElementName = getStartElementName();

					//the data inside of the event
					String eventData = getEventData();

					//if the starting element is a simpleObject, we can create it
					if(startElementName.equals(EXIT))
					{
						debugLog("OPENED EXIT");

						//using the default constructor so that we can add new fields in as
						//they get iterated over
						//This will be done with the getter/setter methods
						exit = new Exit();

						//go to next iteration
						continue;
					}

					//name element
					else if (startElementName.equals(NAME))
					{
						exit.setName(eventData);
						continue;
					}

					//description element
					else if (startElementName.equals(DESCRIPTION))
					{
						exit.setDescription(eventData);
						continue;
					}
					
					else if (startElementName.equals(ENTRANCE_TO))
					{
						exit.setEntranceTo(Integer.parseInt(eventData));
					}
					else if(startElementName.equals(REQUESTS))
					{
						List<Request> requests = readRequests();
						
						//add requests to current object
						for(Request request: requests)
						{
							exit.addRequest(request);
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

					debugLog("CLOSING EXIT ELEMENT: " + endElementName);

					//if it is the end of the entire object, we can add it to the list
					if(endElementName.equalsIgnoreCase(EXIT))
					{
						debugLog("CLOSED EXIT NAMED: " + exit.getName());

						exits.add(exit);
						continue;
					}

					else if(endElementName.equalsIgnoreCase(EXITS))
					{
						debugLog("CLOSED EXITS ELEMENT");

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
			e.printStackTrace();
		}

		System.err.println("LEVEL READER ERROR: exit reader reached end of document");
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

					//if the starting element is a simpleObject, we can create it
					if(elementName.equals(ALIAS))
					{
						debugLog("SETTING ALIAS: " + eventData);

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

					debugLog("CLOSING ALIASES ELEMENT: " + endElementName);

					//if it is the end of the entire list
					if(endElementName.equalsIgnoreCase(ALIASES))
					{
						debugLog("CLOSED ALIAS ELEMENT");

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
			e.printStackTrace();
		}

		System.err.println("LEVEL READER ERROR: alias reader reached end of document");
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

					//if the starting element is a request, read attributes
					if(elementName.equals(REQUEST))
					{
						debugLog("REQUEST");

						//create a new request to reset
						request = new Request();
						
						//no attributes on request
						
						//go to next iteration
						continue;
					}
					else if(elementName.equals(ACTIONS))
					{
						//read actions
						List<Action> actions = readActions();
						
						debugLog("adding " + actions.size() + " actions to request");
						
						for(Action action: actions)
						{
							debugLog("adding action to request...");
							request.addAction(action);
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

					debugLog("CLOSING REQUEST ELEMENT: " + endElementName);

					if(endElementName.equalsIgnoreCase(REQUEST))
					{
						requests.add(request);
					}
					
					//if it is the end of the entire list
					else if(endElementName.equalsIgnoreCase(REQUESTS))
					{
						debugLog("CLOSED REQUESTS ELEMENT");

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
			e.printStackTrace();
		}

		System.err.println("LEVEL READER ERROR: requests reader reached end of document");
		return requests;
	}

	private List<Action> readActions()
	{
		//create a list of string aliases that will be outputed at the end
		List<Action> actions = new ArrayList<Action>();

		debugLog("OPENING ACTIONS");

		//inside the try block incase error occurs
		try
		{
			//begin to read the XML File
			Action action = new Action();

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

					//if the starting element is a action, read attributes
					if(elementName.equals(ACTION))
					{
						debugLog("CREATING NEW ACTION");

						//create a new request to reset
						action = new Action();

						//get attributes of actions
						@SuppressWarnings("unchecked")
						Iterator<Attribute> attributes = startElement.getAttributes();

						//while the iterator has more attributes to go over
						while(attributes.hasNext())
						{
							//create variable for the individual attribute
							Attribute attribute = attributes.next();

							debugLog("CURRENT ATTRIBUTE: " + attribute.getName().getLocalPart());

							//check for the attributes we are looking for and set them
							if(attribute.getName().getLocalPart().toString().equals(TYPE))
							{
								debugLog("VALUE: " + attribute.getValue());
								
								action.setActionType(attribute.getValue());
							}
						}
						
						//set the value of the action
						action.setActionValue(eventData);
						
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

					debugLog("CLOSING ACTION ELEMENT: " + endElementName);

					//if it is the end of this action
					if(endElementName.equalsIgnoreCase(ACTION))
					{
						actions.add(action);
					}
					
					//if it is the end of the entire list
					else if(endElementName.equalsIgnoreCase(ACTIONS))
					{
						debugLog("CLOSED ACTIONS ELEMENT");

						return actions;
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
			e.printStackTrace();
		}

		System.err.println("LEVEL READER ERROR: actions reader reached end of document");
		return actions;
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
		//type string
		String startElementName = startElement.getName().getLocalPart().toString();

		//move to next event [for reasons I don't understand]
		try
		{
			event = eventReader.nextEvent();
		} catch (XMLStreamException e)
		{
			System.err.println("LEVEL READER ERROR: could not get starting element name");
			e.printStackTrace();
		}
		
		return startElementName;
	}
	
	private String getEventData()
	{
		//the data inside of the event
		String eventData = "NO DATA";
		try
		{
			eventData = event.asCharacters().getData();
		}
		catch (ClassCastException e)
		{
			System.err.println("LEVEL READER ERROR: Could not cast event to character data");
			e.printStackTrace();
		}
		
		return eventData;
	}

	private void printXMLStreamException(Exception e)
	{
		System.err.println("LEVEL READER ERROR: Stream Exception: Possible document error");
		e.printStackTrace();
	}
	
	private void debugLog(String str)
	{
		if(debugMode)
		{
			System.out.println("LEVEL READER INFO: " + str);
		}
	}

/*
 * 
 * The original code I found on the internet, above code is my modified version of it
 * 
    @SuppressWarnings({ "unchecked", "null" })
    public List<Item> readConfig(String configFile) {
        List<Item> items = new ArrayList<Item>();
        try {
            // First, create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = new FileInputStream(configFile);
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            Item item = null;

            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();

                if (event.isStartElement()) {
                    StartElement startElement = event.asStartElement();
                    // If we have an item element, we create a new item
                    if (startElement.getName().getLocalPart().equals(ITEM)) {
                        item = new Item();
                        // We read the attributes from this tag and add the date
                        // attribute to our object
                        Iterator<Attribute> attributes = startElement
                                .getAttributes();
                        while (attributes.hasNext()) {
                            Attribute attribute = attributes.next();
                            if (attribute.getName().toString().equals(DATE)) {
                                item.setDate(attribute.getValue());
                            }

                        }
                    }

                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart()
                                .equals(MODE)) {
                            event = eventReader.nextEvent();
                            item.setMode(event.asCharacters().getData());
                            continue;
                        }
                    }
                    if (event.asStartElement().getName().getLocalPart()
                            .equals(UNIT)) {
                        event = eventReader.nextEvent();
                        item.setUnit(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(CURRENT)) {
                        event = eventReader.nextEvent();
                        item.setCurrent(event.asCharacters().getData());
                        continue;
                    }

                    if (event.asStartElement().getName().getLocalPart()
                            .equals(INTERACTIVE)) {
                        event = eventReader.nextEvent();
                        item.setInteractive(event.asCharacters().getData());
                        continue;
                    }
                }
                // If we reach the end of an item element, we add it to the list
                if (event.isEndElement()) {
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equals(ITEM)) {
                        items.add(item);
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
        return items;
    }
 */
}
