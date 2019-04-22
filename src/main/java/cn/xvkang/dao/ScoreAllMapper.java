package cn.xvkang.dao;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

public interface ScoreAllMapper {
	@SelectProvider(type = ScoreAllMapperProvider.class, method = "findByExamcode")
	public Map<String, Object> findByExamcode(@Param("tableName") String tableName, @Param("examcode") String examcode);

	@SelectProvider(type = ScoreAllMapperProvider.class, method = "findAll")
	public List<Map<String, Object>> findAll(@Param("tableName") String tableName);

	@SelectProvider(type = ScoreAllMapperProvider.class, method = "findStudentClassRank")
	@MapKey("examcode")
	public Map<String, Map<String, Object>> findStudentClassRank(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoAllTableName") String scoAllTableName, @Param("sportScoreTableName") String sportScoreTableName,
			@Param("school") String school, @Param("bj") String bj);

	@SelectProvider(type = ScoreAllMapperProvider.class, method = "findStudentSchoolRank")
	@MapKey("examcode")
	public Map<String, Map<String, Object>> findStudentSchoolRank(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoAllTableName") String scoAllTableName, @Param("sportScoreTableName") String sportScoreTableName,
			@Param("school") String school);

	@SelectProvider(type = ScoreAllMapperProvider.class, method = "findAllBySchoolAndBj")
	public List<Map<String, Object>> findAllBySchoolAndBj(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName,
			@Param("sportScoreTableName") String sportScoreTableName, @Param("school") String school,
			@Param("bj") String bj);

	@SelectProvider(type = ScoreAllMapperProvider.class, method = "findCountNotNullBySchoolAndClassAndKemuZiduan")
	public int findCountNotNullBySchoolAndClassAndKemuZiduan(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName, @Param("school") String school,
			@Param("bj") String bj, @Param("kemuZiduan") String kemuZiduan);

	/**
	 * 根据学校 项目 某一科目 查询有多少班级有这个科目
	 */
	@SelectProvider(type = ScoreAllMapperProvider.class, method = "findBjsBySchoolKemuZiduan")
	public List<String> findBjsBySchoolKemuZiduan(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName, @Param("school") String school,
			@Param("kemuZiduan") String kemuZiduan);

	/**
	 * 根据学校 项目 某一科目 查询有多少人有这个科目
	 */
	@SelectProvider(type = ScoreAllMapperProvider.class, method = "findStuCountBySchoolKemuZiduan")
	public int findStuCountBySchoolKemuZiduan(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName, @Param("school") String school,
			@Param("kemuZiduan") String kemuZiduan);

	/**
	 * 根据科目班级计算本班 平均分 最高分
	 * 
	 * @author wu
	 *
	 */
	@SelectProvider(type = ScoreAllMapperProvider.class, method = "findAvgByXmidAndSchoolAndbjAndSubid")
	public Double findBjAvgByXmidAndSchoolAndbjAndSubid(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName, @Param("school") String school,
			@Param("bj") String bj, @Param("kemuZiduan") String kemuZiduan);

	@SelectProvider(type = ScoreAllMapperProvider.class, method = "findMaxByXmidAndSchoolAndbjAndSubid")
	public Double findBjMaxByXmidAndSchoolAndbjAndSubid(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName, @Param("school") String school,
			@Param("bj") String bj, @Param("kemuZiduan") String kemuZiduan);

	/**
	 * 查询某一学校某一科不是空的 有多少学生 如果返回0就代表这个学校不考这一科 就没有必要统计占比了
	 */
	@SelectProvider(type = ScoreAllMapperProvider.class, method = "selectStuCountBySchoolAndKemuZiduan")
	public int selectStuCountBySchoolAndKemuZiduan(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("scoreAllTableName") String scoreAllTableName, @Param("school") String school,
			@Param("kemuZiduan") String kemuZiduan);

	/**
	 * 某一科成绩排名 取第n个人到第m个人（m和n有可能相等） 及这个区间内最高分最低分
	 * 
	 * @param stuInfoTableName
	 * @param scoreAllTableName
	 * @param school
	 * @param bj
	 * @param kemuZiduan
	 * @return
	 */
	@SelectProvider(type = ScoreAllMapperProvider.class, method = "selectNjMaxScoreMinScoreBySchoolAndKemuziduanAndStartAndEnd")
	public Map<String, Object> selectNjMaxScoreMinScoreBySchoolAndKemuziduanAndStartAndEnd(
			@Param("stuInfoTableName") String stuInfoTableName, @Param("scoreAllTableName") String scoreAllTableName,
			@Param("school") String school, @Param("kemuZiduan") String kemuZiduan,
			@Param("start") String start, @Param("end") String end);
	
	@SelectProvider(type = ScoreAllMapperProvider.class, method = "selectStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan")
	public int selectStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan(
			@Param("stuInfoTableName") String stuInfoTableName, @Param("scoreAllTableName") String scoreAllTableName,
			@Param("school") String school, @Param("bj") String bj, @Param("kemuZiduan") String kemuZiduan,
			@Param("maxScore") String maxScore, @Param("minScore") String minScore);
	@SelectProvider(type = ScoreAllMapperProvider.class, method = "selectNjStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan")
	public int selectNjStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan(
			@Param("stuInfoTableName") String stuInfoTableName, @Param("scoreAllTableName") String scoreAllTableName,
			@Param("school") String school, @Param("kemuZiduan") String kemuZiduan,
			@Param("maxScore") String maxScore, @Param("minScore") String minScore);

	class ScoreAllMapperProvider {
		public String selectNjStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String kemuZiduan = (String) para.get("kemuZiduan");
			String maxScore = (String) para.get("maxScore");
			String minScore = (String) para.get("minScore");

			StringBuilder sb = new StringBuilder();
			sb.append(" select count(sco.examcode) from "+stuInfoTableName+" st " + 
					"inner join "+scoreAllTableName+" sco on " + 
					"st.examcode = sco.examcode where  st.school='"+school+"'  and sco."+kemuZiduan+">="+minScore+" and sco."+kemuZiduan+"<="+maxScore+" ");
			return sb.toString();
		}
		public String selectStuCountBySchoolAndBjAndMinScoreAndMaxScoreAndKemuziduan(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String bj = (String) para.get("bj");
			String kemuZiduan = (String) para.get("kemuZiduan");
			String maxScore = (String) para.get("maxScore");
			String minScore = (String) para.get("minScore");

			StringBuilder sb = new StringBuilder();
			sb.append(" select count(sco.examcode) from "+stuInfoTableName+" st " + 
					"inner join "+scoreAllTableName+" sco on " + 
					"st.examcode = sco.examcode where  st.school='"+school+"' and st.bj='"+bj+"' and sco."+kemuZiduan+">="+minScore+" and sco."+kemuZiduan+"<="+maxScore+" ");
			return sb.toString();
		}
		public String selectNjMaxScoreMinScoreBySchoolAndKemuziduanAndStartAndEnd(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String kemuZiduan = (String) para.get("kemuZiduan");
			String start = (String) para.get("start");
			String end = (String) para.get("end");

			StringBuilder sb = new StringBuilder();
			sb.append(" select max(tmp." + kemuZiduan + ") maxScore,min(tmp." + kemuZiduan + ") minScore  from  "
					+ "(select " + "	 Row_Number() OVER (ORDER BY sco." + kemuZiduan + " desc) rank,sco."
					+ kemuZiduan + ",st.uname " + " from " + "	" + stuInfoTableName + " st " + " inner join "
					+ scoreAllTableName + " sco on " + "	st.examcode = sco.examcode " + " where "
					+ "	st.school = '" + school + "'  " + " and sco."+kemuZiduan+" is not null	and st.bj in ( " + "	select "
					+ "		stst.bj " + "	from " + "		" + stuInfoTableName + " stst " + "	inner join "
					+ scoreAllTableName + " scosco on " + "		stst.examcode = scosco.examcode " + "	where "
					+ "		stst.school = '" + school + "' " + "		and scosco." + kemuZiduan + " is not null "
					+ "	group by " + "		stst.bj )) tmp " + "		where tmp.rank>=" + start + " and tmp.rank<="
					+ end + " ");
			return sb.toString();
		}

		public String selectStuCountBySchoolAndKemuZiduan(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String kemuZiduan = (String) para.get("kemuZiduan");

			StringBuilder sb = new StringBuilder();
			sb.append(" select " + "	count(scosco.examcode) " + "	from " + "		" + stuInfoTableName + " stst "
					+ "	inner join " + scoreAllTableName + " scosco on " + "		stst.examcode = scosco.examcode "
					+ "	where " + "		stst.school = '" + school + "' " + "		and scosco." + kemuZiduan
					+ " is not null; ");
			return sb.toString();
		}

		public String findBjsBySchoolKemuZiduan(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String kemuZiduan = (String) para.get("kemuZiduan");

			StringBuilder sb = new StringBuilder();
			sb.append(" select st.bj from " + stuInfoTableName + " st inner join " + scoreAllTableName
					+ " sco  on st.examcode=sco.examcode  " + "where st.school='" + school + "'  and sco." + kemuZiduan
					+ " is not null group by st.bj ");
			return sb.toString();
		}

		public String findStuCountBySchoolKemuZiduan(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String kemuZiduan = (String) para.get("kemuZiduan");

			StringBuilder sb = new StringBuilder();
			sb.append("select " + "	count(sco.examcode) " + " from " + "	" + stuInfoTableName + " st "
					+ " inner join " + scoreAllTableName + " sco on " + "	st.examcode = sco.examcode " + " where "
					+ "	st.school = '" + school + "' " + " and sco." + kemuZiduan + " is not null	and st.bj in ( "
					+ "	select " + "		stst.bj " + "	from " + "		" + stuInfoTableName + " stst "
					+ "	inner join " + scoreAllTableName + " scosco on " + "		stst.examcode = scosco.examcode "
					+ "	where " + "		stst.school = '" + school + "' " + "		and scosco." + kemuZiduan
					+ " is not null " + "	group by " + "		stst.bj ) ");
			return sb.toString();
		}

		public String findCountNotNullBySchoolAndClassAndKemuZiduan(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String bj = (String) para.get("bj");
			String kemuZiduan = (String) para.get("kemuZiduan");

			StringBuilder sb = new StringBuilder();
			sb.append(" select count(sco.examcode) from " + stuInfoTableName + " st inner join " + scoreAllTableName
					+ " sco  on st.examcode=sco.examcode  " + "where st.school='" + school + "' and st.bj='" + bj
					+ "' and sco." + kemuZiduan + " is not null ;");
			return sb.toString();
		}

		public String findAvgByXmidAndSchoolAndbjAndSubid(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String bj = (String) para.get("bj");
			String kemuZiduan = (String) para.get("kemuZiduan");
			StringBuilder sb = new StringBuilder();
			sb.append(" select ROUND(avg(ROUND(ISNULL(sco." + kemuZiduan + ", 0 ),1)),1) " + "		from "
					+ stuInfoTableName + " st inner join " + scoreAllTableName + " sco  on st.examcode=sco.examcode  "
					+ "		where st.school='" + school + "'  " + " and sco." + kemuZiduan
					+ " is not null ");
			if(StringUtils.isNotBlank(bj)) {
				sb.append("and st.bj='" + bj + "'");
			}
			return sb.toString();
		}

		public String findMaxByXmidAndSchoolAndbjAndSubid(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String school = (String) para.get("school");
			String bj = (String) para.get("bj");
			String kemuZiduan = (String) para.get("kemuZiduan");
			StringBuilder sb = new StringBuilder();
			sb.append(" select ROUND(max(ROUND(ISNULL(sco." + kemuZiduan + ", 0 ),1)),1) " + "		from "
					+ stuInfoTableName + " st inner join " + scoreAllTableName + " sco  on st.examcode=sco.examcode  "
					+ "		where st.school='" + school + "'  ");
			if(StringUtils.isNotBlank(bj)) {
				sb.append("and st.bj='" + bj + "'");
			}
			return sb.toString();
		}

		public String findByExamcode(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");
			String examcode = (String) para.get("examcode");

			StringBuilder sb = new StringBuilder();
			sb.append(" select * from  " + tableName + " where examcode='" + examcode + "'");
			return sb.toString();
		}

		public String findAll(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");

			StringBuilder sb = new StringBuilder();
			sb.append(" select * from  " + tableName);
			return sb.toString();
		}

		public String findStudentClassRank(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoAllTableName = (String) para.get("scoAllTableName");
			String sportScoreTableName = (String) para.get("sportScoreTableName");
			String school = (String) para.get("school");
			String bj = (String) para.get("bj");

			StringBuilder sb = new StringBuilder();
			sb.append(
					"select  rank() OVER (ORDER BY (sco.zf+ ISNULL(sportSco.score, 0 )) desc) rank,sco.examcode,(sco.zf+ISNULL(sportSco.score, 0 )) sumScore "
							+ "		from " + stuInfoTableName + " st inner join " + scoAllTableName
							+ " sco  on st.examcode=sco.examcode " + "		left join " + sportScoreTableName
							+ " sportSco on sportSco.examcode=sco.examcode " + "		where st.school='" + school
							+ "' and st.bj='" + bj + "' ");
			return sb.toString();
		}

		public String findStudentSchoolRank(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoAllTableName = (String) para.get("scoAllTableName");
			String sportScoreTableName = (String) para.get("sportScoreTableName");
			String school = (String) para.get("school");

			StringBuilder sb = new StringBuilder();
			sb.append(
					"select  rank() OVER (ORDER BY (sco.zf+ISNULL(sportSco.score, 0 )) desc) rank,sco.examcode,(sco.zf+ISNULL(sportSco.score, 0 )) sumScore "
							+ "		from " + stuInfoTableName + " st inner join " + scoAllTableName
							+ " sco  on st.examcode=sco.examcode " + "		left join " + sportScoreTableName
							+ " sportSco on sportSco.examcode=sco.examcode " + "		where st.school='" + school
							+ "' ");

			return sb.toString();
		}

		public String findAllBySchoolAndBj(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String scoreAllTableName = (String) para.get("scoreAllTableName");
			String sportScoreTableName = (String) para.get("sportScoreTableName");
			String school = (String) para.get("school");
			String bj = (String) para.get("bj");

			StringBuilder sb = new StringBuilder();
			sb.append(" select st.*,sco.*,ISNULL(sportScore.score, 0 ) sportScore from " + stuInfoTableName
					+ " st inner join " + scoreAllTableName + " sco on st.examcode=sco.examcode " + " left join "
					+ sportScoreTableName + " sportScore on sportScore.examcode=sco.examcode where st.school='" + school
					+ "' and st.bj='" + bj + "' ");
			return sb.toString();
		}

	}
}
