package spell;

import java.util.ArrayList;
import java.util.Arrays;

public class Node implements ITrie.INode {
	private Node[] alphabet;
	private String path;
	private int frequency;
	
	public Node(){
		frequency = 0;
		path= new String();
		alphabet = new Node[26];
	}
	
	public Node copyNode(Node n){
		this.path = n.getPath();
		this.frequency = n.getValue();
		this.alphabet = n.getAlphabet();
		return this;
	}
	
	public Node[] getAlphabet() {
		return alphabet;
	}

	public void insertAlphabet(int i, Node n) {
		this.alphabet[i] = n;
	}

	public void incFrequency(){
		frequency++;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int getValue() {
		return frequency;
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
	
}
