package dataStructures;

/**
 * Object used to pass project details from the new project
 * dialog to the main window upon creating a new project
 *
 */
public class RunConfiguration {
	public boolean multipleSelected;
	public boolean cpSelected;
	
	public RunConfiguration(boolean multiple, boolean cp){
		multipleSelected = multiple;
		cpSelected = cp;
	}
	
}
