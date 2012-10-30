package dataStructures;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import dataStructures.maps.OptionMap;

public class MetaData {

	// metadata variables
	private String projectName;
	private GregorianCalendar creationDate;
	private ModelCheckerOption selectedModelChecker;
	private OptionMap displayOptions;
	
	private boolean existChanges;

	public MetaData() {
		this("untitled", new ModelCheckerOption(
				ModelCheckerOption.MODEL_CHECKER_0), new GregorianCalendar(),new OptionMap());
		existChanges = false;
	}

	public MetaData(String projectName,
			ModelCheckerOption selectedModelChecker, GregorianCalendar creationDate,OptionMap optionMap) {

		this.projectName = projectName;
		this.selectedModelChecker = selectedModelChecker;
		this.creationDate = creationDate;
		this.displayOptions = optionMap;
		
		existChanges = false;
	}
	
	public MetaData(String projectName, String selectedModelCheckerString,
			String creationDateString,OptionMap optionMap){
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
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
	
	/**
	 * @return MetaData in XML String format
	 */
	public Element toXML(Document doc){
		Element metaData = doc.createElement("METADATA");
		
		// Create ProjectName element
		Element projectNameElem = doc.createElement("PROJECTNAME");
		projectNameElem.appendChild(doc.createTextNode(projectName));
		metaData.appendChild(projectNameElem);
		
		// Create ModelChecker element
		Element modelCheckerElem = doc.createElement("MODELCHECKER");
		modelCheckerElem.appendChild(doc.createTextNode(selectedModelChecker.toString()));
		metaData.appendChild(modelCheckerElem);
		
		// Create date created element
		Element dateCreatedElem = doc.createElement("DATECREATED");
		dateCreatedElem.appendChild(doc.createTextNode(getDateString()));
		metaData.appendChild(dateCreatedElem);
		
		// Add display Options
		Element displayOptionsElem = displayOptions.toXML(doc);
		metaData.appendChild(displayOptionsElem);
		
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
