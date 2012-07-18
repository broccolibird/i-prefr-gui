package dataStructures;

/**
 * Contains information pertaining to each stakeholder
 */
public class Member extends NameKeyObject<String> {
	
	/**
	 * Create a new Member instance with a null
	 * preference file path.
	 * @param name
	 * @param key
	 */
	public Member(String name, Integer key) {
		super(name, key, null);
	}
	
	public Member(String name, Integer key, String preferenceFilePath) {
		super(name, key, preferenceFilePath);
	}
		
	public String toString() {
		return name;
	}
	
	/**
	 * @return preference file path
	 */
	public String getPreferenceFilePath() {
		return super.getObject();
	}
	
	/**
	 * @param preferenceFilePath
	 */
	public void setPreferenceFilePath(String preferenceFilePath) {
		setObject(preferenceFilePath);
	}
	
	/**
	 * @return xml string representation of this member
	 */
	public String toXML() {
		String xml = "\t\t\t<MEMBER ID = '"+key+"'>\n";
		xml += "\t\t\t\t<NAME>"+name+"</NAME>\n";
		if(object != null)
			xml += "\t\t\t\t<PREFERENCEFILE>"+object+
						"</PREFERENCEFILE>\n";
		xml += "\t\t\t</MEMBER>\n";
		
		return xml;
	}
}
