package mainGUI;

import java.awt.Component;
import java.io.File;

import dataStructures.CIDocument;

/**
 * The PaneTurnerCI class is a PaneTurner populated with 
 * CI-Net input panels.
 */
@SuppressWarnings("serial")
public class PaneTurnerCI extends AbstractPaneTurner{
	
	public PaneTurnerCI(PreferenceReasoner reasoner, CIDocument document, boolean isMultipleStakeholder) {
		super(reasoner, document, isMultipleStakeholder);
		
		setRightComponent(initializeViewPanes());
	}
	
	public PaneTurnerCI(PreferenceReasoner reasoner, CIDocument document, boolean isMultipleStakeholder,
			File currentFile) {
		super(reasoner, document, isMultipleStakeholder);
		
		setRightComponent(initializeViewPanes());
	}

	@Override
	protected Component initializeViewPanes() {
		initializing = true;
		int index = 0;
		viewPanes = new UpdatePane[metaPanes.length];
		projectPane = new SetupProjectPane(document.getMetaData());
		viewPanes[index++] = projectPane;

		// pass the reference in to the AttributePane which creates
		// AttributeTuples the AttributeTuple deletes an Attribute vertex from
		// the graph when it is deleted in the AttributePane
		viewPanes[index++] = new AttributePane(document.getAttributeMap(), null,
				reasoner.getFrame());
		// a similar thing will probably have to be done to the Domains
		viewPanes[index++] = new DomainPaneCI(document.getAttributeMap(), reasoner.getFrame());
		viewPanes[index++] = new AlternativePane(document.getAlternativeMap(), reasoner.getFrame());
		viewPanes[index++] = new ValuePane(document.getAlternativeMap(),
				document.getAttributeMap(), reasoner.getFrame());
		if( isMultipleStakeholder ){
			viewPanes[index++] = new StakeholderPane(reasoner.getFrame(), document, this);
		} 
		preferencesPane = new ImportancePane(reasoner, document);
		viewPanes[index++] = preferencesPane;
		viewPanes[index] = new ViewResultsPaneCI(document, reasoner.getFrame(), this);
		
		initializing = false;
		return viewPanes[currentSelected];
	}

}
