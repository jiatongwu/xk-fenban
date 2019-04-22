package cn.xvkang.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xvkang.entity.User;
import cn.xvkang.service.UserService;
import cn.xvkang.utils.page.PageImpl;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/selectAll")
	@ResponseBody
	public PageImpl<User> selectAll(
			@RequestParam(name = "pageNumber", defaultValue = "1", required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = "1", required = false) Integer pageSize) {
		PageImpl<User> selectAll = userService.selectAll(pageNumber, pageSize);

		return selectAll;
	}

	@GetMapping("/list")
	public String list() {

		return "user/user_list";
	}

	@GetMapping("/addPage")
	public String addPage(String parentId, HttpServletRequest request) {
		

		return "user/addPage";
	}

	@PostMapping("/add")
	@ResponseBody
	public Map<String, Object> add(User user) {

		userService.add(user);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("state", "200");
		return result;
	}

	@PostMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(Integer id) {

		userService.deleteById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("state", "200");
		result.put("message", "删除成功");
		return result;
	}

}
