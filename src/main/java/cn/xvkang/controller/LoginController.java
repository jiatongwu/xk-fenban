package cn.xvkang.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.xvkang.dto.login.LoginUserInformation;

@RestController
public class LoginController {
	public static final String LOGIN_USER_INFORMATION_SESSION_KEY = "loginUserInformation";
	// public static final String LOGIN_USER_INFORMATION_SESSION_KEYS =
	// "loginUserInformations";
	@Autowired
	private RedisTemplate<String, Set<String>> redisTemplate;
	@Autowired
	private RedisOperationsSessionRepository sessionRepository;

	public static final String usernameSessionIdsRedisKey = "usernameSessionIds";

	// public static Map<String, Set<String>> usernameSessionIdsMap = new
	// HashMap<>();
	// public static Map<String, HttpSession> sessionIdSessionMap = new HashMap<>();

	@SuppressWarnings("unchecked")
	@PostMapping("/login")
	public String login(String username, String password, HttpServletRequest request) {
		HttpSession session = request.getSession();
		LoginUserInformation tmp = (LoginUserInformation) session.getAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
		if (tmp == null) {
			LoginUserInformation loginUserInformation = new LoginUserInformation();
			loginUserInformation.setUsername(username);

			String id = session.getId();

			session.setAttribute(LOGIN_USER_INFORMATION_SESSION_KEY, loginUserInformation);
			Object object = redisTemplate.opsForHash().get(LoginController.usernameSessionIdsRedisKey, username);
			if (object != null) {
				Set<String> set = (Set<String>) object;
				set.add(id);
				redisTemplate.opsForHash().put(LoginController.usernameSessionIdsRedisKey, username, set);

			} else {
				Set<String> set = new HashSet<>();
				set.add(id);
				redisTemplate.opsForHash().put(LoginController.usernameSessionIdsRedisKey, username, set);
			}
		}

		return "ok";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		Object attribute = session.getAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
		if (attribute != null) {
			LoginUserInformation loginUserInformation = (LoginUserInformation) attribute;
			String username = loginUserInformation.getUsername();
			String id = session.getId();
			Object object = redisTemplate.opsForHash().get(LoginController.usernameSessionIdsRedisKey, username);
			if (object != null) {
				Set<String> set = (Set<String>) object;
				set.remove(id);
				redisTemplate.opsForHash().put(LoginController.usernameSessionIdsRedisKey, username, set);

			}
		}

		session.removeAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
		session.invalidate();

		return "ok";
	}

	@GetMapping("/me")
	public String me(HttpSession session) {

		LoginUserInformation tmp = (LoginUserInformation) session.getAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
		// LoginUserInformation currentUser = (LoginUserInformation)
		// redisTemplate.opsForHash() .get(LOGIN_USER_INFORMATION_SESSION_KEYS,
		// tmp.getUsername());
		return tmp.getUsername();
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/tiren")
	public String tiren(String username, HttpSession session) {

		Object object = redisTemplate.opsForHash().get(LoginController.usernameSessionIdsRedisKey, username);
		if (object != null) {
			Set<String> set = (Set<String>) object;
			for (String sessionId : set) {
				
				sessionRepository.delete(sessionId);
			}
			redisTemplate.opsForHash().put(LoginController.usernameSessionIdsRedisKey, username, new HashSet<String>());

		}

		return "ok";
	}
}
