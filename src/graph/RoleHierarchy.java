package graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import dataStructures.Role;

import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.util.TreeUtils;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.GraphDecorator;
import edu.uci.ics.jung.graph.Tree;

/**
 * An implementation of <code>Forest<V,E></code> that delegates to a specified <code>DirectedGraph</code>
 * instance.
 * @author Tom Nelson
 * 
 * Modified from the DelegateForest implementation in order to allow the user to
 * add edges to two existing vertices
 * @author Katarina Mitchell
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class RoleHierarchy extends DirectedSparseGraph<Role,Integer>
{
	StaticLayout layout;
	int nextEdge;
	
	public StaticLayout getLayout() {
		return layout;
	}
	
	public void setLayout(StaticLayout layout) {
		this.layout = layout;
	}
	
	public int getNextEdge() {
		return nextEdge;
	}
	
	public void setNextEdge(int nextEdge) {
		this.nextEdge = nextEdge;
	}
	
	 public String toXML() {
	    	String xml = "<ROLEHIERARCHY>\n";
	    	
	    	Collection<Role> roles = getVertices();
	    	
	    	for( Role role : roles ) {
	    		xml += roleToXML(role);
	    	}
	    	
	    	xml += "</ROLEHIERARCHY>\n";
	    	return xml;
	 }
	 
	 private String roleToXML(Role role) {
	    	String xml = "\t<ROLE ID='"+role.getKey()+"'>\n";
	    	xml += "\t\t<TITLE>"+role.getName()+"</TITLE>\n";
	    	xml += "\t\t<COORDINATES>\n";
	    	Point2D coord = layout.transform(role);
	    	xml += "\t\t\t<X>"+coord.getX()+"</X>\n";
	    	xml += "\t\t\t<Y>"+coord.getY()+"</Y>\n";
	    	xml += "\t\t</COORDINATES>\n";
	    	
	    	Collection<Role> predecessors = getPredecessors(role);
	    	if ( predecessors.size() > 0) {
	    		xml += "\t\t<SUPERIORS>\n";
	    		for( Role predecessor : predecessors ) {
	    			xml += "\t\t\t<SUPERIOR ID='"+predecessor.getKey()+"'>"+predecessor.getName()+"</SUPERIOR>\n";
	    		}
	    		xml += "\t\t</SUPERIORS>\n";
	    	}
	    	xml += "\t</ROLE>\n";
	    	return xml;
	    }
	    
}
