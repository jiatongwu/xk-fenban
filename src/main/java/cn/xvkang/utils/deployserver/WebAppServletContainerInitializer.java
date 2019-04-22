package cn.xvkang.utils.deployserver;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

public class WebAppServletContainerInitializer implements ServletContainerInitializer, ApplicationContextAware {
	private ApplicationContext applicationContext;

	@Override
	public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
		XmlWebApplicationContext rootWebAppContext = new XmlWebApplicationContext();
		rootWebAppContext.setConfigLocations("classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml");
		rootWebAppContext.setParent(applicationContext);
		ctx.addListener(new ContextLoaderListener(rootWebAppContext));

		FilterRegistration.Dynamic encodingFilter = ctx.addFilter("encoding-filter", CharacterEncodingFilter.class);
		encodingFilter.setInitParameter("encoding", "UTF-8");
		encodingFilter.setInitParameter("forceEncoding", "true");
		encodingFilter.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, "admin");

		FilterRegistration.Dynamic springSecurityFilterChain = ctx.addFilter("springSecurityFilterChain",
				DelegatingFilterProxy.class);
		springSecurityFilterChain.addMappingForServletNames(EnumSet.allOf(DispatcherType.class), false, "admin");

		ServletRegistration.Dynamic dispatcher = ctx.addServlet("dispatcherServlet", DispatcherServlet.class);
		dispatcher.setInitParameter("contextConfigLocation", "classpath:spring/spring-web.xml");
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping("/");

//        ServletRegistration.Dynamic dispatcher2 = ctx.addServlet("customer", DispatcherServlet.class);
//        dispatcher2.setLoadOnStartup(1);
//        dispatcher2.addMapping("/customer/*");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
}
