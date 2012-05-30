package graph;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.AbstractAction;

import org.apache.commons.collections15.Factory;

import dataStructures.Attribute;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.picking.PickedState;

public class AttributeEditingPopupPlugin<V, E> extends
		EditingPopupGraphMousePlugin<V, E> {

	public AttributeEditingPopupPlugin(Factory<V> vertexFactory,
			Factory<E> edgeFactory) {
		super(vertexFactory, edgeFactory);
	}

	@SuppressWarnings({ "unchecked", "serial" })
	protected void handlePopup(MouseEvent e) {
		final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
				.getSource();
		final Layout<V, E> layout = vv.getGraphLayout();
		final Graph<V, E> graph = layout.getGraph();
		final Point2D p = e.getPoint();
		final Point2D ivp = p;
		GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
		if (pickSupport != null) {
			
			// CARL added a removeAll so that these AbstractActions do not pile
			// up...
			popup.removeAll();
			
			final V vertex = pickSupport.getVertex(layout, ivp.getX(),
					ivp.getY());
			final E edge = pickSupport.getEdge(layout, ivp.getX(), ivp.getY());
			final PickedState<V> pickedVertexState = vv.getPickedVertexState();
			final PickedState<E> pickedEdgeState = vv.getPickedEdgeState();

			if (vertex != null) {
				Set<V> picked = pickedVertexState.getPicked();
				if (picked.size() == 0) {
					popup.add(new AbstractAction("Delete Vertex") {
						public void actionPerformed(ActionEvent e) {
							pickedVertexState.pick(vertex, false);
							//CARL - set used to false
							((Attribute) vertex).setUsed(false);
							graph.removeVertex(vertex);
							vv.repaint();
						}
					});
				}
				/**
				 * CARL These commented out bits seem to handle if multiple
				 * vertexes are selected for popups, which is a feature that the
				 * preference reasoner should not need
				 */
				// if (picked.size() > 0) {
				// if (graph instanceof UndirectedGraph == false) {
				// JMenu directedMenu = new JMenu("Create Directed Edge");
				// popup.add(directedMenu);
				// for (final V other : picked) {
				// directedMenu.add(new AbstractAction("[" + other
				// + "," + vertex + "]") {
				// public void actionPerformed(ActionEvent e) {
				// graph.addEdge(edgeFactory.create(), other,
				// vertex, EdgeType.DIRECTED);
				// vv.repaint();
				// }
				// });
				// }
				// }
				// if (graph instanceof DirectedGraph == false) {
				// JMenu undirectedMenu = new JMenu(
				// "Create Undirected Edge");
				// popup.add(undirectedMenu);
				// for (final V other : picked) {
				// undirectedMenu.add(new AbstractAction("[" + other
				// + "," + vertex + "]") {
				// public void actionPerformed(ActionEvent e) {
				// graph.addEdge(edgeFactory.create(), other,
				// vertex);
				// vv.repaint();
				// }
				// });
				// }
				// }
				// }

			} else if (edge != null) {
				popup.add(new AbstractAction("Delete Edge") {
					public void actionPerformed(ActionEvent e) {
						pickedEdgeState.pick(edge, false);
						graph.removeEdge(edge);
						vv.repaint();
					}
				});
			}
			/**
			 * CARL Original code allowed a vertex to be created from the popup
			 * - removed this feature
			 */
			// else {
			// popup.add(new AbstractAction("Create Vertex") {
			// public void actionPerformed(ActionEvent e) {
			// V newVertex = vertexFactory.create();
			// graph.addVertex(newVertex);
			// layout.setLocation(newVertex, vv.getRenderContext()
			// .getMultiLayerTransformer().inverseTransform(p));
			// vv.repaint();
			// }
			// });
			// }
			if (popup.getComponentCount() > 0) {
				popup.show(vv, e.getX(), e.getY());
			}
		}
	}
}
