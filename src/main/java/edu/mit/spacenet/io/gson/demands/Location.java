package edu.mit.spacenet.io.gson.demands;

public class Location {
  protected Integer id;
  protected String name;

  public static Location createFrom(edu.mit.spacenet.domain.network.Location location) {
    Location l = new Location();
    l.id = location.getTid();
    l.name = location.getName();
    return l;
  }
}
