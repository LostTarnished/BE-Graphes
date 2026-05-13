package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class Label implements Comparable<Label> {
    private final Node currentNode;
    private boolean marked = false;
    private Double cost_realised;
    private Node dad = null;

    public Label(Node currentNode, Double cost_realised) {
        this.currentNode = currentNode;
        this.cost_realised = cost_realised;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public boolean getMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public Node getDad() {
        return dad;
    }

    public void setDad(Node dad) {
        this.dad = dad;
    }

    public Double getCost() {
        return cost_realised;
    }

    public void setCost(Double cost_realised) {
        this.cost_realised = cost_realised;
    }

    public Double getTotalCost() {
        return this.getCost();
    }


    @Override
    public int compareTo(Label l) {

        return this.getTotalCost().compareTo(l.getTotalCost());
    }
}
