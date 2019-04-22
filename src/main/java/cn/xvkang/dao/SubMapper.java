package cn.xvkang.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface SubMapper {
	@SelectProvider(type = SubMapperProvider.class, method = "selectAllSubByTableName")
	public List<Map<String, Object>> selectAllSubByTableName(@Param("tableName") String tableName);

	@SelectProvider(type = SubMapperProvider.class, method = "selectAllSubByTableName")
	@MapKey("subid")
	public Map<String, Map<String, Object>> selectAllSubByTableNameMap(@Param("tableName") String tableName);

	class SubMapperProvider {
		public String selectAllSubByTableName(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");
			StringBuilder sb = new StringBuilder();
			sb.append(" select * from  " + tableName);
			return sb.toString();
		}

	}
}
