package cn.xvkang.dto.login;

import java.io.Serializable;

import javax.servlet.annotation.WebListener;
@WebListener
public class LoginUserInformation implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1863124279476489817L;
	//private Logger logger=LoggerFactory.getLogger(LoginUserInformation.class);
	private String username;
	
	
	
	

	

	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

}
