package com.rideshare.rideshareapi.quadtree;

public class QuadTreeConstants {

    public static final double QUADTREE_LAST_NODE_SIZE_IN_KM=100;
    public static final double ONE_DEGREE_IN_KM=111.f;
    public static final double QUADTREE_LAST_NODE_SIZE_IN_DEGREE=kmToDegree(QUADTREE_LAST_NODE_SIZE_IN_KM);

    public static double kmToDegree(double km){
        return km/ONE_DEGREE_IN_KM;
    }


}
