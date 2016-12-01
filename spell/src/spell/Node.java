package spell;

import java.util.Arrays;

public class Node implements ITrie.INode {
	int frequency;
	Node[] alphabet;
	String path;
	
	public Node(){
		frequency =0;
		alphabet = new Node[26];
		path = new String();
	}
	
	public void incF(){
		frequency++;
	}
	
	public String getPath() {
		return path;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(alphabet);
		result = prime * result + frequency;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		Node other = (Node) obj;
		if (!Arrays.equals(alphabet, other.alphabet))
			return false;
		if (frequency != other.frequency)
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Node[] getAlpha(){
		return alphabet;
	}
	
	@Override
	public int getValue() {
		// TODO Auto-generated method stub
		return frequency;
	}

}
