import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class GrafCostMinimFrame extends JPanel {

    int width = 800;
    int height = 600;
    private static Vector<Node> nodes;
    private static Vector<Edge> edges;
    private int nodeNr = 1;
    private int radacina = -1;
    private static int node_diam = Frame.getNode_diam() * 2;

    private Node draggedNode = null;

    void GrafCostMinim()
    {
        //get pressed item from Main
        if(Main.getPressedItem() == 1)
        {
            AlgPrim.main(null);
            AlgPrim.PrimResult result = AlgPrim.getResult();
            nodes = new Vector<>();
            edges = new Vector<>();

            for (int i = 0; i < AlgPrim.getNodes().size(); i++) {
                String name = "";
                name += AlgPrim.getNodes().get(i).getID();
                Node node = new Node(new Point((int) (Math.random() * (width - node_diam)), (int) (Math.random() * (height - node_diam))), node_diam, name);
                nodes.add(node);
            }
            for (Edge e : result.minimumSpanningTree) {
                edges.add(new Edge(nodes.get(e.getStart().getID() - 1), nodes.get(e.getEnd().getID() - 1), e.getWeight()));
            }
        } else if (Main.getPressedItem() == 2){
            AlgKruskal.main(null);
            AlgKruskal.KruskalResult result = AlgKruskal.getResult();
            nodes = new Vector<>();
            edges = new Vector<>();

            for (int i = 0; i < AlgKruskal.getNodes().size(); i++) {
                String name = "";
                name += AlgKruskal.getNodes().get(i).getID();
                Node node = new Node(new Point((int) (Math.random() * (width - node_diam)), (int) (Math.random() * (height - node_diam))), node_diam, name);
                nodes.add(node);
            }
            for (Edge e : result.minimumSpanningTree) {
                edges.add(new Edge(nodes.get(e.getStart().getID() - 1), nodes.get(e.getEnd().getID() - 1), e.getWeight()));
            }

        }
    }
    GrafCostMinimFrame() {

        GrafCostMinim();

        JFrame frame = new JFrame("Graf Redus");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(this);
        frame.setVisible(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                draggedNode = getNodeAt(e.getPoint());

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedNode = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedNode != null) {
                    for(Node n : nodes) {
                        if(draggedNode != n && n.getBounds().contains(e.getPoint())) {
                            return;
                        }
                    }
                    draggedNode.setCenter(e.getPoint());
                    repaint();
                }
            }
        });


    }

    private Node getNodeAt(Point point) {
        for (Node node : nodes) {
            if (node.getBounds().contains(point)) {
                return node;
            }
        }
        return null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Edge e : edges) {
            e.drawEdge(g);
        }
        for (Node n : nodes) {
            n.drawNode(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GrafCostMinimFrame();
        });
    }
}
