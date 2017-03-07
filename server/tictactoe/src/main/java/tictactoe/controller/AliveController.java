package tictactoe.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tictactoe.json.AliveJson;
import tictactoe.rule.Legal;
import tictactoe.vat.Room;
import tictactoe.vat.User;
import tictactoe.vat.Vat;

@Controller
public class AliveController {
	
	@RequestMapping(value="/alive", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public AliveJson alive(int userId, String userPassword, int roomId, String roomPassword) {
		AliveJson aliveJson = new AliveJson();
		if(userPassword==null&&"".equals(userPassword)) {
			aliveJson.setAliveOk(false);
			return aliveJson;
		}
		if(!Legal.isUserIdAndPasswordLegal(userId, userPassword)) {
			aliveJson.setAliveOk(false);
			return aliveJson;
		}
		
		User user = null;
		for(User u:Vat.getUserlist()) {
			if(u.getUserId()==userId) {
				user = u;
			}
		}
		if(user==null) {
			aliveJson.setAliveOk(false);
			return aliveJson;
		}
		
		
		if(roomId>0) {
			if(Legal.isRoomIdAndPasswordLegal(roomId, roomPassword)) {
				Room room = null;
				User userA = null;
				User userB = null;
				int userAorB = 0;  //default:0, userA:1 ,userB:2
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
						aliveJson.setAliveOk(false);
						return aliveJson;
					case 1:
						if(room.getIsUserBReady()) {
							user.getAliveMessageMap().put("isEnemyReady", "true");
						}
						break;
					case 2:
						if(room.getIsUserAReady()) {
							user.getAliveMessageMap().put("isEnemyReady", "true");
						}
						break;
				}
				user.getAliveMessageMap().put("whoCanPlay", room.getWhoCanPlay());
			}
			else {
				aliveJson.setAliveOk(false);
				return aliveJson;
			}
		}
		aliveJson.setAliveMessageMap(user.getAliveMessageMap());
		
		aliveJson.setAliveOk(true);
		
		
		return aliveJson;
	}

}
