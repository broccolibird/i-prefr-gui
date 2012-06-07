package dataStructures;

public class Member {
	private String name;
	private Integer key;
	
	public Member(String name, Integer key) {
		this.name = name;
		this.key = key;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName( String name) {
		this.name = name;
	}
	
	public Integer getMemberKey() {
		return key;
	}
	
	public String toString() {
		return name;
	}
	
	
}
