package Main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// List of TODO's
// ----------------------------------------------------------
// TODO: add an explanation of what this should be doing
// TODO: Comments you heathen
// TODO: include binary search tree when it is working
// ---------------------------------------------------------



public class Assign4
{
	public static void main(String[] args) throws IOException
	{
		String inFile = args[0];
		String outFile = args[1];
		File file = new File(inFile);
		File file2 = new File(outFile);
		FileReader fileReader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		FileWriter fileWriter = new FileWriter(file2);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		String line;
		Integer val=1;
		List<String[]> lines1 = new ArrayList<String[]>();
		List<String[]> lines2 = new ArrayList<String[]>();
		List<String[]> lines3 = new ArrayList<String[]>();
		List<String[]> lines4 = new ArrayList<String[]>();
		List<String[]> lines5 = new ArrayList<String[]>();
		List<List<String[]>> allLines = new ArrayList<List<String[]>>(){{add(lines1);add(lines2);add(lines3);add(lines4);add(lines5);}};




		while((line = bufferedReader.readLine()) != null)
		{
			lines1.add(line.split("(?<=\\G.{1})"));
			lines2.add(line.split("(?<=\\G.{2})"));
			lines3.add(line.split("(?<=\\G.{3})"));
			lines4.add(line.split("(?<=\\G.{4})"));
			lines5.add(line.split("(?<=\\G.{5})"));

		}

		for(List<String[]> sd : allLines)
		{
			List<Integer> tmpVals = new ArrayList<Integer>();
			HashTable<String,Integer> hash = new HashTable<>();
			for(String[] s : sd)
			{
				for(String x : s)
				{
					if (hash.contains(x))
					{
						val = hash.getValue(x)+1;
					}
					else
					{
						val=1;
					}
					hash.add(x,val);
				}
			}
			for(String[] s : sd)
			{
				for(String x : s)
				{
					tmpVals.add(hash.getValue(x));
				}
			}
			Collections.sort(tmpVals);
			Collections.reverse(tmpVals);
			for(int j =0; j<20;j++)
			{
				writer.write((String)hash.getKey(tmpVals.get(j)));
				writer.write("\n");
				System.out.print(hash.getKey(tmpVals.get(j)));
				//hash.delete(hash.getKey(tmpVals.get(j)));
			}
			System.out.println();
		}
		writer.close();
		fileReader.close();

	}
}
