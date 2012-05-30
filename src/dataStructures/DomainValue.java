package dataStructures;
//just a wrapper for String right now
public class DomainValue implements Comparable<DomainValue>{

	private String value;
	private AttributeKey key;

	public DomainValue(String value,AttributeKey key) {
		this.value = value;
		this.key = key;
	}
	
	public AttributeKey getAttributeKey(){
		return key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString(){
		return value;
	}

	@Override
	public int compareTo(DomainValue other) {
		return value.compareTo(other.toString());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DomainValue other = (DomainValue) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
}
