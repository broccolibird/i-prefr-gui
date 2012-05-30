package graph.jungClasses;

/*
 * Copyright (c) 2005, the JUNG Project and the Regents of the University of
 * California All rights reserved.
 *
 * This software is open-source under the BSD license; see either "license.txt"
 * or http://jung.sourceforge.net/license.txt for a description.
 *
 * 
 */

import java.awt.Paint;
import java.awt.geom.Point2D;

import dataStructures.maps.VertexStatementMap;

/**
 * stores an annotation, either a shape or a string
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 *
 *         CARL - an important note about this class is that it has been
 *         repackaged here to refer to my Annotation class (that is associated
 *         with a Vertex) instead of the JUNG library Annotation class - this is
 *         true for the 6 classes in this package that start with the word
 *         Annotation: Annotation, AnnotationControls, AnnotationManager,
 *         AnnotationPaintable, AnnotationRenderer, AnnotatingGraphMousePlugin
 *
 * @param <T>
 */
public class Annotation<T,V> {
	
	protected VertexStatementMap annotation;
	protected Paint paint;
	protected Point2D location;
	protected V vertex;
	protected Layer layer;
	protected boolean fill;
	public static enum Layer { LOWER, UPPER }
	
	
	
	public Annotation(VertexStatementMap annotation, Layer layer2, Paint paint, 
			boolean fill, Point2D location, V vertex) {
		this.annotation = annotation;
		this.layer = layer2;
		this.paint = paint;
		this.fill = fill;
		this.location = location;
		this.vertex=vertex;
	}
	/**
	 * @return the annotation
	 */
	public VertexStatementMap getAnnotation() {
		return annotation;
	}
	/**
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(VertexStatementMap annotation) {
		this.annotation = annotation;
	}
	/**
	 * @return the location
	 */
	public Point2D getLocation() {
		return location;
	}
	/**
	 * @return the layer
	 */
	public Layer getLayer() {
		return layer;
	}
	/**
	 * @param layer the layer to set
	 */
	public void setLayer(Layer layer) {
		this.layer = layer;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Point2D location) {
		this.location = location;
	}
	public V getVertex() {
		return vertex;
	}
	public void setVertex(V vertex) {
		this.vertex = vertex;
	}
	/**
	 * @return the paint
	 */
	public Paint getPaint() {
		return paint;
	}
	/**
	 * @param paint the paint to set
	 */
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	/**
	 * @return the fill
	 */
	public boolean isFill() {
		return fill;
	}
	/**
	 * @param fill the fill to set
	 */
	public void setFill(boolean fill) {
		this.fill = fill;
	}
	
	@Override
	public String toString(){
		return annotation.toString();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((annotation == null) ? 0 : annotation.hashCode());
		result = prime * result + (fill ? 1231 : 1237);
		result = prime * result + ((layer == null) ? 0 : layer.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((paint == null) ? 0 : paint.hashCode());
		result = prime * result + ((vertex == null) ? 0 : vertex.hashCode());
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Annotation<T,V> other = (Annotation<T,V>) obj;
		if (annotation == null) {
			if (other.annotation != null)
				return false;
		} else if (!annotation.equals(other.annotation))
			return false;
		if (fill != other.fill)
			return false;
		if (layer != other.layer)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (paint == null) {
			if (other.paint != null)
				return false;
		} else if (!paint.equals(other.paint))
			return false;
		if (vertex == null) {
			if (other.vertex != null)
				return false;
		} else if (!vertex.equals(other.vertex))
			return false;
		return true;
	}

}

