package cn.xvkang.config;

import java.io.InputStream;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

@Configuration
public class YmlPropertiesConfig {
	@Bean
	public  Map<String,Object> ymlMap() {
		Yaml yaml = new Yaml();
		InputStream inputStream = this.getClass()
		  .getClassLoader()
		  .getResourceAsStream("yml/interceptorUrl.yml");
		Map<String, Object> obj = yaml.load(inputStream);
		return obj;
	}

}
