package spell;

public class Trie implements ITrie{

	Node root;
	int wordCount;
	int nodeCount;
	
	public Trie(){
	root = new Node();
	wordCount = 0;
	nodeCount = 1;
	}
	
	@Override
	public void add(String word) {
		word= word.toLowerCase();

		addR(word,root);
	}
	
	public void addR(String word, Node n){
		int i = word.charAt(0)-'a';
		//last letter
		if(word.length()==1){
			//word not exists
			if(n.getAlpha()[i]==null){
				Node nn = new Node();
				nn.setPath(n.getPath()+word.charAt(0));
				nn.incF();
				wordCount++;
				nodeCount++;
				n.getAlpha()[i]=nn;
				//System.out.println("path "+nn.getPath());
				return;
			}
			//does  exist
			else{
				n.getAlpha()[i].incF();
				return;
			}
		}
		//node exists
		if(n.getAlpha()[i]!=null){
			String newWord = new String(word.substring(1));
			addR(newWord,n.getAlpha()[i]);
		}
		// does not exist
		else{
			String newWord = new String(word.substring(1));
			Node nn = new Node();
			nn.setPath(n.getPath()+word.charAt(0));
			nodeCount++;
			n.getAlpha()[i]=nn;
			addR(newWord,nn);
		}
		
		
	}

	@Override
	public INode find(String word) {
		return findR(word,root);
	}
	
	public INode findR(String word, Node n){
		if(word.length()==0){
			return null;
		}
		int i = word.charAt(0)-'a';
		if(word.length()==1){
			if(n.getAlpha()[i]!=null&&n.getAlpha()[i].getValue()>0){
				return n.getAlpha()[i];
			}
			else{
			return null;
			}
		}
		if(n.getAlpha()[i]!=null){
			String newWord = new String(word.substring(1));
			return findR(newWord,n.getAlpha()[i]);
		}
		else{
			return null;
		}
	}
	
	

	@Override
	public String toString() {
		StringBuilder dictionaryString= new StringBuilder();
		toStringR(dictionaryString,root);
		return dictionaryString.toString();
	}
	
	public void toStringR(StringBuilder dString,Node n){
		for(Node nn: n.getAlpha()){
			if(nn!=null){		
				if(nn.getValue()>0){
					dString.append(nn.getPath()+"\n");
				}
					toStringR(dString,nn);
			}
		}
	}

	@Override
	public int getWordCount() {
		return wordCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeCount;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		result = prime * result + wordCount+300*nodeCount;
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
		Trie other = (Trie) obj;
		if (nodeCount != other.nodeCount)
			return false;
		if (root == null) {
			if (other.root != null)
				return false;
		} else if (!root.equals(other.root))
			return false;
		if (wordCount != other.wordCount)
			return false;
		return true;
	}

	@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return nodeCount;
	}
	

}
