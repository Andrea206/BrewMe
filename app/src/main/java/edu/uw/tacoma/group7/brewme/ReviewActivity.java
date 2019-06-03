/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/
package edu.uw.tacoma.group7.brewme;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.Serializable;

import edu.uw.tacoma.group7.brewme.model.Brewery;


public class ReviewActivity extends AppCompatActivity
        implements View.OnClickListener, Serializable,
        NewReviewFragment.OnFragmentInteractionListener {

    /**
     * ReviewActivity onCreate retrieves Brewery object from the SearchDetailFragment.
     * @param savedInstanceState Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
       // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Brewery object passed from Write Review button
        Intent detailIntent = getIntent();
        Brewery reviewBrewery =(Brewery) detailIntent.getSerializableExtra("ReviewBrewery");

        String breweryId = reviewBrewery.getBreweryId();
        String breweryName = reviewBrewery.getName();
        //**Debugging**
        //Log.e("Brewery intent data: " ,reviewBrewery.getName());
        //Log.e("Bundle intent brewery ID: " , Integer.toString(reviewBrewery.getBreweryId()));

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


    @Override
    public void onNewReviewFragmentInteraction(Uri uri) {

    }
}
