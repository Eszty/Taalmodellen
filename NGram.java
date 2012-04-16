
public class NGram implements Comparable<NGram> {
	private String string;
	private int count = 1;
	private int n;
	public String[] parts;
	
	public NGram( int n, String inputString, int inputCount ) {
		setN( n );
		setString(inputString);
		setCount(inputCount);
		parts = string.split(" ");
	}
	
	public String toString() {
		return string + " " + Integer.toString(count);
	}
	
	public void increment() {
		n++;
	}
	
	public void addString( String string ) {
		this.string += string;
	}
	
	public void setString( String string ) {
		this.string = string;
	}
	
	public String getString() {
		return string;
	}

	public void setN( int n ) {
		this.n = n;
	}
	
	public int getN() {
		return n;
	}

	public void setCount( int count ) {
		this.count = count;
	}
	
	public int getCount() {
		return count;
	}
	
	public int compareTo( NGram o ) {
		return (int) Math.signum( o.getCount() - this.getCount() );
	}	
}
