package graph;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;

import org.apache.commons.collections15.Factory;

import dataStructures.Role;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class RoleEditingPopupPlugin<V,E> extends EditingPopupGraphMousePlugin<V,E> {

	public RoleEditingPopupPlugin(Factory<V> vertexFactory, Factory<E> edgeFactory ){
		super(vertexFactory, edgeFactory);
	}
	
	@Override
	protected void handlePopup(MouseEvent e){
			final VisualizationViewer<V,E> vv =
	            (VisualizationViewer<V,E>)e.getSource();
	        final Layout<V,E> layout = vv.getGraphLayout();
	        final RoleHierarchy graph = (RoleHierarchy)layout.getGraph();
	        final Point2D p = e.getPoint();
	        final Point2D ivp = p;
	        GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
	        
	        if ( pickSupport != null ) {
	        	
	        	popup.removeAll();

	            final V vertex = pickSupport.getVertex(layout, ivp.getX(), ivp.getY());
	            final E edge = pickSupport.getEdge(layout, ivp.getX(), ivp.getY());
	            final PickedState<V> pickedVertexState = vv.getPickedVertexState();
	            final PickedState<E> pickedEdgeState = vv.getPickedEdgeState();
	            
	            if(vertex != null) {
	            	
	                popup.add(new AbstractAction("Delete Vertex") {
	                    public void actionPerformed(ActionEvent e) {
	                        pickedVertexState.pick(vertex, false);
	                        ((Role) vertex).setUsed(false);
	                        graph.removeVertex((Role) vertex);
	                        vv.repaint();
	                    }});
	            } else if(edge != null) {
	                popup.add(new AbstractAction("Delete Edge") {
	                    public void actionPerformed(ActionEvent e) {
	                        pickedEdgeState.pick(edge, false);
	                        graph.removeEdge((Integer) edge);
	                        vv.repaint();
	                    }});
	            }
	            if(popup.getComponentCount() > 0) {
	                popup.show(vv, e.getX(), e.getY());
	            }
	        }
	    }
	}