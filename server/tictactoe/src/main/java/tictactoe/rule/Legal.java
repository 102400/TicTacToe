package tictactoe.rule;

import tictactoe.vat.Room;
import tictactoe.vat.User;
import tictactoe.vat.Vat;

public class Legal {
	
	public static boolean isUserIdAndPasswordLegal(int userId, String userPassword) {
		
		boolean isUserIdExist = false;
		boolean isUserPasswordLegal = false;
		for(User user:Vat.getUserlist()) {
			if(user.getUserId()==userId) {
				isUserIdExist = true;
				break;
			}
		}
		if(VerifySource.getMD5(userId).equals(userPassword)) {
			isUserPasswordLegal = true;
		}
		if(isUserIdExist && isUserPasswordLegal) {
			return true;
		}
		return false;
	}
	
public static boolean isRoomIdAndPasswordLegal(int roomId, String roomPassword) {
		
		boolean isRoomIdExist = false;
		boolean isRoomPasswordLegal = false;
		for(Room room:Vat.getRoomlist()) {
			if(room.getRoomId()==roomId) {
				isRoomIdExist = true;
				break;
			}
		}
		if(VerifySource.getMD5(roomId).equals(roomPassword)) {
			isRoomPasswordLegal = true;
		}
		if(isRoomIdExist && isRoomPasswordLegal) {
			return true;
		}
		return false;
	}

}