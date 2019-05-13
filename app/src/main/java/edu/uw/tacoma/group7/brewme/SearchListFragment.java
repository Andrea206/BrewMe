package edu.uw.tacoma.group7.brewme;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import org.json.JSONException;
import edu.uw.tacoma.group7.brewme.model.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * A fragment representing a list of Brewery search results.
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class SearchListFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private List<Brewery> mBrewList;
    private RecyclerView mRecyclerview;
    private String mKey;
    private String mValue;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SearchListFragment() {
    }

    @SuppressWarnings("unused")
    public static SearchListFragment newInstance(int columnCount) {
        SearchListFragment fragment = new SearchListFragment();
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
        View view = inflater.inflate(R.layout.fragment_searchlist_list, container, false);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);

        floatingActionButton.show();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerview = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerview.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerview.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            //Hard coding query until can pass values
            mKey="by_state";
            mValue="washington";
            new DownloadBrewSearch().execute("https://api.openbrewerydb.org/breweries?" + mKey + "=" + mValue );
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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
    public interface OnListFragmentInteractionListener {
        //void onBrewListFragmentInteraction(Brewery item);
        void onBrewListFragmentInteraction(String search_key, String search_value);

    }

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
                    response = "Unable to download the list of courses, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        @Override
        protected void onPostExecute(String result){
            try{
                Log.i("Response ", result);

                // Commented out JSONObject conversion, changed check and parseBreweryJson parameter to match generic String results
                //JSONObject resultObject = new JSONObject(result);
                if(result != null){
                    mBrewList = Brewery.parseBreweryJson(result);

                    //Show list
                    if(!mBrewList.isEmpty()){
                        mRecyclerview.setAdapter(new MySearchListRecyclerViewAdapter(mBrewList,mListener));
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                Log.d("onPostExecute error: ", e.getMessage(), new Throwable());

            }
        }
    }//end DownloadBrewSearch


}//end SearchListFragment
