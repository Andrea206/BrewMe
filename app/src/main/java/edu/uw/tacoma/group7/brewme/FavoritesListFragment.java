/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A fragment representing a list of favorite Breweries.
 * Activities containing this fragment MUST implement the {@link OnFavoritesListFragmentInteractionListener}
 * interface.
 */
public class FavoritesListFragment extends Fragment {

    public HashMap<Integer, String> myFavs;

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private OnFavoritesListFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavoritesListFragment() {
        // Do nothing
    }

    @SuppressWarnings("unused")
    public static FavoritesListFragment newInstance(int columnCount) {
        FavoritesListFragment fragment = new FavoritesListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favoriteslist_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            SharedPreferences sp = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            String email = sp.getString(getString(R.string.EMAIL), null);
            new DownloadFavoritesTask().execute(getString(R.string.get_favorites) + email);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFavoritesListFragmentInteractionListener) {
            mListener = (OnFavoritesListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFavoritesListFragmentInteractionListener {
        void onFavoritesListFragmentInteraction(String brewery);
    }

    /**
     * Returns a map of Brewery Ids mapped to that Brewery's name.
     *
     * @return a map
     */
    public HashMap<Integer, String> getFavorites() {
        return myFavs;
    }

    /**
     * Downloads a list of favorite Breweries associated with a specific user from a database
     * hosted in the cloud.
     */
    private class DownloadFavoritesTask extends AsyncTask<String, Void, String> {

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
                    publishProgress();
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of favorites, Reason: "
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
                if (resultObject.getBoolean("success")) {
                    // get brewery_ids from returned JSON
                    myFavs = parseFavoritesJSON(resultObject.getString("names"));
                    //Log.i("Brewery id from favorites table", mFavsIds.get(0).toString());
                    List favsList = new ArrayList(myFavs.values());
                    if (!myFavs.isEmpty()) {
                        mRecyclerView.setAdapter(new MyFavoritesListRecyclerViewAdapter(favsList, mListener));
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Error retrieving favorites from database", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        private HashMap<Integer, String> parseFavoritesJSON(String favsJson) throws JSONException {
            HashMap<Integer, String> favs = new HashMap<>();
            if (favsJson != null) {
                JSONArray arr = new JSONArray(favsJson);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    favs.put(obj.getInt(SearchActivity.BREWERY_ID), obj.getString(SearchActivity.BREWERY_NAME));
                }
            }
            return favs;
        }
    }
}
