package cn.xvkang.dao;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(value=
// {"/spring/spring-mapper.xml","/spring/spring-service.xml","/spring/spring-web.xml"})
@ContextConfiguration(value = { "/spring/spring-dao.xml" })
public class SportScoreMapperTest {
//	@Autowired
//	private SportScoreMapper sportScoreMapper;

	// @Autowired
	// private WebApplicationContext applicationContext;
	// @Autowired
	// private MockServletContext servletContext;
//	@Test
//	public void createSportScoreTableTest() {		
//		sportScoreMapper.createSportScoreTable("T0011"+Constants.SPORT_SCORE_SUFFIX);
//	}
//	@Test
//	public void saveOrUpdateTest() {
//		sportScoreMapper.saveOrUpdate("T0011"+Constants.SPORT_SCORE_SUFFIX,"examcode","8888888");
//	}
//	@Test
//	public void deleteAllTest() {
//		sportScoreMapper.deleteAll("T0011"+Constants.SPORT_SCORE_SUFFIX);
//	}

	
}
