package cn.xvkang.controller;

import java.awt.image.BufferedImage;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Producer;

@RestController
public class CaptchaController {
	//private Logger logger = LoggerFactory.getLogger(getClass());

	public static final String SESSION_IMAGECODE_KEY = "session_imagecode_key";
	public static final String SESSION_SMSCODE_KEY = "session_smscode_key";
	@Resource
	private Producer captchaProducer;

	@GetMapping(value = "/captchaImage")
	public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		String capText = captchaProducer.createText();// 生成验证码字符串
		request.getSession().setAttribute(SESSION_IMAGECODE_KEY, capText);
		// Cookie cookie = new Cookie(Constants.KAPTCHA_SESSION_KEY, capText); //
		// 生成cookie
		// cookie.setMaxAge(300); // 300秒生存期
		// response.addCookie(cookie); // 将cookie加入response
		BufferedImage bi = captchaProducer.createImage(capText);// 生成验证码图片
		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}
		return;
	}

	/**
	 * 发送短信验证码
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
//	@GetMapping(value = "/smsCode")
//	public String smsCode(HttpServletRequest request, HttpServletResponse response, String mobile) throws Exception {
//		// 生成随机数
//		String randomNumber = RandomStringUtil.getNumber(6);
//		logger.debug(mobile + "短信验证码是" + randomNumber);
//		// 通过短信提供商发送短信给mobile手机号
//		// 放到session中
//		sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_SMSCODE_KEY, randomNumber);
//		return "ok";
//	}
}
