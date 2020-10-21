package scr.security.model.response;

public class JwtTokenModel {

	private String token;

	public JwtTokenModel() {
		super();
	}

	public JwtTokenModel(String token) {
		super();
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "JwtTokenModel [token=" + token + "]";
	}
}
