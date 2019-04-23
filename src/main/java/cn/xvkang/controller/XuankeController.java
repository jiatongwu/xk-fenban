package cn.xvkang.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xvkang.dto.login.LoginUserInformation;
import cn.xvkang.entity.Kemuzuhe;
import cn.xvkang.entity.Student;
import cn.xvkang.service.KemuzuheService;
import cn.xvkang.service.StudentService;

@Controller
@RequestMapping("/xuanke")
public class XuankeController {

	@Autowired
	private KemuzuheService kemuzuheService;
	@Autowired
	private StudentService studentService;

	@GetMapping("/index")
	public String index(HttpServletRequest request) {
		Map<String, List<Kemuzuhe>> findAllByCategory = kemuzuheService.findAllByCategory();
		request.setAttribute("findAllByCategory", findAllByCategory);
		Object attribute = request.getSession().getAttribute(LoginController.LOGIN_USER_INFORMATION_SESSION_KEY);
		if (attribute != null) {
			LoginUserInformation loginUserInformation = (LoginUserInformation) attribute;
			Student studentSession = loginUserInformation.getStudent();

			if (studentSession != null) {
				Student student = studentService.findById(studentSession.getId());
				request.setAttribute("student", student);
				Kemuzuhe studentXuankan = kemuzuheService.findById(student.getSelectKemuzuheid());
				request.setAttribute("studentXuankan", studentXuankan);
			}

		}
		request.setAttribute("route", "/xuanke/index");
		return "xuanke/index";
	}

	@PostMapping("/setXuanke")
	@ResponseBody
	public Map<String, Object> setXuanke(String select, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		result.put("state", "fail");
		result.put("msg", "选科失败");
		if (StringUtils.isNotBlank(select)) {
			Object attribute = request.getSession().getAttribute(LoginController.LOGIN_USER_INFORMATION_SESSION_KEY);
			if (attribute != null) {
				LoginUserInformation loginUserInformation = (LoginUserInformation) attribute;
				Student studentSession = loginUserInformation.getStudent();
				Integer id = studentSession.getId();
				Student updateStudent = new Student();
				updateStudent.setId(id);
				Kemuzuhe selectKemuzuhe = kemuzuheService.findByCode(select);
				if (selectKemuzuhe != null) {
					updateStudent.setSelectKemuzuheid(selectKemuzuhe.getId());
					updateStudent.setSelectKemuzuheAgain(0);
					studentService.updateByPrimaryKeySelective(updateStudent);
					result.put("state", "ok");
					result.put("msg", "选科成功");
				}
			}
		}
		return result;

	}
	@PostMapping("/getKemuzuheByCode")
	@ResponseBody
	public Kemuzuhe getKemuzuheByCode(String select, HttpServletRequest request) {
	Kemuzuhe findByCode = kemuzuheService.findByCode(select);
		return findByCode;

	}

}
