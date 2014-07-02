package utilities;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import datastructures.ClassificationLabel;
import datastructures.FeatureVector;
import datastructures.Instance;

/**
 * This class is used for reading the file and creating array list of instances.
 * 
 * @author Vaibhav.
 *
 */
public class DataRead 
{
	String file;
	
	/**
	 * The constructor for initializing file path to be parsed.
	 * @param file path of file to be parsed
	 * 
	 */
	public DataRead(String file)
	{
		this.file=file;
	}
	
	/**
	 * This method parses the file and makes instance object for each line of
	 * input.
	 * 
	 * @return List of instances.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public ArrayList<Instance> file_read()throws IOException, FileNotFoundException
	{
		ArrayList<Instance> instances; 
		Scanner _scan;
		if (this.file==null)
		{
			System.out.println("Check input parameters");
			return null;
		}
		File fl=new File(this.file);
		if(!fl.exists())
		{
			System.out.println("Enter a File");
			return null;
		}
		instances=new ArrayList<Instance>();
		_scan = new Scanner(new BufferedInputStream(new FileInputStream(this.file)));
		while (_scan.hasNextLine()) 
		{
			String line = _scan.nextLine();
			if (line.trim().length() == 0)
				   continue;
			
			FeatureVector feature_vector = new FeatureVector();
		
			// Divide the line into features and label.
			String[] split_line = line.split(",");
			
			String label_string = split_line[1];
			ClassificationLabel label = null;
			int int_label = Integer.parseInt(label_string);
			label = new ClassificationLabel(int_label);
			for (int ii = 2; ii < split_line.length; ii++) 
			{
				String item = split_line[ii];
				int index=ii-1;
				double value = Double.parseDouble(item);
				feature_vector.add(index, value);
			}
			Instance instance = new Instance(feature_vector, label);
			instances.add(instance);
		}
		_scan.close();
		return instances;
	}	
}