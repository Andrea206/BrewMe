/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/
package edu.uw.tacoma.group7.brewme;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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


/**
 * SearchActivity is parent of search field, search list, search detail fragments.
 */
public class SearchActivity extends AppCompatActivity
        implements Serializable, View.OnClickListener,
        SearchListFragment.OnListFragmentInteractionListener,
        SearchDetailFragment.OnAddToFavoritesFragmentInteractionListener,
        SearchFieldFragment.OnSearchFieldFragmentInteractionListener {


    private SearchListFragment.OnListFragmentInteractionListener mListener;
    private SearchDetailFragment mDetailFragment;
    private JSONObject mFavArguments;

    public static final String BREWERY_ID = "brewery_id";
    public static final String BREWERY_NAME= "brewery_name";
    public static final String USERNAME = "username";
    public static final String CITY = "city";
    public static final String STATE = "state";

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
    public void onSearchFieldFragmentInteraction(Uri uri) {

    }

    @Override
    public void onAddToFavoritesFragmentInteraction(String id, String name, String city, String state) {
        SharedPreferences sp = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        String email = sp.getString(getString(R.string.EMAIL), null);
        StringBuilder url = new StringBuilder(getString(R.string.favorites));

        //construct a JSONObject to build a formatted message to send
        mFavArguments = new JSONObject();
        try {
            mFavArguments.put(BREWERY_ID, id);
            mFavArguments.put(BREWERY_NAME, name);
            mFavArguments.put(USERNAME, email);
            mFavArguments.put(CITY, city);
            mFavArguments.put(STATE, state);
            new AddToFavsAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class AddToFavsAsyncTask extends AsyncTask<String, Void, String> {


        private String TAG = "AsyncTask error";

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

                    Log.i(TAG, mFavArguments.toString());
                    wr.write(mFavArguments.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add brewery to favorites, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getBoolean("success") == true) {
                    Toast.makeText(getApplicationContext(),
                            "Added to favorites successfully!", Toast.LENGTH_SHORT)
                            .show();
                    getSupportFragmentManager().popBackStackImmediate();
                } else {
                    Toast.makeText(getApplicationContext(), "This brewery is already in your favorites", Toast.LENGTH_SHORT)
                            .show();
                    Log.e(TAG, resultObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }

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
