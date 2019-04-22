package cn.xvkang.service;

import java.util.List;

import cn.xvkang.entity.Person;

public interface PersonService {
	public List<Person> selectAll();
	public Person selectById();
}
