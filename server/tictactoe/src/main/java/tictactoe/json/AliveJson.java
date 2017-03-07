package tictactoe.json;

import java.util.HashMap;
import java.util.Map;

public class AliveJson {
	
	private boolean isAliveOk;
	private Map<String, Object> aliveMessageMap = new HashMap<>();
	//
//	private static int id = 10;
//	public int getId() {
//		return id++;
//	}
	//

	public boolean getIsAliveOk() {
		return isAliveOk;
	}

	public void setAliveOk(boolean isAliveOk) {
		this.isAliveOk = isAliveOk;
	}

	public Map<String, Object> getAliveMessageMap() {
		return aliveMessageMap;
	}

	public void setAliveMessageMap(Map<String, Object> aliveMessageMap) {
		this.aliveMessageMap = aliveMessageMap;
	}
	

}
