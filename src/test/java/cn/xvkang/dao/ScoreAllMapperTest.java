package cn.xvkang.dao;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import cn.xvkang.utils.Constants;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(value=
// {"/spring/spring-mapper.xml","/spring/spring-service.xml","/spring/spring-web.xml"})
@ContextConfiguration(value = { "/spring/spring-dao.xml" })
public class ScoreAllMapperTest {
	@Autowired
	private ScoreAllMapper scoreAllMapper;

	// @Autowired
	// private WebApplicationContext applicationContext;
	// @Autowired
	// private MockServletContext servletContext;
	@Test
	public void findByExamcodeTest() {
		Map<String, Object> findByExamcode = scoreAllMapper.findByExamcode("T0011" + Constants.SCOREAll_SUFFIX,
				"303220511201");
		System.out.println(findByExamcode);
	}

	@Test
	public void findStudentSchoolRankest() {
		Map<String, Map<String, Object>> findByExamcode = scoreAllMapper.findStudentSchoolRank(
				"T0011" + Constants.STUINFO_SUFFIX, "T0011" + Constants.SCOREAll_SUFFIX,
				"T0011" + Constants.SPORT_SCORE_SUFFIX, "建平中学");
		System.out.println(findByExamcode);
	}

	@Test
	public void findStudentClassRankTest() {
		Map<String, Map<String, Object>> findByExamcode = scoreAllMapper.findStudentClassRank(
				"T0011" + Constants.STUINFO_SUFFIX, "T0011" + Constants.SCOREAll_SUFFIX,
				"T0011" + Constants.SPORT_SCORE_SUFFIX, "建平中学", "1");
		System.out.println(findByExamcode);
	}

	@Test
	public void findCountNotNullBySchoolAndClassAndKemuZiduanTest() {
		int findByExamcode = scoreAllMapper.findCountNotNullBySchoolAndClassAndKemuZiduan(
				"T0011" + Constants.STUINFO_SUFFIX, "T0011" + Constants.SCOREAll_SUFFIX, "建平中学", "3", "x165");
		System.out.println(findByExamcode);
	}

	

}
