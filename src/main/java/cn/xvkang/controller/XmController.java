package cn.xvkang.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import cn.xvkang.entity.TXms;
import cn.xvkang.service.TxmsService;
import cn.xvkang.utils.Constants;
import cn.xvkang.utils.page.Page;

/**
 * 有多少项目（每一次考试算一个项目）
 * 
 * @author wu
 *
 */
@Controller
@RequestMapping("/xm")
public class XmController {
	@Autowired
	private TxmsService txmsService;
	@Value("${templateFolderPath}")
	private String templateFolderPath;

	@GetMapping("/index")
	public String index(HttpServletRequest request) {
		request.setAttribute("route", "/xm/index");
		return "xm/index";
	}

	@GetMapping("/importSportScorePage")
	public String importSportScorePage(HttpServletRequest request, String xmid) {
		request.setAttribute("xmid", xmid);
		return "xm/importSportScorePage";
	}
	@GetMapping("/exportSubPercentPage")
	public String exportSubPercentPage(HttpServletRequest request, String xmid) {
		request.setAttribute("xmid", xmid);
		List<String> schoolsByXmid = txmsService.getSchoolsByXmid(xmid);
		request.setAttribute("schools", schoolsByXmid);
		return "xm/exportSubPercentPage";
	}
	

	@GetMapping("/exportBjRankPage")
	public String exportBjRankPage(HttpServletRequest request, String xmid) {
		request.setAttribute("xmid", xmid);
		List<String> schoolsByXmid = txmsService.getSchoolsByXmid(xmid);
		request.setAttribute("schools", schoolsByXmid);
		return "xm/exportBjRankPage";
	}

	
	@GetMapping("/table")
	@ResponseBody
	public Page<TXms> table(String name,
			@RequestParam(name = "page", required = false, defaultValue = "1") String pageNum,
			@RequestParam(name = "limit", required = false, defaultValue = "10") String pageSize,
			HttpServletRequest request) {
		Page<TXms> table = txmsService.table(name, Integer.parseInt(pageNum), Integer.parseInt(pageSize));
		request.getSession().setAttribute("name","wu");

		return table;
	}

	@PostMapping("/importExcel")
	@ResponseBody
	public Map<String, Object> importExcel(MultipartFile excel, String xmid, String deleteOrigin) throws IOException {
		Map<String, Object> importExcel = txmsService.importExcel(xmid, deleteOrigin, excel.getInputStream());
		return importExcel;
	}

	@GetMapping(value = "/importTemplateDownload", produces = Constants.EXCEL_XLSX_MIMETYPE)
	public ResponseEntity<Resource> importTemplateDownload() throws UnsupportedEncodingException {

		Resource file = null;
		try {
			file = new FileSystemResource(templateFolderPath + "importSportScore.xlsx");
			if (file.exists()) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION,
								"attachment; filename=\"" + UriUtils.encode("导入体育成绩模板.xlsx", "UTF-8")
										+ "\";\nfilename*=utf-8''" + UriUtils.encode("导入体育成绩模板.xlsx", "UTF-8"))
						// .header(HttpHeaders.CONTENT_TYPE, "application/msword")
						.body(file);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

	/**
	 * 统计某一个项目 每个班内 考生 各科成绩 在班的排名 校的排名
	 */

	/**
	 * 根据项目id获取所有学校
	 */
	@GetMapping("/getSchoolsByXmid")
	@ResponseBody
	public List<String> getSchoolsByXmid(String xmid) {
		List<String> schoolsByXmid = txmsService.getSchoolsByXmid(xmid);
		return schoolsByXmid;
	}

	@GetMapping("/getClassesByXmidAndSchool")
	@ResponseBody
	public List<String> getClassesByXmidAndSchool(String xmid, String school) {
		List<String> classes = txmsService.getClassesByXmidAndSchool(xmid, school);
		return classes;
	}

	/**
	 * 根据项目id 学校 班级 导出班级内考生 各科成绩的排名
	 * 
	 * @param request
	 * @param xmid
	 * @param school
	 * @param bj
	 * @return
	 */
	@GetMapping(value = "/exportBjRank", produces = Constants.EXCEL_XLSX_MIMETYPE)
	public ResponseEntity<Resource> exportBjRank( String xmid, String school, String bj) {
		Resource file = null;
		try {
			file = txmsService.exportBjRank(xmid, school, bj);
			if (file == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + UriUtils.encode(school + "导出" + bj + "班学生各科成绩排名.xlsx", "UTF-8")
								+ "\";\nfilename*=utf-8''"
								+ UriUtils.encode(school + "导出" + bj + "班学生各科成绩排名.xlsx", "UTF-8"))
						.header("Set-Cookie", "fileDownload=true; path=/")
						.body(file);
			}
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.notFound().header("Set-Cookie", "fileDownload=false; path=/").build();
		}
	}
	/**
	 *导出各科　各级别　在各个班中的占比 
	 *@param request
	 * @param xmid
	 * @param school
	 * @param bj
	 * @return
	 */
	@GetMapping(value = "/exportSubPercent", produces = Constants.EXCEL_XLSX_MIMETYPE)
	public ResponseEntity<Resource> exportSubPercent( String xmid, String school) {
		Resource file = null;
		try {
			file = txmsService.exportSubPercent(xmid, school);
			if (file == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + UriUtils.encode(school + "导出各科各级别各班占比.xlsx", "UTF-8")
								+ "\";\nfilename*=utf-8''"
								+ UriUtils.encode(school + "导出各科各级别各班占比.xlsx", "UTF-8"))
						.header("Set-Cookie", "fileDownload=true; path=/")
						.body(file);
			}
		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.notFound().header("Set-Cookie", "fileDownload=false; path=/").build();
		}
	}
}
