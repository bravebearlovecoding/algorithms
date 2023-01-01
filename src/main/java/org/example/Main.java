package org.example;

import org.min.graph.Graph;
import org.min.graph.MyGraph;

public class Main {
    public static void main(String[] args) {
        Graph<String, Integer> graph = new MyGraph<>();
        graph.addEdge("A", "B", 5);
        graph.addEdge("A", "C", 3);
        graph.print();
    }
}