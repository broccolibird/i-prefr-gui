package mainGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import dataStructures.AbstractDocument;
import dataStructures.Attribute;
import dataStructures.Member;
import dataStructures.maps.EdgeStatementMap;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import graph.EditingModalGraphMouse;

@SuppressWarnings("serial")
public class SetupGraphPane extends PreferencePane implements ActionListener {

	private SparseMultigraph<Attribute, EdgeStatementMap> graph;
	private AbstractLayout<Attribute, EdgeStatementMap> layout;
	private VisualizationViewer<Attribute, EdgeStatementMap> vv;
	private AbstractDocument abstractDocument;
	@SuppressWarnings("unused")
	private JFrame parentFrame;
	private final EditingModalGraphMouse<Attribute, EdgeStatementMap> graphMouse;

	// remember GUI Elements so they can be redrawn in update()
	private JComboBox attributeBox;
	private JButton plus;
	private JButton minus;
	private JPanel controls;

	public SetupGraphPane(AbstractDocument abstractDocument, JFrame parentFrame) {
		this.abstractDocument = abstractDocument;
		this.parentFrame = parentFrame;

		// create a simple graph for the demo
		graph = new SparseMultigraph<Attribute, EdgeStatementMap>();

		this.layout = new StaticLayout<Attribute, EdgeStatementMap>(graph,
				new Dimension(550, 550));

		vv = new VisualizationViewer<Attribute, EdgeStatementMap>(layout);
		vv.setBackground(Color.white);

		vv.getRenderContext().setVertexLabelTransformer(
				MapTransformer.<Attribute, String> getInstance(LazyMap
						.<Attribute, String> decorate(
								new HashMap<Attribute, String>(),
								new ToStringLabeller<Attribute>())));

		HashMap<EdgeStatementMap,String> edgeLabelMap = new HashMap<EdgeStatementMap,String>();
		
		vv.getRenderContext().setEdgeLabelTransformer(
				MapTransformer.<EdgeStatementMap, String> getInstance(LazyMap
						.<EdgeStatementMap, String> decorate(
								edgeLabelMap,
								new ToStringLabeller<EdgeStatementMap>())));

		vv.setVertexToolTipTransformer(vv.getRenderContext()
				.getVertexLabelTransformer());

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
		add(panel);
		Factory<Attribute> vertexFactory = new VertexFactory();
		Factory<EdgeStatementMap> edgeFactory = new EdgeFactory();

		graphMouse = new EditingModalGraphMouse<Attribute, EdgeStatementMap>(
				vv.getRenderContext(), vertexFactory, edgeFactory, parentFrame,
				abstractDocument.getAttributeMap(),graph,edgeLabelMap);

		// the EditingGraphMouse will pass mouse event coordinates to the
		// vertexLocations function to set the locations of the vertices as
		// they are created
		// graphMouse.setVertexLocations(vertexLocations);
		vv.setGraphMouse(graphMouse);
		vv.addKeyListener(graphMouse.getModeKeyListener());

		graphMouse.setMode(ModalGraphMouse.Mode.EDITING);

		final ScalingControl scaler = new CrossoverScalingControl();
		plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());
			}
		});
		minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1 / 1.1f, vv.getCenter());
			}
		});

		// JButton help = new JButton("Help");
		// help.addActionListener(new ActionListener() {
		//
		// public void actionPerformed(ActionEvent e) {
		// JOptionPane.showMessageDialog(vv, instructions);
		// }});
		// AnnotationControls<Number, Number> annotationControls = new
		// AnnotationControls<Number, Number>(
		// graphMouse.getAnnotatingPlugin());
		// controls.add(help);

		controls = new JPanel();
		update();
		add(controls, BorderLayout.SOUTH);
	}

	public void clearPane() {
		Collection<Attribute> allUsedAttributes = graph.getVertices();
		for( Attribute attr : allUsedAttributes ) {
			attr.setUsed(false);
		}
		update();
	}
	
	@Override
	public void update() {
		controls.removeAll();
		controls.add(plus);
		controls.add(minus);
		controls.add(graphMouse.getModeComboBox());
		setupAttributeBox();
		controls.add(attributeBox);
	}

	public Graph<Attribute, EdgeStatementMap> getGraph() {
		return graph;
	}

	private void setupAttributeBox() {
		// make combobox with all attributes
		Attribute[] allAttributes = abstractDocument.getAttributeMap().values()
				.toArray(new Attribute[0]);
		attributeBox = new JComboBox(allAttributes);

		// make renderer that greys out already-used attributes
		ComboBoxRenderer renderer = new ComboBoxRenderer();
		// renderer.setPreferredSize(new Dimension(200, 130));
		attributeBox.setRenderer(renderer);

		attributeBox.addActionListener(this);
		attributeBox.invalidate();
		if (allAttributes.length > 0) {
			attributeBox.setSelectedIndex(0);
		}
		// attributeBox.setMaximumRowCount(3);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == attributeBox) {

			Attribute selectedAttribute = (Attribute) attributeBox
					.getSelectedItem();
			if (!selectedAttribute.isUsed()) {
//				System.out.println("setting gm attribute to: "
//						+ selectedAttribute.getName());
				graphMouse.getEditingPlugin().setAttribute(selectedAttribute);
			} else {
//				System.out.println("attribute is already used");
//				// maybe warn the user that their selection is not going to work
			}
		}

	}

	class ComboBoxRenderer extends DefaultListCellRenderer {

		public ComboBoxRenderer() {
			super();
			this.setBackground(Color.WHITE);
			this.setForeground(Color.BLUE);
		}

		@Override
		public Component getListCellRendererComponent(JList arg0, Object value,
				int arg2, boolean arg3, boolean arg4) {
			super.getListCellRendererComponent(arg0, value, arg2, arg3, arg4);
			Attribute a = (Attribute) value;
			if (a != null && a.isUsed())
				this.setForeground(Color.LIGHT_GRAY);
			return this;
		}

	}

	class VertexFactory implements Factory<Attribute> {
		public Attribute create() {
			return graphMouse.getEditingPlugin().getSelectedAttribute();
		}
	}

	class EdgeFactory implements Factory<EdgeStatementMap> {
		public EdgeStatementMap create() {
			return new EdgeStatementMap();
		}
	}

	@Override
	public boolean loadMemberPreferences(File file) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveMemberPreferences(File preferenceFile) {
		// TODO Auto-generated method stub
		return false;
	}

}