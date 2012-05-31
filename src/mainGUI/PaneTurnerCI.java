package mainGUI;

import java.awt.Component;

import javax.swing.JFrame;

import dataStructures.CIDocument;

@SuppressWarnings("serial")
public class PaneTurnerCI extends AbstractPaneTurner{
	private CIDocument document;
	
	public PaneTurnerCI(JFrame parent, CIDocument document, boolean isMultipleStakeholder) {
		super(parent, isMultipleStakeholder);
		this.document = document;
		this.isMultipleStakeholder = isMultipleStakeholder;
		
		setRightComponent(intitializeViewPanes());
	}

	@Override
	protected Component intitializeViewPanes() {
		int index = 0;
		viewPanes = new UpdatePane[metaPanes.length];
		viewPanes[index++] = new SetupProjectPane(document.getMetaData());

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
			viewPanes[index++] = new StakeholderPane(parent);
		} 
		//TODO - import saved importanceMap!!
		viewPanes[index++] = new ImportancePane(document.getAttributeMap(),parent,document.getImportanceMap());
		viewPanes[index] = new ViewResultsPaneCI(document,parent);
		
		
		return viewPanes[currentSelected];
	}

	@Override
	public String toXML() {
		return document.toXML();
	}
}
