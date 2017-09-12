public class Register {
	private int size;
	private Object[] register;
	// Store any type of object
	public Register(int size) {
		this.size = size;
		register = new Object[size];
		initRegister(size);
	}
	// No need to check for freshness on insertion as we allow stale transitions
	public boolean add(Object object, int index) {
		if (index >=0 && index < size) {
			register[index] = object;
			return true;
		}
		return false;
	}
	// Check for freshness
	public boolean isFresh(Object object) {
		for(int i=0; i<size; i++) {
			if (register[i] != null) {
				if (register[i].toString().equals(object.toString())) return false;
			}
		}
		return true;
	}
	// Initiate register to null
	private void initRegister(int size){
		for (int i=0; i<size; i++){
			register[i] = null;
		}
	}
}
