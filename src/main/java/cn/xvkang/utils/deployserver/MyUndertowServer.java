package cn.xvkang.utils.deployserver;

import java.io.File;
import java.util.Collections;
import java.util.Set;

import javax.servlet.ServletContainerInitializer;
import javax.servlet.ServletException;

import org.springframework.web.SpringServletContainerInitializer;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainerInitializerInfo;
import io.undertow.servlet.api.ServletStackTraces;

public class MyUndertowServer {
	public static Undertow buildUndertowServer(int port, String address, String contextPath,
			Class<? extends ServletContainerInitializer> servletContainerInitializerClass, Set<Class<?>> initializers,
			ClassLoader classLoader) throws ServletException {

		ServletContainerInitializerInfo servletContainerInitializerInfo = new ServletContainerInitializerInfo(
				servletContainerInitializerClass, initializers);
		DeploymentInfo deployment = Servlets.deployment();
		deployment.addServletContainerInitalizer(servletContainerInitializerInfo);
		deployment.setClassLoader(classLoader);
		deployment.setContextPath(contextPath);
		//deployment.setDisplayName("adam");
		deployment.setDeploymentName("test");
		deployment.setServletStackTraces(ServletStackTraces.ALL);
		deployment.setContextPath("");
		deployment.setResourceManager(new FileResourceManager(new File("webapp")));
		DeploymentManager manager = Servlets.newContainer().addDeployment(deployment);
		manager.deploy();
		Undertow.Builder builder = Undertow.builder();
		builder.addHttpListener(port, address);
		HttpHandler httpHandler = manager.start();
		httpHandler = Handlers.path().addPrefixPath(contextPath, httpHandler);
		builder.setHandler(httpHandler);
		return builder.build();
	}
	public static void main(String[] args) throws ServletException {
		Undertow server = MyUndertowServer.buildUndertowServer(
                6060,
                "localhost",
                "",
                SpringServletContainerInitializer.class,
                Collections.singleton(
                		WebAppServletContainerInitializer.class),
                MyUndertowServer.class.getClassLoader());
        server.start();
	}
}
