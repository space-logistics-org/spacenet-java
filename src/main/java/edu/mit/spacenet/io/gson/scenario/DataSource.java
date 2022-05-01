package edu.mit.spacenet.io.gson.scenario;

import java.util.List;

import edu.mit.spacenet.data.I_DataSource;
import edu.mit.spacenet.data.InMemoryDataSource;

public class DataSource {
	public List<Node> nodeLibrary;
	public List<Edge> edgeLibrary;
	public List<ResourceType> resourceTypeLibrary;
	public List<Element> elementTemplateLibrary;

	/*
	public static DataSource createFrom(I_DataSource dataSource, Context context) {
		DataSource d = new DataSource();
		d.nodeLibrary = Node.createFrom(dataSource.getNodeLibrary(), context);
		d.edgeLibrary = Edge.createFrom(dataSource.getEdgeLibrary(), context);
		d.resourceTypeLibrary = ResourceType.createFrom(dataSource.getResourceLibrary(), context);
		return d;
	}
	*/
	
	public I_DataSource toSpaceNet(Context context) {
		InMemoryDataSource d = new InMemoryDataSource();
		d.getNodeLibrary().addAll(Node.toSpaceNet(nodeLibrary, context));
		d.getEdgeLibrary().addAll(Edge.toSpaceNet(edgeLibrary, context));
		d.getResourceLibrary().addAll(ResourceType.toSpaceNet(resourceTypeLibrary, context));
		for(Element e : elementTemplateLibrary) {
			d.getElementPreviewLibrary().add(e.getPreview(context));
			d.getElementLibrary().add(e.toSpaceNet(context));
		}
		return d;
	}
}
