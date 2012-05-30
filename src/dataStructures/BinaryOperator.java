package dataStructures;

public class BinaryOperator {
	public static final int EQUAL = 0;
	public static final int NOT_EQUAL = 1;
	private int identifier;
	
	public static BinaryOperator[] getOperators(){
		int nOperators=2;
		BinaryOperator[] allOperators = new BinaryOperator[nOperators];
		for(int i=0;i<nOperators;i++){
			allOperators[i]=new BinaryOperator(i);
		}
		return allOperators;
	}

	public BinaryOperator(int identifier) {
		this.identifier = identifier;
	}

	public int getIdentifier() {
		return identifier;
	}

	public String toString() {
		switch (identifier) {
		case EQUAL:
			return "=";
		case NOT_EQUAL:
			return "!=";
		default:
			return "??";
		}
	}
}
