package com.game.hacking.app.hackinggame2d.mapsequence;

import com.game.hacking.app.hackinggame2d.grafiks.Vec2D;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by MAGarcia on 8/11/2016.
 */
public class Vertex<V> {
  private static int count = 0;
  private List< Edge<V> > outgoingEdges;
  private List< Edge<V> > incomingEdges;
  private V data;
  private int number;


  public Vertex(V data) {
    this.data = data;
    outgoingEdges = new LinkedList<>();
    incomingEdges = new LinkedList<>();
    number = count++;
  }


  public Vertex() {
    this(null);
  }


  public void addToOutgoingEdges(Edge<V> edge) {
    if (outgoingEdges != null) {
      outgoingEdges.add(edge);
    }
  }


  public void setNumber(int num) {
    number = num;
  }


  public int getNumber() {
    return number;
  }


  public void addToOutgoingEdges(Vertex<V> destination, int distance) {
    Edge<V> edge = new Edge<>(this, destination, distance);

    outgoingEdges.add(edge);
  }


  public Boolean removeOutgoingEdge(Edge<V> edge) {
    return outgoingEdges.remove(edge);
  }


  public void addToIncomingEdges(Edge<V> edge) {
    if (incomingEdges != null) {
      incomingEdges.add(edge);
    }
  }


  public void addToIncomingEdges(Vertex<V> source, int distance) {
    Edge<V> edge = new Edge<>(source, this, distance);
    incomingEdges.add(edge);
  }


  public Boolean removeIncomingEdge(Edge<V> edge) {
    return incomingEdges.remove(edge);
  }


  public List< Edge<V> > getOutgoingEdges() {
    return outgoingEdges;
  }


  public List< Edge<V> > getIncomingEdges() {
    return incomingEdges;
  }


  public V getData() {
    return data;
  }
}
