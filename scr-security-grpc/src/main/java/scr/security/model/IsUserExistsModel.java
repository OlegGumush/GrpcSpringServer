package scr.security.model;

public class IsUserExistsModel extends BaseModel {

	private boolean isExists;

	public IsUserExistsModel() {
		super();
	}

	public IsUserExistsModel(boolean isExists) {
		super();
		this.isExists = isExists;
	}

	public boolean isExists() {
		return isExists;
	}

	public void setExists(boolean isExists) {
		this.isExists = isExists;
	}

	@Override
	public String toString() {
		return "IsUserExistsModel [isExists=" + isExists + "]";
	}
}
