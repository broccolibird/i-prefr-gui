package dataStructures;

/**
 * Contains information pertaining to each stakeholder
 * 
 * @author Kat
 */
public class Member extends NameKeyObject<String> {
	
	public Member(String name, Integer key) {
		super(name, key, null);
	}
	
	public Member(String name, Integer key, String preferenceFilePath) {
		super(name, key, preferenceFilePath);
	}
		
	public String toString() {
		return name;
	}
	
	public String getPreferenceFilePath() {
		return super.getObject();
	}
	
	public void setPreferenceFilePath(String preferenceFilePath) {
		setObject(preferenceFilePath);
	}
	
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
