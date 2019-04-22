package cn.xvkang.exception;

/**

 */
public class AppointException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8642223204527222696L;

	public AppointException(String message) {
		super(message);
	}

	public AppointException(String message, Throwable cause) {
		super(message, cause);
	}

}
