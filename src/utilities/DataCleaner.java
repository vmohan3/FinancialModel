package utilities;
import java.util.List;
import datastructures.Instance;

/** 
 * This class is implemented for cleaning the instances. It takes instances as 
 * its input and applies algorithm for cleaning it. It returns The cleaned 
 * instances.
 * 
 * @author Vaibhav
 *
 */
public class DataCleaner 
{
	/**
	 * This method is used for filling the missing values in dataset. It finds 
	 * average for that feature and puts that value in all the missing field.
	 * It also checks whether the value in percentage feature is greater than 
	 * 100. If it is it brings that value in range of 100.
	 *  
	 * @param instances The instances that needs to be cleaned
	 * @return cleaned instances
	 */
	public List<Instance> clean(List<Instance> instances)
	{
		List<Instance> temp=instances;
		int ct=0;
		double sum=0.0;
		for(int i=0;i<instances.size();i++)
		{
			if(instances.get(i).getFeatureVector().get(5)!=0)
			{
				sum+=instances.get(i).getFeatureVector().get(5);
				ct++;
			}
			if(instances.get(i).getFeatureVector().get(1)>100)
			{
				int percent=(int)instances.get(i).getFeatureVector().get(1)%100;
				instances.get(i).getFeatureVector().add(1, (double)percent);
			}
			if(instances.get(i).getFeatureVector().get(2)>100)
			{
				int age=(int)instances.get(i).getFeatureVector().get(2)%100;
				instances.get(i).getFeatureVector().add(2, (double)age);
			}
		}
		sum/=ct;
		for(int i=0;i<instances.size();i++)
		{
			if(temp.get(i).getFeatureVector().get(5)==0)
			{
				temp.get(i).getFeatureVector().add(5, sum);
			}
		}
		return temp;
	}
}