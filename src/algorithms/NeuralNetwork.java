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
 * This is the implementation of neural networks algorithm for the project.
 * This classifier is used to learn the weights and then predict the label.
 * @author Vaibhav
 */
public class NeuralNetwork 
{
	static int k_nn=3;
	
	//parameter on how many hidden layer nodes should we have.
	static int k_ensemble=3;
	
	//number of iterations we must do for online learning
	static int ensemble_training_iterations=10;
	
	//the learning rate of this classifier
	static double ensemble_learning_rate=0.1;
	static int predicted_label,origy;
	static double mu[];
	
	//the Perceptrons
	static NeuralPerceptron eibp[];
	
	//training instances.
	static List<Instance> train_inst;
	static List<Instance> test_inst;
	static double w[];
	static int n=0;
	
	//Threshold.
	static double beta;
	
	/**
	 * Main function
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
	 * This is the activation function used by neurons
	 * @param val
	 * @return
	 */
	static int h(double val)
	{
		if(val>=0)
			return 1;
		else 
			return 0;
	}
	
	/**
	 * This method uses instances to train itself and learns weights.
	 * @param instances training instances.
	 */
	public static void train(List<Instance> instances) 
	{
		// Training
		int N=instances.size();
		eibp=new NeuralPerceptron [k_ensemble];
		for(int k=0;k<k_ensemble;k++)
		{
			eibp[k]=new NeuralPerceptron(instances,k,k_ensemble);
			eibp[k].train();
		}
		mu=new double[k_ensemble];
		for(int t=0;t<ensemble_training_iterations;t++)
		{
			for(int i=0;i<N;i++)
			{
				double val=0.0;
				double gk[]=new double[k_ensemble];
				for(int k=0;k<k_ensemble;k++)
					gk[k]=eibp[k].predict(instances.get(i));
				for(int k=0;k<k_ensemble;k++)
				{
					double val1 = mu[k] * gk[k];
					val=val+val1;
				}
				predicted_label=h(val);
				//k - nn
				int num_instances_k_nn=N<50?N:50;
				double arr[][]=new double[num_instances_k_nn][2];
				double temp;
				for(int ii=0;ii<num_instances_k_nn;ii++)
				{
					int d=(int)(0 + (int)(Math.random() * N));
					arr[ii][0]=calcDist(instances.get(d),instances.get(i));
					arr[ii][1]=Integer.valueOf(instances.get(d).getLabel().toString());
				}
				for(int iii=0;iii<num_instances_k_nn;iii++)
				{
					for(int j=0;j<num_instances_k_nn-iii-1;j++)
					{
						if(arr[j][0]>arr[j+1][0])
						{
							temp=arr[j][0];
							arr[j][0]=arr[j+1][0];
							arr[j+1][0]=temp;
							temp=arr[j][1];
							arr[j][1]=arr[j+1][1];
							arr[j+1][1]=temp;
						}
					}
				}
				double vale=0.0;
				for(int iii=0;iii<k_nn;iii++)
				{
					vale=vale+arr[iii][1];
				}
				origy=((vale/k_nn)>=0.5)?1:0;	
				if((origy!=predicted_label)&&(origy==1))
				{
					for(int k=0;k<k_ensemble;k++)
					{
						mu[k] = mu[k] + (ensemble_learning_rate * gk[k]);
					}
				}
				else if((origy!=predicted_label)&&(origy==0))
				{
					for(int k=0;k<k_ensemble;k++)
					{
						mu[k] = mu[k] - (ensemble_learning_rate * gk[k]);
					}
				}
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
	public static void predict(List<Instance> instances, String args)throws IOException
	{
		FileWriter fw=new FileWriter(args);
		Writer bw=new BufferedWriter(fw);
		int pred[]=new int[instances.size()];
		for(int i=0;i<instances.size();i++)
		{
			double g[]=new double[k_ensemble];
			for(int k=0;k<k_ensemble;k++)
			{
				g[k]=eibp[k].predict(instances.get(i));
			}
			double val=0.0;
			for(int k=0;k<k_ensemble;k++)
			{
				val=val+(mu[k]*g[k]);
			}
			pred[i]=h(val);
		}
		int N=instances.size();
		for(int i=0;i<N;i++)
		{
			bw.write(""+pred[i]);
			bw.write("\n");
		}
		
		bw.close();
	}
	
	/**
	 * Used to calculate distances between two instances.
	 * @param inst1 Instance 1
	 * @param inst2 Instance 2
	 * @return distance between the two given instances.
	 */
	static double calcDist(Instance inst1,Instance inst2)
	{
		double dist=0.0;
		double x_j;
		Set<Entry<Integer, Double>> set=inst2.getFeatureVector().getEntrySet();
		Iterator<Entry<Integer, Double>> z = set.iterator(); 
		while(z.hasNext()) 
		{ 
			Map.Entry<Integer,Double> me = (Map.Entry<Integer,Double>)z.next();
			int ind=(int)me.getKey();
			if(inst1.getFeatureVector().containsKey(ind))
			{
				x_j=inst1.getFeatureVector().get(ind);
			}
			else
			{
				x_j=0.0;
			}
			dist=dist+Math.pow(x_j-me.getValue(),2);
		}
		dist=Math.sqrt(dist);
		return dist;
	}
}