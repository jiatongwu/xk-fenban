package cn.xvkang.dao;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface CalcZfScoreAllMapper {

	@SelectProvider(type = CalcZfScoreAllMapperProvider.class, method = "findZfNotNullCountBySchool")
	public int findZfNotNullCountBySchool(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName, @Param("school") String school);

	/**
	 * 根据分数排名 根据某个区间（从多少名到多少名）查询这个区间最高分和最低分
	 */
	@SelectProvider(type = CalcZfScoreAllMapperProvider.class, method = "findMaxScoreAndMinScoreBySchoolAndStartAndEnd")
	public Map<String, Object> findMaxScoreAndMinScoreBySchoolAndStartAndEnd(
			@Param("stuInfoTableName") String stuInfoTableName, @Param("scoreAllTableName") String scoreAllTableName,
			@Param("school") String school, @Param("start") String start, @Param("end") String end);

	/**
	 * 总分平均分
	 */
	@SelectProvider(type = CalcZfScoreAllMapperProvider.class, method = "findZfAvgBySchoolAndBj")
	public Double findZfAvgBySchoolAndBj(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName, @Param("school") String school,
			@Param("bj") String bj);

	/**
	 * 总分最高分
	 * 
	 * @param stuInfoTableName
	 * @param scoreAllTableName
	 * @param school
	 * @param bj
	 * @return
	 */
	@SelectProvider(type = CalcZfScoreAllMapperProvider.class, method = "findZfMaxBySchoolAndBj")
	public Double findZfMaxBySchoolAndBj(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName, @Param("school") String school,
			@Param("bj") String bj);

	/**
	 * 分数区间 各班占多少人
	 * 
	 * @author wu
	 *
	 */
	@SelectProvider(type = CalcZfScoreAllMapperProvider.class, method = "findStuCountByMaxScoreAndMinScore")
	public int findStuCountByMaxScoreAndMinScore(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName, @Param("school") String school,
			@Param("bj") String bj, @Param("maxScore") String maxScore, @Param("minScore") String minScore);

	class CalcZfScoreAllMapperProvider {
		public String findZfAvgBySchoolAndBj(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String bj = (String) para.get("bj");

			StringBuilder sb = new StringBuilder();
			sb.append(" select ROUND(avg(ROUND(ISNULL(sco.zf, 0 ),1)),1)  " + "from " + stuInfoTableName
					+ " st inner join " + scoreAllTableName + " sco  on st.examcode=sco.examcode   "
					+ "where st.school='" + school + "'  and sco.zf is not null  ");
			if (StringUtils.isNotBlank(bj)) {
				sb.append(" and st.bj='" + bj + "' ");
			}
			return sb.toString();
		}

		public String findStuCountByMaxScoreAndMinScore(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String bj = (String) para.get("bj");
			String maxScore = (String) para.get("maxScore");
			String minScore = (String) para.get("minScore");

			StringBuilder sb = new StringBuilder();
			sb.append("  select count(sco.examcode) from " + stuInfoTableName + " st " + "inner join "
					+ scoreAllTableName + " sco on " + "st.examcode = sco.examcode where  st.school='" + school
					+ "'  and sco.zf>=" + minScore + " and sco.zf<=" + maxScore + " ");
			if (StringUtils.isNotBlank(bj)) {
				sb.append(" and st.bj='" + bj + "'");
			}

			return sb.toString();
		}

		public String findZfMaxBySchoolAndBj(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String bj = (String) para.get("bj");
			StringBuilder sb = new StringBuilder();
			sb.append(" select ROUND(max(ROUND(ISNULL(sco.zf, 0 ),1)),1)  " + "from " + stuInfoTableName
					+ " st inner join " + scoreAllTableName + " sco  on st.examcode=sco.examcode   "
					+ "where st.school='" + school + "'  and sco.zf is not null  ");
			if (StringUtils.isNotBlank(bj)) {
				sb.append(" and st.bj='" + bj + "' ");
			}
			return sb.toString();
		}

		public String findZfNotNullCountBySchool(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");

			StringBuilder sb = new StringBuilder();
			sb.append(" select count(sco.examcode) " + "from " + stuInfoTableName + " st inner join "
					+ scoreAllTableName + " sco  on st.examcode=sco.examcode  " + "where st.school='" + school
					+ "'  and sco.zf is not null  ");
			return sb.toString();
		}

		public String findMaxScoreAndMinScoreBySchoolAndStartAndEnd(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String start = (String) para.get("start");
			String end = (String) para.get("end");

			StringBuilder sb = new StringBuilder();
			sb.append(" select max(tmp.zf) maxScore,min(tmp.zf) minScore  from  " + "(select "
					+ "	 Row_Number() OVER (ORDER BY sco.zf desc) rank,sco.zf,st.uname " + " from " + "	"
					+ stuInfoTableName + " st " + " inner join " + scoreAllTableName + " sco on "
					+ "	st.examcode = sco.examcode " + " where " + "	st.school = '" + school + "'  "
					+ "	and sco.zf is not null " + " ) tmp " + " where tmp.rank>=" + start + " and tmp.rank<=" + end
					+ " ");
			return sb.toString();
		}

	}
}
