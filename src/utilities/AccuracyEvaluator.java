package utilities;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class is implemented for finding the accuracy by comparing the 
 * predicted labels with original labels. It accepts two filenames as 
 * arguments. First argument should be the file that contains original 
 * labels. The second argument should be the file that contains predicted 
 * labels.
 * 
 * @author Vaibhav
 * 
 */

public class AccuracyEvaluator 
{
	public static void main(String args[]) throws IOException
	{
		int tot=0,ct=0;
		
		//give error if the program is run with too few arguments
		if(args.length!=2)
		{
			System.out.println("Usage : test_file_name prediction_file_name");
			return;
		}
		File test=new File(args[0]);
		File pred=new File(args[1]);
		
		//give error if predictions file is not found
		if(!pred.exists())
		{
			System.out.println("Predictions file not found");
			return;
		}
		
		//give error if test file against which labels are to be compared is not 
		//found
		if(!test.exists())
		{
			System.out.println("Testing file not found");
			return;
		}
		Scanner _scan = new Scanner(new BufferedInputStream(new FileInputStream(args[0])));
		Scanner _scan1 = new Scanner(new BufferedInputStream(new FileInputStream(args[1])));
		
		//read the files line by line and compare the labels
		while(_scan.hasNextLine())
		{
			String line = _scan.nextLine();
			String line1 = _scan1.next();
			String[] split_line = line.split(",");
			String label_string = split_line[1];
			
			//if label matches, increment the counter of correct labels
			if(label_string.equalsIgnoreCase(line1))
				ct++;
			tot++;
		}
		
		System.out.println("Total : "+tot);
		System.out.println("Correct Predictions : "+ct);
		double accuracy=(double)ct/(double)tot;
		accuracy*=100;
		System.out.println("Accuracy : "+accuracy+"%");
		_scan.close();
		_scan1.close();
	}
}
