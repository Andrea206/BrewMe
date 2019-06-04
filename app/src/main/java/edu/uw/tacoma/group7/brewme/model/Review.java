package edu.uw.tacoma.group7.brewme.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Review class that defines a custom data structure for holding review data.
 */
public class Review implements Serializable {

    private int mBreweryId;
    private String mBreweryName;
    private  String mUsername;
    private String mTitle;
    private double mRating;
    private String mReview;

    public static final String BREWERY_ID = "brewery_id";
    public static final String BREWERY_NAME = "brewery_name";
    public static final String USERNAME = "username";
    public static final String TITLE = "title";
    public static final String RATING = "rating";
    public static final String REVIEW = "review";


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

    public String getTitle() {
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

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    /**
     * Returns a List populated with Review objects returned from database call
     */
    public static List<Review> parseReviewJson(String reviewJson) throws JSONException {
        ArrayList<Review> reviewList = new ArrayList<>();
        if(reviewJson != null){
            if(!reviewJson.contains("\"brewery_id\":"))
                throw new JSONException("Could not parse breweryJSON due to formatting.");
            JSONArray arr = new JSONArray(reviewJson);

            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Review review = new Review(obj.getInt(Review.BREWERY_ID), obj.getString(Review.BREWERY_NAME),
                        obj.getString(Review.USERNAME), obj.getString(Review.TITLE), obj.getDouble(Review.RATING),
                        obj.getString(Review.REVIEW));
                reviewList.add(review);
            }
        }
        return reviewList;
    }//end parseBreweryJson

    @Override
    public String toString(){
        return this.getBreweryId() + ", " +  this.mBreweryName +", " + this.getUsername() +", " +
                this.getTitle() + ", " + this.getRating() + ", " + this.getRating();
    }

}// end Review class





