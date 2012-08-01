package mainGUI;

import java.awt.Component;
import java.io.File;

import javax.swing.JFrame;

import dataStructures.CIDocument;

/**
 * The PaneTurnerCI class is a PaneTurner populated with 
 * CI-Net input panels.
 */
@SuppressWarnings("serial")
public class PaneTurnerCI extends AbstractPaneTurner{
	
	public PaneTurnerCI(JFrame parent, CIDocument document, boolean isMultipleStakeholder) {
		super(parent, document, isMultipleStakeholder, null);
		
		setRightComponent(initializeViewPanes());
	}
	
	public PaneTurnerCI(JFrame parent, CIDocument document, boolean isMultipleStakeholder,
			File currentFile) {
		super(parent, document, isMultipleStakeholder, currentFile);
		
		setRightComponent(initializeViewPanes());
	}

	@Override
	protected Component initializeViewPanes() {
		int index = 0;
		viewPanes = new UpdatePane[metaPanes.length];
		projectPane = new SetupProjectPane(document.getMetaData());
		viewPanes[index++] = projectPane;

		// pass the reference in to the AttributePane which creates
		// AttributeTuples the AttributeTuple deletes an Attribute vertex from
		// the graph when it is deleted in the AttributePane
		viewPanes[index++] = new AttributePane(document.getAttributeMap(), null,
				parent);
		// a similar thing will probably have to be done to the Domains
		viewPanes[index++] = new DomainPaneCI(document.getAttributeMap(), parent);
		viewPanes[index++] = new AlternativePane(document.getAlternativeMap(), parent);
		viewPanes[index++] = new ValuePane(document.getAlternativeMap(),
				document.getAttributeMap(), parent);
		if( isMultipleStakeholder ){
			viewPanes[index++] = new StakeholderPane(parent, document, this);
		} 
		preferencesPane = new ImportancePane(parent, document);
		viewPanes[index++] = preferencesPane;
		viewPanes[index] = new ViewResultsPaneCI(document,parent);
		
		
		return viewPanes[currentSelected];
	}

}
