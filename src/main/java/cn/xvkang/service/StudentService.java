package cn.xvkang.service;

import java.io.InputStream;
import java.util.Map;

import cn.xvkang.dto.student.StudentExtendDto;
import cn.xvkang.entity.Student;
import cn.xvkang.utils.page.Page;
import cn.xvkang.utils.page.PageImpl;

public interface StudentService {
	public PageImpl<Student> selectAll(Integer pageNum, Integer pageSize);

	public int add(Student p);

	public Student findById(Integer id);
	public Student findByIdcard(String idcard);
	
	public int updateByPrimaryKeySelective(Student s);

	public int deleteById(Integer id);

	public Page<StudentExtendDto> table(String name, String idcard, String phone, String kemuzuheId, String isSelectKemuzuhe, int pageNum, int pageSize);

	public Map<String, Object> importExcel(String deleteOrigin, InputStream inputStream);

	public int edit(Student student);
}
