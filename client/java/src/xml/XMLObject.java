package xml;

/**
 * XMLObjects represent data objects that can be serialized into valid XML.
 * 
 * @since November 9, 2014
 * @version 0.1.0
 */
public abstract class XMLObject {
	
	/**
	 * Serializes this object into xml form following the Coursinator course in the XML schema.
	 *
	 * @return A valid XML serialized string representing this object
	 *
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */	
	public abstract String serialize();
	
	/**
	 * Returns a serialized version of this object following the Coursinator schema.
	 * 
	 * @return A XML serialized version of this object
	 *
	 * @see #serialize()
	 *
	 * @since November 9, 2014
	 * @author Matthew Maynes
	 */
	@Override
	public String toString(){
		return this.serialize();
	}
	
}
