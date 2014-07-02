package datastructures;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * This class contains a HashMap that represents features of the given instance.
 * 
 * @author Vaibhav
 *
 */
public class FeatureVector
{
	private HashMap<Integer,Double> l=new HashMap<Integer,Double>();

	/**
	 * Adds a feature and its value to HashMap
	 * @param index Feature number
	 * @param value Feature value
	 */
	public void add(int index, double value) 
	{
		l.put(index,value);
	}
	
	/**
	 * Returns the value of given feature number.
	 * @param index Given feature number.
	 * @return value corresponding to given feature number.
	 */
	public double get(int index) 
	{
		return ((double)l.get(index));
	}

	/**
	 * This method gives the size of feature vector.
	 * @return size of HashMap containing features.
	 */
	public int size() 
	{
		return l.size();
	}
	
	/**
	 * This method returns entrySet of the HashMap that contains features.
	 * @return
	 */
	public Set<Entry<Integer, Double>> getEntrySet()
	{
		return l.entrySet();
	}
	
	/**
	 * This method returns whether given feature is present.
	 * 
	 * @param key Feature to be searched.
	 * @return true or false depending on whether the feature is present.
	 */
	public boolean containsKey(int key)
	{
		return l.containsKey(key);
	}
}