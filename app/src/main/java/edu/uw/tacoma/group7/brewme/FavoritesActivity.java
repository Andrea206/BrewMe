/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import javax.net.ssl.HttpsURLConnection;
import edu.uw.tacoma.group7.brewme.model.Brewery;

/**
 * FavoritesActivity is the parent for all the Fragments related to the Favorites feature of the
 * app.
 */
public class FavoritesActivity extends AppCompatActivity implements
        FavoritesListFragment.OnFavoritesListFragmentInteractionListener,
        FavoritesDetailFragment.OnFragmentInteractionListener,
        ReviewListFragment.OnListFragmentInteractionListener {

    private Brewery mBrewery;
    private FavoritesListFragment mFavoritesListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        mFavoritesListFragment = new FavoritesListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_favorites_container, mFavoritesListFragment)
                .commit();
    }

    /**
     * The Listener activated when a Brewery is selected from the FavoritesListFragment.
     * It finds the Id for the Brewery selected and initiates an async task to download the
     * appropriate information from Open Brewery DB.
     *
     * @param brewery the Brewery selected
     */
    @Override
    public void onFavoritesListFragmentInteraction(String brewery) {
        int id = 0;
        HashMap<Integer, String> map = mFavoritesListFragment.getFavorites();
        for(Integer num : map.keySet()) {
            if(map.get(num).equals(brewery)) {
                id = num;
            }
        }
        new DownloadBrewSearch().execute(getString(R.string.get_single_brew) + id);
    }


    @Override
    public void onReviewListFragmentInteraction(String result) {
        // Not used
    }

    @Override
    public void onFragmentInteraction() {
        // Not used
    }


    /**
     * AsyncTask class used for connecting to the Open Brewery DB webservice.
     */
    private class DownloadBrewSearch extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpsURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpsURLConnection) urlObject.openConnection();
                    urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                    urlConnection.setRequestMethod("GET");
                    //Added .addRequestProperty and .setRequestMethod("GET") per research about calling HTTP GET requests from Java
                    // https://www.codingpedia.org/ama/how-to-handle-403-forbidden-http-status-code-in-java/
                    //https://stackoverflow.com/questions/1485708/how-do-i-do-a-http-get-in-java
                    //https://stackoverflow.com/questions/24399294/android-asynctask-to-make-an-http-get-request
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the brewery, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * Uses JSON string response fetched from webservice to create a new Brewery object and launch
         * the FavoritesDetailFragment.
         *
         * @param result the JSON string result
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                Log.i("Response ", result);
                if (result != null) {
                    mBrewery = parseSingleBreweryJson(result);
                }
                FavoritesDetailFragment mDetailFragment = FavoritesDetailFragment.getFavoritesDetailFragment(mBrewery);

                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_favorites_container, mDetailFragment)
                        .addToBackStack(null);
                transaction.commit();
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
                        .show();
                //Log.d("onPostExecute error: ", e.getMessage(), new Throwable());
            }
        }

        /**
         * Creates a new Brewery object from the JSON string result returned by Open Brewery DB.
         *
         * @param breweryJson JSON string to parse
         * @return a new Brewery object
         * @throws JSONException if there is an error parsing the JSON string
         */
        public Brewery parseSingleBreweryJson(String breweryJson) throws JSONException {
            JSONObject obj = new JSONObject(breweryJson);
            Brewery brewery = new Brewery(obj.getString(Brewery.NAME),
                      obj.getString(Brewery.BREWERY_TYPE), obj.getString(Brewery.STREET),
                      obj.getString(Brewery.CITY), obj.getString(Brewery.STATE),
                      obj.getString(Brewery.POSTAL_CODE), obj.getString(Brewery.PHONE),
                      obj.getString(Brewery.WEBSITE), obj.getString(Brewery.ID));
            return brewery;
        }
    }
}
