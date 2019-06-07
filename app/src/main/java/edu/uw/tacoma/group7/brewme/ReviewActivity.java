/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import edu.uw.tacoma.group7.brewme.model.Brewery;
import edu.uw.tacoma.group7.brewme.model.Review;

/**
 * ReviewActivity is parent activity for Review related fragments, used for creating
 * and viewing reviews.
 */
public class ReviewActivity extends AppCompatActivity
        implements View.OnClickListener, Serializable,
        NewReviewFragment.OnFragmentInteractionListener {

    private JSONObject mArguments;

    /**
     * ReviewActivity onCreate retrieves Brewery object from the SearchDetailFragment.
     *
     * @param savedInstanceState Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //Brewery object passed from Write Review button
        Intent detailIntent = getIntent();
        Brewery reviewBrewery = (Brewery) detailIntent.getSerializableExtra("ReviewBrewery");
        String breweryId = reviewBrewery.getBreweryId();
        String breweryName = reviewBrewery.getName();
        //Log.e("Brewery ID: ", breweryId);

        //Pass brewery name and brewery id to NewReviewFragment
        Bundle bundle = new Bundle();
        bundle.putString("breweryId", breweryId);
        bundle.putString("breweryName", breweryName);
        NewReviewFragment newReviewFragment = new NewReviewFragment();
        newReviewFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_review_container, newReviewFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * Fragment interaction executes when Post Review button is pushed,
     * adding a new review to the Reviews database table.
     *
     * @param review Review object.
     */
    @Override
    public void onNewReviewFragmentInteraction(Review review) {
        StringBuilder url = new StringBuilder(getString(R.string.add_review_endpoint));
        //Construct a JSONObject to build a formatted message to send.
        mArguments = new JSONObject();
        try {
            mArguments.put(Review.BREWERY_ID, review.getBreweryId());
            mArguments.put(Review.BREWERY_NAME, review.getBreweryName());
            mArguments.put(Review.USERNAME, review.getUsername());
            mArguments.put(Review.TITLE, review.getTitle());
            mArguments.put(Review.RATING, review.getRating());
            mArguments.put(Review.REVIEW, review.getReview());

            // *** Debugging ***
            Log.e("JSON values in doInBackground: ", mArguments.toString());
            new AddReviewAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation: " + e.getMessage()
                    , Toast.LENGTH_SHORT).show();
        }
    }



    /**
     * AddReviewAsyncTask inner class for connecting and posting a new review to the database.
     */
    private class AddReviewAsyncTask extends AsyncTask<String, Void, String> {

        /**
         * doInBackground makes connection to Reviews table in database
         * and attempts a POST request.
         *
         * @param urls String url values.
         * @return String response from the database webservice.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());
                    wr.write(mArguments.toString());
                    wr.flush();
                    wr.close();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
//                    // *** Debugging ***
//                    Log.e("JSON values in doInBackground: ", response);

                } catch (Exception e) {
                    response = "Unable to add the new review, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }//end doInBackground

        /**
         * onPostExecute passes the result from doInBackground into a JSON object.
         *
         * @param result String value.
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if (resultObject.getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), "Review added successfully", Toast.LENGTH_SHORT)
                            .show();
                    getSupportFragmentManager().popBackStackImmediate();
                } else {
                    Toast.makeText(getBaseContext(), "Review couldn't be added, you have already posted a review for this brewery", Toast.LENGTH_SHORT).show();

                    //*** Debugging ***
                    Log.e("Error adding review, result string: ", result);
                    Log.e("Error adding review, error: ", resultObject.getString("error"));
                    getSupportFragmentManager().popBackStackImmediate();
                }
            } catch (JSONException e) {}

        }//end onPostExecute


    }
}

