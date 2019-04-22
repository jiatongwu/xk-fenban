package cn.xvkang.service;

import cn.xvkang.entity.User;
import cn.xvkang.utils.page.PageImpl;

public interface UserService {
	public 	PageImpl<User> selectAll(Integer pageNum,Integer pageSize);
	public void add(User p);
	public User findById(Integer id);
	
	public void  deleteById(Integer id);
}
