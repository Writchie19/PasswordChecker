package Main;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

public class BinarySearchTree<K extends Comparable<K>,V extends Comparable<V>> implements MapADT<K, V> {
	private Node<K,V> root;
	private int size;
	private int modCounter;

	public BinarySearchTree() {
		root = null;
		size = 0;
		modCounter = 0;
	}
	
//	public BinarySearchTree(MapADT<K,V> obj)
//	{
//
//	}
	
	class Node<K extends Comparable<K>, V> {
		public K key;
		public V value;
		public Node<K,V> leftChild;
		public Node<K,V> rightChild;
		
		public Node(K k, V v) {
			key = k;
			value = v;
			leftChild = rightChild = null;
		}
		
		public int numChildren() {
			if (leftChild == null & rightChild ==null)
			{
				return 0;
			}
			else if (leftChild == null & rightChild != null)
			{
				return 1;
			}
			else if (rightChild == null)
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
	public boolean contains(K key) {
		return findValue(key, root) != null;
	}

	@Override
	public V add(K key, V value) {
		V tmpValue = null;

		if(root == null) {
			root = new Node<>(key,value);
		}
		else {
			tmpValue = insert(key,value,root,null,false);
		}

		size++;
		modCounter++;
		return tmpValue;
	}

	private V insert(K key, V value,Node<K, V> n, Node<K,V> parent, boolean wasLeft) {
		if(n == null) { // Reached the bottom of the tree
			if(wasLeft) {
				parent.leftChild = new Node<>(key,value);
			}
			else {
				parent.rightChild = new Node<>(key,value);
			}
			return null;
		}
		else if(key.compareTo(n.key) < 0) {
			return insert(key,value,n.leftChild,n,true);
		}
		else if (key.compareTo(n.key) > 0){
			return insert(key,value,n.rightChild,n,false);
		}
		else { // If there are duplicate keys, replace the current key's value with the new one and return the old value
			V tmpValue = n.value;
			n.value = value;
			return tmpValue;
		}
	}

	@Override
	public boolean delete(K key) {
		if(key == null) {
			return false;
		}

		if (root == null) {
			return false;
		}
		
		// Special case where the node to delete is the root node
		if (key.compareTo(root.key) == 0) {
			if (root.numChildren() == 0) {
				root = null;
			}
			else if (root.numChildren() == 1){
				if (root.leftChild != null) {
					root = root.leftChild;
				}
				else {
					root = root.rightChild;
				}
			}
			else {
				Node<K,V> successor = findInorderSuc(root.rightChild, root, false); // This is the new root
				successor.leftChild = root.leftChild;
				successor.rightChild = root.rightChild;
				root = successor;
			}
		}
		else {
			Node<K,V> nodeToDelete;
			Node<K,V> parent = findParent(key, root,null);
			if(parent == null) { // The key is not in the tree
				return false;
			}

			if (parent.leftChild.key.compareTo(key) == 0) { // Get a reference to the desired node
				nodeToDelete = parent.leftChild;
			}
			else {
				nodeToDelete = parent.rightChild;
			}
			//Find key: then check for number of children, apply case as appropriate
			//case 1: NO children
			// set appropriate parent pointer(left or right child which is the key) to null
			// above is done by keeping track if went left or not
			if (nodeToDelete.numChildren() == 0) {
				if (parent.leftChild.key.compareTo(key) == 0) {
					parent.leftChild = null;
				} else {
					parent.rightChild = null;
				}
			}


			//case 2: One child
			//must check which child (left or right) but does not matter which child it is
			//replace parent node with child node
			else if (nodeToDelete.numChildren() == 1) {
				if (nodeToDelete.leftChild != null) {
					if (parent.leftChild.key.compareTo(key) == 0) {
						parent.leftChild = nodeToDelete.leftChild;
					}
					else {
						parent.rightChild = nodeToDelete.leftChild;
					}
				} else {
					if (parent.leftChild.key.compareTo(key) == 0) {
						parent.leftChild = nodeToDelete.rightChild;
					}
					else {
						parent.rightChild = nodeToDelete.rightChild;
					}
				}
			}

			// case 3: two children
			// find inorder successor(go to right one and then left as far as possible)
			// comment: cannot have two children or a left child, else that left child would be the in order successor
			else if (nodeToDelete.numChildren() == 2) {
				Node<K, V> successor = findInorderSuc(nodeToDelete.rightChild, nodeToDelete, false);
				if (parent.leftChild.key.compareTo(key) == 0) {
					parent.leftChild = successor;
				}
				else {
					parent.rightChild = successor;
				}
				successor.leftChild = nodeToDelete.leftChild;
				successor.rightChild = nodeToDelete.rightChild;
			}
		}
		size--;
		modCounter++;
		return true;
	}

	private Node<K,V> findInorderSuc(Node<K,V> n, Node<K,V> parent, Boolean wasLeft) {
		if (n.numChildren() == 2) {
			return findInorderSuc(n.leftChild, n, true);
		}
		else if (n.numChildren() == 1) {
			if (wasLeft) {
				if (n.leftChild != null) {
					parent.leftChild = n.leftChild;
				} else {
					parent.leftChild = n.rightChild;
				}
			}
			else {
				if (n.leftChild != null) {
					parent.rightChild = n.leftChild;
				} else {
					parent.rightChild = n.rightChild;
				}
			}
		}
		else {
			if (wasLeft) {
				if (n.leftChild != null) {
					parent.leftChild = null;
				} else {
					parent.leftChild = null;
				}
			}
			else {
				if (n.leftChild != null) {
					parent.rightChild = null;
				} else {
					parent.rightChild = null;
				}
			}
		}
		return n;
	}

	// If a node with the specified key is found, then return its parent, else return null
	private Node<K,V> findParent(K key,Node<K,V> n, Node<K,V> parent) {
		if (n == null) {
			return null;
		}

		if(key.compareTo(n.key) < 0) {
			return findParent(key, n.leftChild, n);
		}
		else if (key.compareTo(n.key) > 0 ) {
			return findParent(key, n.rightChild, n);
		}
		else {
			return parent;
		}
	}

	@Override
	public V getValue(K key) 
	{
		return findValue(key,root);
	}
	
	private V findValue(K  key, Node<K,V> n) {
		if (n == null) {
			return null;
		}
		if(key.compareTo(n.key) < 0) {
			return findValue(key, n.leftChild);
		}
		else if (key.compareTo(n.key) > 0 ) {
			return findValue(key, n.rightChild);
		}
		else {
			return n.value;
		}
	}

	@Override
	public K getKey(V value) {
		return findKey(value,root);
	}

	private K findKey(V value, Node<K, V> n) {
		if (n == null) {
			return null;
		}

		K key = findKey(value, n.leftChild);
		if (key != null) {
			return key;
		}

		if (value.compareTo(n.value) == 0) {
			return n.key;
		}

		key = findKey(value, n.rightChild);
		if (key != null) {
			return key;
		}
		return null;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size < 1;
	}

	@Override
	public void clear() {
		root = null;
		size = 0;
		modCounter++;
	}

	@Override
	public Iterator<K> keys() {
		return new KeyIteratorHelper();
	}

	@Override
	public Iterator<V> values() {
		return new ValueIteratorHelper();
	}

	private abstract class IteratorHelper<E> implements Iterator<E> {
		protected ArrayList<Node<K,V>> auxNodeArray;
		protected int idx;
		protected long modCheck;

		public IteratorHelper() {
			auxNodeArray = new ArrayList<>(size);
			inorderFillArray(root); // Fills the auxillary array with the nodes in the BST inorder
			idx = 0;
			modCheck = modCounter;

		}

		public boolean hasNext() {
			if(modCheck != modCounter)
			{
				throw new ConcurrentModificationException();
			}
			return idx < size;
		}

		public abstract E next();

		public void remove()
		{
			throw new UnsupportedOperationException();
		}

		private void inorderFillArray(Node<K,V> n) { // Inorder traversal of the BST
			if(n == null) {
				return;
			}

			inorderFillArray(n.leftChild);
			auxNodeArray.add(n);
			inorderFillArray(n.rightChild);
		}
	}

	private class KeyIteratorHelper extends IteratorHelper<K> {
		public KeyIteratorHelper()
		{
			super();
		}

		public K next()
		{
			return auxNodeArray.get(idx++).key;
		}
	}

	private class ValueIteratorHelper extends IteratorHelper<V> {
		public ValueIteratorHelper()
		{
			super();
		}

		public V next() { return auxNodeArray.get(idx++).value; }
	}
}
