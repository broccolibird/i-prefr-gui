package dataStructures;

/**
 * Object used to pass project details upon creating a new project
 * 
 * @author Kat
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
