package graph;

import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.apache.commons.collections15.Factory;

import dataStructures.Role;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;

public class RoleEditingGraphMousePlugin<V, E> extends EditingGraphMousePlugin<V, E> {

	private Role roleToCreate;
	
	public RoleEditingGraphMousePlugin(Factory<V> vertexFactory, Factory<E> edgeFactory) {
		super(vertexFactory, edgeFactory);
	}
	
	public Role getSelectedRole() {
		return roleToCreate;
	}
	
	public void setRole(Role toCreate){
		this.roleToCreate = toCreate;
	}
	
	/**
	 * If startVertex is non-null, and the mouse is released over an existing
	 * vertex, create an undirected edge from startVertex to the vertex under
	 * the mouse pointer. If shift was also pressed, create a directed edge
	 * instead.
	 */
	@SuppressWarnings("unchecked")
	public void mouseReleased(MouseEvent e) {
		if (checkModifiers(e)) {
			final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
					.getSource();
			final Point2D p = e.getPoint();
			Layout<V, E> layout = vv.getModel().getGraphLayout();
			GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
			if (pickSupport != null) {
				final V vertex = pickSupport.getVertex(layout, p.getX(),
						p.getY());
				// CARL - added a check that startVertex!=vertex - otherwise can
				// throw illegal argument exception
				if (vertex != null && startVertex != null
						&& vertex != startVertex) {
					E newEdge = edgeFactory.create();
					Graph<V, E> graph = vv.getGraphLayout().getGraph();
					graph.addEdge(newEdge, startVertex, vertex,
							edgeIsDirected);
					vv.repaint();
				}
			}
			startVertex = null;
			down = null;
			edgeIsDirected = EdgeType.UNDIRECTED;
			vv.removePostRenderPaintable(edgePaintable);
			vv.removePostRenderPaintable(arrowPaintable);

			// CARL - added a repaint on mouse released so that stray edges that
			// are removed actually disappear
			vv.repaint();
		}
	}

	/**
	 * If the mouse is pressed in an empty area, create a new vertex there. If
	 * the mouse is pressed on an existing vertex, prepare to create an edge
	 * from that vertex to another
	 */
	@SuppressWarnings("unchecked")
	public void mousePressed(MouseEvent e) {
		if (checkModifiers(e)) {
			final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
					.getSource();
			final Point2D p = e.getPoint();
			GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
			if (pickSupport != null) {
				Graph<V, E> graph = vv.getModel().getGraphLayout().getGraph();
				// set default edge type
				if (graph instanceof DirectedGraph) {
					edgeIsDirected = EdgeType.DIRECTED;
				} else {
					edgeIsDirected = EdgeType.UNDIRECTED;
				}

				final V vertex = pickSupport.getVertex(vv.getModel()
						.getGraphLayout(), p.getX(), p.getY());
				System.out.println("pX: " + p.getX() + " pY: " + p.getY());
				if (vertex != null) { // get ready to make an edge
					startVertex = vertex;
					down = e.getPoint();
					transformEdgeShape(down, down);
					vv.addPostRenderPaintable(edgePaintable);
					if ((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0
							&& vv.getModel().getGraphLayout().getGraph() instanceof UndirectedGraph == false) {
						edgeIsDirected = EdgeType.DIRECTED;
					}
					if (edgeIsDirected == EdgeType.DIRECTED) {
						transformArrowShape(down, e.getPoint());
						vv.addPostRenderPaintable(arrowPaintable);
					}

					// CARL - if the vertex is null but there is an attribute to
					// create, then make a vertex out of the attribute
				} else if (roleToCreate != null) {
					V newVertex = vertexFactory.create();
					Layout<V, E> layout = vv.getModel().getGraphLayout();
					graph.addVertex(newVertex);
					layout.setLocation(newVertex,
							vv.getRenderContext().getMultiLayerTransformer()
									.inverseTransform(e.getPoint()));

					// consume this attribute
					roleToCreate.setUsed(true);
					roleToCreate = null;

				}

			}
			vv.repaint();
		}
	}
	
	/**
	 * code lifted from PluggableRenderer to move an edge shape into an
	 * arbitrary position
	 */
	// CARL - changed to protected
	protected void transformEdgeShape(Point2D down, Point2D out) {
		float x1 = (float) down.getX();
		float y1 = (float) down.getY();
		float x2 = (float) out.getX();
		float y2 = (float) out.getY();

		AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);

		float dx = x2 - x1;
		float dy = y2 - y1;
		float thetaRadians = (float) Math.atan2(dy, dx);
		xform.rotate(thetaRadians);
		float dist = (float) Math.sqrt(dx * dx + dy * dy);
		xform.scale(dist / rawEdge.getBounds().getWidth(), 1.0);
		edgeShape = xform.createTransformedShape(rawEdge);
	}

	// CARL - changed to protected
	protected void transformArrowShape(Point2D down, Point2D out) {
		float x1 = (float) down.getX();
		float y1 = (float) down.getY();
		float x2 = (float) out.getX();
		float y2 = (float) out.getY();

		AffineTransform xform = AffineTransform.getTranslateInstance(x2, y2);

		float dx = x2 - x1;
		float dy = y2 - y1;
		float thetaRadians = (float) Math.atan2(dy, dx);
		xform.rotate(thetaRadians);
		arrowShape = xform.createTransformedShape(rawArrowShape);
	}
}
