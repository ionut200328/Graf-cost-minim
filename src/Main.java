import javax.swing.*;

public class Main {
    private static int x;
    JFrame frame = new JFrame("Graph Algorithms");

    Frame Graph=new Frame();

    static int pressedItem = 0;
    public Main() {
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create a menu
        JMenu menu = new JMenu("Graph cost minim");
        menuBar.add(menu);

        // Create a menu item
        JMenuItem Prim = new JMenuItem("Alg. Prim");
        menu.add(Prim);

        //add action listener for menu item
        Prim.addActionListener(e -> {
            pressedItem = 1;
            GrafCostMinimFrame.main(null);
        });

        JMenuItem Kruskal = new JMenuItem("Alg. Kruskal");
        menu.add(Kruskal);

        //add action listener for menu item
        Kruskal.addActionListener(e -> {
            pressedItem = 2;
            GrafCostMinimFrame.main(null);
        });

        //add undo with ctrl+z
        Graph.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control Z"), "undo");
        Graph.getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                Graph.undo();
            }
        });


        // Add the menu bar to the frame
        frame.setJMenuBar(menuBar);

        frame.add(Graph);

        frame.setVisible(true);
    }

    //modify parameter x in function p

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    public static int getPressedItem() {
        return pressedItem;
    }
}
