package com.game.hacking.app.hackinggame2d.mapsequence;

/**
 * Created by MAGarcia on 8/11/2016.
 */
public class Edge<V> {

  private Vertex<V> source;
  private Vertex<V> destination;

  private Integer distance;


  public Edge(Vertex<V> source, Vertex<V> destination, int distance) {
    this.destination = destination;
    this.source = source;
    this.distance = distance;
  }


  public Vertex<V> getSource() {
    return source;
  }


  public void setSource(Vertex<V> source) {
    this.source = source;
  }


  public Vertex<V> getDestination() {
    return destination;
  }


  public void setDestination(Vertex<V> destination) {
    this.destination = destination;
  }


  public Integer getDistance() {
    return distance;
  }


  public void setDistance(Integer distance) {
    this.distance = distance;
  }
}
