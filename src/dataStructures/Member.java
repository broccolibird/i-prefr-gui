package dataStructures;

import java.io.File;

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
	public String toXML(File projectFolder) {
		String xml = "\t\t\t<MEMBER ID = '"+key+"'>\n";
		xml += "\t\t\t\t<NAME>"+name+"</NAME>\n";
		if(object != null) {
			String path = createRelativePath(projectFolder);
			xml += "\t\t\t\t<PREFERENCEFILE>"+path+
						"</PREFERENCEFILE>\n";
		}
		xml += "\t\t\t</MEMBER>\n";
		
		return xml;
	}
	
	/**
	 * Returns a relative file path if the preference file is in
	 * the project folder. Returns an absolute file path if the 
	 * file is not in the project path.
	 * 
	 * @return the Preference file path to be used in role file
	 */
	private String createRelativePath(File projectFolder) {
		File objectFile = new File(object);
		if(objectFile != null) {
			if(objectFile.getParent().equals(projectFolder.getAbsolutePath())) {
				return objectFile.getName();
			}
		}
		
		// return absolute path
		return object;
	}
}
