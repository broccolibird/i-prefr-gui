package dataStructures;

//just a wrapper for Integer right now
public class AttributeKey implements Comparable<Integer>{
	
	private Integer key;

	public AttributeKey(Integer key) {
		this.key = key;
	}

	public Integer getKey() {
		return key;
	}

	public void setKey(Integer key) {
		this.key = key;
	}

	@Override
	public int hashCode() {
		return key;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AttributeKey other = (AttributeKey) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
//
//	@Override
//	public int compareTo(AttributeKey o) {
//		return key.compareTo(o.key);
//	}

	@Override
	public int compareTo(Integer o) {
		return key.compareTo(o);
	}
	
	@Override
	public String toString(){
		return key.toString();
	}
	
	
	
	
}
