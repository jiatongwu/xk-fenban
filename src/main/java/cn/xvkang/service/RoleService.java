package cn.xvkang.service;

import java.util.List;

import cn.xvkang.entity.Role;

public interface RoleService {
	public List<Role> selectAll();
	public void add(Role p);
	public Role findById(Integer id);
	
	public void  deleteById(Integer id);
}
