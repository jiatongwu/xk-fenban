package cn.xvkang.service;

import java.util.List;

import cn.xvkang.entity.Permission;

public interface PermissionService {
	public List<Permission> selectAll();
	public void add(Permission p);
	public Permission findById(Integer id);
	public Permission getByCode(String code);
	public void  deleteById(Integer id);
}
