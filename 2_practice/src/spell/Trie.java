package spell;

public class Trie implements ITrie {

	private int wordCount;
	private int nodeCount;
	private Node root;
	
	public Trie(){
		wordCount = 0;
		nodeCount = 1;
		root = new Node();
	}
	
	@Override
	public void add(String word) {
		word.toLowerCase();
		addR(word, root);	
	}
	
	public void addR(String word, Node n){
		char c= word.charAt(0);
		int i = c-'a';
		//if last letter
		if(word.length()==1){

			// word does not already exist
			 if(n.getAlphabet()[i]==null){
				Node nn = new Node();
				nn.setPath(n.getPath()+Character.toString(c));
				n.getAlphabet()[i]=nn;
				nn.incFrequency();
				nodeCount++;
				wordCount++;
				return;
			}
				//if word already exists
				if(n.getAlphabet()[i]!=null){
					n.getAlphabet()[i].incFrequency();
					return;
				}
		}
		//node already exists
		if(n.getAlphabet()[i]!=null){
			word = word.substring(1);
			addR(word,n.getAlphabet()[i]);
		}
		if(n.getAlphabet()[i]==null){
			Node nn = new Node();
			nodeCount++;
			nn.setPath(n.getPath()+Character.toString(c));
			word = word.substring(1);
			n.getAlphabet()[i]=nn;
			addR(word,nn);
		}
	

	}


	@Override
	public INode find(String word) {
		return	findR(word,root);
	}
	
	public INode findR(String word, Node n){
		char c= word.charAt(0);
		int i = c-'a';
		String newWord = word.substring(1);
		if(word.length()==1){
			if(n.getAlphabet()[i]!=null&&n.getAlphabet()[i].getValue()!=0){
				//Word was found
				return n.getAlphabet()[i];
			}
			else{
				// word not found;
				return null;
			}
		}
		if(n.getAlphabet()[i]!=null){
			return findR(newWord,n.getAlphabet()[i]);
		}
		return null;
	}

	@Override
	public int getWordCount() {
		return wordCount;
	}

	@Override
	public int getNodeCount() {
		// TODO Auto-generated method stub
		return nodeCount;
	}
	
	public String toString(){
		StringBuilder dictionary = new StringBuilder();
		toStringR(root,dictionary);
		return dictionary.toString();
	}
	
	public String toStringR(Node n,StringBuilder dictionary){
	int count = 0;
		for(int i= 0; i<26;i++){
			int j = i+'a';
			if(n.getAlphabet()[i]!=null){
				if(n.getAlphabet()[i].getValue()>0){
					dictionary.append(n.getPath()+Character.toString((char) j)+ "\n");				}
				toStringR(n.getAlphabet()[i],dictionary);
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
		result = prime * result + wordCount;
		result = result + nodeCount % (wordCount) * 100;
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



}
