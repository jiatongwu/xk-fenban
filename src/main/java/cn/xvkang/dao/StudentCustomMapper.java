package cn.xvkang.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.xvkang.dto.student.StudentExtendDto;

public interface StudentCustomMapper {
	@Update("delete from  student ")
	public void deleteAllStudent();

	@SelectProvider(type = StudentCustomMapperProvider.class, method = "findAll")
	public List<StudentExtendDto> findAll(@Param("name") String name, @Param("idcard") String idcard,
			@Param("phone") String phone, @Param("kemuzuheId") String kemuzuheId, @Param("isSelectKemuzuhe")String isSelectKemuzuhe);

	class StudentCustomMapperProvider {
		public String findAll(Map<String, Object> para) {
			String name = (String) para.get("name");
			String idcard = (String) para.get("idcard");
			String phone = (String) para.get("phone");
			String kemuzuheId = (String) para.get("kemuzuheId");
			String isSelectKemuzuhe = (String) para.get("isSelectKemuzuhe");

			SQL sql = new SQL() {
				{
					SELECT("s.*");
					SELECT("kz.name xuankezuheShortName");
					FROM("student s");
					LEFT_OUTER_JOIN("kemuzuhe kz on kz.id = s.select_kemuzuheid");
				}
			};

			if (StringUtils.isNotBlank(name)) {
				sql.WHERE(" s.name like #{name}");
			}
			if (StringUtils.isNotBlank(isSelectKemuzuhe)) {
				
				if(isSelectKemuzuhe.equals("1")){
					sql.WHERE(" s.select_kemuzuheid is not null ");
				}else if(isSelectKemuzuhe.equals("0")){
					sql.WHERE(" s.select_kemuzuheid is null");
				}
				
			}
			if (StringUtils.isNotBlank(idcard)) {
				sql.WHERE(" s.idcard like #{idcard}");
			}
			if (StringUtils.isNotBlank(phone)) {
				sql.WHERE(" s.phone like #{phone}");
			}
			if (StringUtils.isNotBlank(kemuzuheId)) {
				sql.WHERE(" s.select_kemuzuheid = #{kemuzuheId}");
			}

			return sql.toString();
		}
	}
}
