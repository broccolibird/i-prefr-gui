package dataStructures;

import java.awt.geom.Point2D;

/**
 * SavedAnnotation is used for storing annotations from
 * an opened project file that are to be reinstated in
 * a graph.
 * 
 * @param <V>
 * @param <T>
 */
public class SavedAnnotation<V,T> {

	private V vertex;
	private T annotation;
	private Point2D location;
	
	public SavedAnnotation(T annotation, V vertex, Point2D location) {
		this.vertex = vertex;
		this.annotation = annotation;
		this.location = location;
	}

	public V getVertex() {
		return vertex;
	}
	
	public T getAnnotation() {
		return annotation;
	}
	
	public Point2D getLocation() {
		return location;
	}
}
