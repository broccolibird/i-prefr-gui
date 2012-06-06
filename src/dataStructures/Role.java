package dataStructures;

public class Role extends NameKeyObject<MemberList>{

	private boolean isUsed;

	public Role(String name, Integer key, MemberList object) {
		super(name, key, object);
		isUsed = false;
	}
	
	public boolean isUsed() {
		return isUsed;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
