package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.List;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;

public class MarathonAlgorithm extends ShortestPathAlgorithm {

    private static final float DISTANCE_MARATHON = 42195.0f;
    private static final float ACCEPTABLE = 0.1f;

    private boolean isFinished;
    private List<Arc> bestSolution;
    private float bestDistanceDiff;

    public MarathonAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        Node origin = data.getOrigin();

        this.isFinished = false;
        this.bestSolution = null;
        this.bestDistanceDiff = Float.POSITIVE_INFINITY;

        boolean[] visited = new boolean[graph.size()];
        List<Arc> currentPath = new ArrayList<>();

        visited[origin.getId()] = true;

        DFS(origin, 0.0f, currentPath, origin, visited);

        if (bestSolution != null) {
            Path finalPath = new Path(graph, bestSolution);
            return new ShortestPathSolution(data, Status.FEASIBLE, finalPath);
        }
        else {
            return new ShortestPathSolution(data, Status.INFEASIBLE);
        }
    }

    private void DFS(Node currentNode, float currentDistance, List<Arc> currentPath,
            Node origin, boolean[] visited) {
        if (
            isFinished || 
            currentDistance >= DISTANCE_MARATHON + this.bestDistanceDiff || 
            currentDistance + currentNode.getPoint().distanceTo(origin.getPoint()) >= DISTANCE_MARATHON + this.bestDistanceDiff
        ) {
            return;
        }

        for (Arc arc : currentNode.getSuccessors()) {
            Node destination = arc.getDestination();
            float newDistance = currentDistance + arc.getLength();

            if (destination.equals(origin)) {
                float diff = Math.abs(newDistance - DISTANCE_MARATHON);

                if (diff < bestDistanceDiff) {
                    this.bestDistanceDiff = diff;
                    this.bestSolution = new ArrayList<>(currentPath);
                    this.bestSolution.add(arc);

                    if (diff <= ACCEPTABLE) {
                        this.isFinished = true;
                        return;
                    }
                }
            }
            else if (!visited[destination.getId()]) {
                visited[destination.getId()] = true;
                currentPath.add(arc);

                DFS(destination, newDistance, currentPath, origin, visited);

                currentPath.remove(currentPath.size() - 1);
                visited[destination.getId()] = false;
            }
        }
    }
}
