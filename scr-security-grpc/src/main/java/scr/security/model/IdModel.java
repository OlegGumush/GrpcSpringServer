package scr.security.model;

public class IdModel extends BaseModel {

	public Integer id;

	public IdModel() {
		super();
	}

	public IdModel(Integer id) {
		super();
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "UserPasswordModel [password=" + id + "]";
	}
}
