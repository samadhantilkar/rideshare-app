package com.rideshare.rideshareapi.quadtree;

import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

public class QuadTree {

    public static final int TOTAL_X_DEGREE=360;
    public static final int TOTAL_Y_DEGREE=180;
    public static final int NORMALIZE_X=180;
    public static final int NORMALIZE_Y=90;

    private QuadTreeNode mRootNode;

    public QuadTree(){
        mRootNode=new QuadTreeNode(0,0,TOTAL_Y_DEGREE,TOTAL_X_DEGREE);
    }

    public QuadTree(QuadTreeNode rootNode){
        mRootNode=rootNode;
    }

    public synchronized void addNeighbour(long id,double latitude, double longitude){
        Neighbour neighbour=new NeighbourImpl(id,normalizeLatitude(latitude),
                normalizeLongitude(longitude));
        mRootNode.addNeighbour(neighbour,QuadTreeConstants.QUADTREE_LAST_NODE_SIZE_IN_DEGREE);
    }

    public void removeNeighbour(long id){
        mRootNode.removeNeighbour(id);
    }

    public Set<Neighbour> findNeighbours(double latitude, double longitude,double rangeInKm){
        Set<Neighbour> neighbourSet=new HashSet<>();
        double rangeInDegree=QuadTreeConstants.kmToDegree(rangeInKm);
        Rectangle2D.Double areaOfInterest=getRangeAsRectangle(normalizeLatitude(latitude),normalizeLongitude(longitude),rangeInDegree);
        mRootNode.findNeighboursWithinRectangle(neighbourSet,areaOfInterest);
        return neighbourSet;
    }

    public Set<Long> findNeighboursIds(double latitude, double longitude, double rangeInKm){
        Set<Neighbour> neighbourSet=findNeighbours(latitude,longitude,rangeInKm);
        Set<Long> neighboursIds=new HashSet<>();

        for(Neighbour neighbour:neighbourSet){
            neighboursIds.add(neighbour.getId());
        }
        return neighboursIds;
    }

    private double normalizeLatitude(double latitude){
        return latitude+NORMALIZE_Y;
    }

    private double normalizeLongitude(double longitude){
        return longitude+NORMALIZE_X;
    }

    private Rectangle2D.Double getRangeAsRectangle(double latitude,double longitude,double range){

        return new Rectangle2D.Double(Math.max(longitude-range,0),
                Math.max(latitude-range,0),
                Math.max(range*2,TOTAL_X_DEGREE),
                Math.max(range*2,TOTAL_Y_DEGREE));
    }
}
