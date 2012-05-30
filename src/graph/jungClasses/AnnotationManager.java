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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import dataStructures.maps.VertexStatementMap;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.transform.AffineTransformer;
import edu.uci.ics.jung.visualization.transform.LensTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * handles the selection of annotations, and the support for the
 * tools to draw them at specific layers.
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
 *
 */
public class AnnotationManager<V,E> {
	
    protected AnnotationRenderer annotationRenderer = new AnnotationRenderer();
	protected AnnotationPaintable<E,V> lowerAnnotationPaintable;
	protected AnnotationPaintable<E,V> upperAnnotationPaintable;
	
	protected RenderContext<V,E> rc;
	protected AffineTransformer transformer;

	public AnnotationManager(RenderContext<V,E> rc) {
		this.rc = rc;
		this.lowerAnnotationPaintable = new AnnotationPaintable<E,V>(rc, annotationRenderer);
		this.upperAnnotationPaintable = new AnnotationPaintable<E,V>(rc, annotationRenderer);
		
		MutableTransformer mt = rc.getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		if(mt instanceof AffineTransformer) {
			transformer = (AffineTransformer)mt;
		} else if(mt instanceof LensTransformer) {
			transformer = (AffineTransformer)((LensTransformer)mt).getDelegate();
		}

	}
	
	public AnnotationPaintable<E,V> getAnnotationPaintable(Annotation.Layer layer) {
		if(layer == Annotation.Layer.LOWER) {
			return this.lowerAnnotationPaintable;
		}
		if(layer == Annotation.Layer.UPPER) {
			return this.upperAnnotationPaintable;
		}
		return null;
	}
	/*
	 * 
	 */
	public void add(Annotation.Layer layer, Annotation<VertexStatementMap, V> thisAnnotation) {
		if(layer == Annotation.Layer.LOWER) {
			this.lowerAnnotationPaintable.add(thisAnnotation);
		}
		if(layer == Annotation.Layer.UPPER) {
			this.upperAnnotationPaintable.add(thisAnnotation);
		}
	}
	
	public void remove(Annotation<VertexStatementMap, V> thisAnnotation) {
		this.lowerAnnotationPaintable.remove(thisAnnotation);
		this.upperAnnotationPaintable.remove(thisAnnotation);
	}
	
	//CARL - changed from protected to public
	public AnnotationPaintable<E,V> getLowerAnnotationPaintable() {
		return lowerAnnotationPaintable;
	}
	
	//CARL - changed from protected to public
	public AnnotationPaintable<E,V> getUpperAnnotationPaintable() {
		return upperAnnotationPaintable;
	}
	
	public Annotation<VertexStatementMap,V> getAnnotation(Point2D p) {
		Set<Annotation<VertexStatementMap,V>> annotations = new HashSet<Annotation<VertexStatementMap,V>>(lowerAnnotationPaintable.getAnnotations());
		annotations.addAll(upperAnnotationPaintable.getAnnotations());
		return getAnnotation(p, annotations);
	}
	
	//CARL - added this method
	@SuppressWarnings("rawtypes")
	public Annotation getAnnotation(V vertex){
		Set<Annotation> annotations = new HashSet<Annotation>(lowerAnnotationPaintable.getAnnotations());
		annotations.addAll(upperAnnotationPaintable.getAnnotations());
		for(Annotation a : annotations){
			if(a.getVertex().equals(vertex))
				return a;
		}
		return null;
	}
	
    public Annotation<VertexStatementMap,V> getAnnotation(Point2D p, Collection<Annotation<VertexStatementMap,V>> annotations) {
		double closestDistance = Double.MAX_VALUE;
		Annotation<VertexStatementMap,V> closestAnnotation = null;
		for(Annotation<VertexStatementMap,V> annotation : annotations) {
			Object ann = annotation.getAnnotation();
			if(ann instanceof Shape) {
				Point2D ip = rc.getMultiLayerTransformer().inverseTransform(p);
				Shape shape = (Shape)ann;
				if(shape.contains(ip)) {
					
					Rectangle2D shapeBounds = shape.getBounds2D();
					Point2D shapeCenter = new Point2D.Double(shapeBounds.getCenterX(), shapeBounds.getCenterY());
					double distanceSq = shapeCenter.distanceSq(ip);
					if(distanceSq < closestDistance) {
						closestDistance = distanceSq;
						closestAnnotation = annotation;
					}
				}
			} else if(ann instanceof String) {
				
				Point2D ip = rc.getMultiLayerTransformer().inverseTransform(Layer.VIEW, p);
				Point2D ap = annotation.getLocation();
				String label = (String)ann;
				Component component = prepareRenderer(rc, annotationRenderer, label);
				
				AffineTransform base = new AffineTransform(transformer.getTransform());
				double rotation = transformer.getRotation();
				// unrotate the annotation
				AffineTransform unrotate =
					AffineTransform.getRotateInstance(-rotation, ap.getX(), ap.getY());
				base.concatenate(unrotate);
				
				Dimension d = component.getPreferredSize();
				Rectangle2D componentBounds = new Rectangle2D.Double(ap.getX(), ap.getY(), d.width, d.height);
				
				Shape componentBoundsShape = base.createTransformedShape(componentBounds);
				Point2D componentCenter = new Point2D.Double(componentBoundsShape.getBounds().getCenterX(),
						componentBoundsShape.getBounds().getCenterY());
				if(componentBoundsShape.contains(ip)) {
					double distanceSq = componentCenter.distanceSq(ip);
					if(distanceSq < closestDistance) {
						closestDistance = distanceSq;
						closestAnnotation = annotation;
					}
				}
				
			}
		}
		return closestAnnotation;
	}
	
	public Component prepareRenderer(RenderContext<?,?> rc, AnnotationRenderer annotationRenderer, Object value) {
		return annotationRenderer.getAnnotationRendererComponent(rc.getScreenDevice(), value);
	}


	
	

}
