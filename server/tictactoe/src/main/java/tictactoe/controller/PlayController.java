package tictactoe.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tictactoe.json.PlayJson;
import tictactoe.rule.Legal;
import tictactoe.vat.Room;
import tictactoe.vat.User;
import tictactoe.vat.Vat;

@Controller
public class PlayController {
	
	@RequestMapping(value="/play", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public PlayJson play(int userId, String userPassword, int roomId, String roomPassword, int i, int j) {
		PlayJson playJson = new PlayJson();
		User user = null;
		Room room = null;
		
		if(!Legal.isUserIdAndPasswordLegal(userId, userPassword)) {
			playJson.setPlayOk(false);
			return playJson;
		}
		if(!Legal.isRoomIdAndPasswordLegal(roomId, roomPassword)) {
			playJson.setPlayOk(false);
			return playJson;
		}
		for(User u:Vat.getUserlist()) {
			if(u.getUserId()==userId) {
				user = u;
			}
		}
		for(Room r:Vat.getRoomlist()) {
			if(r.getRoomId()==roomId) {
				room = r;
				break;
			}
		}
		if(user==null || room==null) {
			playJson.setPlayOk(false);
			return playJson;
		}
		synchronized (this) {
			int step = room.getStep();
			step++;
			if(room.getUserA().getUserId()==user.getUserId()) {  //x
				if(room.getWhoCanPlay()==false) {
					
					if(room.getChessBoard()[i][j] == 0) {
						room.getChessBoard()[i][j] = 1;
						room.setStep(step);
						room.setLastActivityTime(System.currentTimeMillis());
						
						playJson.setPlayOk(true);
						judge(room, 1);
						room.setWhoCanPlay(true);
					}
				}
			}
			else if(room.getUserB().getUserId()==user.getUserId()) {  //o
				if(room.getWhoCanPlay()==true) {
					
					if(room.getChessBoard()[i][j] == 0) {
						room.getChessBoard()[i][j] = 2;
						room.setStep(step);
						room.setLastActivityTime(System.currentTimeMillis());
						
						playJson.setPlayOk(true);
						judge(room, 1);
						room.setWhoCanPlay(false);
					}
				}
			}
		}
		return playJson;
	}
	
	private void judge(Room room,int pawn) {
		room.getUserA().getAliveMessageMap().put("chessBoard", room.getChessBoard());
		room.getUserB().getAliveMessageMap().put("chessBoard", room.getChessBoard());
		
		int[][] mChessBoard = room.getChessBoard();
        if(mChessBoard[0][0]!=0&&mChessBoard[0][0]==mChessBoard[0][1]&&mChessBoard[0][1]==mChessBoard[0][2]) win(mChessBoard[0][0], room, pawn);
        else if(mChessBoard[1][0]!=0&&mChessBoard[1][0]==mChessBoard[1][1]&&mChessBoard[1][1]==mChessBoard[1][2]) win(mChessBoard[1][0], room, pawn);
        else if(mChessBoard[2][0]!=0&&mChessBoard[2][0]==mChessBoard[2][1]&&mChessBoard[2][1]==mChessBoard[2][2]) win(mChessBoard[2][0], room, pawn);
        else if(mChessBoard[0][0]!=0&&mChessBoard[0][0]==mChessBoard[1][0]&&mChessBoard[1][0]==mChessBoard[2][0]) win(mChessBoard[0][0], room, pawn);
        else if(mChessBoard[0][1]!=0&&mChessBoard[0][1]==mChessBoard[1][1]&&mChessBoard[1][1]==mChessBoard[2][1]) win(mChessBoard[0][1], room, pawn);
        else if(mChessBoard[0][2]!=0&&mChessBoard[0][2]==mChessBoard[1][2]&&mChessBoard[1][2]==mChessBoard[2][2]) win(mChessBoard[0][2], room, pawn);
        else if(mChessBoard[0][0]!=0&&mChessBoard[0][0]==mChessBoard[1][1]&&mChessBoard[1][1]==mChessBoard[2][2]) win(mChessBoard[0][0], room, pawn);
        else if(mChessBoard[2][0]!=0&&mChessBoard[2][0]==mChessBoard[1][1]&&mChessBoard[1][1]==mChessBoard[0][2]) win(mChessBoard[2][0], room, pawn);
        else if(room.getStep()==9) win(0, room, pawn);
    }

    private void win(int who, Room room, int pawn) {
        if(who==0) {
           room.getUserA().getAliveMessageMap().put("isTie", "true");
           room.getUserB().getAliveMessageMap().put("isTie", "true");
        }
        else if(who==1) {  //x win
        	room.getUserA().getAliveMessageMap().put("isWin", "true");
        	room.getUserB().getAliveMessageMap().put("isLose", "true");
        }
        else if(who==2) {  //o win
        	room.getUserB().getAliveMessageMap().put("isWin", "true");
        	room.getUserA().getAliveMessageMap().put("isLose", "true");
        }
        room.getUserA().getAliveMessageMap().put("isReady", "false");
        room.getUserB().getAliveMessageMap().put("isReady", "false");
        room.getUserA().getAliveMessageMap().put("isEnemyReady", "false");
        room.getUserB().getAliveMessageMap().put("isEnemyReady", "false");
        room.getUserA().getAliveMessageMap().put("isBothReady", "false");
        room.getUserB().getAliveMessageMap().put("isBothReady", "false");
        room.getUserA().getAliveMessageMap().put("whoCanPlay", "false");
        room.getUserB().getAliveMessageMap().put("whoCanPlay", "false");
        room.setStep(0);
        room.setChessBoard(new int[3][3]);
        room.setUserAReady(false);
        room.setUserBReady(false);
        room.setWhoCanPlay(false);
        room.setLastActivityTime(System.currentTimeMillis());
    }

}
