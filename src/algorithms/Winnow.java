package algorithms;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import utilities.DataCleaner;
import utilities.DataRead;
import datastructures.Instance;

/**
 * Winnow Predictor Algorithm implementation.
 * @author Vaibhav
 *
 */
public class Winnow 
{
	static List<Instance> train_inst;
	static List<Instance> test_inst;
	static double w[];
	static int n=0;
	static double beta;
	static double mu=1.0e6;
	
	/**
	 * Main method
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException
	{
		//printing appropriate error messages
		if(args.length!=3)
		{
			System.out.println("Usage : train_file_name test_file_name prediction_file_name");
			return;
		}
		File train=new File(args[0]);
		File test=new File(args[1]);
		if(!train.exists())
		{
			System.out.println("Training file not found");
			return;
		}
		if(!test.exists())
		{
			System.out.println("Testing file not found");
			return;
		}
		DataCleaner dc=new DataCleaner();
		DataRead dr=new DataRead(args[0]);
		
		//reading training instances
		train_inst=dr.file_read();
		
		//cleaning training instances
		train_inst=dc.clean(train_inst);
		
		DataRead dr1=new DataRead(args[1]);
		
		//reading testing instances
		test_inst=dr1.file_read();
		
		//cleaning testing instances
		test_inst=dc.clean(test_inst);
		
		//training the classifier
		train(train_inst);
		
		//predicting the labels.
		predict(test_inst,args[2]);
	}
	
	/**
	 * This method uses instances to train itself and learns weights.
	 * @param instances training instances.
	 */
	public static void train(List<Instance> instances)
	{
		double thickness=0.0;
		beta=0.0;
		double online_learning_rate=2.0;
		double wixi=0.0;
		int online_training_iterations=50;
		int predicted_label;
		n=calc_n(train_inst);
		w=new double[n];
		int len=instances.size();
		beta=len/2;
		for(int i=0;i<n;i++)
			w[i]=1;
		for(int i=0;i<online_training_iterations;i++)
		{
			for(int j=0;j<len;j++)
			{
				Set<Entry<Integer, Double>> set=instances.get(j).getFeatureVector().getEntrySet();
				Iterator<Entry<Integer, Double>> z = set.iterator(); 
				while(z.hasNext()) 
				{ 
					Map.Entry<Integer,Double> me = (Map.Entry<Integer,Double>)z.next();
					int ind=(int)me.getKey();	
					double value=(double)me.getValue();
					wixi=wixi+w[ind-1]*value;
				}
				if(wixi>=beta+thickness)
					predicted_label=1;
				else if(wixi<=beta-thickness)
					predicted_label=-1;
				else
					predicted_label=0;
				int origy=Integer.valueOf(instances.get(j).getLabel().toString());
				origy=(origy==0)?-1:origy;
				if(predicted_label!=origy)
				{
					for(int k=0;k<n;k++)
					{
						double x=0.0;
						if(instances.get(j).getFeatureVector().containsKey(k+1))
						{
							x=instances.get(j).getFeatureVector().get(k+1);
						}
						double s=Math.signum(x);
						double w_dash=w[k]*Math.pow(online_learning_rate,s*origy);
						if(w_dash>mu)
							w_dash=mu;
						w[k]=w_dash;
					}
				}
				wixi=0.0;
			}
		}
	}
	
	/**
	 * This method is used to predict the labels of given instance once the
	 * classifier has been trained.
	 * @param instances List of instances for which prediction is to be done.
	 * @param args Name of file in which predicted labels are to be stored.
	 * @throws IOException
	 */
	public static void predict(List<Instance> instances,String args)throws IOException
	{
		FileWriter fw=new FileWriter(args);
		Writer bw=new BufferedWriter(fw);
		for(int i=0;i<instances.size();i++)
		{
			double wx=0.0;
			Set<Entry<Integer, Double>> set=instances.get(i).getFeatureVector().getEntrySet();
			Iterator<Entry<Integer, Double>> z = set.iterator(); 
			while(z.hasNext()) 
			{ 
				Map.Entry<Integer,Double> me = (Map.Entry<Integer,Double>)z.next();
				int ind=(int)me.getKey();	
				double val=(double)me.getValue();
				if(ind<n)
					wx=wx+w[ind-1]*val;
			}
			if(wx>beta)
			{
				bw.write(""+1);
				bw.write("\n");
			}
			else
			{
				bw.write(""+0);
				bw.write("\n");
			}
		}
		bw.close();
	}
	
	/**
	 * This method is used for calculating the number of distinct features present 
	 * in given set of instances.
	 * 
	 * @param instances
	 * @return
	 */
	static int calc_n(List<Instance> instances)
	{
		for(int i=0;i<instances.size();i++)
		{
			Set<Entry<Integer, Double>> set=instances.get(i).getFeatureVector().getEntrySet();
			Iterator<Entry<Integer, Double>> z = set.iterator(); 
			while(z.hasNext()) 
			{ 
				Map.Entry<Integer,Double> me = z.next();
				int ind=(int)me.getKey();
				if(ind>n)
				{
					n=ind;
				}
			}
		}
		return n;
	}
}
