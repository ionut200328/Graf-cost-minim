import java.util.*;

class Pair<A,B>
{
    public A first;
    public B second;

    public Pair(A first, B second)
    {
        this.first = first;
        this.second = second;
    }
}

public class AlgKruskal {
    static class Graf {
        int V; // Number of vertices
        ArrayList<Edge> edges;
        ArrayList<ArrayList<Edge>> ListaAdiacenta;

        Graf(int V) {
            this.V = V;
            this.edges = new ArrayList<>();
            this.ListaAdiacenta = new ArrayList<>(V);
            for (int i = 0; i < V; ++i) {
                this.ListaAdiacenta.add(new ArrayList<>());
            }
        }

        void setV(int V) {
            this.V = V;
            //delete ListaAdiacenta
            this.ListaAdiacenta.clear();
            this.ListaAdiacenta = new ArrayList<>(V);
            for (int i = 0; i < V; ++i) {
                this.ListaAdiacenta.add(new ArrayList<>());
            }
        }

        void addEdge(Node start, Node end, int weight) {
            this.ListaAdiacenta.get(start.getID() - 1).add(new Edge(start, end, weight));
            this.ListaAdiacenta.get(end.getID() - 1).add(new Edge(end, start, weight)); // Assuming undirected graph
            this.edges.add(new Edge(start, end, weight));
        }
    }

    static class KruskalResult {
        ArrayList<Edge> minimumSpanningTree;
        int totalWeight;

        KruskalResult(ArrayList<Edge> minimumSpanningTree, int totalWeight) {
            this.minimumSpanningTree = minimumSpanningTree;
            this.totalWeight = totalWeight;
        }
    }

    static KruskalResult result;

    private static Vector<Node> nodes;
    private static Vector<Edge> edges;

    private static Vector<Vector<Integer>> AdjencyMatrix;
    void createListaAdiacenta(AlgKruskal.Graf graf)
    {
        //read adjency matrix from file

        FilePrint filePrint=new FilePrint("src/AdjencyMatrix.txt");
        AdjencyMatrix=filePrint.FileReadMatrix();
        edges=filePrint.FileReadEdges();

        nodes = new Vector<Node>();
        for(int i=0;i<AdjencyMatrix.size();i++)
        {
            nodes.add(new Node(i+1));
        }

        if(AdjencyMatrix.size()==0)
        {
            return;
        }
        graf.ListaAdiacenta.clear();
        graf.setV(AdjencyMatrix.size());

        //create ListaAdiacenta from Frame.edges
        for(Edge e:edges)
        {
            graf.addEdge(e.getStart(),e.getEnd(),e.getWeight());
        }
    }

    void printListaAdiacenta(ArrayList<ArrayList<Edge>> ListaAdiacenta)
    {
        for(int i=0;i<ListaAdiacenta.size();i++)
        {
            System.out.print(i+1+": ");
            for(int j=0;j<ListaAdiacenta.get(i).size();j++)
            {
                System.out.print(ListaAdiacenta.get(i).get(j).getEnd().getID()+" ");
            }
            System.out.println();
        }
    }

    // implement the Kruskal algorithm and return an array of edges
    public static KruskalResult kruskal(Graf graph) {
        int V = graph.V;
        ArrayList<Edge> minimumSpanningTree = new ArrayList<>();
        int totalWeight = 0;

        // Sort edges in non-decreasing order by weight
        graph.edges.sort(Comparator.comparingInt(Edge::getWeight));

        // Create a parent array to keep track of components
        int[] parent = new int[V];
        for (int i = 0; i < V; i++) {
            parent[i] = i;
        }

        for (Edge edge : graph.edges) {
            Pair<Integer,Integer> roots = new Pair<>(0, 0);
            // If including this edge does not form a cycle
            if (!isCyclic(parent, edge, roots)) {
                minimumSpanningTree.add(edge);
                totalWeight += edge.getWeight();
                union(parent, roots.first, roots.second);
            }
            /*int rootStart = find(parent, edge.getStart().getID() - 1);
            int rootEnd = find(parent, edge.getEnd().getID() - 1);
            if(rootStart != rootEnd) {
                minimumSpanningTree.add(edge);
                totalWeight += edge.getWeight();
                union(parent, rootStart, rootEnd);
            }*/
        }

        return new KruskalResult(minimumSpanningTree, totalWeight);
    }

    static boolean isCyclic(int[] parent, Edge edge, Pair<Integer,Integer> roots)
    {

        int rootStart = find(parent, edge.getStart().getID() - 1);
        int rootEnd = find(parent, edge.getEnd().getID() - 1);
        roots.first = rootStart;
        roots.second = rootEnd;
        if(rootStart == rootEnd)
            return true;
        return false;
    }

    // Helper method to find the root of the set to which a node belongs
    private static int find(int[] parent, int node) {
        if (parent[node] == node) {
            return node;
        }
        // Path compression iterative
        Stack<Integer> stack = new Stack<>();
        while (parent[node] != node) {
            stack.push(node);
            node = parent[node];
        }
        while (!stack.isEmpty()) {
            parent[stack.pop()] = node;
        }
        return parent[node];
    }

    // Helper method to perform union of two sets
    private static void union(int[] parent, int root1, int root2) {
        parent[root2] = root1;
    }

    public static void main(String[] args) {
        Graf graf = new Graf(0);

        AlgKruskal algKruskal = new AlgKruskal();
        algKruskal.createListaAdiacenta(graf);
        algKruskal.printListaAdiacenta(graf.ListaAdiacenta);

        result = kruskal(graf);
        System.out.print("Minimum Spanning Tree: ");
        for (Edge edge : result.minimumSpanningTree) {
            System.out.print("(" + edge.getStart().getID() + ", " + edge.getEnd().getID() + ") ");
        }
        System.out.println("\nTotal Weight: " + result.totalWeight);
    }

    static KruskalResult getResult() {
        return result;
    }

    static Vector<Node> getNodes() {
        return nodes;
    }
}
