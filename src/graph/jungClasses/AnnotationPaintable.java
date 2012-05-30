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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

import dataStructures.maps.VertexStatementMap;

import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.VisualizationServer.Paintable;
import edu.uci.ics.jung.visualization.transform.AffineTransformer;
import edu.uci.ics.jung.visualization.transform.LensTransformer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

/**
 * handles the actual drawing of annotations
 * 
 * @author Tom Nelson - tomnelson@dev.java.net
 * 
 * 
  *         CARL - an important note about this class is that it has been
 *         repackaged here to refer to my Annotation class (that is associate
 *         with a Vertex) instead of the JUNG library Annotation class - this is
 *         true for the 6 classes in this package that start with the word
 *         Annotation: Annotation, AnnotationControls, AnnotationManager,
 *         AnnotationPaintable, AnnotationRenderer, AnnotatingGraphMousePlugin
 *
 */
public class AnnotationPaintable<T,V> implements Paintable {
	
	protected Set<Annotation<VertexStatementMap,V>> annotations = new HashSet<Annotation<VertexStatementMap,V>>();
    protected AnnotationRenderer annotationRenderer;

	protected RenderContext<?,?> rc;
	protected AffineTransformer transformer;
	
	public AnnotationPaintable(RenderContext<?,?> rc, AnnotationRenderer annotationRenderer) {
		this.rc = rc;
		this.annotationRenderer = annotationRenderer;
		MutableTransformer mt = rc.getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		if(mt instanceof AffineTransformer) {
			transformer = (AffineTransformer)mt;
		} else if(mt instanceof LensTransformer) {
			transformer = (AffineTransformer)((LensTransformer)mt).getDelegate();
		}
	}
	
    public void add(Annotation<VertexStatementMap, V> thisAnnotation) {
		annotations.add(thisAnnotation);
		
//		String toPrint ="Annotation Set contents: [ ";
//		for(Annotation<T,V> a : annotations)
//			toPrint+=a.toString()+" | ";
//		System.out.println(toPrint+ "]");
	}
	
	public void remove(Annotation<VertexStatementMap, V> thisAnnotation) {
		annotations.remove(thisAnnotation);
	}
	
    /**
	 * @return the annotations
	 */
    public Set<Annotation<VertexStatementMap,V>> getAnnotations() {
		return Collections.unmodifiableSet(annotations);
	}

    public void paint(Graphics g) {
    	Graphics2D g2d = (Graphics2D)g;
        Color oldColor = g.getColor();
        for(Annotation<VertexStatementMap,V> annotation : annotations) {
        	Object ann = annotation.getAnnotation();
        	if(ann instanceof Shape) {
            	Shape shape = (Shape)ann;
            	Paint paint = annotation.getPaint();
            	Shape s = transformer.transform(shape);
            	g2d.setPaint(paint);
            	if(annotation.isFill()) {
            		g2d.fill(s);
            	} else {
            		g2d.draw(s);
            	}
        	} else if(ann instanceof String) {
            	Point2D p = annotation.getLocation();
            	String label = (String)ann;
                Component component = prepareRenderer(rc, annotationRenderer, label);
                component.setForeground((Color)annotation.getPaint());
                if(annotation.isFill()) {
                	((JComponent)component).setOpaque(true);
                	component.setBackground((Color)annotation.getPaint());
                	component.setForeground(Color.black);
                }
                Dimension d = component.getPreferredSize();
                AffineTransform old = g2d.getTransform();
                AffineTransform base = new AffineTransform(old);
                AffineTransform xform = transformer.getTransform();

                double rotation = transformer.getRotation();
                // unrotate the annotation
                AffineTransform unrotate = 
                	AffineTransform.getRotateInstance(-rotation, p.getX(), p.getY());
                base.concatenate(xform);
                base.concatenate(unrotate);
                g2d.setTransform(base);
                rc.getRendererPane().paintComponent(g, component, rc.getScreenDevice(), 
                        (int)p.getX(), (int)p.getY(),
                        d.width, d.height, true);
                g2d.setTransform(old);
        	}
//        	else if(ann instanceof DomainPreferenceMap){
//        		Point2D p = annotation.getLocation();
//        		
//        	}
        }
        g.setColor(oldColor);
    }
    
	public Component prepareRenderer(RenderContext<?,?> rc, AnnotationRenderer annotationRenderer, Object value) {
		return annotationRenderer.getAnnotationRendererComponent(rc.getScreenDevice(), value);
	}

    public boolean useTransform() {
        return true;
    }
}
