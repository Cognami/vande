package mil.darpa.vande.converters.graphml;




import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import mil.darpa.vande.generic.V_GenericNode;
import mil.darpa.vande.generic.V_GraphObjectData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphmlNode {
	protected static final Logger logger = LoggerFactory
			.getLogger(GraphmlNode.class);

	@XmlAttribute
	public String id="";
	@XmlAttribute	
	public String label="";
	
	@XmlElement(name = "data")	
	protected SortedSet<V_GraphObjectData> dataSet = new TreeSet<V_GraphObjectData>();

	
	public GraphmlNode(V_GenericNode node)
	{
		this.id = node.getId();
		this.label=node.getLabel();// temp - not really part of graphml
		dataSet.addAll(node.getDataSet());
		setLabel(node.getLabel()); // node-prop format
	}
	
	
	private void setLabel(String label) {
		if (label == null || label.length() == 0) {
			logger.error("Setting invalid label into node");
			return;
		}
		removeData("label");
		removeData("node-prop-DisplayValue");

		addData("label", label);
		addData("node-prop-DisplayValue", label);
	}
	public void addData(String attribute, String value) {
		// We can have two entries for an identifier name. But SnagL barfs
		// if it gets two entries for the same name. So we kludge the name
		// here

		try {
			if (attribute == null || attribute.length() == 0) {
				throw new Exception();
			}
		} catch (Exception e) {
			logger.error(
					"Add Data with empty attribute name " + e.getMessage(), e);
			return;
		}

		if (value == null || value.length() == 0) {
			logger.error("Add Data for name " + attribute + " with null value");
			new Exception().printStackTrace();
			return;
		}

		// Now add the value
		boolean need_add = false;
		for (V_GraphObjectData d : dataSet) {
			if (d.getKey().equals(attribute)) {
				if (d.getKeyVal().equals(value))
					return; // Duplicate attr + value - ignore it
				else {
					need_add = true; // Duplicate attr, new value
					break;
				}
			}
		}
		if (need_add) { // we have more than one value for the same attribute
			addData(attribute + "_", value); // recursive
			return;
		}

		// So the name doesn't exist in the data set

		dataSet.add(new V_GraphObjectData(attribute, value));
	}
	private void removeData(String type) {
		Object l = null;
 
		for (V_GraphObjectData d : dataSet) {
			if (d.getKey().equals(type)) {
				l = d;
				break;
			}
		}
		if (l != null)
			dataSet.remove(l);
	}
	
	public void setColors(boolean GQTStyle)
	{
		// TODO
	}

}
