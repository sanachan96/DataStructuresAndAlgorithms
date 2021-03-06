
package misc.graphs;

import datastructures.concrete.ArrayDisjointSet;
import datastructures.concrete.ArrayHeap;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.DoubleLinkedList;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.exceptions.NoPathExistsException;

/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    IDictionary<V, ISet<V>> adjList; 
    int numVerts;
    int numEdges;
    IPriorityQueue<E> eWeights;
    IList<E> edges;
    
    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException  if any of the edges have a negative weight
     * @throws IllegalArgumentException  if one of the edges connects to a vertex not
     *                                   present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        // i think make adjacency list or matrix here
        // lets say we decided on a list
        // map of vertices to the vertices they link to.
        adjList = new ChainedHashDictionary<V, ISet<V>>();
        numVerts = vertices.size();
        numEdges = edges.size();
        eWeights = new ArrayHeap<>();
        this.edges = edges;
        
        for (V ver : vertices) {
            adjList.put(ver, new ChainedHashSet<>());
        }
        for (E edge : edges) {
            if (edge.getWeight() < 0.0) {
                throw new IllegalArgumentException();
            }
            V vert1 = edge.getVertex1();
            V vert2 = edge.getVertex2();
            if (!vertices.contains(vert1) || !vertices.contains(vert2)) {
                throw new IllegalArgumentException();
            }
            adjList.get(vert1).add(vert2);
            adjList.get(vert2).add(vert1);
            
            eWeights.insert(edge);
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return numVerts;
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return numEdges;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        // Kruskal's heckin Algorithm
        ISet<E> mst = new ChainedHashSet<>();
        ISet<V> visited = new ChainedHashSet<>();
        IDisjointSet<V> tree = new ArrayDisjointSet<>();
        
        // while it isnt a complete tree,
        while (mst.size() < numVerts - 1) {
            // take the smallest edge weight, 
            E minW = eWeights.removeMin();
            
            // vertices with the smallest edgeWeight
            V vert1 = minW.getVertex1();
            V vert2 = minW.getVertex2();
            
            // save their IDs
            int num1 = -1;
            int num2 = -1;
            
            
            if (visited.contains(vert1)) {
                num1 = tree.findSet(vert1);
            } else {
                tree.makeSet(vert1);
                num1 = tree.findSet(vert1);
            }
            if (visited.contains(vert2)) {
                num2 = tree.findSet(vert2);
            } else {
                tree.makeSet(vert2);
                num2 = tree.findSet(vert2);
            }
            if (num1 != num2) {                
                tree.union(vert1, vert2);
                mst.add(minW);
            }
            visited.add(vert1);
            visited.add(vert2);
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     */
    public IList<E> findShortestPathBetween(V start, V end) {
        if (start.equals(end)) {
            return new DoubleLinkedList<E>();
        }
        
        IDictionary<V, Double> ttlCosts = new ChainedHashDictionary<>();
        IDictionary<V, E> backPointer = new ChainedHashDictionary<>();
        // Set everything to infinity
        for (KVPair<V, ISet<V>> vertToWeight : adjList) {
            V vert = vertToWeight.getKey();
            ttlCosts.put(vert, Double.POSITIVE_INFINITY);
        }
        
        ISet<V> completed = new ChainedHashSet<>();
        ISet<V> toBeVisited = new ChainedHashSet<>();
        ISet<E> visitedEdges = new ChainedHashSet<>();
        IList<E> shortestPath = new DoubleLinkedList<>();
        toBeVisited.add(start);
        ttlCosts.put(start, 0.0);
        completed.add(start);
        
        IPriorityQueue<E> costs = new ArrayHeap<>();
        
        V next = start;
        while (!toBeVisited.isEmpty() && !completed.contains(end)) {
            toBeVisited.remove(next);
            for (E edge : edges) {
                addsAdjacentVertsToCosts(edge, next, costs, ttlCosts, toBeVisited, visitedEdges, completed);
            }
            
            E lowestEdge = costs.removeMin();
            while (!costs.isEmpty() && completed.contains(lowestEdge.getVertex1()) 
                    && completed.contains(lowestEdge.getVertex2())) {
                lowestEdge = costs.removeMin();
            }
            
            V other = lowestEdge.getVertex1();
            
            if (completed.contains(other)) {
                other = lowestEdge.getOtherVertex(other);
            }
            completed.add(other);
            backPointer.put(other, lowestEdge);
            next = other;
        }
        
        if (ttlCosts.get(end) == Double.POSITIVE_INFINITY || !completed.contains(start) 
                || !completed.contains(end)) {
            throw new NoPathExistsException();
        }
        
        IList<E> temp = new DoubleLinkedList<>();
        V look = end;
        System.out.println(look);
        while (look != start && backPointer.get(look).getOtherVertex(look) != start) {
            E path = backPointer.get(look);
            temp.add(path);
            look = path.getOtherVertex(look);
        }
        temp.add(backPointer.get(look));
        for (int i = temp.size() - 1; i >= 0; i--) {
            shortestPath.add(temp.get(i));
        }
        return shortestPath;
    }
    
    private void addsAdjacentVertsToCosts(E edge, V next, IPriorityQueue<E> costs, IDictionary<V, Double> ttlCosts, 
            ISet<V> toBeVisited, ISet<E> visitedEdges, ISet<V> completed) {
        if ((edge.getVertex1().equals(next) || edge.getVertex2().equals(next)) && 
                !visitedEdges.contains(edge)) {
            
            visitedEdges.add(edge);
            costs.insert(edge);
            
            V vertex = edge.getVertex1();
            if (vertex.equals(next)) {
                vertex = edge.getVertex2();
            }
            if (edge.getWeight() + ttlCosts.get(next) < ttlCosts.get(vertex)) {
                ttlCosts.put(vertex, edge.getWeight() + ttlCosts.get(next));
            }
            
            if (!completed.contains(vertex) && !toBeVisited.contains(vertex)) {
                toBeVisited.add(vertex); 
            }
        }
    }
    
}

