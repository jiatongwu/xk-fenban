package cn.xvkang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.xvkang.dao.UserMapper;
import cn.xvkang.entity.User;
import cn.xvkang.service.UserService;
import cn.xvkang.utils.page.PageImpl;
import cn.xvkang.utils.page.PageRequest;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	
	
	@Override
	public 	PageImpl<User> selectAll(Integer pageNum,Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		List<User> selectByExample = userMapper.selectByExample(null);
		PageImpl<User> pageImpl = new PageImpl<User>(selectByExample,
				new PageRequest(pageNum - 1, pageSize), ((com.github.pagehelper.Page<User>) selectByExample).getTotal());
		return pageImpl;
	}





	


	@Override
	public void deleteById(Integer id) {
		userMapper.deleteByPrimaryKey(id);
		
	}


	@Override
	public void add(User p) {
		userMapper.insert(p);
		
	}


	@Override
	public User findById(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}




}
