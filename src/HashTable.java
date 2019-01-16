import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HashTable<K extends Comparable<K>, V> implements MapADT<K, V>
{
	private List[] listOfBuckets; // Buckets in this case refers to the List of entries, the buckets are to handle collision
	private final int DEFAULT_SIZE = 101;
	private int curNumEntries;
	private int hashTableSize; // I believe that is number of buckets
	private long modCounter; // Used with the iterator to handle an edge case where someone might try add or delete elements while simultaneously using an iterator

	
	public HashTable()
	{
		curNumEntries = 0;
		modCounter = 0;
		hashTableSize = DEFAULT_SIZE;
		listOfBuckets = new LinkedList[DEFAULT_SIZE];
		
		for (int i = 0; i < hashTableSize; i++)
		{
			listOfBuckets[i] = new LinkedList<Entry<K,V>>();
		}
	}
	
//	public HashTable(MapADT<K,V> inputMap)
//	{
//		this();
//
//		for (K key, V val : inputMap)
//		{
//
//		}
//	}

	// INPUT: initNum is the initial number to begin looking for the next prime from
	// OUTPUT: the next prime number after initNum
	private int prime(int initNum)
	{
		boolean isPrime = false;

		do 
		{
			initNum++; // Become the next number to check

			// Immediately fail if the number is even
			if (initNum % 2 != 0)
			{
				// start at 3 (since 1 is useless and 2 has already been checked for) then loop until sqrt(initNum) [this
				// is because checking the numbers after sqrt(initNum) is redundant), each loop increment by 2 keeping the
				// i number (that is being used to modulo initNum with) from becoming an even number
				for (int i = 3; i <= (int)Math.sqrt(initNum); i += 2)
				{
					if (initNum % i == 0)
					{
						isPrime = false;
						break;
					}
					else
					{
						// Do not break, must be sure that initNum is prime for all instances of i in this loop
						isPrime = true;
					}
				}
			}
			else
			{
				isPrime = false;
			}
		}
		while(isPrime != true);

		return initNum;
	}
	
	@Override
	public boolean contains(K key)
	{
		return listOfBuckets[getHashCode(key)].contains(new Entry<K,V>(key,null));
	}

	private int getHashCode(K k)
	{
		return (k.hashCode() & 0x7FFFFFFF) % hashTableSize;
	}

	@Override
	public V add(K key, V value) 
	{
		if(null == key)
		{
			return null;
		}
		
		growCheck();
		
		if(listOfBuckets[getHashCode(key)].contains(new Entry<K,V>(key,null)))
		{
			Entry<K,V> tmp = new Entry<>(key,getValue(key));
			listOfBuckets[getHashCode(key)].remove(new Entry<>(key,getValue(key)));
			listOfBuckets[getHashCode(key)].add(new Entry<>(key,value));
			modCounter++;
			return tmp.value; 
		}
		
		listOfBuckets[getHashCode(key)].add(new Entry<>(key,value));
		curNumEntries++;
		modCounter++;
		
		return null;
	}
	
	/**
	 * copy Key,Value pairs into a temporary array list, double tablesize
	 * and find nearest prime number, reconstruct buckets to size of table size
	 * add all of the key,value pairs back into buckets
	 */
	@SuppressWarnings("unchecked")
	private void growCheck()
	{
		if(((double)curNumEntries/(double)hashTableSize)>=.75)
		{
			List<Entry<K,V>> tmpList;
			tmpList = new ArrayList<Entry<K,V>>(curNumEntries);

			for (int i = 0; i < hashTableSize; i++)
			{
				for (Entry<K,V> n : listOfBuckets[i])
				{
					tmpList.add(new Entry<K,V>(n.key,n.value));
				}
			}

			hashTableSize = prime(2*hashTableSize);
			listOfBuckets = new List[hashTableSize];

			for(int j =0; j<hashTableSize;j++)
			{
				listOfBuckets[j] = new LinkedList<Entry<K,V>>();
			}

			for (int k =0; k< curNumEntries;k++)
			{
				listOfBuckets[getHashCode(tmpList.get(k).key)].add(new Entry<>(tmpList.get(k).key,tmpList.get(k).value));
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void shrinkCheck()
	{
		if (((double)curNumEntries/(double)hashTableSize)<=.15)
		{
			List<Entry<K,V>> tmpList;
			tmpList = new ArrayList<Entry<K,V>>(curNumEntries);
			for (int i =0; i<hashTableSize;i++)
			{
				for (Entry<K,V> n : listOfBuckets[i])
				{
					tmpList.add(new Entry(n.key,n.value));
				}
			}
			hashTableSize = prime(hashTableSize/2);
			listOfBuckets = new List[hashTableSize];
			for(int j =0; j<hashTableSize;j++)
			{
				listOfBuckets[j] = new LinkedList<Entry<K,V>>();
			}
			for (int k =0; k<curNumEntries;k++)
			{
				listOfBuckets[getHashCode(tmpList.get(k).key)].add(new Entry<K,V>(tmpList.get(k).key,tmpList.get(k).value));
			}
		}
	}

	@Override
	public boolean delete(K key) 
	{
		// possibly get rid of buckets[getHashCode(key)].indexOf(  instead of passing null passin getValue(key)
		if (key==null)
		{
			return false;
		}
		boolean tmpBool = listOfBuckets[getHashCode(key)].remove(new Entry<K,V>(key,getValue(key)));
		if(tmpBool==true)
		{
			curNumEntries--;
			shrinkCheck();
		}
		return tmpBool;
	}

	@Override
	public V getValue(K key) 
	{
		if (key == null)
		{
			return null;
		}
		Entry<K, V> tmp = listOfBuckets[getHashCode(key)].get(listOfBuckets[getHashCode(key)].indexOf(new Entry<K,V>(key,null)));
		if (tmp == null)
		{
			return null;
		}
		return tmp.value;
	}

	@Override
	public K getKey(V value)
	{
		if (value == null)
		{
			return null;
		}
		for (int i =0; i<hashTableSize;i++)
		{
			for (Entry<K,V> n : listOfBuckets[i])
			if (n.value==value)
			{
				return (K) n.key;
			}
		}
		return null;
	}

	@Override
	public int size()
	{
		return curNumEntries;
	}

	@Override
	public boolean isEmpty()
	{
		return curNumEntries<1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void clear() 
	{
		curNumEntries = 0;
		modCounter = 0;
		hashTableSize = DEFAULT_SIZE;
		listOfBuckets = new List[hashTableSize];
		for(int i =0; i<hashTableSize;i++)
		{
			listOfBuckets[i] = new LinkedList<Entry<K,V>>();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Iterator<K> keys() 
	{
		return new KeyIteratorHelper();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Iterator<V> values() 
	{
		return new ValueIteratorHelper();
	}
	
	private abstract class IteratorHelper<E> implements Iterator<E>
	{
		protected Entry<K,V>[] nodes;
		protected int idx;
		protected long modCheck;
		
		@SuppressWarnings("unchecked")
		public IteratorHelper()
		{
			nodes = new Entry[curNumEntries];
			idx=0;
			int j =0;
			for(int i =0;i<hashTableSize;i++)
			{
				for(Entry<K,V> n : listOfBuckets[i])
				{
					nodes[j++] = n;
				}
			}
		}
		
		public boolean hasNext()
		{
//			if(modCheck != modCounter)
//			{
//				throw new ConcurrentModificationException();
//			}
			return idx<curNumEntries;
		}
		
		public abstract E next();
		
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	@SuppressWarnings("hiding")
	private class KeyIteratorHelper<K> extends IteratorHelper<K>
	{
		public KeyIteratorHelper()
		{
			super();
		}
		
		@SuppressWarnings("unchecked")
		public K next()
		{
			return (K) nodes[idx++].key;
		}

	}
	
	@SuppressWarnings("hiding")
	private class ValueIteratorHelper<V> extends IteratorHelper<V>
	{
		public ValueIteratorHelper()
		{
			super();
		}
		
		@SuppressWarnings("unchecked")
		public V next()
		{
			V tmpVal = (V) nodes[idx].value;
			idx++;
			return tmpVal;
		}
	}

	private class Entry<K extends Comparable<K>, V> implements Comparable<Entry<K,V>>
	{
		K key;
		V value;

		public Entry(K k, V v)
		{
			key = k;
			value = v;
		}

		@Override
		public int compareTo(Entry<K, V> node)
		{
			return ((Comparable<K>)key).compareTo((K)node.key);
		}

		@Override
		public boolean equals(Object o)
		{
			return compareTo((Entry<K,V>) o) == 0;
		}
		public String toString() {
			return "" + key;
		}
	}
}
