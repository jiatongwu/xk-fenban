package cn.xvkang.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xvkang.h2.dao.StudentDao;
import cn.xvkang.h2.entity.Student;
import cn.xvkang.service.PersonService;

@Controller
public class IndexController {
	
	
	@Autowired
	private StudentDao studentDao;
	
	

	
	
	
	@SuppressWarnings("unused")
	@Autowired
	private PersonService personService;
	
	@GetMapping("/index")
	public String index(HttpServletRequest request) {
//		System.out.println(request.getSession().getId());
//		request.setAttribute("name","namenamenamename");
//		List<Person> persons = personService.selectAll();
//		request.getSession().setAttribute("name", "wu");
//		Person person = personService.selectById();
//		request.setAttribute("person", person);
//		request.setAttribute("persons", persons);
		//personService.selectAll();
		return "index/index";
	}
	@GetMapping("/ok")
	@ResponseBody
	public List<Student> ok(HttpServletRequest request) {
		System.out.println(request.getSession().getId());
		Student s=new Student();
		s.setName("test");
		studentDao.save(s);
		List<Student> selectAll = studentDao.selectAll();
		return selectAll;
	}
}
