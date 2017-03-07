package tictactoe.vat;

import java.util.HashMap;
import java.util.Map;

public class User {
	
	private int userId;
	private String userPassword;
	/**
	 * 
	 */
	private Map<String, Object> aliveMessageMap = new HashMap<>();
	
	{
		initAliveMessageMap();
	}
	
	public void initAliveMessageMap() {
		aliveMessageMap.put("isEnemyInTheRoom", "false");
		aliveMessageMap.put("isRoomClose", "false");
		aliveMessageMap.put("isReady", "false");
		aliveMessageMap.put("isEnemyReady", "false");
		aliveMessageMap.put("isBothReady", "false");
		aliveMessageMap.put("isWin", "false");
		aliveMessageMap.put("isTie", "false");
		aliveMessageMap.put("isLose", "false");
		aliveMessageMap.put("chessBoard", new int[3][3]);
		aliveMessageMap.put("whoCanPlay", false);
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public Map<String, Object> getAliveMessageMap() {
		return aliveMessageMap;
	}

	public void setAliveMessageMap(Map<String, Object> aliveMessageMap) {
		this.aliveMessageMap = aliveMessageMap;
	}

}
