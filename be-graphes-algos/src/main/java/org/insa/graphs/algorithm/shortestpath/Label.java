package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.Node;

public class Label {
    private Node currentNode;
    private boolean marked;
    private int cost;
    private Node origin;

    public Node getCurrentNode() 
    {
        return currentNode;
    }

    public boolean getMarked() 
    {
        return marked;
    }

    public Node getOrigin() 
    {
        return origin;
    }

    public int getCost() 
    {
        return cost;
    }
}
