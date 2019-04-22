package cn.xvkang.web.springmvcinterceptor;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import cn.xvkang.controller.LoginController;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
	private Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);

	@Autowired
	@Qualifier("ymlMap")
	private Object ymlMap;
	@Autowired
	private PathMatcher pathMatcher;

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		Map<String, Object> map = (Map<String, Object>) ymlMap;
		List<String> notLoginCanAccessUrls = (List<String>) map.get("notLoginCanAccessUrls");

		String contextPath = request.getSession().getServletContext().getContextPath();
		int contextPathLength = contextPath.length();

		String requestURI = request.getRequestURI();
		int requestURILength = requestURI.length();
		
		if (requestURILength > contextPathLength) {
			requestURI = requestURI.substring(contextPathLength, requestURILength);
		}

		for (String tmp : notLoginCanAccessUrls) {
			boolean match = pathMatcher.match(tmp, requestURI);
			if(match) {
				return true;
			}
//			if (tmp.equals(requestURI)) {
//				return true;
//			}
		}
		logger.debug("preHandler");
		HttpSession session = request.getSession();
		Object attribute = session.getAttribute(LoginController.LOGIN_USER_INFORMATION_SESSION_KEY);
		if (attribute != null) {
			return true;
		}
		// forward to a view
		ModelAndView mav = new ModelAndView("/index/notLogin");
		// eventually populate the model
		
		throw new ModelAndViewDefiningException(mav);
		
		//return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		logger.debug("postHandle");

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		logger.debug("afterCompletion");
	}

}
