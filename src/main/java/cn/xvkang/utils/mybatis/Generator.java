package cn.xvkang.utils.mybatis;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

public class Generator {
	public static void main(String[] args)
			throws IOException, XMLParserException, InvalidConfigurationException, SQLException, InterruptedException {
		List<String> warnings = new ArrayList<String>();
		boolean overwrite = true;

		// File configFile = new File(
		// "/Volumes/mac-bak/main-workspace/eclipse-workspace/mybatis-generator/generatorConfig-oracle.xml");
		// File configFile = new
		// File("/Volumes/mac-bak/main-workspace/eclipse-workspace/mybatis-generator/generatorConfig-oracle.xml");
		File configFile = new File(
				"/Users/wu/git-local-repository/ssm-spring-session-ok/src/main/java/cn/xvkang/utils/mybatis/generatorConfig-mysql.xml");

		ConfigurationParser cp = new ConfigurationParser(warnings);
		Configuration config = cp.parseConfiguration(configFile);
		DefaultShellCallback callback = new DefaultShellCallback(overwrite);
		MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
		myBatisGenerator.generate(null);
	}
}
