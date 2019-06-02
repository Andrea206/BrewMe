package edu.uw.tacoma.group7.brewme.model;
import java.io.Serializable;

public class Review implements Serializable {

    private int mBreweryId;
    private String mBreweryName;
    private  String mUsername;
    private String mTitle;
    private double mRating;
    private String mReview;

    public Review(int breweryId, String breweryName,
                  String username, String title,
                  double rating, String review){
        this.mBreweryId = breweryId;
        this.mBreweryName = breweryName;
        this.mUsername = username;
        this.mTitle = title;
        this.mRating = rating;
        this.mReview = review;
    }

    public String getReview() {
        return mReview;
    }

    public void setReview(String mReview) {
        this.mReview = mReview;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double mRating) {
        this.mRating = mRating;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getBreweryName() {
        return mBreweryName;
    }

    public void setBreweryName(String mBreweryName) {
        this.mBreweryName = mBreweryName;
    }

    public int getBreweryId() {
        return mBreweryId;
    }

    public void setBreweryId(int mBreweryId) {
        this.mBreweryId = mBreweryId;
    }
}
