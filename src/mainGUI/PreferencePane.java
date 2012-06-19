package mainGUI;

import java.io.File;

import dataStructures.Member;

public abstract class PreferencePane extends UpdatePane{

	public abstract void clearPane();
	
	public abstract boolean loadMemberPreferences(Member member);
	
	public abstract boolean saveMemberPreferences(File preferenceFile);
}
