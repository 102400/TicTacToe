package tictactoe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import tictactoe.vat.Vat;

@Controller
@RequestMapping(value="/status")
public class StatusController {
	
	@RequestMapping(value="/overview", method=RequestMethod.GET)
	public String overview() {
		
		return "status_overview";
	}
	
	@RequestMapping(value="/roomlist", method=RequestMethod.GET)
	public String roomList(Model model) {
		model.addAttribute("roomList", Vat.getRoomlist());
		return "status_roomlist";
	}
	
	@RequestMapping(value="/userlist", method=RequestMethod.GET)
	public String userList(Model model) {
		model.addAttribute("userList", Vat.getUserlist());
		return "status_userlist";
	}

}
