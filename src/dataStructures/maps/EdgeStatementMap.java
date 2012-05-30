package dataStructures.maps;

import java.util.Collection;

import dataStructures.EdgeStatement;

@SuppressWarnings("serial")
public class EdgeStatementMap extends SuperkeyMap<EdgeStatement>{
	
	@Override
	public String toString(){

		String toReturn = "";
		Collection<EdgeStatement> edgeStatements = values();
		for(EdgeStatement es : edgeStatements){
			toReturn+=" "+es.toString()+", ";
		}
		if(toReturn.length()>1){
			toReturn = toReturn.substring(0,toReturn.length()-2);
		}
		return toReturn;
	}

}
