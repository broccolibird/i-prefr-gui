package graph;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

import dataStructures.Role;
import dataStructures.SavedAnnotation;
import dataStructures.maps.RoleMap;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import graph.annotations.VertexAnnotation;

/** 
 * Acyclic version of DirectedSparseGraph.
 * @author Katarina Mitchell
 *
 * @param <V> the vertex type
 * @param <E> the edge type
 */
@SuppressWarnings({ "serial" })
public class RoleHierarchy extends DirectedSparseGraph<Role,Integer>
{
	RoleMap map;
	AbstractLayout<Role, Integer> layout;
	RoleEditingModalGraphMouse<Role, Integer> graphMouse;
	
	int nextEdge;
	ArrayList<SavedAnnotation<Role,String>> savedAnnotations;
	
	public RoleHierarchy(RoleMap map) {
		this.map = map;
	}
	
	public AbstractLayout<Role, Integer> getLayout() {
		return layout;
	}
	
	public void setLayout(AbstractLayout<Role, Integer> layout2) {
		this.layout = layout2;
	}
	
	public void setGraphMouse(
			RoleEditingModalGraphMouse<Role, Integer> graphMouse) {
		this.graphMouse = graphMouse;
		
	}
	
	public int getNextEdge() {
		return nextEdge;
	}
	
	public void setNextEdge(int nextEdge) {
		this.nextEdge = nextEdge;
	}
	
	@Override
    public boolean addEdge(Integer edge, Pair<? extends Role> endpoints, EdgeType edgeType)
    {
    	this.validateEdgeType(edgeType);
        Pair<Role> new_endpoints = getValidatedEndpoints(edge, endpoints);
        if (new_endpoints == null)
            return false;
        
        Role source = new_endpoints.getFirst();
        Role dest = new_endpoints.getSecond();
        
        if (findEdge(source, dest) != null)
            return false;
        
        if(makesCycle(source,dest)) {
        	
        	return false;
        }
        
        edges.put(edge, new_endpoints);

        if (!vertices.containsKey(source))
            this.addVertex(source);
        
        if (!vertices.containsKey(dest))
            this.addVertex(dest);
        
        // map source of this edge to <dest, edge> and vice versa
        vertices.get(source).getSecond().put(dest, edge);
        vertices.get(dest).getFirst().put(source, edge);

        // new edge, role map has unsaved changes
        map.setSaved(false);
        return true;
    }
	
	@Override
	public boolean addVertex(Role vertex){
		// new vertex, role map has unsaved changes
		map.setSaved(false);
		return super.addVertex(vertex);
	}
	
	@Override
	public boolean removeEdge(Integer edge) {
		// edge removed, role map has unsaved changes
		map.setSaved(false);
		return super.removeEdge(edge);
	}
	
	@Override
	public boolean removeVertex(Role vertex) {
		// vertex removed, role map has unsaved changes
		map.setSaved(false);
		return super.removeVertex(vertex);
	}
	
	private boolean makesCycle(Role source, Role dest) {
		Collection<Integer> outEdges =  getOutEdges(dest);
		for(Integer edge : outEdges ) {
			Role edgeDest = getDest(edge);
			if(edgeDest == source) {
				return true;
			} else {
				boolean makesCycle = makesCycle(source, edgeDest);
				if(makesCycle)
					return true;
			}
		}
		return false;
	}
	
	public void addSavedAnnotation(SavedAnnotation annotation) {
		if(savedAnnotations == null)
				savedAnnotations = new ArrayList<SavedAnnotation<Role,String>>();
		
		savedAnnotations.add(annotation);
	}
	
	public ArrayList<SavedAnnotation<Role,String>> getSavedAnnotations() {
		return savedAnnotations;
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
	    	
	    	if(graphMouse != null){
	    		VertexAnnotation annotation = graphMouse.getAnnotatingPlugin().getAnnotation(role);
	    		System.out.println("Annotation: "+annotation);
	    		if(annotation != null)
	    			xml += "\t\t<ANNOTATION>"+annotation.getAnnotation()+"</ANNOTATION>\n";
	    	}
	    	
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
