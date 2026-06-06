package org.insa.graph.launch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.*;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;

public class LaunchLeFinal {

    static Graph graphPetit;
    static Graph graphGrand;
    static Random rand = new Random();

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

    @BeforeClass
    public static void initAll() throws Exception {
        final String urlPetitGraph =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        final String urlGrandGraph =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/bretagne.mapgr";

        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(urlPetitGraph))))) {
            graphPetit = reader.read();
        }

        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(urlGrandGraph))))) {
            graphGrand = reader.read();
        }
    }

    public static void main(String[] args) throws Exception {
        initAll();
        final Drawing drawing = createDrawing();
        drawing.drawGraph(graphPetit);

        Node origine =
                graphPetit.getNodes().get(rand.nextInt(graphPetit.getNodes().size()));
        Node destination =
                graphPetit.getNodes().get(rand.nextInt(graphPetit.getNodes().size()));

        ShortestPathData data = new ShortestPathData(graphPetit, origine, destination,
                ArcInspectorFactory.getAllFilters().get(0));

        ShortestPathSolution solDj = (new DijkstraAlgorithm(data)).run();

        if (solDj.isFeasible()) {
            drawing.drawPath(solDj.getPath(), Color.green);
        }
    }

    @Test
    public void testCheminLongueurNulle() {
        if (graphPetit == null)
            return;

        int randomNodeId = rand.nextInt(graphPetit.getNodes().size());
        Node origineEtDestination = graphPetit.getNodes().get(randomNodeId);

        ShortestPathData data = new ShortestPathData(graphPetit, origineEtDestination,
                origineEtDestination, ArcInspectorFactory.getAllFilters().get(0));

        ShortestPathSolution solDj = new DijkstraAlgorithm(data).run();

        assertFalse(solDj.isFeasible());
        assertTrue(solDj.getPath() == null);
    }

    @Test
    public void testDijkstraVsBellmanFordSurPetitGraphe() {
        if (graphPetit == null)
            return;

        Node origine =
                graphPetit.getNodes().get(rand.nextInt(graphPetit.getNodes().size()));
        Node destination =
                graphPetit.getNodes().get(rand.nextInt(graphPetit.getNodes().size()));

        ShortestPathData data = new ShortestPathData(graphPetit, origine, destination,
                ArcInspectorFactory.getAllFilters().get(0));

        ShortestPathSolution solDj = new DijkstraAlgorithm(data).run();
        ShortestPathSolution solBf = new BellmanFordAlgorithm(data).run();

        if (solBf.isFeasible()) {
            assertTrue(solDj.getPath().isValid());
            assertEquals(solBf.getPath().getLength(), solDj.getPath().getLength(),
                    1e-6);
        }
    }

    @Test
    public void testValiditeDijkstraSurGrandGraphe() {
        if (graphGrand == null)
            return;

        Node origine =
                graphGrand.getNodes().get(rand.nextInt(graphGrand.getNodes().size()));
        Node destination =
                graphGrand.getNodes().get(rand.nextInt(graphGrand.getNodes().size()));

        ShortestPathData data = new ShortestPathData(graphGrand, origine, destination,
                ArcInspectorFactory.getAllFilters().get(0));

        ShortestPathSolution solDj = new DijkstraAlgorithm(data).run();

        if (solDj.isFeasible()) {
            Path pathDj = solDj.getPath();

            assertTrue(pathDj.isValid());
            assertTrue(pathDj.getLength() >= 0);
            assertEquals(origine, pathDj.getOrigin());
            assertEquals(destination, pathDj.getDestination());
        }
    }

    @Test
    public void testDijkstraTempsSurPetitGraphe() {
        if (graphPetit == null)
            return;

        Node origine =
                graphPetit.getNodes().get(rand.nextInt(graphPetit.getNodes().size()));
        Node destination =
                graphPetit.getNodes().get(rand.nextInt(graphPetit.getNodes().size()));

        ShortestPathData data = new ShortestPathData(graphPetit, origine, destination,
                ArcInspectorFactory.getAllFilters().get(2));

        ShortestPathSolution solDj = new DijkstraAlgorithm(data).run();
        ShortestPathSolution solBf = new BellmanFordAlgorithm(data).run();

        if (solDj.isFeasible()) {
            assertTrue(solBf.isFeasible());
            assertTrue(solDj.getPath().isValid());
            assertEquals(solBf.getPath().getMinimumTravelTime(),
                    solDj.getPath().getMinimumTravelTime(), 1e-6);
        }
        else {
            assertFalse(solBf.isFeasible());
        }
    }
}
