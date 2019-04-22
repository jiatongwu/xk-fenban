package cn.xvkang.dao;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

public interface SportScoreMapper {
	@UpdateProvider(type = SportScoreProvider.class, method = "createSportScoreTable")
	public void createSportScoreTable(@Param("tableName") String tableName);

	@UpdateProvider(type = SportScoreProvider.class, method = "saveOrUpdate")
	public void saveOrUpdate(@Param("tableName") String tableName, @Param("examcode") String examcode,
			@Param("score") String score);

	@UpdateProvider(type = SportScoreProvider.class, method = "deleteAll")
	public void deleteAll(@Param("tableName") String tableName);

	/**
	 * 根据班级查询本班体育平均分
	 * 
	 * @param stuInfoTableName
	 * @param sportScoreTableName
	 * @param school
	 * @param bj
	 * @return
	 */
	@SelectProvider(type = SportScoreProvider.class, method = "findSportAvgByBj")
	public Double findSportAvgByBj(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("sportScoreTableName") String sportScoreTableName, @Param("school") String school,
			@Param("bj") String bj);
	@SelectProvider(type = SportScoreProvider.class, method = "findSportAvg")
	public Double findSportAvg(@Param("stuInfoTableName") String stuInfoTableName,
			@Param("sportScoreTableName") String sportScoreTableName, @Param("school") String school
			);

	class SportScoreProvider {
		public String createSportScoreTable(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");

			StringBuilder sb = new StringBuilder();
			sb.append(" IF NOT EXISTS(SELECT * FROM sysobjects WHERE name='tableName') " + " CREATE TABLE tableName ( "
					+ "	id int NOT NULL IDENTITY(1,1)  PRIMARY KEY , " + "	examcode varchar(20) UNIQUE, "
					+ "	score real	 " + ") ");

			String replace = StringUtils.replace(sb.toString(), "tableName", tableName);
			return replace;

		}

		public String findSportAvgByBj(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String sportScoreTableName = (String) para.get("sportScoreTableName");
			String school = (String) para.get("school");
			String bj = (String) para.get("bj");
			StringBuilder sb = new StringBuilder();
			sb.append(" select " + "	ROUND(avg(ROUND(ISNULL(sportSco.score, 0 ),1)),1) " + "from " + "	"
					+ stuInfoTableName + " st " + "left join " + sportScoreTableName + " sportSco on "
					+ "	sportSco.examcode = st.examcode " + "where st.school='" + school + "' "
							+ "  and sportSco.score is not null ");
			if(StringUtils.isNotBlank(bj)) {
				sb.append(" and st.bj='" + bj + "'");
			}
			return sb.toString();
		}
		public String findSportAvg(Map<String, Object> para) {
			String stuInfoTableName = (String) para.get("stuInfoTableName");
			String sportScoreTableName = (String) para.get("sportScoreTableName");
			String school = (String) para.get("school");
			StringBuilder sb = new StringBuilder();
			sb.append(" select " + "	ROUND(avg(ROUND(ISNULL(sportSco.score, 0 ),1)),1) " + "from " + "	"
					+ stuInfoTableName + " st " + "left join " + sportScoreTableName + " sportSco on "
					+ "	sportSco.examcode = st.examcode " + "where st.school='" + school + "'  "
							+ "  and sportSco.score is not null ");
			return sb.toString();
		}

		public String deleteAll(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");

			StringBuilder sb = new StringBuilder();
			sb.append(" delete from  " + tableName);
			return sb.toString();

		}

		public String saveOrUpdate(Map<String, Object> para) {
			String tableName = (String) para.get("tableName");
			String examcode = (String) para.get("examcode");
			String score = (String) para.get("score");

			StringBuilder sb = new StringBuilder();
			sb.append(" IF NOT EXISTS(SELECT * FROM tableName where examcode='examcodeValue') " + " BEGIN  "
					+ "   insert into tableName(examcode,score) values('examcodeValue',scoreValue);   " + " END  "
					+ " ELSE  " + " BEGIN "
					+ "     update tableName set score=scoreValue where examcode='examcodeValue'; " + " END ");
			String string = sb.toString();

			String replace = StringUtils.replace(string, "tableName", tableName);
			String replace2 = StringUtils.replace(replace, "examcodeValue", examcode);
			String replace3 = StringUtils.replace(replace2, "scoreValue", score);
			return replace3;
		}
	}
}
