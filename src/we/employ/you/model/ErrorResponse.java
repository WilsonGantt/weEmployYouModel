package we.employ.you.model;

public class ErrorResponse extends Response {

	private int errorCode;
	
	public ErrorResponse() {
		super();
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
}
