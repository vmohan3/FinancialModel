package datastructures;

/**
 * This class represents one instance of the data 
 * in the memory. The feature vector component contains 
 * the vector of features present in the data. The label 
 * component contains the label of that given data set.
 * 
 * @author Vaibhav Mohan
 *
 */
public class Instance
{
	ClassificationLabel _label = null;
	FeatureVector _feature_vector = null;

	/**
	 * constructor for the instance class
	 * @param feature_vector
	 * @param label
	 */
	public Instance(FeatureVector feature_vector, ClassificationLabel label) 
	{
		this._feature_vector = feature_vector;
		this._label = label;
	}

	/**
	 * This method returns the label component of instance.
	 * 
	 * @return Returns the label object.
	 */
	public ClassificationLabel getLabel() 
	{
		return _label;
	}

	/**
	 * This method is used for setting the label of an instance.
	 * 
	 * @param label Label to be set for the particular instance.
	 */
	public void setLabel(ClassificationLabel label) 
	{
		this._label = label;
	}

	/**
	 * This method returns the feature vector component.
	 * 
	 * @return Returns the feature vector of given instance.
	 */
	public FeatureVector getFeatureVector() 
	{
		return _feature_vector;
	}

	/**
	 * This method is used for setting the feature vector of an instance.
	 * 
	 * @param feature_vector Feature Vector to be set for the particular instance.
	 */
	public void setFeatureVector(FeatureVector feature_vector) 
	{
		this._feature_vector = feature_vector;
	}
}