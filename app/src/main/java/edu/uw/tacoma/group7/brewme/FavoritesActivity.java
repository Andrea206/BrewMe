package edu.uw.tacoma.group7.brewme;

import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import edu.uw.tacoma.group7.brewme.model.Brewery;

public class FavoritesActivity extends AppCompatActivity implements
        FavoritesListFragment.OnFavoritesListFragmentInteractionListener,
        FavoritesDetailFragment.OnFragmentInteractionListener, ReviewListFragment.OnListFragmentInteractionListener{

    private FavoritesDetailFragment mDetailFragment;
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
    public void onFragmentInteraction() {

    }

    @Override
    public void onReviewListFragmentInteraction(String result) {

    }

    /**
     * AsyncTask class used for connecting to database webservice.
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
         * Uses JSON string response fetched from webservice to parse into list object and pass to ListView.
         *
         * @param result String to be parsed.
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                Log.i("Response ", result);

                // Commented out JSONObject conversion, changed check and parseBreweryJson parameter to match generic String results
                //JSONObject resultObject = new JSONObject(result);
                if (result != null) {
                    mBrewery = parseSingleBreweryJson(result);
                }
                mDetailFragment = FavoritesDetailFragment.getFavoritesDetailFragment(mBrewery);

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
         * Parses JSON array into a List data structure containing custom Brewery objects.
         *
         * @param breweryJson JSON string search results returned from web service.
         * @return List containing Brewery objects.
         * @throws JSONException
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
