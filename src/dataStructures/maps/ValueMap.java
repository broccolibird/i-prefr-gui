package dataStructures.maps;

import java.util.HashMap;

import dataStructures.AttributeKey;
import dataStructures.DomainValue;

@SuppressWarnings("serial")
public class ValueMap extends HashMap<AttributeKey, DomainValue>{
	
	public DomainValue put(AttributeKey key, DomainValue value){
		DomainValue v = super.put(key, value);
		//System.out.println("putting key: "+key+" value: "+value);
		return v;
		
	}

}
