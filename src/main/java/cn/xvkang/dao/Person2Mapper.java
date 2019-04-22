package cn.xvkang.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

public interface Person2Mapper {
	
	@Select("select * from person")
	public List<Map<String,Object>> selectAll();

}
