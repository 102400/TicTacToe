package tictactoe.controller;

import java.util.Iterator;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tictactoe.json.MatchJson;
import tictactoe.rule.Legal;
import tictactoe.rule.VerifySource;
import tictactoe.vat.Room;
import tictactoe.vat.User;
import tictactoe.vat.Vat;

@Controller
public class MatchController {
	
	@RequestMapping(value="/match", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public MatchJson match(int userId, String userPassword, boolean pawn) {  //pawn false:x, true:o
		MatchJson matchJson = new MatchJson();
		if(userPassword!=null&&!"".equals(userPassword)) {
			
			if(!Legal.isUserIdAndPasswordLegal(userId, userPassword)) {
				matchJson.setMatchOk(false);
				return matchJson;
			}
			
			synchronized (this) {
				User user = null;
				for(User u:Vat.getUserlist()) {
					if(u.getUserId()==userId) {
						user = u;
					}
				}
				if(user==null) {
					matchJson.setMatchOk(false);
					return matchJson;
				}
				Iterator<Room> each = Vat.getRoomlist().iterator();
				while(each.hasNext()) {
					Room room = each.next();
					int userAId = -11;
					int userBId = -11;
					if(room.getUserA()!=null) {
						userAId = room.getUserA().getUserId();
					}
					if(room.getUserB()!=null) {
						userBId = room.getUserB().getUserId();
					}
					long InactivityTime = System.currentTimeMillis() - room.getLastActivityTime();
					if(InactivityTime>1000*60*3) {  //room不活跃时间大于3分钟,则关闭房间
						if(room.getUserA()!=null) {
							room.getUserA().getAliveMessageMap().put("isRoomClose", "true");
						}
						if(room.getUserB()!=null) {
							room.getUserB().getAliveMessageMap().put("isRoomClose", "true");
						}
						each.remove();
						continue;
					}
//					else if(room.getUserA().getUserId()==userId || room.getUserB().getUserId()==userId) {  //销毁userId相同的房间
//					else if(new Integer(userId).equals(room.getUserA().getUserId()) || new Integer(userId).equals(room.getUserB().getUserId())) {  //销毁userId相同的房间
					else if(userAId==userId || userBId==userId) {
						if(room.getUserA()!=null) {
							room.getUserA().getAliveMessageMap().put("isRoomClose", "true");
						}
						if(room.getUserB()!=null) {
							room.getUserB().getAliveMessageMap().put("isRoomClose", "true");
						}
						each.remove();
						continue;
					}
					else {
						
						if(!room.getIsFull()&&(room.getWaitPawn()==pawn)) {
							room.setFull(true);
							if(pawn) {
								room.setUserB(user);
								room.getUserB().initAliveMessageMap();
							}
							else {
								room.setUserA(user);
								room.getUserA().initAliveMessageMap();
							}
							room.getUserA().getAliveMessageMap().put("isEnemyInTheRoom", "true");
							room.getUserB().getAliveMessageMap().put("isEnemyInTheRoom", "true");
							
							matchJson.setRoomId(room.getRoomId());
							matchJson.setRoomPassword(room.getRoomPassword());
							matchJson.setMatchOk(true);
//							return matchJson;  //不应立马return,必须扫描完数组处理数组中的不活跃与重复用户
						}
					}
				}
				if(!matchJson.getIsMatchOk()) {  //未匹配到,则新建房间
					Room room = Vat.addRoom();
					if(pawn) {
						room.setUserB(user);
						room.setWaitPawn(!pawn);
						room.getUserB().initAliveMessageMap();
					}
					else {
						room.setUserA(user);
						room.setWaitPawn(!pawn);
						room.getUserA().initAliveMessageMap();
					}
					room.setLastActivityTime(System.currentTimeMillis());
					
					matchJson.setRoomId(room.getRoomId());
					matchJson.setRoomPassword(room.getRoomPassword());
					matchJson.setMatchOk(true);
					return matchJson;
				}
				return matchJson;
			}
		}
		else {
			matchJson.setMatchOk(false);
			return matchJson;
		}
	}

}
