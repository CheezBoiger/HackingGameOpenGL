package com.game.hacking.app.hackinggame2d.mapsequence;

import com.game.hacking.app.hackinggame2d.grafiks.Vec2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MAGarcia on 8/11/2016.
 */
public class GameGraph<T> implements Graph<Vertex<T> , Edge<T> > {
  private List<Vertex<T> > vertices;
  private List<Edge<T>> edges;
  private HashMap<Vec2D, Vertex<T> > quickLookup;
  private HashMap<Vertex<T>, Vec2D> coordLookup;

  public GameGraph() {
    vertices = new ArrayList<>();
    edges = new ArrayList<>();
    quickLookup = new HashMap<>();
    coordLookup = new HashMap<>();
  }

  public void Randomize() {

  }

  public void addVertexToGraph(T data, Vec2D coords) {
    Vertex<T> newVertex = new Vertex<>(data);

    vertices.add(newVertex);
    quickLookup.put(coords, newVertex);
    coordLookup.put(newVertex, coords);
  }

  public void addVertexToGraph(Vertex<T> vertex, Vec2D coords) {
    if ( vertex != null) {
      vertices.add(vertex);
      quickLookup.put(coords, vertex);
      coordLookup.put(vertex, coords);
    }
  }

  public void addEdgeBetweenTwoVertices(int distance, Vec2D source, Vec2D destination) {
    Vertex<T> start = quickLookup.get(source);
    Vertex<T> dest = quickLookup.get(destination);

    if (start == null || dest == null) {
      return;
    }

    Edge<T> newEdge = new Edge<>(start, dest, distance);

    start.addToOutgoingEdges(newEdge);
    dest.addToIncomingEdges(newEdge);
    edges.add(newEdge);
  }


  public Vertex<T> getVertex(Vec2D pos) {
    return quickLookup.get(pos);
  }





  public void removeVertexFromGraph(Vec2D coords) {
    Vertex<T> remVert = quickLookup.get(coords);

    if (remVert == null) {
      return;
    }

    List<Edge<T>> outgoing = remVert.getOutgoingEdges();
    List<Edge<T>> incoming = remVert.getIncomingEdges();

    for (Edge<T> edge : outgoing) {
      Vertex<T> dest = edge.getDestination();
      dest.removeIncomingEdge(edge);
    }

    for (Edge<T> edge : incoming) {
      Vertex<T> start = edge.getSource();
      start.removeOutgoingEdge(edge);
    }

    vertices.remove(remVert);
  }

  public List<Vertex<T>> getVertices() {
    return vertices;
  }

  public List<Edge<T>> getEdges() {
    return edges;
  }

  public Vec2D getCoordForVertex(Vertex<T> vertex) {
    return coordLookup.get(vertex);
  }
}
