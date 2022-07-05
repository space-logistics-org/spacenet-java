package edu.mit.spacenet.io.gson.demands;

public class Element {
  protected Integer id;
  protected String name;

  public static Element createFrom(edu.mit.spacenet.domain.element.I_Element element) {
    if (element == null) {
      return null; // from mission-level demand model
    }
    Element e = new Element();
    e.id = element.getUid();
    e.name = element.getName();
    return e;
  }
}
