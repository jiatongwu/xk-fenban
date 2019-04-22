package cn.xvkang.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface SubExtMapper {
	@SelectProvider(type = SubExtMapperProvider.class, method = "selectNjAvgAndMaxScore")
	public Map<String, Object> selectNjAvgAndMaxScore(@Param("tableName") String tableName,@Param("school") String school,
			@Param("subid") String subid);

	class SubExtMapperProvider {
		public String selectNjAvgAndMaxScore(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");
			String school = (String) para.get("school");
			String subid = (String) para.get("subid");
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from "+tableName+" where school='"+school+"' and subid='"+subid+"' and type='N' ");
			return sb.toString();
		}

	}
}
