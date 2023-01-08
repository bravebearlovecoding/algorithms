package org.min.graph;

/**
 * Graph interface
 * @param <V>
 * @param <E>
 */
public interface Graph<V, E> {
    int getVerticesSize();
    int getEdgeSize();
    void addVertex(V v);
    void removeVertex(V v);
    void addEdge(V from, V to);
    void addEdge(V from, V to, E weight);
    void removeEdge(V from, V to);

    void bfs(V first);

    void dfs(V first);
    void print();
}
