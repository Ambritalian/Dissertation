public class Pair<L,R> {
	private L left; 
	private R right;
	private int key;
	
	public Pair(L left, R right) {
	    this.left = left;
	    this.right = right;
	    this.key = left.hashCode()+right.hashCode();
	}
	
	public L getLeft() {
	    return left;
	}
	
	public R getRight() {
	    return right;
	}
	
	public int getKey() {
		return key;
	}
	
}