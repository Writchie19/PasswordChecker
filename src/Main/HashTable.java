package Main;

import java.util.*;

public class HashTable<K extends Comparable<K>, V> implements MapADT<K, V>
{
    // CONSTANTS
	private final int DEFAULT_SIZE = 101; // 101 is somewhat arbitrary save the fact that it is findNextPrime

    // VARIABLES
    private LinkedList<Entry<K,V>>[] listOfBuckets; // Buckets in this case refers to the List of entries, the buckets are to handle collision
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
			listOfBuckets[i] = new LinkedList<>();
		}
	}
	
//	public Main.HashTable(Main.MapADT<K,V> inputMap)
//	{
//		this();
//
//		for (K key, V val : inputMap)
//		{
//
//		}
//	}

	// INPUT: initNum is the initial number to begin looking for the next findNextPrime from
	// OUTPUT: the next findNextPrime number after initNum
	private int findNextPrime(int initNum)
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
						// Do not break, must be sure that initNum is findNextPrime for all instances of i in this loop
						isPrime = true;
					}
				}
			}
		}
		while(!isPrime);

		return initNum;
	}
	
	@Override
	public boolean contains(K key)
	{
		return listOfBuckets[getHashCode(key)].contains(new Entry<K,V>(key,null));
	}

	private int getHashCode(K k)
	{
		return (k.hashCode() & 0x7FFFFFFF) % hashTableSize; // modulo with the hashtablesize to turn the hashcode into a valid index
		// by "valid" i mean can access an array of size: hashTableSize
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

	private void growCheck()
	{
        // Percentage of number of elements versus number of buckets at which the array needs to dynamically grow
        float growAmount = 0.75f;
        if(((float)curNumEntries / (float)hashTableSize) >= growAmount)
		{
			List<Entry<K,V>> tmpList;
			tmpList = new ArrayList<>(curNumEntries);

			for (LinkedList<Entry<K,V>> list : listOfBuckets)
			{
				for (Entry<K,V> n : list)
				{
					tmpList.add(new Entry<K,V>(n.key,n.value));
				}
			}

			hashTableSize = findNextPrime(2*hashTableSize);
			listOfBuckets = new LinkedList[hashTableSize];

			for(int j =0; j<hashTableSize;j++)
			{
				listOfBuckets[j] = new LinkedList<>();
			}

			for (int k =0; k< curNumEntries;k++)
			{
				listOfBuckets[getHashCode(tmpList.get(k).key)].add(new Entry<>(tmpList.get(k).key,tmpList.get(k).value));
			}
		}
	}

	private void shrinkCheck()
	{
        // Percentage of number of elements versus number of buckets at which the array needs to dynamically shrink
        float shrinkAmount = 0.15f;
        if (((float)curNumEntries / (float)hashTableSize) <= shrinkAmount)
		{
			List<Entry<K,V>> tmpList = new ArrayList<>(curNumEntries);

			for (int i = 0; i<hashTableSize;i++)
			{
				for (Entry<K,V> n : listOfBuckets[i])
				{
					tmpList.add(new Entry<>(n.key,n.value));
				}
			}

			hashTableSize = findNextPrime(hashTableSize / 2); // we want hashTableSize to be findNextPrime since it is used as part of
			// the hashing function, and primes have a lower chance of causing collisions

			listOfBuckets = new LinkedList[hashTableSize];

			for(int j = 0; j < hashTableSize; j++)
			{
				listOfBuckets[j] = new LinkedList<>();
			}

			for (int k = 0; k < curNumEntries; k++)
			{
				listOfBuckets[getHashCode(tmpList.get(k).key)].add(new Entry<>(tmpList.get(k).key,tmpList.get(k).value));
			}
		}
	}

	@Override
	public boolean delete(K key) 
	{
		if (null == key)
		{
			return false;
		}

		boolean tmpBool = listOfBuckets[getHashCode(key)].remove(new Entry<>(key,getValue(key)));

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
		V value = null;
		if (key != null) {
			for (Entry<K, V> entry : listOfBuckets[getHashCode(key)]) {
				if (entry.key.compareTo(key) == 0) {
					value = entry.value;
					break;
				}
			}
		}
		return value;
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
			for (Entry<K,V> n : listOfBuckets[i])
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
		listOfBuckets = new LinkedList[hashTableSize];

		for(int i = 0; i < hashTableSize; i++)
		{
			listOfBuckets[i] = new LinkedList<>();
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
			modCheck = modCounter;

			for(int i = 0; i < hashTableSize; i++)
			{
				nodes.addAll(listOfBuckets[i]);
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

		@Override
		public String toString() {
			return "" + key;
		}
	}
}
