package dataStructures;

import java.util.Iterator;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class DomainValueList extends LinkedList<DomainValue> {
	
	public DomainValue[] toArray(){

		Iterator<DomainValue> it = iterator();
		DomainValue[] toReturn = new DomainValue[size()];
		int i=0;
		while(it.hasNext()){
			toReturn[i]=it.next();
			i++;
		}
		return toReturn;
	}

}
