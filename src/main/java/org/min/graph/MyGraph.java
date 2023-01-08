package org.min.graph;

import java.util.*;
/**
 * @author Qing Min (Bernard)
 * @email bravebear922@gmail.com
 * @Date
 * @Version 1.00
 */
public class MyGraph<V, E> implements Graph<V, E> {
    // save all the vertices
    Map<V, Vertex<V, E>> vertices = new HashMap<>();
    // save all the edges
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
        Vertex<V, E> vertex = new Vertex<>(v);
        List<Edge<V, E>> list = new ArrayList<>();
        edges.forEach(edge -> {
            if (Objects.equals(edge.to, vertex)) {
                list.add(edge);
            }

            if (Objects.equals(edge.from, vertex)) {
                list.add(edge);
            }
        });
        list.forEach(edgeToRemove -> {
            edges.remove(edgeToRemove);
        });
        vertices.remove(vertex.value);
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
            toVertex.fromEdge.remove(edge);
            edges.remove(edge);
            edge.weight = weight;
            fromVertex.toEdge.add(edge);
            toVertex.fromEdge.add(edge);
            edges.add(edge);
        } else {
            edge.weight = weight;
            fromVertex.toEdge.add(edge);
            toVertex.fromEdge.add(edge);
            edges.add(edge);
        }
    }

    @Override
    public void removeEdge(V from, V to) {
        Vertex<V, E> fromVertex = vertices.get(from);
        Vertex<V, E> toVertex = vertices.get(to);
        if (fromVertex == null || toVertex == null) return;
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        if (fromVertex.toEdge.contains(edge)) {
            fromVertex.toEdge.remove(edge);
            toVertex.fromEdge.remove(edge);
            edges.remove(edge);
        }
    }

    @Override
    public void bfs(V first) {
        System.out.println("bfs start====");
        Vertex<V, E> firstVertex = vertices.get(first);
        if (firstVertex == null) return;
        Set<Vertex<V, E>> visitedList = new HashSet<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        visitedList.add(firstVertex);
        queue.offer(firstVertex);

        while ( !queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            System.out.println(vertex.value);
            vertex.toEdge.forEach(edge -> {
                if (!visitedList.contains(edge.to)) {
                    queue.offer(edge.to);
                    visitedList.add(edge.to);
                }
            });
        }
        System.out.println("bfs end====");
    }

    @Override
    public void dfs(V first) {
        System.out.println("dfs start====");
        Vertex<V, E> firstVertex = vertices.get(first);
        if (firstVertex == null) return;
        dfs(firstVertex, new HashSet<>());
        System.out.println("dfs end====");
    }

    public void dfs(Vertex<V, E> v, Set<Vertex<V, E>> visitedVertex) {
        System.out.println(v.value);
        visitedVertex.add(v);

        for(Edge<V, E> edge : v.toEdge)
            if (!visitedVertex.contains(edge.to)) {
                dfs(edge.to, visitedVertex);
            }
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
