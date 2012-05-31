package mainGUI;

import java.awt.Component;

import javax.swing.JFrame;

import dataStructures.CIDocument;

@SuppressWarnings("serial")
public class PaneTurnerCI extends AbstractPaneTurner{
	private CIDocument document;

	public PaneTurnerCI(JFrame parent, CIDocument document) {
		super(parent);
		this.document = document;
		setRightComponent(intitializeViewPanes());
	}

	@Override
	protected Component intitializeViewPanes() {
		viewPanes = new UpdatePane[metaPanes.length];
		viewPanes[0] = new SetupProjectPane(document.getMetaData());

		// pass the reference in to the AttributePane which creates
		// AttributeTuples the AttributeTuple deletes an Attribute vertex from
		// the graph when it is deleted in the AttributePane
		viewPanes[1] = new AttributePane(document.getAttributeMap(), null,
				parent);
		// a similar thing will probably have to be done to the Domains
		viewPanes[2] = new DomainPaneCI(document.getAttributeMap(), parent);
		viewPanes[3] = new AlternativePane(document.getAlternativeMap(), parent);
		viewPanes[4] = new ValuePane(document.getAlternativeMap(),
				document.getAttributeMap(), parent);
		viewPanes[5] = new StakeholderPane(parent);
		//TODO - import saved importanceMap!!
		viewPanes[6] = new ImportancePane(document.getAttributeMap(),parent,document.getImportanceMap());
		viewPanes[7] = new ViewResultsPaneCI(document,parent);
		return viewPanes[currentSelected];
	}

	@Override
	public String toXML() {
		return document.toXML();
	}
}
