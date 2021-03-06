package dataStructures;

public class Pair<L, R> implements Comparable<Pair<L, R>> {
	protected L left;
	protected R right;

	public Pair(L left, R right) {
		this.left = left;
		this.right = right;
	}

	public L getLeft() {
		return left;
	}

	public void setLeft(L left) {
		this.left = left;
	}

	public R getRight() {
		return right;
	}

	public void setRight(R right) {
		this.right = right;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		 if (right == null) {
		 if (other.right != null)
		 return false;
		 } else if (!right.equals(other.right))
		 return false;
		return true;
	}

	@Override
	public String toString() {
		return left.toString() + ", " + right.toString();
	}

	@Override
	public int compareTo(Pair<L, R> o) {
		return new Integer(this.hashCode())
				.compareTo(new Integer(o.hashCode()));
	}

}
