package dataStructures;

public class Role extends NameKeyObject<MemberList>{

	public Role(String name, Integer key, MemberList object) {
		super(name, key, object);
	}
	
	@Override
	public String toString() {
		return name;
	}

}
