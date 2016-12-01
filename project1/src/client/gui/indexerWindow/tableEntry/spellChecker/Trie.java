package client.gui.indexerWindow.tableEntry.spellChecker;

public class Trie implements ITrie {
	Node root;
	int wordCount;
	int nodeCount;
	public Trie(){
		root= new Node();
		nodeCount=1;
		wordCount=0;
	}

	@Override
	public void add(String word) {
		addR(word,root);
	}

	public void addR(String word, Node n){
		int i = word.charAt(0)-'a';
		if(word.length()==1){
			//word not exist
			if(n.getAlphabet()[i]==null){
				Node nn = new Node();
				nn.setPath(n.getPath()+word.charAt(0));
				nodeCount++;
				wordCount++;
				nn.incFrequency();
				n.getAlphabet()[i]= nn;
				return;
			}
			if(n.getAlphabet()[i]!=null){
				n.getAlphabet()[i].incFrequency();
				return;
			}
		}
		if(n.getAlphabet()[i]!=null){
			String newWord = new String(word.substring(1));
			addR(newWord,n.getAlphabet()[i]);
		}
		else if(n.getAlphabet()[i]==null){
			String newWord = new String(word.substring(1));
			Node nn = new Node();
			nn.setPath(n.getPath()+word.charAt(0));
			nodeCount++;
			n.getAlphabet()[i]= nn;
			addR(newWord,nn);
		}


	}

	@Override
	public INode find(String word) {
		// TODO Auto-generated method stub
		return findR(word, root);
	}

	public INode findR(String word, Node n){
		//	System.out.println("findR word"+word);
		System.out.println("this is the word: "+word);
		int i = 0;
		if(!word.equals("")){
			i = word.charAt(0)-'a';

			if(word.length()==1){
				if (n.getAlphabet()[i] != null && n.getAlphabet()[i].getValue() != 0) {
					return n.getAlphabet()[i];
				} else {
					return null;
				}
			}
			if(n.getAlphabet()[i]!=null){
				String newWord = new String(word.substring(1));
				return findR(newWord,n.getAlphabet()[i]);
			}
			else if(n.getAlphabet()[i]==null){
				return null;
			}
		}	
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + nodeCount;
		result = prime * result + ((root == null) ? 0 : root.hashCode());
		result = prime * result + wordCount+wordCount%8*1000;
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

	public String toString(){
		StringBuilder toString= new StringBuilder();
		toStringR(root, toString);
		return toString.toString();
	}

	public void toStringR(Node n, StringBuilder toString){
		if(n.getValue()>0){
			toString.append(n.getPath()+"\n");
		}
		for(int i=0;i<26;i++){
			if(n.getAlphabet()[i]!=null){
				toStringR(n.getAlphabet()[i],toString);
			}
		}

	}



	@Override
	public int getWordCount() {
		// TODO Auto-generated method stub
		return wordCount;
	}

	@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return nodeCount;
	}

}
