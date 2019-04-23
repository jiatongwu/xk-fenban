package cn.xvkang.dto.login;

import java.io.Serializable;

import javax.servlet.annotation.WebListener;

import cn.xvkang.entity.Student;
import cn.xvkang.entity.User;

@WebListener
public class LoginUserInformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1863124279476489817L;
	// private Logger logger=LoggerFactory.getLogger(LoginUserInformation.class);
	private User user;
	private Student student;
	private String displayName;
	

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
