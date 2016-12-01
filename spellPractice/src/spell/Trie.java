package spell;

public class Trie implements ITrie {
	Node root;
	int nodeCount;
	int wordCount;
	
	public Trie(){
		root = new Node();
		nodeCount = 1;
		wordCount = 0;
	}
	
	@Override
	public void add(String word) {
		addR(word,root);
	}
	
	public void addR(String word, Node n){
		int i = word.charAt(0)-'a';
		System.out.println(word);
		if(word.length()==1){
			// if word already exists
		if(n.getAlpha()[i]!=null){
			n.getAlpha()[i].incFrequency();
			return;
		}
		else{
			//word does not exist
			Node nn = new Node();
			nn.setPath(n.getPath()+word.charAt(0));
			wordCount++;
			nodeCount++;
			nn.incFrequency();
			n.getAlpha()[i]= nn;
			return;
		}
		}
		// if node already exists
		if(n.getAlpha()[i]!=null){
			word = word.substring(1);
			addR(word,n.getAlpha()[i]);
		}
		// if new node
		else{
		Node nn = new Node();
		nn.setPath(n.getPath()+word.charAt(0));
		n.getAlpha()[i]=nn;
		nodeCount++;
		word = word.substring(1);
		addR(word,nn);
		}

	}

	@Override
	public INode find(String word) {
		return findR(word,root);
	}
	
	public INode findR(String word, Node n){
		int i = word.charAt(0)-'a';
		System.out.println("find"+word);
		if(word.length()==1){
			//found word 
			if(n.getAlpha()[i]!=null){
		//		System.out.println("1");
		//		System.out.println("This is path"+n.getAlpha()[i].getPath());
				return n;
			}
			else{
			//not found word
				System.out.println("2");
			return null;
			}
		}
		if(n.getAlpha()[i]!=null){
		 return	findR(word.substring(1),n.getAlpha()[i]);
		}
		System.out.println("3");
		return null;
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
	
	@Override
	public String toString() {
		StringBuilder dictionary = new StringBuilder();
		toStringR(dictionary,root);
		return dictionary.toString();
	}
	
	public String toStringR(StringBuilder dictionary, Node n){
		
		for(Node nn : n.getAlpha()){
			if(nn!=null){
				if(nn.getValue()>0){
					dictionary.append(nn.getPath()+"\n");
				}
			toStringR(dictionary,nn);	
			}
		}

		return null;
	}
	

	
	@Override
	public int hashCode(){
		return nodeCount;
		
	}
	
	@Override
	public boolean equals(Object o) {
		return false;
	}

}
