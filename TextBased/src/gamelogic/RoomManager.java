package gamelogic;
import sceneObjects.Room;

//acts as a state machine for what room the player is in
public class RoomManager 
{

	Room currentRoom;
	
	public void ChangeRoom(Room newRoom)
	{
		currentRoom = newRoom;
		//System.out.println(currentRoom.getDescription());
	}
	
	public Room getRoom ()
	{
		return currentRoom;
	}
	
}
