package cn.xvkang.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.xvkang.dao.Person1Mapper;
import cn.xvkang.dao.Person2Mapper;
import cn.xvkang.dao.PersonMapper;
import cn.xvkang.entity.Person;
import cn.xvkang.service.PersonService;

@Service
public class PersonServiceImpl implements PersonService {
	@Autowired
	private PersonMapper personMapper;
	@Autowired
	private Person1Mapper person1Mapper;
	@Autowired
	private Person2Mapper person2Mapper;
	
	@Override
	public List<Person> selectAll() {
		List<Person> selectAll = person1Mapper.selectAll();
		System.out.println(selectAll);
		PageHelper.startPage(1, 1);
		List<Map<String,Object>> selectAll2 = person2Mapper.selectAll();
		System.out.println(selectAll2);
		
		List<Map<String,Object>> selectListMap = person1Mapper.selectListMap();
		System.out.println(selectListMap);
		
		Person p=new Person();
		p.setName("name88测试");
		personMapper.insert(p);
		
		return personMapper.selectByExample(null);
	}

	@Override
	public Person selectById() {
		Person person = personMapper.selectByPrimaryKey(1);
		return person;
	}

}
