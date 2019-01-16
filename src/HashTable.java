import java.util.*;

public class HashTable<K extends Comparable<K>, V> implements MapADT<K, V>
{
	private ArrayList<List<Entry<K,V>>> listOfBuckets; // Buckets in this case refers to the List of entries, the buckets are to handle collision
	private final int DEFAULT_SIZE = 101; // 101 is somewhat arbitrary save the fact that it is prime
	private final float GROW_AT = 0.75f; // Percentage of number of elements versus number of buckets at which the array needs to dynamically grow
	private final float SHRINK_AT = 0.15f; // Percentage of number of elements versus number of buckets at which the array needs to dynamically shrink
	private int curNumEntries;
	private int hashTableSize; // I believe that is number of buckets
	private long modCounter; // Used with the iterator to handle an edge case where someone might try add or delete elements while simultaneously using an iterator

	
	public HashTable()
	{
		curNumEntries = 0;
		modCounter = 0;
		hashTableSize = DEFAULT_SIZE;
		listOfBuckets = new ArrayList<>(DEFAULT_SIZE);
		
		for (int i = 0; i < hashTableSize; i++)
		{
			listOfBuckets.add(new LinkedList<>());
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
		return listOfBuckets.get(getHashCode(key)).contains(new Entry<K,V>(key,null));
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
		
		if(listOfBuckets.get(getHashCode(key)).contains(new Entry<K,V>(key,null)))
		{
			Entry<K,V> tmp = new Entry<>(key,getValue(key));
			listOfBuckets.get(getHashCode(key)).remove(new Entry<>(key,getValue(key)));
			listOfBuckets.get(getHashCode(key)).add(new Entry<>(key,value));
			modCounter++;
			return tmp.value; 
		}
		
		listOfBuckets.get(getHashCode(key)).add(new Entry<>(key,value));
		curNumEntries++;
		modCounter++;
		
		return null;
	}
	
	/**
	 * copy Key,Value pairs into a temporary array list, double tablesize
	 * and find nearest prime number, reconstruct buckets to size of table size
	 * add all of the key,value pairs back into buckets
	 */
	private void growCheck()
	{
		if(((float)curNumEntries / (float)hashTableSize) >= GROW_AT)
		{
			List<Entry<K,V>> tmpList;
			tmpList = new ArrayList<>(curNumEntries);

			for (int i = 0; i < hashTableSize; i++)
			{
				for (Entry<K,V> n : listOfBuckets.get(i))
				{
					tmpList.add(new Entry<K,V>(n.key,n.value));
				}
			}

			hashTableSize = prime(2*hashTableSize);
			listOfBuckets = new ArrayList<>(hashTableSize);

			for(int j =0; j<hashTableSize;j++)
			{
				listOfBuckets.add(new LinkedList<>());
			}

			for (int k =0; k< curNumEntries;k++)
			{
				listOfBuckets.get(getHashCode(tmpList.get(k).key)).add(new Entry<>(tmpList.get(k).key,tmpList.get(k).value));
			}
		}
	}

	private void shrinkCheck()
	{
		if (((float)curNumEntries / (float)hashTableSize) <= SHRINK_AT)
		{
			List<Entry<K,V>> tmpList = new ArrayList<>(curNumEntries);

			for (int i = 0; i<hashTableSize;i++)
			{
				for (Entry<K,V> n : listOfBuckets.get(i))
				{
					tmpList.add(new Entry<>(n.key,n.value));
				}
			}

			hashTableSize = prime(hashTableSize / 2); // we want hashTableSize to be prime since it is used as part of
			// the hashing function, and primes have a lower chance of causing collisions

			listOfBuckets = new ArrayList<>(hashTableSize);

			for(int j = 0; j < hashTableSize; j++)
			{
				listOfBuckets.add(new LinkedList<>());
			}

			for (int k = 0; k < curNumEntries; k++)
			{
				listOfBuckets.get(getHashCode(tmpList.get(k).key)).add(new Entry<>(tmpList.get(k).key,tmpList.get(k).value));
			}
		}
	}

	@Override
	public boolean delete(K key) 
	{
		// possibly get rid of buckets[getHashCode(key)].indexOf(  instead of passing null passin getValue(key)
		if (null == key)
		{
			return false;
		}

		boolean tmpBool = listOfBuckets.get(getHashCode(key)).remove(new Entry<>(key,getValue(key)));

		if(tmpBool)
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
		Entry<K, V> tmp = listOfBuckets.get(getHashCode(key)).get(listOfBuckets.get(getHashCode(key)).indexOf(new Entry<K,V>(key,null)));

		if (tmp == null)
		{
			return null;
		}
		return tmp.value;
	}

	@Override
	public K getKey(V value)
	{
		if (null == value)
		{
			return null;
		}
		for (int i = 0; i < hashTableSize; i++)
		{
			for (Entry<K,V> n : listOfBuckets.get(i))
			if (n.value == value)
			{
				return n.key;
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
		return 0 == curNumEntries;
	}

	@Override
	public void clear() 
	{
		curNumEntries = 0;
		modCounter = 0;
		hashTableSize = DEFAULT_SIZE;
		listOfBuckets = new ArrayList<>(hashTableSize);

		for(int i = 0; i < hashTableSize; i++)
		{
			listOfBuckets.add(new LinkedList<>());
		}
	}


	@Override
	public Iterator<K> keys() 
	{
		return new KeyIteratorHelper();
	}

	@Override
	public Iterator<V> values() 
	{
		return new ValueIteratorHelper();
	}
	
	private abstract class IteratorHelper<E> implements Iterator<E>
	{
		protected ArrayList<Entry<K,V>> nodes;
		protected int idx;
		protected long modCheck;

		public IteratorHelper()
		{
			nodes = new ArrayList<>(curNumEntries);
			idx = 0;

			for(int i = 0; i < hashTableSize; i++)
			{
				nodes.addAll(listOfBuckets.get(i));
			}
		}
		
		public boolean hasNext()
		{
			if(modCheck != modCounter)
			{
				throw new ConcurrentModificationException();
			}
			return idx<curNumEntries;
		}
		
		public abstract E next();
		
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}

	private class KeyIteratorHelper extends IteratorHelper<K>
	{
		public KeyIteratorHelper()
		{
			super();
		}

		public K next()
		{
			return nodes.get(idx++).getKey();
		}

	}

	private class ValueIteratorHelper extends IteratorHelper<V>
	{
		public ValueIteratorHelper()
		{
			super();
		}

		public V next()
		{
			V tmpVal = nodes.get(idx).getValue();
			idx++;
			return tmpVal;
		}
	}

	private class Entry<K extends Comparable<K>, V> implements Comparable<Entry<K,V>>
	{
		private K key;
		private V value;

		public Entry(K k, V v)
		{
			key = k;
			value = v;
		}

		public K getKey()
		{
			return key;
		}

		public V getValue()
		{
			return value;
		}

		@Override
		public int compareTo(Entry<K, V> node)
		{
			return (key).compareTo(node.key);
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
