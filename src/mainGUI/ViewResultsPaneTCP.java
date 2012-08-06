package mainGUI;

import java.io.File;

import javax.swing.JFrame;

import dataStructures.AbstractDocument;
import dataStructures.Alternative;

public class ViewResultsPaneTCP extends ViewResultsPane {

	public ViewResultsPaneTCP(AbstractDocument document, JFrame parentFrame) {
		super(document, parentFrame);
	}

	@Override
	protected File xmlToText(File prefXml) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getVariableSet(String line) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Alternative getAlternative(String variableSet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void checkConsistency() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initReasoner(String prefXml) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void topNext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void dominance() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void leave() {/*do nothing*/}

}
