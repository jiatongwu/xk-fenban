package cn.xvkang.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.jfinal.template.ext.spring.JFinalViewResolver;

import cn.xvkang.jfinal.directive.SchoolIdToNameDirective;

public class SpringUtils implements ApplicationContextAware {
	public SpringUtils() {
		JFinalViewResolver.engine.addDirective("idToNameTest", SchoolIdToNameDirective.class);
		
	}

	public static ApplicationContext ac;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringUtils.ac = applicationContext;
	}

}
