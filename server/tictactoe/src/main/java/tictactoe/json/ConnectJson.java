package tictactoe.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectJson {
	
	private boolean isConnectOk;
	private int userId;
	private String userPassword;
	
//	private Map<String, String> map = new HashMap<>();
//	{
//		map.put("key", "112");
//		map.put("abc", "0025");
//		map.put("key", "qwer");
//	}
//	public Map<String, String> getMap() {
//		return map;
//	}
	
	
	public boolean isConnectOk() {
		return isConnectOk;
	}
	public void setConnectOk(boolean isConnectOk) {
		this.isConnectOk = isConnectOk;
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

}
