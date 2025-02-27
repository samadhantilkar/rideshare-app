package com.rideshare.rideshareapi.quadtree;

public class NeighbourImpl implements Neighbour{

    private final long mId;
    private final double mLatitude;
    private final double mLongitude;


    public NeighbourImpl(long mId, double mLatitude, double mLongitude) {
        this.mId = mId;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public double getLatitude() {
        return mLatitude;
    }

    @Override
    public double getLongitude() {
        return mLongitude;
    }
}
