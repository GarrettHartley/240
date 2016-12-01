package spell;

public class Node implements ITrie.INode{
	int frequency;
	Node[] alphabet;
	String path;
	
	public Node(){
		frequency =0;
		alphabet = new Node[26];
		path = new String();
	}

	public void incFrequency() {
		frequency++;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Node[] getAlpha(){
		return alphabet;
	}

	@Override
	public int getValue() {
		return frequency;
	}
	
	

}
