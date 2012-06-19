package dataStructures;

/**
 * Contains information pertaining to each stakeholder
 * 
 * @author Kat
 */
public class Member {
	private String name; 
	private Integer key;
	
	// file location of user's preferences
	private String preferenceFilePath;
	
	public Member(String name, Integer key) {
		this.name = name;
		this.key = key;
		preferenceFilePath = null;
	}
	
	public Member(String name, Integer key, String preferenceFilePath) {
		this.name = name;
		this.key = key;
		this.preferenceFilePath = preferenceFilePath;
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
	
	public String getPreferenceFilePath() {
		return preferenceFilePath;
	}
	
	public void setPreferenceFilePath(String preferenceFilePath) {
		this.preferenceFilePath = preferenceFilePath;
	}
}
