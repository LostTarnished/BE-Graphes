package org.insa.graphs.algorithm.shortestpath;

import java.util.List;

import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Node;

public class MarathonAlgorithm extends ShortestPathAlgorithm 
{
    private final float ACCEPTABLE = 0.1f;

    private static boolean is_acceptable = false;

    public MarathonAlgorithm(ShortestPathData data) 
    {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() 
    {

        // Retrieve the graph.
        ShortestPathData data = getInputData();

        List<Arc> solution = null;
        float distance_solution = Float.POSITIVE_INFINITY;

        boolean visited[] = new boolean[data.getGraph().size()];

        //Do the algorithm
        DFS(data.getOrigin(), 0, solution, distance_solution, 42.125f, data.getOrigin(), visited);
    }

    private ShortestPathSolution DFS(Node origin, float distance, List<Arc> solution, float distance_solution, float distance_marathon, Node origin_marathon, boolean[] visited) 
    {
        for (Arc arc_voisin : origin.getSuccessors()) {
            Node destination = arc_voisin.getDestination();

            if(!visited[destination.getId()]) {
                visited[destination.getId()] = true;

                if(destination == origin_marathon) {
                    if(Math.abs(distance - distance_marathon) < Math.abs(distance_solution - distance_marathon)) {
                        if(Math.abs(distance - distance_marathon) <= ACCEPTABLE) {
                            is_acceptable = true;
                        }   
                        // Meilleur chemin trouver
                        return 
                    }
                } else {
                    // Ce chemin peut mener à une meilleur solution
                    if(Math.abs(distance) < Math.abs(distance_solution)) {

                        solution.add(arc_voisin);
                        DFS(destination, distance + arc_voisin.getLength(), solution, distance_solution, distance_marathon, origin_marathon, visited);

                    }
                }
            }
        }
    }
}
