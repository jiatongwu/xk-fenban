package cn.xvkang.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface StuInfoMapper {
	@SelectProvider(type = StuInfoMapperProvider.class, method = "findAllSchool")
	public List<String> findAllSchool(@Param("tableName") String tableName);

	@SelectProvider(type = StuInfoMapperProvider.class, method = "findAllClazz")
	public List<String> findAllClazz(@Param("tableName") String tableName, @Param("schoolName") String schoolName);
	

	/**
	 * 统计本班人数
	 * 
	 * @author wu
	 *
	 */
	@SelectProvider(type = StuInfoMapperProvider.class, method = "findStuCount")
	public int findStuCount(@Param("tableName") String tableName, @Param("schoolName") String schoolName,
			@Param("bj") String bj);
	/**
	 * 统计本年级人数
	 * 
	 * @author wu
	 *
	 */
	@SelectProvider(type = StuInfoMapperProvider.class, method = "findNjStuCount")
	public int findNjStuCount(@Param("tableName") String tableName, @Param("schoolName") String schoolName);

	class StuInfoMapperProvider {
		public String findAllSchool(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");

			StringBuilder sb = new StringBuilder();
			sb.append(" select school from " + tableName + " group by school;");
			return sb.toString();
		}

		public String findStuCount(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");
			String schoolName = (String) para.get("schoolName");
			String bj = (String) para.get("bj");

			StringBuilder sb = new StringBuilder();
			sb.append(" select count( examcode) from " + tableName + " where school='" + schoolName + "' and bj='" + bj
					+ "' ");
			return sb.toString();
		}

		public String findNjStuCount(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");
			String schoolName = (String) para.get("schoolName");

			StringBuilder sb = new StringBuilder();
			sb.append(" select count( examcode) from " + tableName + " where school='" + schoolName 
					+ "' ");
			return sb.toString();
		}
		public String findAllClazz(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");
			String schoolName = (String) para.get("schoolName");
			StringBuilder sb = new StringBuilder();
			sb.append(" select bj from " + tableName + " where school='" + schoolName + "' group by bj order by bj asc");
			return sb.toString();
		}

	}
}
