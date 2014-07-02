package datastructures;
/**
 * This class is used for holding the label part of given instance.
 * 
 * @author Vaibhav
 *
 */
public class ClassificationLabel
{
	private int label;
	
	/**
	 * This method is used for setting the label to specified value.
	 * 
	 * @param label the label which needs to be assigned to the label attribute
	 * 			of this object
	 */
	public void setLabel(int label)
	{
		this.label = label;
	}
	
	/**
	 * This function returns the label of this object.
	 * 
	 * @return label
	 */
	public int getLabel()
	{
		return this.label;
	}
	
	/**
	 * This is the default constructor of this class which assigns '-1' to label.
	 */
	public ClassificationLabel()
	{
		this.label = -1;
	}
	
	/**
	 * This constructor is used when we instantiate this class with the given
	 * label
	 * 
	 * @param label
	 */
	public ClassificationLabel(int label) 
	{
		this.label=label;
	}
	
	/**
	 * This method returns the string format of label
	 */
	@Override
	public String toString() 
	{
		String st=""+label;
		return st;
	}
}