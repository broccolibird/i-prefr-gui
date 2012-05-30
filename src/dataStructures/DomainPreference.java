package dataStructures;

public class DomainPreference extends Pair<DomainValue,DomainValue>{

	public DomainPreference(DomainValue left, DomainValue right) {
		super(left, right);
	}
	
	@Override
	public String toString(){
		return left.toString()+ " > "+ right.toString();
	}

}
