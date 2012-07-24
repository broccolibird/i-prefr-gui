package graph;

import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashMap;

import javax.swing.JFrame;

import dataStructures.Attribute;
import dataStructures.maps.AttributeMap;
import dataStructures.maps.EdgeStatementMap;
import dataStructures.maps.VertexStatementMap;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.picking.PickedState;
import graph.jungClasses.AnnotatingGraphMousePlugin;
import graph.jungClasses.Annotation;
import guiElements.EdgeAnnotationDialog;
import guiElements.VertexAnnotationDialog;

public class AnnotationDialogMousePlugin<V, E> extends
		AnnotatingGraphMousePlugin<V, E> {
	private JFrame parentFrame;
	private AttributeMap attributeMap;
	private SparseMultigraph<V,E> graph;
	private HashMap<EdgeStatementMap,String> edgeLabelMap;

	/**
	 * the picked Vertex, if any
	 */
	protected V vertex;

	/**
	 * the picked Edge, if any
	 */
	protected E edge;

	/**
	 * create an instance with default settings
	 */
	public AnnotationDialogMousePlugin(RenderContext<V, E> rc,
			JFrame parentFrame, AttributeMap attributeMap,SparseMultigraph<V,E> graph,HashMap<EdgeStatementMap,String> edgeLabelMap) {
		super(rc);
		this.parentFrame = parentFrame;
		this.attributeMap = attributeMap;
		this.graph=graph;
		this.edgeLabelMap=edgeLabelMap;
	}

	/**
	 * selects a single vertex, or if one is selected and pop up button is
	 * pressed, opens VertexAnnotationDialog to annotate that vertex
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void mousePressed(MouseEvent e) {
		// get the tools to discern the vertex
		down = e.getPoint();
		@SuppressWarnings("rawtypes")
		VisualizationViewer<V, E> vv = (VisualizationViewer) e.getSource();
		GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
		PickedState<V> pickedVertexState = vv.getPickedVertexState();
		PickedState<E> pickedEdgeState = vv.getPickedEdgeState();

		// if these tools are not null, get the vertex selected
		if (pickSupport != null && pickedVertexState != null && vv != null) {
			Layout<V, E> layout = vv.getGraphLayout();
			Point2D ip = e.getPoint();
			double x = ip.getX();
			double y = ip.getY();
			edge = pickSupport.getEdge(layout, x,y);
			vertex = pickSupport.getVertex(layout, ip.getX(), ip.getY());
			if (vertex != null) {
				// if this vertex is not already selected, then just select it
				if (pickedVertexState.isPicked(vertex) == false) {
					pickedVertexState.clear();
					pickedVertexState.pick(vertex, true);

					// if it is a right-click, then pop up the
					// VertexAnnotationDialog
				} else if (e.isPopupTrigger()) {
					Annotation<VertexStatementMap, V> thisAnnotation = annotationManager
							.getAnnotation(vertex);
					VertexStatementMap oldMap = null;
					if (thisAnnotation != null) {
						oldMap = (VertexStatementMap) thisAnnotation.getAnnotation();
						annotationManager.remove(thisAnnotation);
					} else {
						oldMap = new VertexStatementMap();
						thisAnnotation = new Annotation<VertexStatementMap, V>(null, layer,
								annotationColor, fill, ip, vertex);
					}
					// CARL - this is one of the critical areas of code - the
					// ability to associate a particular VertexStatementMap with an
					// Annotation that remembers its associated Vertex
					
					//Attribute existingAttribute = (Attribute)vertex;
					//System.out.println("existing Attribute: "+existingAttribute.toString());
					VertexAnnotationDialog vertexAnnotationDialog = new VertexAnnotationDialog(
							parentFrame, oldMap, attributeMap,
							(Attribute) vertex);
					VertexStatementMap newTable = (VertexStatementMap) vertexAnnotationDialog
							.getMap();
					thisAnnotation.setAnnotation((VertexStatementMap) newTable);
					annotationManager.add(layer, thisAnnotation);
				}
			} else if (edge != null) {
				// if this edge is not already selected, then just select it
				if (pickedEdgeState.isPicked(edge) == false) {
					pickedEdgeState.clear();
					pickedEdgeState.pick(edge, true);

					// if it is a right-click, then pop up the
					// VertexAnnotationDialog
				} else {
					Pair<Attribute> endpoints = (Pair<Attribute>) graph.getEndpoints((E)edge);				
					@SuppressWarnings("unused")
					EdgeAnnotationDialog edgeAnnotationDialog = new EdgeAnnotationDialog(
							parentFrame, (EdgeStatementMap) edge, attributeMap,endpoints.getFirst(), endpoints.getSecond());
					
					//the edge annotation might have been altered so swap out the label in the map
					String newLabel = edge.toString();
					System.out.println("new label should be: " + newLabel);
					edgeLabelMap.put((EdgeStatementMap)edge, edge.toString());
					vv.repaint();
				}

			} else {
				vv.addPostRenderPaintable(lensPaintable);
				pickedVertexState.clear();
			}

		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			if(vertex != null) {
				Point2D ip = e.getPoint();
				
				Annotation<VertexStatementMap, V> thisAnnotation = annotationManager
						.getAnnotation(vertex);
				VertexStatementMap oldMap = null;
				if (thisAnnotation != null) {
					oldMap = (VertexStatementMap) thisAnnotation.getAnnotation();
					annotationManager.remove(thisAnnotation);
				} else {
					oldMap = new VertexStatementMap();
					thisAnnotation = new Annotation<VertexStatementMap, V>(null, layer,
							annotationColor, fill, ip, vertex);
				}
				// CARL - this is one of the critical areas of code - the
				// ability to associate a particular VertexStatementMap with an
				// Annotation that remembers its associated Vertex
				
				//Attribute existingAttribute = (Attribute)vertex;
				//System.out.println("existing Attribute: "+existingAttribute.toString());
				VertexAnnotationDialog vertexAnnotationDialog = new VertexAnnotationDialog(
						parentFrame, oldMap, attributeMap,
						(Attribute) vertex);
				VertexStatementMap newTable = (VertexStatementMap) vertexAnnotationDialog
						.getMap();
				thisAnnotation.setAnnotation((VertexStatementMap) newTable);
				annotationManager.add(layer, thisAnnotation);
			}
		}
	}

	public void mouseDragged(MouseEvent e) {
		// do nothing
	}

}
