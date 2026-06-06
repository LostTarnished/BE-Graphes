package org.insa.graphs.algorithm.shortestpath;

import java.util.List;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class MarathonAlgorithm extends ShortestPathAlgorithm{

    public MarathonAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        // Retrieve the graph.
        ShortestPathData data = getInputData();
        final int nbNodes = data.getGraph().size();

        // Initialize array of distances.
        Label[] distance = new Label[nbNodes];

        for (int i = 0; i < nbNodes; i++) {
            Label l = new Label(data.getGraph().getNodes().get(i),
                    Double.POSITIVE_INFINITY);
            distance[i] = l;
        }

        List<Arc> solution;
        float distance_solution = Float.POSITIVE_INFINITY;

        //Do the algorithm
        DFS(data.getOrigin(), 0, solution, distance_solution, 42.125f, data.getOrigin());
    }

    private void DFS(Node origin, float distance, List<Arc> solution, float distance_solution, float distance_marathon, Node origin_marathon) 
    {
        for (Arc arc_voisin : origin.getSuccessors()) {
            if(arc_voisin.getDestination() == origin_marathon) {
                if(Math.abs(distance - distance_marathon) < Math.abs(distance_solution - distance_marathon)) {
                    // Meilleur chemin trouver
                }
            } else {
                // Verification condition pour continuer
            }
        }
    }
}
