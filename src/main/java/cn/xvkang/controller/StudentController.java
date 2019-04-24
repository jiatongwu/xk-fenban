package cn.xvkang.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import cn.xvkang.entity.Student;
import cn.xvkang.entity.User;
import cn.xvkang.service.StudentService;
import cn.xvkang.service.UserService;
import cn.xvkang.utils.Constants;
import cn.xvkang.utils.page.Page;

@Controller
@RequestMapping("/student")
public class StudentController {

	@Value("${templateFolderPath}")
	private String templateFolderPath;
	@Autowired
	private StudentService studentService;
	@Autowired
	private UserService userService;

	@GetMapping("/index")
	public String index(HttpServletRequest request) {
		request.setAttribute("route", "/student/index");
		return "student/index";
	}

	@GetMapping("/importExcelPage")
	public String importSportScorePage(HttpServletRequest request) {

		return "student/importExcelPage";
	}

	@GetMapping("/addPage")
	public String addPage(HttpServletRequest request) {

		return "student/addPage";
	}

	@PostMapping("/add")
	@ResponseBody
	public Map<String, Object> add(HttpServletRequest request, Student student) {
		Map<String, Object> result = new HashMap<>();
		student.setSelectKemuzuheAgain(0);
		int i = studentService.add(student);
		if (i > 0) {
			result.put("state", "ok");
			result.put("msg", "操作成功");

		} else {
			result.put("state", "fail");
			result.put("msg", "操作失败");
		}

		return result;
	}

	@GetMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request, String id) {
		Map<String, Object> result = new HashMap<>();
		result.put("state", "fail");
		result.put("msg", "操作失败");
		if (StringUtils.isNumeric(id)) {
			studentService.deleteById(Integer.parseInt(id));
			result.put("state", "ok");
			result.put("msg", "操作成功");
		}
		return result;
	}

	@GetMapping("/editPage")
	public String editPage(HttpServletRequest request, String id) {
		if (StringUtils.isNumeric(id)) {
			Student findById = studentService.findById(Integer.parseInt(id));
			request.setAttribute("student", findById);
		}
		return "student/editPage";
	}

	@GetMapping("/viewPage")
	public String viewPage(HttpServletRequest request, String id) {
		if (StringUtils.isNumeric(id)) {
			Student findById = studentService.findById(Integer.parseInt(id));
			request.setAttribute("student", findById);
		}
		return "student/viewPage";
	}

	@GetMapping("/idcardUnique")
	@ResponseBody
	public String idcardUnique(HttpServletRequest request, String idcard) {

		Student findById = studentService.findByIdcard(idcard);
		if (findById != null) {
			return "false";
		}
		return "true";

	}

	@GetMapping("/phoneUnique")
	@ResponseBody
	public String phoneUnique(HttpServletRequest request, String phone) {

		User findById = userService.findByUsername(phone);
		if (findById != null) {
			return "false";
		}
		return "true";

	}

	@PostMapping("/edit")
	@ResponseBody
	public Map<String, Object> edit(HttpServletRequest request, Student student) {
		Map<String, Object> result = new HashMap<>();
		
		int i = studentService.edit(student);
		if (i > 0) {
			result.put("state", "ok");
			result.put("msg", "操作成功");
		} else {
			result.put("state", "fail");
			result.put("msg", "操作失败");
		}

		return result;
	}

	@GetMapping("/table")
	@ResponseBody
	public Page<Student> table(String name, String idcard, String phone,
			@RequestParam(name = "page", required = false, defaultValue = "1") String pageNum,
			@RequestParam(name = "limit", required = false, defaultValue = "10") String pageSize,
			HttpServletRequest request) {
		Page<Student> table = studentService.table(name, idcard, phone, Integer.parseInt(pageNum),
				Integer.parseInt(pageSize));

		return table;
	}

	@PostMapping("/importExcel")
	@ResponseBody
	public Map<String, Object> importExcel(MultipartFile excel, String deleteOriginStudent) throws IOException {
		Map<String, Object> importExcel = studentService.importExcel(deleteOriginStudent, excel.getInputStream());
		return importExcel;
	}

	@GetMapping(value = "/importTemplateDownload", produces = Constants.EXCEL_XLSX_MIMETYPE)
	public ResponseEntity<Resource> importTemplateDownload() throws UnsupportedEncodingException {

		Resource file = null;
		try {
			file = new FileSystemResource(templateFolderPath + "importStudent2.xlsx");
			if (file.exists()) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=\"" + UriUtils.encode("导入学生信息.xlsx", "UTF-8")
										+ "\";\nfilename*=utf-8''" + UriUtils.encode("导入学生信息.xlsx", "UTF-8"))
						// .header(HttpHeaders.CONTENT_TYPE, "application/msword")
						.header("Set-Cookie", "fileDownload=true; path=/").body(file);
			} else {
				return ResponseEntity.notFound().header("Set-Cookie", "fileDownload=false; path=/").build();
			}
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.notFound().header("Set-Cookie", "fileDownload=false; path=/").build();
		}
	}

	@PostMapping("/selectKemuzuheAgain")
	@ResponseBody
	public Map<String, Object> setXuanke(String id, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		result.put("state", "fail");
		result.put("msg", "操作失败");
		if (StringUtils.isNumeric(id)) {

			Student updateStudent = new Student();
			updateStudent.setId(Integer.parseInt(id));
			updateStudent.setSelectKemuzuheAgain(1);
			studentService.updateByPrimaryKeySelective(updateStudent);
			result.put("state", "ok");
			result.put("msg", "操作成功");

		}
		return result;

	}

//	@GetMapping("/getClassesByXmidAndSchool")
//	@ResponseBody
//	public List<String> getClassesByXmidAndSchool(String xmid, String school) {
//		List<String> classes = studentService.getClassesByXmidAndSchool(xmid, school);
//		return classes;
//	}

}
