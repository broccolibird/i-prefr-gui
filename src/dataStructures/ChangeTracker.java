package dataStructures;

public interface ChangeTracker {
	public boolean existUnsavedChanges();
	
	public void setSaved(boolean saved);
}
