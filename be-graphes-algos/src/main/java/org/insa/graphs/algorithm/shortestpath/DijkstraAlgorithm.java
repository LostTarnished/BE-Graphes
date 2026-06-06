package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    protected Label[] distance;

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    public long getMarkedLabel() 
    {
        return Arrays.stream(this.distance).filter(l -> l.getMarked()).count();
    }  

    @Override
    protected ShortestPathSolution doRun() {

        // Retrieve the graph.
        ShortestPathData data = getInputData();
        final int nbNodes = data.getGraph().size();

        // Initialize array of distances.
        this.distance = new Label[nbNodes];

        for (int i = 0; i < nbNodes; i++) {
            Label l = new Label(data.getGraph().getNodes().get(i),
                    Double.POSITIVE_INFINITY);
            this.distance[i] = l;
        }

        return this.doAlgorithm(data);
    }

    protected ShortestPathSolution doAlgorithm(ShortestPathData data) {
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();

        BinaryHeap<Label> heap = new BinaryHeap<Label>();

        this.distance[data.getOrigin().getId()].setCost(Double.valueOf(0));
        heap.insert(this.distance[data.getOrigin().getId()]);

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];

        while (!heap.isEmpty()) {
            Label l = heap.deleteMin();
            l.setMarked(true);

            if (l.getCurrentNode() == data.getDestination()) {
                break;
            }

            for (Arc arc : l.getCurrentNode().getSuccessors()) {

                // Small test to check allowed roads...
                if (!data.isAllowed(arc)) {
                    continue;
                }

                if (this.distance[arc.getDestination().getId()].getMarked()) {
                    continue;
                }

                // Retrieve weight of the arc.
                double w = data.getCost(arc);
                double oldDistance = this.distance[arc.getDestination().getId()].getCost();
                double newDistance = this.distance[arc.getOrigin().getId()].getCost() + w;

                if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                    notifyNodeReached(arc.getDestination());
                }

                // Check if new distances would be better, if so update...
                if (newDistance < oldDistance) {
                    Label l2 = this.distance[arc.getDestination().getId()];

                    if (!Double.isInfinite(oldDistance)) {
                        heap.remove(l2);
                    }

                    l2.setCost(newDistance);
                    l2.setDad(l.getCurrentNode());
                    heap.insert(l2);

                    predecessorArcs[arc.getDestination().getId()] = arc;
                }
            }
        }

        ShortestPathSolution solution = null;

        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL,
                    new Path(graph, arcs));
        }

        return solution;
    }
}
