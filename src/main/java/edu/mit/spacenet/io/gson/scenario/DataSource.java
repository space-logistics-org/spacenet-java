package edu.mit.spacenet.io.gson.scenario;

import java.util.List;

import edu.mit.spacenet.data.I_DataSource;
import edu.mit.spacenet.data.InMemoryDataSource;

public class DataSource {
	public List<Node> nodeLibrary;
	public List<Edge> edgeLibrary;
	public List<ResourceType> resourceTypeLibrary;
	public List<DemandModel> demandModelLibrary;
	public List<Element> elementTemplateLibrary;
	
	public I_DataSource toSpaceNet(Context context) {
		InMemoryDataSource d = new InMemoryDataSource();
		d.getNodeLibrary().addAll(Node.toSpaceNet(nodeLibrary, context));
		d.getEdgeLibrary().addAll(Edge.toSpaceNet(edgeLibrary, context));
		d.getResourceLibrary().addAll(ResourceType.toSpaceNet(resourceTypeLibrary, context));
		DemandModel.toSpaceNet(null, demandModelLibrary, context); // load demand models
		for(Element e : elementTemplateLibrary) {
			d.getElementLibrary().add(e.toSpaceNet(context));
			d.getElementPreviewLibrary().add(e.getPreview(context));
		}
		return d;
	}
}
