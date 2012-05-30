package dataStructures;

public class AttributePreference extends Pair<Attribute,Attribute>{

	public AttributePreference(Attribute left, Attribute right) {
		super(left, right);
	}
	
	@Override
	public String toString(){
		return left.toString()+ " > "+ right.toString();
	}

}
