package cn.xvkang.dao;

import org.apache.ibatis.annotations.Update;

public interface StudentCustomMapper {
	@Update("delete from  student ")
	public void deleteAllStudent();

}
