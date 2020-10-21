package scr.security.model.request;

public class LoginModel {

	private String username;
	private String password;
	private String farmId;
	
	public LoginModel(String username, String password, String farmId) {
		super();
		this.username = username;
		this.password = password;
		this.farmId = farmId;
	}
	
	public LoginModel() {
		super();
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFarmId() {
		return farmId;
	}
	
	public void setFarmId(String farmId) {
		this.farmId = farmId;
	}

	@Override
	public String toString() {
		return "LoginModel [username=" + username + ", password=" + password + ", farmId=" + farmId + "]";
	}
}
