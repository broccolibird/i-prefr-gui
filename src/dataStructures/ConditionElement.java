package dataStructures;

public class ConditionElement{
	
	private boolean negated;
	private boolean isTrue;
	private Attribute attribute;
	private BinaryOperator op;
	private DomainValue domainValue;
	
	public ConditionElement(boolean negated, Attribute attribute,
			BinaryOperator op, DomainValue domainValue,boolean isTrue) {
		this.negated = negated;
		this.attribute = attribute;
		this.op = op;
		this.domainValue = domainValue;
		this.isTrue=isTrue;
	}
	
	public boolean isNegated() {
		return negated;
	}
	public void setNegated(boolean negated) {
		this.negated = negated;
	}
	public boolean isTrue() {
		return isTrue;
	}
	public void setTrue(boolean isTrue) {
		this.isTrue = isTrue;
	}
	public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	public BinaryOperator getOp() {
		return op;
	}
	public void setOp(BinaryOperator op) {
		this.op = op;
	}
	public DomainValue getDomainValue() {
		return domainValue;
	}
	public void setDomainValue(DomainValue domainValue) {
		this.domainValue = domainValue;
	}
	@Override
	public String toString(){
		String toReturn="";
		if(isTrue){
			toReturn= "TRUE";
		}
		else{
			if(negated){
				toReturn+="NOT[";
			}
			toReturn+=attribute.toString()+" "+op.toString()+" "+domainValue.toString();
			if(negated){
				toReturn+="]";
			}
		}

		return toReturn;
	}
}
