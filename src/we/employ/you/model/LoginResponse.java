package we.employ.you.model;

public class LoginResponse {

	private String loginMessage;
	private String userRole;
	
	public LoginResponse() {
		super();
	}

	public String getLoginMessage() {
		return loginMessage;
	}

	public void setLoginMessage(String loginMessage) {
		this.loginMessage = loginMessage;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
}
