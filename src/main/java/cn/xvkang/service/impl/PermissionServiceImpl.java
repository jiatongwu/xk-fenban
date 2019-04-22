package cn.xvkang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.xvkang.dao.PermissionMapper;
import cn.xvkang.entity.Permission;
import cn.xvkang.entity.PermissionExample;
import cn.xvkang.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {
	@Autowired
	private PermissionMapper permissionMapper;
	
	
	@Override
	public List<Permission> selectAll() {
		List<Permission> selectByExample = permissionMapper.selectByExample(null);
		return selectByExample;
	}


	@Override
	public void add(Permission p) {
		permissionMapper.insert(p);
		
	}


	@Override
	public Permission findById(Integer id) {
		return permissionMapper.selectByPrimaryKey(id);
	}


	@Override
	public Permission getByCode(String code) {
		PermissionExample example=new PermissionExample();
		example.createCriteria().andCodeEqualTo(code);
		List<Permission> selectByExample = permissionMapper.selectByExample(example);
		if(selectByExample!=null&&selectByExample.size()>0)
		{
			return selectByExample.get(0);
		}
		return null;
	}


	@Override
	public void deleteById(Integer id) {
		permissionMapper.deleteByPrimaryKey(id);
		
	}


}
