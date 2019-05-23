package Test;
import Main.*;

import java.util.*;

// TODO: TEST DYNAMIC RESIZING

public class UnitTester {
	private static MapADT<String, Integer> map = new BinarySearchTree<>();

	public static void main(String args[]) {
		test_Add_Size_Clear(); // does not check if the elements added are correct or not
		test_Add_Contains();
		test_Add_GetKey_GetValue();
		test_Delete();
		test_Iterators();
	}

	public static void test_Iterators() {
		String[] str = {"carl","bob","fred","george"};
		Integer[] ints = {1, 3, 5, 7};

		try {
			for (int i = 0; i < str.length; i++) {
				map.add(str[i], ints[i]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Iterator<String> keysIter = map.keys();
			Iterator<Integer> valuesIter = map.values();
			String previousKey = null;

			if (keysIter == null || valuesIter == null) {
				System.out.println("FAIL: test_Iterators: KEYS / VALUES / NULL ITERATORS");
				return;
			}

			while (keysIter.hasNext() && valuesIter.hasNext()) {
				String key = keysIter.next();
				Integer value = valuesIter.next();
				if (previousKey == null) { // Check for null because of the compareTo method
					previousKey = key;
				}
				else {
					if (previousKey.compareTo(key) == 0) { // Checks for duplicat keys
						System.out.println("FAIL: test_Iterators: KEYS / DUPLICATE");
						return;
					}
					previousKey = key;
				}

				Boolean foundKey = false;
				for (int i = 0; i < str.length; i++) {
					if (key.compareTo(str[i]) == 0) {
						foundKey = true;

						if (value.compareTo(ints[i]) != 0) { // Values should come out the same order as their associated keys
							System.out.println("FAIL: test_Iterators: VALUES OUT OF ORDER");
							return;
						}
					}
				}

				if (!foundKey) {
					System.out.println("FAIL: test_Iterators: KEY NOT FOUND");
					return;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		map.clear();
		System.out.println("SUCCESS ITERATORS");
	}

	public static void test_Delete() {
		String[] str = {"carl","bob","fred","george"};
		Integer[] ints = {1, 3, 5, 7};
		int idxElementToDelete = 2;

		try {
			for (int i = 0; i < str.length; i++) {
				map.add(str[i], ints[i]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			map.delete(str[idxElementToDelete]);
			if (map.contains(str[idxElementToDelete])) {
				System.out.println("FAIL: test_Delete: DELETE / CONTAINS");
				return;
			}

			if (map.size() != str.length - 1) {
				System.out.println("FAIL: test_Delete: DELETE / SIZE");
				return;
			}

			if (map.getValue(str[idxElementToDelete]) != null) {
				System.out.println("FAIL: test_Delete: DELETE / GETVALUE");
				return;
			}

			if (map.getKey(ints[idxElementToDelete]) != null) {
				System.out.println("FAIL: test_Delete: DELETE / GETKEY");
				return;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < str.length; i++) {
				if (i != idxElementToDelete) {
					map.delete(str[i]);
				}
			}

			for (int i = 0; i < str.length; i++) {
				if (map.contains(str[i])) {
					System.out.println("FAIL: test_Delete: DELETE / CONTAINS");
					return;
				}

				if (map.getValue(str[i]) != null) {
					System.out.println("FAIL: test_Delete: DELETE / GETVALUE");
					return;
				}

				if (map.getKey(ints[i]) != null) {
					System.out.println("FAIL: test_Delete: DELETE / GETKEY");
					return;
				}
			}

			if (map.size() != 0) {
				System.out.println("FAIL: test_Delete: DELETE / SIZE");
				return;
			}

			if (!map.isEmpty()) {
				System.out.println("FAIL: test_Delete: DELETE / ISEMPTY");
				return;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		map.clear();
		System.out.println("SUCCESS DELETE");
	}

	public static void test_Add_GetKey_GetValue() {
		String[] str = {"carl","bob","fred","george"};
		Integer[] ints = {1, 3, 5, 7};
		try {
			for (int i = 0; i < str.length; i++) {
				map.add(str[i], ints[i]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < str.length; i++) {
				if (map.getValue(str[i]).compareTo(ints[i]) != 0) {
					System.out.println("FAIL: test_Add_GetKey_GetValue: ADD / GETVALUE");
					return;
				}

				if (map.getKey(ints[i]).compareTo(str[i]) != 0) {
					System.out.println("FAIL: test_Add_GetKey_GetValue: ADD / GETKEY");
					return;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		map.clear();
		System.out.println("SUCCESS ADD_GETKEY_GETVALUE");
	}

	public static void test_Add_Contains() {
		String[] str = {"carl","bob","fred","george"};
		Integer[] ints = {1, 3, 5, 7};
		try {
			for (int i = 0; i < str.length; i++) {
				map.add(str[i], ints[i]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			for (int i = 0; i < str.length; i++) {
				if (!map.contains(str[i])) {
					System.out.println("FAIL: test_Add_Contains: ADD / CONTAINS");
					return;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		map.clear();
		System.out.println("SUCCESS: ADD_CONTAINS");
	}

	public static void test_Add_Size_Clear() {
		String[] str = {"carl","bob","fred","george"};
		Integer[] ints = {1, 3, 5, 7};
		try {
			for (int i = 0; i < str.length; i++) {
				map.add(str[i], ints[i]);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (map.size() != 4) {
				System.out.println("FAIL: test_Add_Size_Clear: ADD / SIZE");
				return;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			map.clear();
			if (map.size() != 0) {
				System.out.println("FAIL: test_Add_Size_Clear: CLEAR / SIZE");
				return;
			}

			if (!map.isEmpty()) {
				System.out.println("FAIL: test_Add_Size_Clear: CLEAR / ISEMPTY");
				return;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		map.clear();
		System.out.println("SUCCESS ADD_SIZE_CLEAR");
	}
}
