import java.util.*;

public class AlgPrim {
    static class Graf{
        int V; // Number of vertices
        ArrayList<ArrayList<Edge>> ListaAdiacenta;

        Graf(int V) {
            this.V = V;
            this.ListaAdiacenta = new ArrayList<>(V);
            for (int i = 0; i < V; ++i) {
                this.ListaAdiacenta.add(new ArrayList<>());
            }
        }

        void setV(int V)
        {
            this.V=V;
            //delete ListaAdiacenta
            this.ListaAdiacenta.clear();
            this.ListaAdiacenta = new ArrayList<>(V);
            for (int i = 0; i < V; ++i) {
                this.ListaAdiacenta.add(new ArrayList<>());
            }
        }

        void addEdge(Node start, Node end, int weight) {
            this.ListaAdiacenta.get(start.getID()-1).add(new Edge(start, end, weight));
            this.ListaAdiacenta.get(end.getID()-1).add(new Edge(end, start, weight)); // Assuming undirected graph
        }
    }

    static class PrimResult {
        ArrayList<Edge> minimumSpanningTree;
        int totalWeight;

        PrimResult(ArrayList<Edge> minimumSpanningTree, int totalWeight) {
            this.minimumSpanningTree = minimumSpanningTree;
            this.totalWeight = totalWeight;
        }
    }
    static PrimResult result;

    private static Vector<Node> nodes;
    private static Vector<Edge> edges;
    static ArrayList<ArrayList<Edge>>getListaAdiacenta(Graf graf)
    {
        return graf.ListaAdiacenta;
    }
    private static Vector<Vector<Integer>>AdjencyMatrix;

    void createListaAdiacenta(Graf graf)
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

    //implement the Prim algorithm and return an array of edges
    public static PrimResult prim(Graf graph) {
        int V = graph.V;
        ArrayList<Edge> minimumSpanningTree = new ArrayList<>();
        int totalWeight = 0;

        boolean[] visited = new boolean[V];
        PriorityQueue<Edge> minHeap = new PriorityQueue<>((a, b) -> Integer.compare(a.getWeight(), b.getWeight()));

        for (int startVertex = 0; startVertex < V; startVertex++) {
            if (!visited[startVertex]) {
                visited[startVertex] = true;

                for (Edge edge : graph.ListaAdiacenta.get(startVertex)) {
                    minHeap.add(edge);
                }

                while (!minHeap.isEmpty()) {
                    Edge currentEdge = minHeap.poll();

                    if (!visited[currentEdge.getEnd().getID() - 1]) {
                        visited[currentEdge.getEnd().getID() - 1] = true;
                        minimumSpanningTree.add(currentEdge);
                        totalWeight += currentEdge.getWeight();

                        for (Edge edge : graph.ListaAdiacenta.get(currentEdge.getEnd().getID() - 1)) {
                            if (!visited[edge.getEnd().getID() - 1]) {
                                minHeap.add(edge);
                            }
                        }
                    }
                }
            }
        }

        return new PrimResult(minimumSpanningTree, totalWeight);
    }


    public static void main(String[] args) {

        Graf graf=new Graf(0);

        AlgPrim algPrim=new AlgPrim();
        algPrim.createListaAdiacenta(graf);
        algPrim.printListaAdiacenta(graf.ListaAdiacenta);


        result = prim(graf);
        System.out.print("Minimum Spanning Tree: ");
        for (Edge edge : result.minimumSpanningTree) {
            System.out.print("(" + edge.getStart().getID() + ", " + edge.getEnd().getID() + ") ");
        }
        System.out.println("\nTotal Weight: " + result.totalWeight);
    }

    static PrimResult getResult(){
        return result;
    }

    static Vector<Node> getNodes(){
        return nodes;
    }

}
