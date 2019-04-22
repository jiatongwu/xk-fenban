package cn.xvkang.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import cn.xvkang.entity.Person;

public interface Person1Mapper {
	@Select("select * from person")
	public List<Person> selectAll();
	@Select("select * from person")
	public List<Map<String,Object>> selectListMap();
	

}
