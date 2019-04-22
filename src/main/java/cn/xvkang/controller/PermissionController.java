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

import cn.xvkang.entity.Permission;
import cn.xvkang.service.PermissionService;

@Controller
@RequestMapping("/permission")
public class PermissionController {

	@GetMapping("/selectAll")
	@ResponseBody
	public Map<String, Object> selectAll() {
		List<Permission> selectAll = permissionService.selectAll();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("msg", "ok");
		result.put("code", 0);

		result.put("data", selectAll);
		return result;
	}

	@GetMapping("/list")
	public String list() {

		return "permission/list";
	}

	@GetMapping("/addPage")
	public String addPage(String parentId, HttpServletRequest request) {
		if (StringUtils.isBlank(parentId)) {
			request.setAttribute("addFirstMenu", 1);
			
		} else {
			request.setAttribute("addFirstMenu", 0);
			request.setAttribute("parentPermission", permissionService.findById(Integer.parseInt(parentId)));
		}
		
		return "permission/addPage";
	}

	@PostMapping("/add")
	@ResponseBody
	public Map<String, Object> add(Permission permission) {
		
		Integer parentId = permission.getParentId();
		if (parentId != null) {

		} else {
			// 新增 顶级目录
			permission.setParentId(-1);

		}
		permissionService.add(permission);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("state", "200");
		return result;
	}

	
	@PostMapping("/delete")
	@ResponseBody
	public Map<String, Object> delete(Integer id) {
		
		permissionService.deleteById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("state", "200");
		result.put("message", "删除成功");
		return result;
	}
	@PostMapping("/checkCode")
	@ResponseBody
	public Map<String, Object> checkCode(String code) {
		Permission byCode = permissionService.getByCode(code);
		Map<String, Object> result = new HashMap<String, Object>();
		if(byCode!=null) {
			result.put("state", "2001");
		}else {
			result.put("state", "200");
		}
		return result;
	}

	@Autowired
	private PermissionService permissionService;

}
