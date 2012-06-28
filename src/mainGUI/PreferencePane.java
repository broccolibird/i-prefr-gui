package mainGUI;

import java.io.File;

import dataStructures.ChangeTracker;
import dataStructures.Member;

public abstract class PreferencePane extends UpdatePane {

	public abstract void clearPane();
	
	public abstract boolean loadMemberPreferences(File file);
	
	public abstract boolean saveMemberPreferences(File preferenceFile);
	
	public abstract boolean existUnsavedChanges();
	
}
