package algorithms;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import datastructures.Instance;

/**
 * Perceptron subclassifier for Neural Networks. This class is written 
 * to encode one perceptron present in the neural network.
 * @author Vaibhav Mohan
 *
 */
public class NeuralPerceptron
{
	double learning_rate=1.0;//This is used to determine how fast the learning should be.
	double wixi=0.0;//Holds the sum of weight*x for each perceptron
	int training_iterations=10;//the number of iterations for which this perceptron should be trained
	int predicted_label;
	int k,K;
	int n;
	double beta=0.0;
	List<Instance> instances;
	public double w[];
	
	/**
	 * The constructor for initializing a perceptron
	 * @param instances
	 * @param k
	 * @param K
	 */
	public NeuralPerceptron(List<Instance> instances, int k,int K)
	{
		this.instances=instances;
		this.k=k;
		this.K=K;
	}
	
	/**
	 * This method is used to train one perceptron in the hidden layer.
	 */
	public void train()
	{
		int len=instances.size();
		for(int i=0;i<len;i++)
		{
			Set<Entry<Integer, Double>> set=instances.get(i).getFeatureVector().getEntrySet();
			Iterator<Entry<Integer, Double>> z = set.iterator(); 
			while(z.hasNext()) 
			{ 
				Map.Entry<Integer,Double> me = (Map.Entry<Integer,Double>)z.next();
				int ind=(int)me.getKey();
				if(ind>n)
				{
					n=ind;
				}
			}
		}
		w=new double[n];
		for(int i=0;i<training_iterations;i++)
		{
			for(int j=0;j<len;j++)
			{
				if(j%K!=k)
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
					if(wixi>=beta)
						predicted_label=1;
					else
						predicted_label=-1;
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
							w[k]=w[k]+learning_rate*x*origy;
						}
					}
					wixi=0.0;
				}
			}
		}
	}
	
	/**
	 * This method is used for predicting the weights by individual perceptrons.
	 * @param instance the instance for which we need prediction
	 * @return predicted value.
	 */
	double predict(Instance instance)
	{
		double wx=0.0;
		Set<Entry<Integer, Double>> set=instance.getFeatureVector().getEntrySet();
		Iterator<Entry<Integer, Double>> z = set.iterator(); 
		while(z.hasNext()) 
		{ 
			Map.Entry<Integer,Double> me = (Map.Entry<Integer,Double>)z.next();
			int ind=(int)me.getKey();	
			double val=(double)me.getValue();
			if(ind<n)
				wx=wx+w[ind-1]*val;
		}
		return g(wx);
	}
	
	/**
	 * This is the activation function of individual perceptrons
	 * @param wixi 
	 * @return the expected reward
	 */
	double g(double wixi)
	{
		double val1=1+Math.pow(wixi, 2);
		double val2 = Math.sqrt(val1);
		double val=wixi/val2;
		return val;
	}
}