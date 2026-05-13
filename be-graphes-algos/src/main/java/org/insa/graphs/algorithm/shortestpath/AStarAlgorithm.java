package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Point;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }

    public Double getMaxSpeed(ShortestPathData data) 
    {
        Double maxSpeed = Double.min(data.getMaximumSpeed(), data.getGraph().getGraphInformation().getMaximumSpeed());

        return maxSpeed == -1 ? 500 : maxSpeed;
    }

    @Override
    protected ShortestPathSolution doRun() {

        // Retrieve the graph.
        ShortestPathData data = getInputData();
        final int nbNodes = data.getGraph().size();

        // Initialize array of distances.
        StarLabel[] distance = new StarLabel[nbNodes];

        for (int i = 0; i < nbNodes; i++) {
            Node n = data.getGraph().getNodes().get(i);
            StarLabel l = new StarLabel(n, Double.POSITIVE_INFINITY, Double.valueOf(
                    Point.distance(n.getPoint(), data.getDestination().getPoint())));

            if(data.getMode() == AbstractInputData.Mode.TIME) {
                l.setCostEstimated(l.getCostEstimated() / getMaxSpeed(data));
            }

            distance[i] = l;
        }

        return this.doAlgorithm(data, distance);
    }
}
