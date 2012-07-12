package dataStructures;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import dataStructures.maps.OptionMap;

public class MetaData {

	// metadata variables
	private String filename;
	private String projectName;
	private GregorianCalendar creationDate;
	private ModelCheckerOption selectedModelChecker;
	private OptionMap displayOptions;
	
	private boolean existChanges;

	public MetaData() {
		this("untitled.xml", "untitled", new ModelCheckerOption(
				ModelCheckerOption.MODEL_CHECKER_0), new GregorianCalendar(),new OptionMap());
		existChanges = false;
	}

	public MetaData(String filename, String projectName,
			ModelCheckerOption selectedModelChecker, GregorianCalendar creationDate,OptionMap optionMap) {
		this.filename = filename;
		this.projectName = projectName;
		this.selectedModelChecker = selectedModelChecker;
		this.creationDate = creationDate;
		this.displayOptions = optionMap;
		
		existChanges = false;
	}
	
	public MetaData(String filename, String projectName,
			String selectedModelCheckerString,String creationDateString,OptionMap optionMap){
		this.filename = filename;
		this.projectName = projectName;
		this.selectedModelChecker = ModelCheckerOption.getOption(selectedModelCheckerString);
		this.displayOptions = optionMap;
		
		DateFormat df = new SimpleDateFormat("dd MM yyyy");
		df.setLenient(false);
		Date date;
		GregorianCalendar cal = (GregorianCalendar) Calendar.getInstance();
		try {
			date = df.parse(creationDateString);
			cal.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		creationDate = cal;
		
		existChanges = false;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		// System.out.println("filename set to "+filename);
		this.filename = filename;
		existChanges = true;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		// System.out.println("project name set to "+projectName);
		this.projectName = projectName;
		existChanges = true;
	}

	public ModelCheckerOption getSelectedModelChecker() {
		return selectedModelChecker;
	}

	public void setSelectedModelChecker(ModelCheckerOption selectedModelChecker) {
		this.selectedModelChecker = selectedModelChecker;
		existChanges = true;
	}

	public GregorianCalendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(GregorianCalendar creationDate) {
		this.creationDate = creationDate;
		existChanges = true;
	}

	public OptionMap getDisplayOptions() {
		return displayOptions;
	}
	
	private String getDateString(){
		return creationDate.get(Calendar.DATE) + " "
				+ creationDate.get(Calendar.MONTH) + " "
				+ creationDate.get(Calendar.YEAR);
	}
	
	public String toXML(){
		String metaData = "\t<METADATA>\n";
		metaData += "\t\t<FILENAME>"+filename+"</FILENAME>\n";
		metaData += "\t\t<PROJECTNAME>"+projectName+"</PROJECTNAME>\n";
		metaData += "\t\t<MODELCHECKER>"+selectedModelChecker+"</MODELCHECKER>\n";
		metaData += "\t\t<DATECREATED>"+getDateString()+"</DATECREATED>\n";
		metaData += displayOptions.toXML();
		metaData += "\t</METADATA>\n";
		return metaData;
	}

	/**
	 * Returns true if meta data has been changed since the last save
	 * @return true if MetaData has been changed since the last save
	 */
	public boolean existChanges() {
		return existChanges;
	}

	/**
	 * Clears existing changes when saved is set to true
	 * @param saved
	 */
	public void setSaved(boolean saved) {
		if(saved == true)
			existChanges = false;
		
	}
}
