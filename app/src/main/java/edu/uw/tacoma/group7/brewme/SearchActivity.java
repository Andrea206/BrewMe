/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/
package edu.uw.tacoma.group7.brewme;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.Serializable;

import edu.uw.tacoma.group7.brewme.model.Brewery;

/**
 * SearchActivity is parent of search field, search list, search detail fragments.
 */
public class SearchActivity extends AppCompatActivity
        implements Serializable, View.OnClickListener,
        SearchListFragment.OnListFragmentInteractionListener,
        SearchDetailFragment.OnFragmentInteractionListener,
        SearchFieldFragment.OnSearchFieldFragmentInteractionListener{

    private SearchListFragment.OnListFragmentInteractionListener mListener;
    private SearchDetailFragment mDetailFragment;

    /**
     * Launches SearchFieldFragment, which contains input field for search function.
     * @param savedInstanceState Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_search_container, new SearchFieldFragment())
                .commit();
    }

    @Override
    public void onClick(View v) {
    }

    /**
     * Launches Detail Fragment when a list item is selected. Brewery data object is passed to
     * Detail Fragment to display more information than the search result list provides.
     * @param item Brewery object containing JSON data.
     */
    @Override
    public void onBrewListFragmentInteraction(Brewery item) {
        mDetailFragment = SearchDetailFragment.getSearchDetailFragment(item);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_search_container, mDetailFragment)
                .addToBackStack(null);

        transaction.commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSearchFieldFragmentInteraction(Uri uri) {

    }

//    /**
//     * Called by the "New Review" button to launch the ReviewActivity.
//     * @param view a View object
//     */
//    @Override
//    public void launchUserReviews(View view){
//        Intent myIntent = new Intent(this, ReviewActivity.class);
//        myIntent.putExtra("ReviewBrewery", mDetailFragment.getArguments());
//        mDetailFragment.startActivity(myIntent);
//    }




}//end SearchActivity
