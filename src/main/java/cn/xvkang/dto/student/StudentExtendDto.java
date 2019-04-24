package cn.xvkang.dto.student;

import java.io.Serializable;

import cn.xvkang.entity.Student;

public class StudentExtendDto extends Student implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5920849968326892250L;
	
	
	private String xuankezuheShortName;


	public String getXuankezuheShortName() {
		return xuankezuheShortName;
	}


	public void setXuankezuheShortName(String xuankezuheShortName) {
		this.xuankezuheShortName = xuankezuheShortName;
	}
	
	
	
	

}
