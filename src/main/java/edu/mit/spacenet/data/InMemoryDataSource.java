package edu.mit.spacenet.data;

import java.util.ArrayList;
import java.util.List;

import edu.mit.spacenet.domain.element.I_Element;
import edu.mit.spacenet.domain.network.edge.Edge;
import edu.mit.spacenet.domain.network.node.Node;
import edu.mit.spacenet.domain.resource.I_Resource;

public class InMemoryDataSource extends AbstractDataSource {
  private List<I_Element> elementTemplateLibrary;

  public InMemoryDataSource() {
    super();
    elementTemplateLibrary = new ArrayList<I_Element>();
  }

  public List<I_Element> getElementLibrary() {
    return elementTemplateLibrary;
  }

  @Override
  public boolean deleteEdge(int tid) throws Exception {
    Edge toRemove = null;
    for (Edge e : edgeLibrary) {
      if (e.getTid() == tid) {
        toRemove = e;
        break;
      }
    }
    if (toRemove != null) {
      edgeLibrary.remove(toRemove);
      return true;
    }
    return false;
  }

  @Override
  public boolean deleteElement(int tid) throws Exception {
    ElementPreview toRemove = null;
    for (ElementPreview e : elementPreviewLibrary) {
      if (e.ID == tid) {
        toRemove = e;
        break;
      }
    }
    if (toRemove != null) {
      elementPreviewLibrary.remove(toRemove);
      return true;
    }
    return false;
  }

  @Override
  public boolean deleteNode(int tid) throws Exception {
    Node toRemove = null;
    for (Node e : nodeLibrary) {
      if (e.getTid() == tid) {
        toRemove = e;
        break;
      }
    }
    if (toRemove != null) {
      nodeLibrary.remove(toRemove);
      return true;
    }
    return false;
  }

  @Override
  public boolean deleteResource(int tid) throws Exception {
    I_Resource toRemove = null;
    for (I_Resource e : resourceTypeLibrary) {
      if (e.getTid() == tid) {
        toRemove = e;
        break;
      }
    }
    if (toRemove != null) {
      resourceTypeLibrary.remove(toRemove);
      return true;
    }
    return false;
  }

  @Override
  public DataSourceType getDataSourceType() {
    return DataSourceType.IN_MEMORY;
  }

  @Override
  public String getName() {
    return "In Memory Data Source";
  }

  @Override
  public void loadEdgeLibrary() throws Exception {
    return;
  }

  @Override
  public I_Element loadElement(int tid) throws Exception {
    for (I_Element e : elementTemplateLibrary) {
      if (e.getTid() == tid) {
        return e;
      }
    }
    return null;
  }

  @Override
  public void loadElementLibrary() throws Exception {
    return;
  }

  @Override
  public void loadNodeLibrary() throws Exception {
    return;
  }

  @Override
  public void loadResourceLibrary() throws Exception {
    return;
  }

  @Override
  public void saveEdge(Edge edge) throws Exception {
    edgeLibrary.add(edge);
  }

  @Override
  public void saveElement(I_Element element) throws Exception {
    return;
  }

  @Override
  public void saveNode(Node node) throws Exception {
    nodeLibrary.add(node);
  }

  @Override
  public void saveResource(I_Resource resource) throws Exception {
    resourceTypeLibrary.add(resource);
  }

  @Override
  public List<String> validateData() throws Exception {
    return new ArrayList<String>();
  }

  @Override
  public void format() throws Exception {
    return;
  }

}
