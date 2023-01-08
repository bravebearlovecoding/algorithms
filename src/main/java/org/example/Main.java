package org.example;

import org.min.graph.Graph;
import org.min.graph.MyGraph;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * @author Qing Min (Bernard)
 * @email bravebear922@gmail.com
 * @Date
 * @Version 1.00
 */
public class Main {
    public static void main(String[] args) throws IOException {
        // use scanner to get user input string
        System.out.println("Please input your cmd: ");
        Scanner sc = new Scanner(System.in);
        String command = sc.nextLine();
        sc.close();
        int n = 0, s = 0;
        String[] cmd = command.split(" ");
        for(int i = 0; i < cmd.length; i++) {
            if(i == 0 && !"graph".equals(cmd[i])) {
                break;
            }
            if("-N".equalsIgnoreCase(cmd[i])) {
                if(i + 1 > cmd.length -1) {
                    // invalid cmd
                    break;
                }else{
                    n = Integer.parseInt(cmd[i + 1]);
                }
            }
            if("-S".equalsIgnoreCase(cmd[i])) {
                if(i + 1 > cmd.length -1) {
                    // invalid cmd
                    break;
                }else{
                    s = Integer.parseInt(cmd[i + 1]);
                }
            }
        }

        if(s > 0 && n >0) {
            Map<String, Object[][]> mapGraph = convertToMap(generateRandomGraph(n, s), n);
            print(mapGraph);
            Graph<String, Integer> graph = directedGraph(mapGraph);
            mapGraph.forEach((v, k) -> {
                if(k.length > 0){
                    graph.bfs(v);
                }
            });
        }else{
            System.out.println("invalid command! System exit!");
        }
    }

    /**
     * generate random graph
     * @param n the number of vertices
     * @param edge the number of edges
     * @return array for edges e.g. {{":1", ":2", 15}} means the edge connect from :1 and :2 with weight 15
     */
    public static Object[][] generateRandomGraph(int n, int edge) {
        List<Object> node = new ArrayList<>();
        List<Object> appearedNode = new ArrayList<>();
        List<Object> notAppearedNode = new ArrayList<>();
        for(int i = 1; i < n + 1; i++) {
            node.add(i);
            notAppearedNode.add(i);
        }
        int m = edge;
        List<Object[]> result = new ArrayList<>();
        Random rand = new Random();
        // generate (n -1) edges
        while(result.size() != n - 1) {
            if(result.size() == 0) {
                int p1 = rand.nextInt(n - 1);
                int p2 = rand.nextInt(n - p1 - 1) + p1 + 1;
                int x = (Integer) node.get(p1);
                int y = (Integer) node.get(p2);
                // add to appearedNode
                appearedNode.add(x);
                appearedNode.add(y);
                notAppearedNode = getDifferListByMap(node, appearedNode);
                result.add(new Object[]{x, y});
            }else {
                int p1 = (Integer) rand.nextInt(appearedNode.size());
                int x = (Integer)appearedNode.get(p1);
                int p2 = (Integer) rand.nextInt(notAppearedNode.size());
                int y = (Integer) notAppearedNode.get(p2);
                appearedNode.add(y);
                notAppearedNode = getDifferListByMap(node, appearedNode);
                if(node.indexOf(y) > node.indexOf(x)) {
                    result.add(new Object[]{y, x});
                }else {
                    result.add(new Object[]{x, y});
                }
            }
        }
        // generate more edges if edge number is more than (n - 1)
        while(result.size() != m) {
            int p1 = rand.nextInt(n - 1);
            int p2 = rand.nextInt(n - p1 - 1) + p1 + 1;
            int x = (Integer) node.get(p1);
            int y = (Integer) node.get(p2);
            // if edge already exist, generate again
            if(existInList(new Object[]{x, y}, result)) {
                continue;
            }else{
                result.add(new Object[]{x, y});
            }
        }
        Object[][] graph = new Object[result.size()][3];
        // change node to string and generate random weight
        for( int i = 0; i < result.size(); i++) {
            Object[] newObj = new Object[3];
            Object[] currObj = result.get(i);
            for(int j = 0; j < currObj.length; j++) {
                newObj[j] = ":" + currObj[j];
            }
            newObj[2] = rand.nextInt(10) + 1;
            graph[i] = newObj;
        }
        return graph;
    }

    /**
     * to check whether Object array exist in an List<Object[]>
     * @param obj object array
     * @param objList List with Object array
     * @return true or false
     */
    public static boolean existInList(Object[] obj, List<Object[]> objList) {
        for(Object[] o: objList) {
            if (o.length == obj.length) {
                for(int i = 0; i < obj.length; i++) {
                    if(obj[i] != o[i]) {
                        break;
                    }
                    if(i == obj.length - 1){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * get a new list with different values in 2 Lists
     * @param listA
     * @param listB
     * @return list with different values between two given lists
     */
    public static List<Object> getDifferListByMap(List<Object> listA, List<Object> listB) {
        List<Object> differList = new ArrayList<>();
        Map<Object, Integer> map = new HashMap<>();
        for (Object strA : listA) {
            map.put(strA, 1);
        }
        for (Object strB : listB) {
            Integer value = map.get(strB);
            if (value != null) {
                map.put(strB, ++value);
                continue;
            }
            map.put(strB, 1);
        }

        for (Map.Entry<Object, Integer> entry : map.entrySet()) {
            // get diff list
            if (entry.getValue() == 1) {
                differList.add(entry.getKey());
            }
        }
        return differList;
    }

    /**
     * Convert to Map<String, Object[][]>
     * @param mapData 2 dimensional array with information for edges and weight
     * @param n number of vertices
     * @return Map e.g. (":1", {{":2", 5}, {":3", 3}})
     */
    public static Map<String, Object[][]> convertToMap(Object[][] mapData, int n) {
        Map<String, Object[][]> map = new HashMap<>();
        for(int i = 1; i < n + 1; i++) {
            String vertex = ":" + i;
            for( Object[] objects: mapData ) {
                String currVertex = objects[0].toString();
                if(vertex.equals(currVertex)){
                    Object[] currEndVertex = new Object[3];
                    if(objects.length == 3) {
                        // with weight
                        currEndVertex[0] = objects[1];
                        currEndVertex[1] = objects[2];
                    }else {
                        // without weight
                        currEndVertex[0] = objects[1];
                    }
                    if(map.containsKey(currVertex)) {
                        // update
                        Object[][] currEndVList = map.get(currVertex);
                        Object[][] newEndVList = new Object[currEndVList.length + 1][3];
                        for(int j = 0; j < currEndVList.length; j++) {
                            newEndVList[j] = currEndVList[j];
                        }
                        newEndVList[currEndVList.length] = currEndVertex;
                        map.put(currVertex, newEndVList);
                    }else{
                        // add
                        map.put(currVertex, new Object[][]{currEndVertex});
                    }
                }
            }
            if(!map.containsKey(vertex)) {
                map.put(vertex, new Object[][]{});
            }
        }

        return map;
    }

    /**
     * print the graph to the console
     * @param graphMap generated by converToMap method
     */
    public static void print(Map<String, Object[][]> graphMap) {
        //      print
        System.out.println("{");
        graphMap.forEach( (k, v) -> {
            String output = k + " [";
            if(v != null && v.length > 0) {
                for (Object[] obj : v) {
                    output += "(" + obj[0] + " " + obj[1] + "),";
                }
                output = output.substring(0, output.length() - 1) + "],";
            }else {
                output += "],";
            }
            output = output.substring(0, output.length() - 1);
            System.out.println(output);
        });
        System.out.println("}");
    }

    /**
     * create directed graph with Map
     * @param map generated by convertToMap
     * @return Graph object
     */
    public static Graph<String, Integer> directedGraph(Map<String, Object[][]> map) {
        Graph<String, Integer> graph = new MyGraph<>();
        map.forEach((v, k) -> {
            graph.addVertex(v);
            if(k != null && k.length > 0) {
                for(Object[] neigbours: k) {
                    graph.addEdge(v, (String)neigbours[0], (Integer) neigbours[1] );
                }
            }
        });
        return graph;
    }

    /**
     * create directed graph with edges array
     * @param data
     * @return Graph Object
     */
    public static Graph<Object, Double> directedGraph(Object[][] data) {
        Graph<Object, Double> graph = new MyGraph<>();
        for (Object[] edge : data) {
            if(edge.length == 1) {
                // only one vertex without edge
                graph.addVertex(edge[0]);
            }else if(edge.length == 2) {
                // 2 vertices with one edge
                graph.addEdge(edge[0], edge[1]);
            }else if (edge.length == 3) {
                // 2 vertices with weight
                double weight = Double.parseDouble(edge[2].toString());
                graph.addEdge(edge[0], edge[1], weight);
            }
        }
        return graph;
    }
}