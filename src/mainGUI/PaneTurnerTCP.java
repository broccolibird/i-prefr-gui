package mainGUI;

import java.awt.Component;
import java.io.File;

import javax.swing.JFrame;

import dataStructures.TCPDocument;

@SuppressWarnings("serial")
public class PaneTurnerTCP extends AbstractPaneTurner{

	public PaneTurnerTCP(JFrame parent, TCPDocument document, boolean multipleStakeholder) {
		super(parent, document, multipleStakeholder);
		setRightComponent(initializeViewPanes());
	}

	@Override
	protected Component initializeViewPanes() {
		viewPanes = new UpdatePane[metaPanes.length];

		int index = 5;
		if( isMultipleStakeholder ){
			viewPanes[index++] = new StakeholderPane(parent, document, this);
		}
		
		// the graph used in the SetupGraphPane must be linked to each
		// AttributeTuple, so get a reference to it
		//viewPanes[index] = new SetupGraphPane(document,parent);
		preferencesPane = new SetupPreferencesPane(parent, document, isMultipleStakeholder);
		viewPanes[index++] = preferencesPane;
		viewPanes[index] = new ViewResultsPane(document,parent);
		
		viewPanes[0] = new SetupProjectPane(document.getMetaData());

		// pass the reference in to the AttributePane which creates
		// AttributeTuples the AttributeTuple deletes an Attribute vertex from
		// the graph when it is deleted in the AttributePane
		viewPanes[1] = new AttributePane(document.getAttributeMap(), preferencesPane,
				parent);

		//? a similar thing will probably have to be done to the Domains
		viewPanes[2] = new DomainPane(document.getAttributeMap(), parent);
		viewPanes[3] = new AlternativePane(document.getAlternativeMap(), parent);
		viewPanes[4] = new ValuePane(document.getAlternativeMap(),
				document.getAttributeMap(), parent);

		return viewPanes[currentSelected];
	}

	@Override
	public String toXML(File xmlfile) {
		return document.toXML(xmlfile);
	}
}
