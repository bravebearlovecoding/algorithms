package org.min.graph;

import java.util.*;

public class MyGraph<V, E> implements Graph<V, E> {
    Map<V, Vertex<V, E>> vertices = new HashMap<>();
    Set<Edge<V, E>> edges = new HashSet<>();

    private static class Vertex<V, E > {
        V value;
        Set<Edge<V, E>> fromEdge = new HashSet<>();
        Set<Edge<V, E>> toEdge = new HashSet<>();
        public Vertex(V v) {
            this.value = v;
        }

        @Override
        public boolean equals(Object o) {
            Vertex<V, E> vertex = (Vertex<V, E>) o;
            return Objects.equals(vertex.value, value);
        }

        @Override
        public int hashCode() {
            return value == null? 0 : value.hashCode();
        }

        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    private static class Edge<V, E> {
        Vertex<V, E> from;
        Vertex<V, E> to;
        E weight;

        public Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public boolean equals(Object obj) {
            Edge<V, E> edge = (Edge<V, E>) obj;
            return Objects.equals(edge.from, from) && Objects.equals(edge.to, to);
        }

        @Override
        public int hashCode() {
            return from.hashCode() * 31 + to.hashCode();
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
        }
    }

    @Override
    public int getVerticesSize() {
        return this.vertices.size();
    }

    @Override
    public int getEdgeSize() {
        return this.edges.size();
    }

    @Override
    public void addVertex(V v) {
        if( vertices.containsKey(v)) return;
        vertices.put(v, new Vertex<>(v));
    }

    @Override
    public void removeVertex(V v) {

    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    @Override
    public void addEdge(V from, V to, E weight) {
        // check whether from vertex exists
        Vertex<V, E> fromVertex = vertices.get(from);
        if(fromVertex == null) {
            fromVertex = new Vertex<>(from);
            vertices.put(from, fromVertex);
        }
        // check whether to vertex exists
        Vertex<V, E> toVertex = vertices.get(to);
        if(toVertex == null) {
            toVertex = new Vertex<>(to);
            vertices.put(to, toVertex);
        }

        // check whether edge exists
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        if (fromVertex.toEdge.contains(edge)) {
            fromVertex.toEdge.remove(edge);
            fromVertex.fromEdge.remove(edge);
            edges.remove(edge);
            edge.weight = weight;
            fromVertex.toEdge.add(edge);
            fromVertex.fromEdge.add(edge);
            edges.add(edge);
        } else {
            edge.weight = weight;
            edges.add(edge);
        }
    }

    @Override
    public void removeEdge(V from, V to) {

    }

    @Override
    public void print() {
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            System.out.println(v);
        });

        edges.forEach((Edge<V, E> edge) -> {
            System.out.println(edge);
        });
    }


}
