import java.util.Iterator;

public class BinarySearchTree<K extends Comparable<K>,V> implements MapADT<K, V>
{
	Node<K,V> root;
	private boolean answerFound;
	private int size;
	public BinarySearchTree()
	{
		root = null;
		size=0;
	}
	
	public BinarySearchTree(MapADT<K,V> obj)
	{
		
	}
	
	@SuppressWarnings("hiding")
	class Node<K extends Comparable<K>, V>
	{
		public K key;
		public V value;
		public Node leftChild;
		public Node rightChild;
		
		public Node(K k, V v)
		{
			key = k;
			value = v;
			leftChild = rightChild = null;
		}
		
		public int numChildren()
		{
			if (leftChild == null & rightChild ==null)
			{
				return 0;
			}
			else if (leftChild == null & rightChild != null)
			{
				return 1;
			}
			else if (rightChild == null & leftChild != null)
			{
				return 1;
			}
			else
			{
				return 2;
			}
			
		}
	}

	@Override
	public boolean contains(K key)
	{
		
		return hasKey(key,root);
	}
	
	

	private boolean hasKey(K key,Node<K,V> n) 
	{
		if (n==null)
		{
			return false;
		}
		if((key).compareTo(n.key) < 0) //left out cast to Comparable<K>
		{
			return hasKey(key, n.leftChild);
		}
		else if ((key).compareTo(n.key) > 0 )
		{
			return hasKey(key, n.rightChild);
		}
		else 
		{
			return true;
		}
	}

	@Override
	public V add(K key, V value) 
	{
		if(contains(key))
		{
			V tmpVal = getValue(key);
			replaceVal(key,value,root);
			return tmpVal;
		}
		if(root == null)
		{
			root = new Node<K,V>(key,value);
		}
		else
		{
			insert(key,value,root,null,false);
		}
		size++;
		return null;
	}

	@SuppressWarnings("unchecked")
	private void replaceVal(K key, V value,Node<K, V> n)
	{
		if (n==null)
		{
			return;
		}
		if((key).compareTo(n.key) < 0) //left out cast to Comparable<K>
		{
			replaceVal(key,value, n.leftChild);
		}
		else if ((key).compareTo(n.key) > 0 )
		{
			replaceVal(key,value, n.rightChild);
		}
		else
		{
			n.value = value;
		}
	}

	private void insert(K key, V value,Node<K, V> n, Node<K,V> parent, boolean wasLeft)
	{
		if(n==null)
		{
			if(wasLeft)
			{
				parent.leftChild = new Node<K,V>(key,value);
			}
			else
			{
				parent.rightChild = new Node<K,V>(key,value);
			}
		}
		else if((key).compareTo((K)n.key)<0)
		{
			insert(key,value,n.leftChild,n,true);
		}
		else
		{
			insert(key,value,n.rightChild,n,false);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean delete(K key)
	{
		int tmpNumChildren;
		Node<K,V> tmpParent;
		Node<K,V> node;
		if(key==null)
		{
			return false;
		}
		if(contains(key)==false)
		{
			return false;
		}
		
		//EACH case must check to see if the key is the root node; if true then must reset root node
		if (key == root.key)
		{
			if (root.numChildren()==0)
			{
				root=null;
			}
			else
			{
				//do some other stuff
			}
		}
		//Find key: then check for number of children, apply case as appropriate
		node = findNode(key,root,false);
		tmpNumChildren = node.numChildren();
		//case 1: NO children
		// set appropriate parent pointer(left or right child which is the key) to null
		// above is done by keeping track if went left or not
		if (tmpNumChildren == 0)
		{
			replaceNode(node,null);
		}
		
		
		//case 2: One child
		//must check which child (left or right) but does not matter which child it is
		//replace parent node with child node
		else if (tmpNumChildren == 1)
		{
			if (node.leftChild!=null)
			{
				node=node.leftChild;
				//replace parent with leftchild
			}
			else
			{
				node=node.rightChild;
				//replace parent with right child
			}
		}
		
		//case 3: two children
		//Comments: cannot replace node to be deleted with one of its children b/c there are two and cannot quarantee
		//ordering property of tree will remain intact, also structural property may be violated b/c node may be 
		//orphaned: so turn into case with deletion of one child
		//Steps:
		//find inorder successor(go to right one and then left as far as possible)
		// comment: cannot have two children b/c it cannot have a left child or else that left child would be the in order successor
		//Overwrite node to be deleted with a copy of the inorder successor
		//Remove previous inorder Successor node OR replace with its single child if it has one
		else if (tmpNumChildren == 2)
		{
			Node<K,V> tmpNode = findInorderSuc(key);
			
			if(tmpNode.numChildren()==0)
			{
				findInorderSuc(key).key=null;
				findInorderSuc(key).value=null;
			}
			else
			{
				if (tmpNode.leftChild!=null)
				{
					findInorderSuc(key).key=(K) tmpNode.leftChild.key;
					findInorderSuc(key).value=(V) tmpNode.leftChild.value;
					
					//replace parent with leftchild
				}
				else
				{
					findInorderSuc(key).key=(K) tmpNode.rightChild.key;
					findInorderSuc(key).value=(V) tmpNode.rightChild.value;
					//replace parent with right child
				}
			}
			node.key = tmpNode.key;
			node.value = tmpNode.value;
		}
		size--;
		return true;
	}

	private Node<K,V> findInorderSuc(K key) 
	{
		//go right from key, then until hit null(as far as can go)
		return root;
			
	}

	private void replaceNode(Node<K,V> node, Node<K,V> newNode)
	{
		//make sure parent nodes point to the right place
		
	}

	private Node<K,V> findNode(K key,Node<K,V> n,boolean wasLeft)
	{
		if (n==null)
		{
			return null;
		}
		if((key).compareTo(n.key) < 0) //left out cast to Comparable<K>
		{
			return (Node<K,V>)findNode(key, n.leftChild,true);
		}
		else if ((key).compareTo(n.key) > 0 )
		{
			return (Node<K,V>)findNode(key, n.rightChild,false);
		}
		else
		{
			return n;
		}
	}

	@Override
	public V getValue(K key) 
	{
		return findValue(key,root);
	}
	
	@SuppressWarnings("unchecked")
	private V findValue(K  key, Node<K,V> n)
	{
		if (n==null)
		{
			return null;
		}
		if((key).compareTo(n.key) < 0) //left out cast to Comparable<K>
		{
			return (V) findValue(key, n.leftChild);
		}
		else if ((key).compareTo(n.key) > 0 )
		{
			return (V) findValue(key, n.rightChild);
		}
		else
		{
			return (V) n.value;
		}
	}

	@Override
	public K getKey(V value)
	{
		answerFound = false;
		return findKey(value,root,null);
	}

	@SuppressWarnings("unchecked")
	private K findKey(V value, Node<K, V> n,Node<K,V> parent) 
	{
		if (n==null)
		{
			return null;
		}
		if(((Comparable<V>) value).compareTo(n.value) == 0) //left out cast to Comparable<K>
		{
			answerFound = true;
			return (K) n.key;
		}
		else if (answerFound == false)
		{
			findKey(value, n.leftChild,n);
		}
		if(answerFound == false)
		{
			findKey(value,n.rightChild,n);
		}
		return null;
	}

	@Override
	public int size() 
	{
		return size;
	}

	@Override
	public boolean isEmpty()
	{
		return size<1;
	}

	@Override
	public void clear()
	{
		root=null;
		size=0;
	}

	@Override
	public Iterator<K> keys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	private void inorderFillArray(Node<K,V> n)
	{
		if(n==null)
		{
			return;
		}
		inorderFillArray(n.leftChild);
		array[iterIndex++] = n;
		inorderFillArray(n.rightChild);
	}
}
