package mainGUI;


import guiElements.tuples.ImportanceTuple;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dataStructures.Attribute;
import dataStructures.AttributeList;
import dataStructures.Importance;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.ImportanceMap;

@SuppressWarnings("serial")
public class ImportancePane extends PreferencePane implements ActionListener{

	private AttributeMap attributeMap;
	private ImportanceMap map;
	private JPanel importancePanel;
	private JFrame parentFrame;
	private JButton plusButton;
		
		/**
		 * Create new instance of ImportancePane
		 * @param attributeMap
		 * @param parent
		 */
		public ImportancePane(AttributeMap attributeMap,JFrame parent) {
			this.parentFrame=parent;
			this.attributeMap=attributeMap;
			this.map = new ImportanceMap();
			this.add(initializeGUI());
		}
		
		/**
		 * Setup ImportancePane GUI
		 * @return JPanel
		 */
		private JPanel initializeGUI(){
			JPanel panel= new JPanel();
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			importancePanel = new JPanel();
			importancePanel.setLayout(new BoxLayout(importancePanel, BoxLayout.Y_AXIS));
			update();
			panel.add(importancePanel);
			plusButton = new JButton("+");
			plusButton.addActionListener(this);
			panel.add(plusButton);
			return panel;
		}

		@Override
		public void update() {
			importancePanel.removeAll();
			
			JTextField columnName = new JTextField("Conditional Importance Expression");
			columnName.setEditable(false);
			importancePanel.add(columnName);
			
			
			//for every map entry, add a tuple to the table,then one more
			Collection<Entry<Integer, Importance>> set = map.entrySet();
			System.out.println("set size: "+set.size());
			Attribute[] allAttributes = getAttributes();
			for (Entry<Integer, Importance> p : set)
				importancePanel.add(new ImportanceTuple(p.getKey(),map,parentFrame,importancePanel,allAttributes));
			importancePanel.add(new ImportanceTuple(map,parentFrame,importancePanel,allAttributes));
			parentFrame.pack();
		}

		public void clearPane() {
			// nothing to clear
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (plusButton == e.getSource()) {
				importancePanel.add(new ImportanceTuple(map,parentFrame,importancePanel,getAttributes()));
				parentFrame.pack();
			}
		}
		
		/**
		 * Returns an array of all attributes in the AttributeMap
		 * @return array of attributes
		 */
		private Attribute[] getAttributes(){
			return (Attribute[])attributeMap.values().toArray(new Attribute[attributeMap.size()]);
		}

		@Override
		public boolean loadMemberPreferences(File file) {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			Document doc=null;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				doc = dBuilder.parse(file);
			} catch (ParserConfigurationException | SAXException | 
						IOException e) {
				e.printStackTrace();
			}
			
			// create the importanceMap
			Element importances = (Element) ((doc
					.getElementsByTagName("IMPORTANCES")).item(0));
			int uniqueID = Integer.parseInt(Util.getOnlyChildText(importances,
					"UNIQUEMAPID"));

			// populate it with importances
			NodeList importanceList = importances
					.getElementsByTagName("IMPORTANCE");
			int nImportances = importanceList.getLength();
			for (int i = 0; i < nImportances; i++) {

				// each importance has 4 lists
				Element importance = (Element) importanceList.item(i);
				int thisKey = Integer.parseInt(importance.getAttribute("ID"));
				NodeList listList = ((Element) ((importance
						.getElementsByTagName("LISTS")).item(0)))
						.getElementsByTagName("LIST");
				int nLists = listList.getLength();
				Importance thisValue = new Importance(thisKey);
				for (int j = 0; j < nLists; j++) {
					Element list = (Element) listList.item(j);
					int index = Integer.parseInt(Util.getOnlyChildText(list,
							"INDEX"));

					// and each list has 0 to many attribute keys
					AttributeList thisList = new AttributeList();
					NodeList attributeKeyList = list
							.getElementsByTagName("ATTRIBUTEKEY");
					int nAttributeKeys = attributeKeyList.getLength();
					for (int k = 0; k < nAttributeKeys; k++) {
						Element thisAttributeKey = (Element) attributeKeyList
								.item(k);
						Integer attributeKey = Integer.parseInt(thisAttributeKey
								.getTextContent());
						thisList.add(attributeMap.get(attributeKey));
					}
					thisValue.setList(index, thisList);
				}
				map.put(thisKey, thisValue);
			}
			
			// loaded preferences are already saved
			map.setSaved(true);
			
			return true;
		}

		public boolean saveMemberPreferences(File preferenceFile) {
			String xmlRepresentation = map.toXML();
			
			System.out.println(xmlRepresentation);
			BufferedWriter writer = null;
			try {
			    writer = new BufferedWriter(new FileWriter(preferenceFile));
			    writer.write(xmlRepresentation);
			}
			catch (IOException e) {
			    e.printStackTrace();
			    return false;
			}
			
			try {
			    writer.close();
			}
			catch(IOException e) {
			    e.printStackTrace();
			}
			
			map.setSaved(true);
			
			return true;
		}

		@Override
		public boolean existUnsavedChanges() {
			return map.existUnsavedChanges();
		}


}
