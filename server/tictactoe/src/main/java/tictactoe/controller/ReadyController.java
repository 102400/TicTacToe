package tictactoe.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tictactoe.json.ReadyJson;
import tictactoe.rule.Legal;
import tictactoe.vat.Room;
import tictactoe.vat.User;
import tictactoe.vat.Vat;

@Controller
public class ReadyController {

	@RequestMapping(value="/ready", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ReadyJson alive(int userId, String userPassword, int roomId, String roomPassword) {
		ReadyJson readyJson = new ReadyJson();
		Room room = null;
		User userA = null;
		User userB = null;
		int userAorB = 0;  //default:0, userA:1 ,userB:2
		if(userPassword==null&&"".equals(userPassword)) {
			readyJson.setReadyOk(false);
			return readyJson;
		}
		if(!Legal.isUserIdAndPasswordLegal(userId, userPassword)) {
			readyJson.setReadyOk(false);
			return readyJson;
		}
		if(!Legal.isRoomIdAndPasswordLegal(roomId, roomPassword)) {
			readyJson.setReadyOk(false);
			return readyJson;
		}
		for(Room r:Vat.getRoomlist()) {
			if(r.getRoomId()==roomId) {
				room = r;
				break;
			}
		}
		if(room.getUserA()!=null) {
			userA = room.getUserA();
			if(userA.getUserId()==userId) {
				userAorB = 1;
			}
		}
		if(room.getUserB()!=null) {
			userB = room.getUserB();
			if(userB.getUserId()==userId) {
				userAorB = 2;
			}
		}
		switch(userAorB) {
			case 0:
				readyJson.setReadyOk(false);
				return readyJson;
			case 1:
				room.setUserAReady(true);
				userA.getAliveMessageMap().put("isReady", "true");
				userA.getAliveMessageMap().put("isWin", "false");
				userA.getAliveMessageMap().put("isTie", "false");
				userA.getAliveMessageMap().put("isLose", "false");
				userA.getAliveMessageMap().put("chessBoard", new int[3][3]);
				break;
			case 2:
				room.setUserBReady(true);
				userB.getAliveMessageMap().put("isReady", "true");
				userB.getAliveMessageMap().put("isWin", "false");
				userB.getAliveMessageMap().put("isTie", "false");
				userB.getAliveMessageMap().put("isLose", "false");
				userB.getAliveMessageMap().put("chessBoard", new int[3][3]);
				break;
		}
		if(room.getIsUserAReady()&&room.getIsUserBReady()) {
			room.setLastActivityTime(System.currentTimeMillis());
			userA.getAliveMessageMap().put("isBothReady", "true");
			userB.getAliveMessageMap().put("isBothReady", "true");
		}
		
		readyJson.setReadyOk(true);
		
		return readyJson;
	}
}
