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

    private String mBreweryName;
    private int mBreweryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
       // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //Brewery object passed from Write Review button
        Intent detailIntent = getIntent();
        Brewery reviewBrewery =(Brewery) detailIntent.getSerializableExtra("ReviewBrewery");

        mBreweryName = reviewBrewery.getName();
        mBreweryId = reviewBrewery.getBreweryId();

        //**Debugging**
        Log.e("Brewery intent data: " ,reviewBrewery.getName());
        Log.e("Bundle intent brewery ID: " , Integer.toString(reviewBrewery.getBreweryId()));



        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_review_container, new NewReviewFragment())
                .commit();
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onNewReviewFragmentInteraction(Uri uri) {

    }
}
