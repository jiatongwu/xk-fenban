package cn.xvkang.utils;

public class Constants {

	public static final String SPORT_SCORE_SUFFIX = "_Sport_Sco";
	public static final String SCOREAll_SUFFIX = "_ScoAll";
	public static final String STUINFO_SUFFIX = "_Stu_info";
	public static final String SUB_SUFFIX = "_Sub";
	public static final String SUB_EXT_SUFFIX = "_Sub_Ext";
	

	// 文件下载时用到的MIME类型
	public static final String EXCEL_XLSX_MIMETYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String WORD_DOC_MIME = "application/msword";
	public static final String EXCEL_XLS_MIME = "application/vnd.ms-excel";
	public static final String WORD_DOCX_MIME = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	
	
	
	
	
	public static enum ReturnCode {
		成功(0, "执行成功"), 参数错误(1, "参数错误"), 未认证(2, "未认证"), 未授权(3, "未授权"), 服务器内部错误(5, "服务器内部错误"), 其他错误(4, "其他错误"),
		账号不存在(6, "账号不存在"), 密码错误(7, "密码错误"), 您已经登录了不能再次登录(8, "您已经登录了不能再次登录"), 该用户已注册(9, "该用户已注册"),
		扫码登录时_登录失败提示信息_这种情况不应该出现(10, "扫码登录失败,这种情况不应该出现，因为如果超时了前端就应该提示重新获取验证码，让用户重新扫"), 刚注册的用户不允许登录(11, "刚注册的用户不允许登录"),验证码错误(12, "验证码错误");
		private Integer code;
		private String message;

		ReturnCode(Integer code, String message) {
			this.code = code;
			this.message = message;
		}

		public Integer getCode() {
			return this.code;
		}

		public String getMessage() {
			return this.message;
		}
	}

}
