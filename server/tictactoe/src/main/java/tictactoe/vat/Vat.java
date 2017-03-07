package tictactoe.vat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tictactoe.rule.VerifySource;

public class Vat {
	
	private static int userId = 100001;
	private static int roomId = 1001;
	private static final List<User> UserList = new ArrayList<>();
	private static final List<Room> RoomList = new  ArrayList<>();
	
	private List<String> stringList = Collections.synchronizedList(new ArrayList<String>());
	
//	static {
//		User user = new User();
//		user.setUserId(userId);
//		UserList.add(user);
//		userId++;
//	}
	
	public static synchronized User addUser() {
		User user = new User();
		user.setUserId(userId);
		user.setUserPassword(VerifySource.getMD5(userId));
		UserList.add(user);
		
		userId++;
		return user;
	}
	
	public static synchronized Room addRoom() {
		Room room = new Room();
		room.setRoomId(roomId);
		room.setRoomPassword(VerifySource.getMD5(roomId));
		roomId++;
		RoomList.add(room);
		return room;
	}

	public static List<User> getUserlist() {
		return UserList;
	}

	public static List<Room> getRoomlist() {
		return RoomList;
	}
	

}
