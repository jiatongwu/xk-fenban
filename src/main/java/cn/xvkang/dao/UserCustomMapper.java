package cn.xvkang.dao;

import org.apache.ibatis.annotations.Update;

public interface UserCustomMapper {
	@Update("delete  u from user u where u.username in (select s.phone from student s) ")
	public void deleteStudentAllUser() ;
		
	

}
