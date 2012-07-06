package mainGUI;

import java.io.File;


public abstract class PreferencePane extends UpdatePane {

	public abstract void clearPane();
	
	public abstract boolean loadMemberPreferences(File file);
	
	public abstract boolean saveMemberPreferences(File preferenceFile);
	
	public abstract boolean existUnsavedChanges();
	
}
