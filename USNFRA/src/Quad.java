public class Quad<L,ML,MR,R> {
	private L left; 
	private ML midleft;
	private MR midright;
	private R right;
	
	public Quad(L left, ML midleft, MR midright, R right) {
	    this.left = left;
	    this.midleft = midleft;
	    this.midright = midright;
	    this.right = right;
	}
	
	public ML getMidLeft() {
	    return midleft;
	}
	
	public MR getMidRight() {
	    return midright;
	}
	
	public L getLeft() {
	    return left;
	}
	
	public R getRight() {
	    return right;
	}
}