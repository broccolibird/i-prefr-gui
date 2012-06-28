package multiStakeholderGUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import dataStructures.AbstractDocument;
import dataStructures.Role;
import dataStructures.maps.MemberMap;
import dataStructures.maps.RoleMap;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import graph.RoleHierarchy;
import graph.RoleEditingModalGraphMouse;

import mainGUI.UpdatePane;

@SuppressWarnings("serial")
public class HierarchyPane extends UpdatePane implements ActionListener {
	
	private AbstractDocument document;
	private RoleHierarchy graph;
	
	Factory<Integer> edgeFactory;

    Factory<Role> vertexFactory = new Factory<Role>() {
		public Role create() {
			return graphMouse.getEditingPlugin().getSelectedRole();
		}};
		
	private AbstractLayout<Role, Integer> layout;
	private VisualizationViewer<Role, Integer> vv;
	private final RoleEditingModalGraphMouse<Role, Integer> graphMouse;
	
	// remember GUI Elements so they can be redrawn in update()
	private JComboBox<Role> roleBox;
	private JButton plus;
	private JButton minus;
	private JPanel controls;
	
	
	public HierarchyPane(AbstractDocument abstractDocument) {
		this.document = abstractDocument;
		
		RoleMap rm = abstractDocument.getRoleMap();
		if( rm.getRoleHierarchy() == null) {
			graph = new RoleHierarchy(document.getRoleMap());
			layout = new StaticLayout(graph);
			graph.setLayout(layout);
			edgeFactory = new Factory<Integer>() {
				int i=0;
				public Integer create() {
					return i++;
				}};
		} else {
			graph = rm.getRoleHierarchy();
			layout = graph.getLayout();
			edgeFactory = new Factory<Integer>() {
				int i = graph.getNextEdge();
				public Integer create() {
					return i++;
				}};
		}
		
		// set up view settings
		vv = new VisualizationViewer<Role, Integer>(layout, new Dimension(400, 600));
		vv.setBackground(Color.white);
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line()); 
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Role>());
	
        // add a listener for ToolTips
        vv.setVertexToolTipTransformer(new RoleToolTipTransformer());
        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
	
        // add visualizer to zoom/scroll pane
        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
        add(panel);
        
        // 
        graphMouse = new RoleEditingModalGraphMouse<Role, Integer>(
				vv.getRenderContext(), vertexFactory, edgeFactory);
        
        vv.setGraphMouse(graphMouse);
        vv.addKeyListener(graphMouse.getModeKeyListener());
        
        // Add zoom controls
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
                scaler.scale(vv, 1/1.1f, vv.getCenter());
            }
        });
                
        // set up control panel
        controls = new JPanel();
        update();
        add(controls, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == roleBox) {

			Role selectedRole = (Role) roleBox
					.getSelectedItem();
			if (!selectedRole.isUsed()) {
				graphMouse.getEditingPlugin().setRole(selectedRole);
			} else {
//				System.out.println("stakeholder is already used");
//				// maybe warn the user that their selection is not going to work
			}
		}
		
	}

	@Override
	public void update() {
		controls.removeAll();
		controls.add(plus);
		controls.add(minus);
		controls.add(graphMouse.getModeComboBox());
		setupRoleBox();
		controls.add(roleBox);		
	}
	
	public RoleHierarchy getGraph() {
		return graph;
	}
	
	private void setupRoleBox() {
		// make combobox with all attributes
		Role[] allRoles = document.getRoleMap().values()
				.toArray(new Role[0]);
		roleBox = new JComboBox<Role>(allRoles);

		// make renderer that greys out already-used roles
		ComboBoxRenderer renderer = new ComboBoxRenderer();
		// renderer.setPreferredSize(new Dimension(200, 130));
		roleBox.setRenderer(renderer);

		roleBox.addActionListener(this);
		roleBox.invalidate();
		if (allRoles.length > 0) {
			roleBox.setSelectedIndex(0);
		}
		// attributeBox.setMaximumRowCount(3);
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
			Role r = (Role) value;
			if (r != null && r.isUsed())
				this.setForeground(Color.LIGHT_GRAY);
			return this;
		}
	}
	
	class RoleToolTipTransformer implements Transformer<Role,String> {
		@Override
		public String transform(Role role) {
			MemberMap mMap = role.getObject();
			if(mMap == null)
				return "";
			return mMap.toString();
		}
	}

}