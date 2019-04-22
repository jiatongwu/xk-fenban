package cn.xvkang.web.session;

import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import cn.xvkang.controller.LoginController;
import cn.xvkang.dto.login.LoginUserInformation;
import cn.xvkang.utils.SpringUtils;

/**
 * 框架中使用spring security、spring
 * session、session的存储使用了redis。因此对于session的监听通过HttpSessionListener的方式已经监听不到session创建和销毁事件。
 * 
 * 
 * 其实spring session框架是有提供session事件的监听处理，可以这样配置session事件发布：
 */

public class SessionListener implements HttpSessionListener {
	private Logger logger = LoggerFactory.getLogger(SessionListener.class);

	@Override
	public void sessionCreated(HttpSessionEvent se) {

		HttpSession session = se.getSession();
		String id = session.getId();
		logger.debug("sessionCreated:sessionId:" + id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		String id = session.getId();

		logger.debug("sessionDestroy:sessionid:" + id);
		Object attribute = session.getAttribute(LoginController.LOGIN_USER_INFORMATION_SESSION_KEY);
		if (attribute != null) {

			LoginUserInformation loginUserInformation = (LoginUserInformation) attribute;
			String username = loginUserInformation.getUsername();

			Object object = ((RedisTemplate<String, Set<String>>) SpringUtils.ac.getBean("redisTemplate")).opsForHash()
					.get(LoginController.usernameSessionIdsRedisKey, username);
			if (object != null) {
				Set<String> set = (Set<String>) object;
				set.remove(id);
				((RedisTemplate<String, Set<String>>) SpringUtils.ac.getBean("redisTemplate")).opsForHash()
						.put(LoginController.usernameSessionIdsRedisKey, username, set);

			}

			// ((RedisTemplate<String, LoginUserInformation>)
			// SpringUtils.ac.getBean("redisTemplate")).opsForHash()
			// .delete(LoginController.LOGIN_USER_INFORMATION_SESSION_KEYS, username);
		}

	}

}
