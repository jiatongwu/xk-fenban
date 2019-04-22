package cn.xvkang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.xvkang.dao.RoleMapper;
import cn.xvkang.entity.Role;
import cn.xvkang.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleMapper userMapper;
	
	
	@Override
	public List<Role> selectAll() {
		List<Role> selectByExample = userMapper.selectByExample(null);
		return selectByExample;
	}





	


	@Override
	public void deleteById(Integer id) {
		userMapper.deleteByPrimaryKey(id);
		
	}


	@Override
	public void add(Role p) {
		userMapper.insert(p);
		
	}


	@Override
	public Role findById(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}




}
