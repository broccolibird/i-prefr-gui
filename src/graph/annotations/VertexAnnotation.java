package graph.annotations;

import java.awt.Paint;
import java.awt.geom.Point2D;

import edu.uci.ics.jung.visualization.annotations.Annotation;

/**
 * VertexAnnotation is an extension of the jung Annotation class
 * which associates each annotation with its own Vertex.
 * 
 * @author Katarina Mitchell
 *
 * @param <T>
 * @param <V>
 */
public class VertexAnnotation<T,V> extends Annotation<T>{

	protected V vertex;
	
	/**
	 * Create a new instance of VertexAnnotation
	 * @param annotation
	 * @param layer
	 * @param paint
	 * @param fill
	 * @param location
	 * @param vertex
	 */
	public VertexAnnotation(T annotation, Layer layer,
			Paint paint, boolean fill, Point2D location,
			V vertex) {
		super(annotation, layer, paint, fill, location);
		this.vertex=vertex;
	}

	/**
	 * Returns the vertex associated with this annotation
	 * @return the associated vertex
	 */
	public V getVertex() {
		return vertex;
	}
}
