package tictactoe.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import tictactoe.json.ConnectJson;
import tictactoe.rule.VerifySource;
import tictactoe.vat.User;
import tictactoe.vat.Vat;

@Controller
public class ConnectController {
	
	@RequestMapping(value="/connect", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ConnectJson connect() {
		
		User user = Vat.addUser();
		ConnectJson connectJson = new ConnectJson();
		connectJson.setConnectOk(true);
		connectJson.setUserId(user.getUserId());
		connectJson.setUserPassword(user.getUserPassword());
		
		return connectJson;
	}

}
