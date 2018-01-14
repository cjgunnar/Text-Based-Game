package gamelogic;
import sceneObjects.Room;

//acts as a state machine for what room the player is in
public class RoomManager 
{

	Room currentRoom;
	
	Scenario scenario;
	
	public void ChangeRoom(Room newRoom)
	{
		//null checks
		if(newRoom == null)
		{
			System.out.println("ROOM MANAGER: ERROR: null room entered as new room");
			return;
		}
		if(newRoom.getID() == 0)
		{
			System.out.println("ROOM MANAGER: ERROR: new room has an ID of 0");
			return;
		}
		if(scenario == null)
		{
			System.out.println("ROOM MANAGER: ERROR: scenario is NULL");
		}
		
		currentRoom = newRoom;
		
		scenario.changeProperty("current-room", newRoom.getID());
	}
	
	//TODO add ID support to ChangeRoom
	public void ChangeRoom(int roomID)
	{
		System.err.println("ROOM MANAGER: NOT IMPLEMENTED ERROR: ChangeRoom by ID not created yet");
	}
	
	public Room getRoom ()
	{
		return currentRoom;
	}
	
}
