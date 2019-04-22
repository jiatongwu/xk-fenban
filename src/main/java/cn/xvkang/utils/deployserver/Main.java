package cn.xvkang.utils.deployserver;

import java.util.Properties;

import org.springframework.core.io.FileSystemResource;

public class Main {

	public static void main(String[] args) throws Exception {	
		UndertowServer undertowServer = new UndertowServer();
		Properties prop = new Properties();
		prop.load(Main.class.getClassLoader().getResourceAsStream("undertow.properties"));
		String portString=(String)prop.get("undertow.port");
		undertowServer.setPort(Integer.parseInt(portString));
		WebAppServletContainerInitializer webAppServletContainerInitializer = new WebAppServletContainerInitializer();
		undertowServer.setServletContainerInitializer(webAppServletContainerInitializer);
		undertowServer.setWebAppName("");
		undertowServer.setWebAppRoot(new FileSystemResource("webapp"));
		undertowServer.afterPropertiesSet();
	}

}
