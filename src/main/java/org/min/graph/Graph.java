package org.min.graph;

public interface Graph<V, E> {
    int getVerticesSize();
    int getEdgeSize();
    void addVertex(V v);
    void removeVertex(V v);
    void addEdge(V from, V to);
    void addEdge(V from, V to, E weight);
    void removeEdge(V from, V to);

    void print();
}
