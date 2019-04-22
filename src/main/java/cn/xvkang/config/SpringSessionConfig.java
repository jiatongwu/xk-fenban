package cn.xvkang.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
public class SpringSessionConfig {//extends SessionEventHttpSessionListenerAdapter{
	
	 
//	@Override
//	public void onApplicationEvent(AbstractSessionEvent event) {
//		
//		super.onApplicationEvent(event);
//		
//	}


	private Logger logger=LoggerFactory.getLogger(SpringSessionConfig.class);
//	public SpringSessionConfig(List<HttpSessionListener> listeners) {
//		super(listeners);		
//		listeners.add(new SessionListener());
//		
//	}
	

	@Bean
     public CookieSerializer cookieSerializer() {
		logger.debug("cookieSerializer");
             DefaultCookieSerializer serializer = new DefaultCookieSerializer();
             serializer.setCookieName("JSESSIONID"); 
             serializer.setCookiePath("/"); 
             serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$"); 
             return serializer;
     }
	 
	 
	 
}
