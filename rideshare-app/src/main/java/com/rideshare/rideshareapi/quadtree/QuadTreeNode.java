package com.rideshare.rideshareapi.quadtree;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class QuadTreeNode {

    /**
     * Represents the whole rectangle of this node
     * ---------
     * |       |
     * |       |
     * |       |
     * ---------
     */
    protected Rectangle2D.Double mBound;
    /**
     * Represents the top left node of this node
     * ---------
     * | x |   |
     * |---|---|
     * |   |   |
     * ---------
     */
    protected QuadTreeNode mTopLeftNode;
    /**
     * Represents the top right node of this node
     * ---------
     * |   | x |
     * |---|---|
     * |   |   |
     * ---------
     */
    protected QuadTreeNode mTopRightNode;
    /**
     * Represents the bottom left node of this node
     * ---------
     * |   |   |
     * |---|---|
     * | x |   |
     * ---------
     */
    protected QuadTreeNode mBottomLeftNode;
    /**
     * Represents the bottom right node of this node
     * ---------
     * |   |   |
     * |---|---|
     * |   | x |
     * ---------
     */
    protected QuadTreeNode mBottomRightNode;

    /**
     * List of points of interest A.K.A neighbours inside this node
     * this list is only filled in the deepest nodes
     */
    protected List<Neighbour> mNeighbours=new ArrayList<>();

    /**
     * Creates a new node
     *
     * @param latitude       node's Y start point
     * @param longitude      node's X start point
     * @param latitudeRange  node's height
     * @param longitudeRange node's width
     */
    public QuadTreeNode(double latitude,double longitude,double latitudeRange,double longitudeRange){
        mBound=new Rectangle2D.Double(longitude,latitude,longitudeRange,latitudeRange);
    }

    /**
     * Adds a neighbour in the quadtree.
     * This method will navigate and create nodes if necessary, until the smallest (deepest) node is reached
     *
     * @param neighbour
     */
    public void addNeighbour(Neighbour neighbour,double deepestNodeSize){
        double halfSize=mBound.width * .5f;

        if(halfSize <deepestNodeSize){
            mNeighbours.add(neighbour);
            return;
        }

        QuadTreeNode node=locateAndCreateNodeForPoint(neighbour.getLatitude(),neighbour.getLongitude());
        node.addNeighbour(neighbour,deepestNodeSize);
    }


    /**
     * Removes a neighbour from the quadtree
     *
     * @param id the neighbour's id
     * @return if the neighbour existed and was removed
     */
    public boolean removeNeighbour(Long id){

        for(Neighbour neighbour:mNeighbours){
            if(id==neighbour.getId()){
                mNeighbours.remove(neighbour);
                return true;
            }
        }

        if(null != mTopLeftNode){
            if(mTopLeftNode.removeNeighbour(id)){
                return true;
            }
        }

        if(null !=mBottomLeftNode){
            if(mBottomLeftNode.removeNeighbour(id)){
                return true;
            }
        }

        if(null !=mTopRightNode){
            if(mTopRightNode.removeNeighbour(id)){
                return true;
            }
        }

        if(null != mBottomRightNode){
            if(mBottomRightNode.removeNeighbour(id)){
                return true;
            }
        }

        return false;
    }

    /**
     * Adds neighbours to the found set
     *
     * @param contains         if the rangeAsRectangle is contained inside the node
     * @param neighborSet      a set to be filled by this method
     * @param rangeAsRectangle the area of interest
     */
    private void addNeighbours(boolean contains,Set<Neighbour> neighbourSet,Rectangle2D.Double rangeAsRectangle){
        if(contains){
            neighbourSet.addAll(mNeighbours);
            return;
        }
        findAll(neighbourSet,rangeAsRectangle);
    }

    /**
     * If the rangeAsRectangle is not contained inside this node we must
     * search for neighbours that are contained inside the rangeAsRectangle
     *
     * @param neighborSet      a set to be filled by this method
     * @param rangeAsRectangle the area of interest
     */
    private void findAll(Set<Neighbour> neighbourSet,Rectangle2D.Double rangeAsRectangle){
        for(Neighbour neighbour: mNeighbours){
            if(rangeAsRectangle.contains(neighbour.getLongitude(),neighbour.getLatitude())){
                neighbourSet.add(neighbour);
            }
        }
    }

    /**
     * Recursively search for neighbours inside the given rectangle
     *
     * @param neighbourSet     a set to be filled by this method
     * @param rangeAsRectangle the area of interest
     */
    public void findNeighboursWithinRectangle(Set<Neighbour> neighbourSet,Rectangle2D.Double rangeAsRectangle){
        boolean end;

        if(mBound.contains(rangeAsRectangle)){
            end=true;

            if(mTopLeftNode!=null){
                mTopLeftNode.findNeighboursWithinRectangle(neighbourSet,rangeAsRectangle);
                end=false;
            }

            if(mBottomLeftNode!=null){
                mBottomLeftNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end=false;
            }

            if(mTopRightNode != null){
                mTopRightNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end=false;
            }

            if(mBottomLeftNode!=null){
                mBottomLeftNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end=false;
            }

            if(end){
                addNeighbours(true,neighbourSet,rangeAsRectangle);
            }
            return;
        }

        //In case of intersection with the area of interest

        if(mBound.intersects(rangeAsRectangle)){
            end=true;

            // If end is true it means that we are on the deepest node
            // otherwise we should keep going deeper

            if(null != mTopLeftNode){
                mTopLeftNode.findNeighboursWithinRectangle(neighbourSet,rangeAsRectangle);
                end=false;
            }

            if(null != mBottomLeftNode){
                mBottomLeftNode.findNeighboursWithinRectangle(neighbourSet,rangeAsRectangle);
                end=false;
            }

            if(null != mTopRightNode){
                mTopRightNode.findNeighboursWithinRectangle(neighbourSet,rangeAsRectangle);
                end=false;
            }

            if(null != mBottomRightNode){
                mBottomRightNode.findNeighboursWithinRectangle(neighbourSet,rangeAsRectangle);
                end=false;
            }

            if(end){
                addNeighbours(false,neighbourSet,rangeAsRectangle);
            }
        }
    }

    protected QuadTreeNode locateAndCreateNodeForPoint(double latitude, double longitude){
        double  halfWidth=mBound.width * .5f;
        double halfHeight=mBound.height * .5f;

        if(longitude< mBound.x+halfWidth){
            if(latitude<mBound.y+halfHeight){
                return mTopRightNode != null? mTopLeftNode:(mTopLeftNode=new QuadTreeNode(mBound.y,mBound.x,halfHeight,halfWidth));
            }
            return mBottomLeftNode!=null? mBottomLeftNode:(mBottomLeftNode=new QuadTreeNode(mBound.y+halfHeight,mBound.x,halfHeight,halfWidth));
        }
        if(latitude<mBound.y+halfHeight){
            return mTopRightNode !=null ? mTopRightNode:(mTopRightNode=new QuadTreeNode(mBound.y,mBound.x+halfWidth,halfHeight,halfWidth));
        }
        return mBottomLeftNode!=null ? mBottomLeftNode:(mBottomLeftNode=new QuadTreeNode(mBound.y+halfHeight,mBound.x+halfWidth,halfHeight,halfWidth));
    }

    protected double getLongitude(){
        return mBound.x;
    }

    protected double getLatitude(){
        return mBound.y;
    }

    protected double getWidth(){
        return mBound.width;
    }

    protected double getHeight(){
        return mBound.height;
    }


}
