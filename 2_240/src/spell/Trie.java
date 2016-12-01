package spell;

public class Trie implements ITrie {
	Node root;
	int wordCount;
	int nodeCount;

	public Trie() {
		root = new Node();
		wordCount = 0;
		nodeCount = 1;
	}
//	@Override
	public void add(String word) {
		word = word.toLowerCase();
		addR(word, root);
	}
	public void addR(String word, Node n) {
		char c = word.charAt(0);
		int i = c - 'a';
//Check if we are on the last character of the word
		if (word.length() == 1) {
			// if we have reached the end of the word and it has not yet been added
			//When the word is at size 1 then we have found the end of the word.
			//Create a new node, set its path to the previous node path + first character of word string,
			//populate the old node's alphabet with the new node, increment frequency of the new node, nodeCoun++, wordCount++
			if (n.getAlphabet()[i] == null) {
				Node nn = new Node();
				nn.setPath(n.getPath() + Character.toString(c));
				n.getAlphabet()[i] = nn;
				nn.incrementFrequency();
				nodeCount++;
				wordCount++;
				return;
			}
		//This means word already exists	
			if (n.getAlphabet()[i] != null) {
				n.getAlphabet()[i].incrementFrequency();
				return;
			}
		}
	// There is already an existing path and node at this point so we take off the first letter and continue	
		if (n.getAlphabet()[i] != null) {
			word = word.substring(1);
			addR(word, n.getAlphabet()[i]);
		}
	//We have not yet reached the end of the word so we simply add a node,, take off the first letter of the word and continue	
		if (n.getAlphabet()[i] == null) {
			Node nn = new Node();
			nodeCount++;
			nn.setPath(n.getPath() + Character.toString(c));
			word = word.substring(1);
			n.getAlphabet()[i] = nn;
			addR(word, nn);
		}
	}

//	@Override
	public INode find(String word) {
		word = word.toLowerCase();
		return findR(word, root);
	}

	
	public INode findR(String word, Node n) {
		char c = word.charAt(0);
		int i = c - 'a';
	// if the current word is length 1 then we have reached the last letter/node in the word	
		if (word.length() == 1) {
	// if the node at this location is not the last character then the word does not exist, otherwise
			if (n.getAlphabet()[i] != null && n.getAlphabet()[i].getValue() != 0) {
				return n.getAlphabet()[i];
			} else {
				return null;
			}
		}
	// 	
		if (n.getAlphabet()[i] != null) {
			String newword = new String();
			//newword = word.substring(1, word.length());
			newword = word.substring(1);
			return findR(newword, n.getAlphabet()[i]);
		} else {
			return null;
		}

	}

//	@Override
	public int getWordCount() {

		return wordCount;
	}

//	@Override
	public int getNodeCount() {

		return nodeCount;
	}

//	@Override
	public String toString() {
		StringBuilder dictionary = new StringBuilder();
		toStringR(root, dictionary);
		return dictionary.toString();
	}

	public String toStringR(Node n, StringBuilder dictionary) {

		for (int i = 0; i < 26; i++) {
			int j = i + 'a';
			if (n.getAlphabet()[i] != null) {
				if (n.getAlphabet()[i].getValue() > 0) {
					dictionary.append(n.getPath()+Character.toString((char) j) + "\n");
				}
				toStringR(n.getAlphabet()[i], dictionary);
			}
		}
		return null;
	}

	public boolean compareNodes(Node objectRoot) {
		return compareNodesR(root, objectRoot);
	}

	public boolean compareNodesR(Node n, Node objectNode) {
		for (int i = 0; i < 26; i++) {
			if (n.getAlphabet()[i] != null && objectNode.getAlphabet()[i] != null) {
				if (n.getAlphabet()[i].equals(objectNode.getAlphabet()[i])) {
					return compareNodesR(n.getAlphabet()[i],objectNode.getAlphabet()[i]);
				}
				else{
					return false;
				}
			}
		}
		return true;
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

		if (compareNodes(other.root) == false) {
			return false;
		}

		return true;
	}

}
