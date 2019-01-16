import java.util.*;
public class UnitTester {
	public static void main(String args[])
	{
//		test_size();
//		addKeysAndVals();
//		test_Add_grow();
//		test_Contains();
//		test_isEmpty();
		test_delete_shrink();
//		test_clear();
		
	}
	
	public static void test_Add_grow()
	{
		HashTable ht = new HashTable();
		String[] str = {"carl","bob","fred","george"};
		for (int i = 0; i<str.length;i++ )
		{
			ht.add(str[i],i);
		}
		ht.clear();
		Integer Int = 5;
		for (int j =0;j<1000;j++)
		{
			ht.add(Int+j, j);
		}
		System.out.println(ht.tableSize);	
	}
	public static void test_clear()
	{
		HashTable ht = new HashTable();
		String[] str = {"carl","bob","fred","george"};
		for (int i = 0; i<str.length;i++ )
		{
			ht.add(str[i],i);
		}
		ht.clear();
		System.out.println("Is it empty: "+ht.isEmpty());
		for (int i = 0; i<str.length;i++ )
		{
			ht.add(str[i],i);
		}
		for (int j =0; j<str.length;j++)
		{
			System.out.println("Key deleted: "+ht.getKey(j)+" was it deleted? "+ht.delete(str[j])+"testSize: "+ht.tableSize);
		}
		System.out.println("Is it empty: "+ht.isEmpty());
	}
	public static void test_delete_shrink()
	{
		HashTable ht = new HashTable();
		String[] str = {"bob","bob","bob","bob"};
		for (int i = 0; i<str.length;i++ )
		{
			ht.add(str[i],i);
			//System.out.println("Contains: "+ str[i] + ": "+ht.contains(str[i]));
		}
		System.out.println(ht.getKey(2));
		System.out.println(ht.delete("bob"));
		System.out.println(ht.delete("bob"));
		System.out.println("Contains: "+ht.contains("bob"));
		System.out.println("Size: "+ ht.size());
//		for (int j =0; j<ht.size();j++)
//		{
//			System.out.println("Key: " + ht.getKey(j) + "    Value: " + ht.getValue(ht.getKey(j)));
//		}
		//System.out.println("Tablesize: "+ht.tableSize);
		
	}
	
	public static void test_KeyIterator()
	{
		HashTable ht = new HashTable();
		String[] str = {"carl","bob","fred","george"};
		for (int i = 0; i<str.length;i++ )
		{
			ht.add(str[i],i);
			//System.out.println("Contains: "+ str[i] + ": "+ht.contains(str[i]));
		}
//		for (String key : ht.keys())
//		{
//			
//		}
	}
	
	public static void test_Contains()
	{
		HashTable ht = new HashTable();
		String[] str = {"carl","bob","fred","george"};
		for (int i = 0; i<str.length;i++ )
		{
			ht.add(str[i],i);
			System.out.println("Contains: "+ str[i] + ": "+ht.contains(str[i]));
		}
	}
	
	public static void test_size()
	{
		HashTable ht = new HashTable();
		String[] str = {"carl","bob","fred","george"};
		for (int i = 0; i<str.length;i++ )
		{
			ht.add(str[i],i);
			//System.out.println("Contains: "+ str[i] + ": "+ht.contains(str[i]));
		}
		System.out.println(ht.size());
	}
	
	public static void addKeysAndVals()
	{
		HashTable ht = new HashTable();
		String[] str = {"carl","bob","fred","george"};
		for (int i = 0; i<str.length;i++ )
		{
			//System.out.println(ht.getHashCode(str[i]));
			ht.add(str[i],i);
		}
		for (int j =0; j<ht.size();j++)
		{
			System.out.println("Key: " + ht.getKey(j) + "    Value: " + ht.getValue(ht.getKey(j)));
		}
	}
	
	public static void test_isEmpty()
	{
		HashTable ht = new HashTable();
		System.out.println("Empty: "+ht.isEmpty());
		String[] str = {"carl","bob","fred","george"};
		for (int i = 0; i<str.length;i++ )
		{
			//System.out.println(ht.getHashCode(str[i]));
			ht.add(str[i],i);
		}
		System.out.println("Empty: "+ht.isEmpty());
	}
}
