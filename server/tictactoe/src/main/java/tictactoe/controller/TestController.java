package tictactoe.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Deprecated
@Controller
public class TestController {
	
	@RequestMapping(value="/jsontest", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	/** @ResponseBody
	 * 该注解用于将Controller的方法返回的对象，通过适当的HttpMessageConverter转换为指定格式后，写入到Response对象的body数据区。
		使用时机：
      	返回的数据不是html标签的页面，而是其他某种格式的数据时（如json、xml等）使用；
	 */
	public String jsonTest() {
		
		return "jsonTest";
	}
	
	@RequestMapping(path="/people/{userId}", method=RequestMethod.GET)
	public String pathTest(@PathVariable("userId") int userId, Model model) {
		model.addAttribute("userId", userId);
		return "userid";
	}
	
	@RequestMapping(path="/q/{id}", method=RequestMethod.GET)
	public String paramTest(@PathVariable("id") int id, @RequestParam("q")int q,ModelMap model) {
		model.addAttribute("id", id);
		model.addAttribute("q", q);
		return "param";
	}

}
