package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class StarLabel extends Label {
    private Double cost_estimated;

    public StarLabel(Node currentNode, Double cost_realised, Double cost_estimated) {
        super(currentNode, cost_realised);
        this.cost_estimated = cost_estimated;
    }

    public Double getCostEstimated() {
        return this.cost_estimated;
    }

    public void setCostEstimated(Double cost_estimated) {
        this.cost_estimated = cost_estimated;
    }

    @Override
    public Double getTotalCost() {
        return this.getCost() + this.getCostEstimated();
    }

    @Override
    public int compareTo(Label l) {
        int compare = this.getTotalCost().compareTo(l.getTotalCost());

        if (compare == 0) {
            return this.getCostEstimated()
                    .compareTo(((StarLabel) l).getCostEstimated());
        }

        return compare;
    }
}
