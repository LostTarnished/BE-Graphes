package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;


public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     *
     * @return The created drawing.
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static void main(String[] args) throws Exception {

        // visit these directory to see the list of available files on commetud.
        final String mapName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        final String pathName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        final Graph graph;
        final Path path;

        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {

            graph = reader.read();
        }

        // create the drawing
        final Drawing drawing = createDrawing();

        drawing.drawGraph(graph);

        try (final PathReader pathReader = new BinaryPathReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(pathName))))) {
            path = pathReader.readPath(graph);

        }
        ShortestPathData data = new ShortestPathData(graph, path.getOrigin(),
                path.getDestination(), ArcInspectorFactory.getAllFilters().get(0));
        ShortestPathSolution PCC_length_dj = (new DijkstraAlgorithm(data)).run();
        ShortestPathSolution PCC_length_astar = (new AStarAlgorithm(data)).run();
        ShortestPathSolution PCC_length_bf = (new BellmanFordAlgorithm(data)).run();

        drawing.drawPath(path);
        drawing.drawPath(PCC_length_dj.getPath(), Color.green);
        drawing.drawPath(PCC_length_astar.getPath(), Color.cyan);
        drawing.drawPath(PCC_length_bf.getPath(), Color.black);
    }


}
