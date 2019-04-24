package cn.xvkang.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xvkang.dto.login.LoginUserInformation;
import cn.xvkang.entity.Student;
import cn.xvkang.entity.User;
import cn.xvkang.service.StudentService;
import cn.xvkang.service.UserService;
import cn.xvkang.utils.Constants;

@Controller
public class LoginController {
	public static final String LOGIN_USER_INFORMATION_SESSION_KEY = "loginUserInformation";
	// public static final String LOGIN_USER_INFORMATION_SESSION_KEYS =
	// "loginUserInformations";
	@Autowired
	private RedisTemplate<String, Set<String>> redisTemplate;
	@Autowired
	private RedisOperationsSessionRepository sessionRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private StudentService studentService;

	public static final String usernameSessionIdsRedisKey = "usernameSessionIds";

	// public static Map<String, Set<String>> usernameSessionIdsMap = new
	// HashMap<>();
	// public static Map<String, HttpSession> sessionIdSessionMap = new HashMap<>();
	@RequestMapping(value = { "/loginPage" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request) {
		return "login/login";
	}

	@RequestMapping(value = { "/", "/studentLogin" }, method = RequestMethod.GET)
	public String studentLogin(HttpServletRequest request) {
		return "login/studentLogin";
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/studentLogin")
	@ResponseBody
	public Map<String, Object> studentLogin(String username, String password, String captcha,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		//String rightImageCode = (String) request.getSession().getAttribute(CaptchaController.SESSION_IMAGECODE_KEY);
		Map<String, Object> result = new HashMap<String, Object>();
		// List<String> message = new ArrayList<String>();

		User findByUsername = userService.findByUsername(username);

		if (findByUsername == null) {
			result.put("code", Constants.ReturnCode.账号不存在.getCode());
			result.put("message", Constants.ReturnCode.账号不存在.getMessage());
		} else {
			// 如果是学生登录不需要输入密码
			Integer studentid = findByUsername.getStudentid();

			// 校验密码
			try {
				String md5Crypt = DigestUtils.md5Hex(password + findByUsername.getSalt());
				if ((!md5Crypt.equals(findByUsername.getPassword())) && (studentid == null)) {
					result.put("code", Constants.ReturnCode.密码错误.getCode());
					result.put("message", Constants.ReturnCode.密码错误.getMessage());
				} else {
					// 登录成功
					result.put("code", Constants.ReturnCode.成功.getCode());
					LoginUserInformation tmp = (LoginUserInformation) session
							.getAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
					if (tmp == null) {
						LoginUserInformation loginUserInformation = new LoginUserInformation();
						findByUsername.setPassword(null);
						loginUserInformation.setUser(findByUsername);
						loginUserInformation.setDisplayName(findByUsername.getUsername());
						if (findByUsername.getStudentid() != null) {
							// 获取student信息
							Student findById = studentService.findById(findByUsername.getStudentid());
							loginUserInformation.setStudent(findById);
							loginUserInformation.setDisplayName(findById.getName());
						}
						String id = session.getId();
						session.setAttribute(LOGIN_USER_INFORMATION_SESSION_KEY, loginUserInformation);
						Object object = redisTemplate.opsForHash().get(LoginController.usernameSessionIdsRedisKey,
								username);
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
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("code", Constants.ReturnCode.密码错误.getCode());
				result.put("message", Constants.ReturnCode.密码错误.getMessage());
			}
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	@PostMapping("/login")
	@ResponseBody
	public Map<String, Object> login(String username, String password, String captcha, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String rightImageCode = (String) request.getSession().getAttribute(CaptchaController.SESSION_IMAGECODE_KEY);
		Map<String, Object> result = new HashMap<String, Object>();
		// List<String> message = new ArrayList<String>();

		User findByUsername = userService.findByUsername(username);
		if (!rightImageCode.equalsIgnoreCase(captcha)) {
			result.put("code", Constants.ReturnCode.验证码错误.getCode());
			result.put("message", Constants.ReturnCode.验证码错误.getMessage());
		} else {
			if (findByUsername == null) {
				result.put("code", Constants.ReturnCode.账号不存在.getCode());
				result.put("message", Constants.ReturnCode.账号不存在.getMessage());
			} else {
				// 如果是学生登录不需要输入密码
				Integer studentid = findByUsername.getStudentid();

				// 校验密码
				try {
					String md5Crypt = DigestUtils.md5Hex(password + findByUsername.getSalt());
					if ((!md5Crypt.equals(findByUsername.getPassword())) && (studentid == null)) {
						result.put("code", Constants.ReturnCode.密码错误.getCode());
						result.put("message", Constants.ReturnCode.密码错误.getMessage());
					} else {
						// 登录成功
						result.put("code", Constants.ReturnCode.成功.getCode());
						LoginUserInformation tmp = (LoginUserInformation) session
								.getAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
						if (tmp == null) {
							LoginUserInformation loginUserInformation = new LoginUserInformation();
							findByUsername.setPassword(null);
							loginUserInformation.setUser(findByUsername);
							loginUserInformation.setDisplayName(findByUsername.getUsername());
							if (findByUsername.getStudentid() != null) {
								// 获取student信息
								Student findById = studentService.findById(findByUsername.getStudentid());
								loginUserInformation.setStudent(findById);
								loginUserInformation.setDisplayName(findById.getName());
							}
							String id = session.getId();
							session.setAttribute(LOGIN_USER_INFORMATION_SESSION_KEY, loginUserInformation);
							Object object = redisTemplate.opsForHash().get(LoginController.usernameSessionIdsRedisKey,
									username);
							if (object != null) {
								Set<String> set = (Set<String>) object;
								set.add(id);
								redisTemplate.opsForHash().put(LoginController.usernameSessionIdsRedisKey, username,
										set);

							} else {
								Set<String> set = new HashSet<>();
								set.add(id);
								redisTemplate.opsForHash().put(LoginController.usernameSessionIdsRedisKey, username,
										set);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.put("code", Constants.ReturnCode.密码错误.getCode());
					result.put("message", Constants.ReturnCode.密码错误.getMessage());
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// 校验密码
		try {
			String md5Hex = DigestUtils.md5Hex("123456123456");

			System.out.println(md5Hex);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	@GetMapping("/logout")
	@ResponseBody
	public String logout(HttpSession session) {
		Object attribute = session.getAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
		if (attribute != null) {
			LoginUserInformation loginUserInformation = (LoginUserInformation) attribute;
			User user = loginUserInformation.getUser();
			String id = session.getId();
			Object object = redisTemplate.opsForHash().get(LoginController.usernameSessionIdsRedisKey,
					user.getUsername());
			if (object != null) {
				Set<String> set = (Set<String>) object;
				set.remove(id);
				redisTemplate.opsForHash().put(LoginController.usernameSessionIdsRedisKey, user.getUsername(), set);
			}
		}

		session.removeAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
		session.invalidate();

		return "ok";
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/logoutToLoginPage")
	public String logoutToLoginPage(HttpSession session) {
		Object attribute = session.getAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
		if (attribute != null) {
			LoginUserInformation loginUserInformation = (LoginUserInformation) attribute;
			User user = loginUserInformation.getUser();
			String id = session.getId();
			Object object = redisTemplate.opsForHash().get(LoginController.usernameSessionIdsRedisKey,
					user.getUsername());
			if (object != null) {
				Set<String> set = (Set<String>) object;
				set.remove(id);
				redisTemplate.opsForHash().put(LoginController.usernameSessionIdsRedisKey, user.getUsername(), set);
			}
		}

		session.removeAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
		session.invalidate();

		return "redirect:/";
	}

	@GetMapping("/me")
	public String me(HttpSession session) {
		LoginUserInformation tmp = (LoginUserInformation) session.getAttribute(LOGIN_USER_INFORMATION_SESSION_KEY);
		// LoginUserInformation currentUser = (LoginUserInformation)
		// redisTemplate.opsForHash() .get(LOGIN_USER_INFORMATION_SESSION_KEYS,
		// tmp.getUsername());
		return tmp.getUser().getUsername();
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
