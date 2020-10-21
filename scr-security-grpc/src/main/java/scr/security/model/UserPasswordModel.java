package scr.security.model;

public class UserPasswordModel extends BaseModel {

	public String password;

	public UserPasswordModel() {
		super();
	}

	public UserPasswordModel(String password) {
		super();
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserPasswordModel [password=" + password + "]";
	}
}
