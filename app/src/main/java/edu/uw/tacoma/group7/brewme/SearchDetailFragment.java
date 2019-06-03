/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/
package edu.uw.tacoma.group7.brewme;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import edu.uw.tacoma.group7.brewme.model.Brewery;
import edu.uw.tacoma.group7.brewme.model.Review;

/**
 * SearchDetailFragment displays extended information about a brewery that is selected
 * from the search results list.
 * Activities that contain this fragment must implement the
 * {@link SearchDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchDetailFragment extends Fragment {

    private static final String BREWERY_DETAILS_PARAM = "brewerydetailsparam";

    private Brewery mBrewery;
    private ImageView mBreweryImage;
    private TextView mDescription;
    private Button mWriteReviewButton;
    private Button mGoogleMapButton;
    private Button mCheckInButton;
    private Button mUserReviewsButton;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences mSharedPreferences;
    private List<Review> mReviewList;


    public SearchDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * SearchDetailFragment using the provided parameters.
     *
     * @param param a Brewery object.
     * @return new instance of fragment SearchDetailFragment.
     */
    public static SearchDetailFragment getSearchDetailFragment(Brewery param) {
        SearchDetailFragment fragment = new SearchDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(BREWERY_DETAILS_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Retrieves Brewery object selected from SearchListFragment.
     * @param savedInstanceState Bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBrewery = (Brewery) getArguments().getSerializable(BREWERY_DETAILS_PARAM);
            mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        }
    }

    /**
     * Creates view of formatted Brewery information text, and links to more features such as
     * launching a map of the brewery.
     * @param inflater LayoutInflater.
     * @param container ViewGroup.
     * @param savedInstanceState Bundle.
     * @return View object.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_detail, container, false);
        mDescription = view.findViewById(R.id.brewery_description);
        String type = mBrewery.getBreweryType();
        String capType = type.substring(0, 1).toUpperCase() + type.substring(1);
        String phone = mBrewery.getPhone();
        String phoneFormatted = formatPhoneNumber(phone);
        mDescription.setText(mBrewery.getName() + "\n" +  "Type: " + capType +
                "\n" + mBrewery.getStreet() + " " + mBrewery.getCity() +
                ", " + mBrewery.getState() + "\n" + phoneFormatted +
                "\n" + mBrewery.getWebsite());
        mGoogleMapButton = view.findViewById(R.id.google_maps_button);
        mGoogleMapButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String uri = "geo:0,0?q=" + mBrewery.getName() + "+" + mBrewery.getCity()
                        + "+" + mBrewery.getState();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                getContext().startActivity(intent);
            }
        });

        Log.e("Url params: ", mBrewery.getBreweryId() + mSharedPreferences.getString("Email", null));
        new DownloadReviews().execute("https://jamess33-services-backend.herokuapp.com/reviews/reviewsId?brewery_id=" +
                mBrewery.getBreweryId() + "&username=" + mSharedPreferences.getString("Email", null));


        Button writeReviewButton = view.findViewById(R.id.write_review_button);
        writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSharedPreferences.getBoolean("loggedin", true)) {
                    Intent myIntent = new Intent(getActivity(), ReviewActivity.class);
                    myIntent.putExtra("ReviewBrewery", mBrewery);
                    startActivity(myIntent);
                }
                else{
                    Toast.makeText(getActivity(), "Must be logged in to write a review", Toast.LENGTH_SHORT)
                            .show();
                }
            }


        });
        return view;
    }

    private String formatPhoneNumber(String num) {
        String result;
        if(num.isEmpty()) {
            result = "No Phone Number Listed";
        } else {
            result = "(" + num.substring(0, 3) + ") " + num.substring(3, 6) +
                    "-" + num.substring(6);
        }
        return result;
    }

    /**
     * Passes uri object to onFragmentInteraction.
     * @param uri Uri object.
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /**
     * Attaches required OnFragmentInteractionListener, throws error if listener not implemented.
     * @param context Context object.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Sets OnFragmentInteractionListener to null on detach.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        //void launchUserReviews(View view);
        void onFragmentInteraction(Uri uri);
    }




    /**
     * AsyncTask class used for connecting to database webservice.
     */
    private class DownloadReviews extends AsyncTask<String, Void, String> {

        //private ProgressBar mProgressBar;

//        @Override
//        protected void onProgressUpdate(Void... progress) {
//            mProgressBar = getActivity().findViewById(R.id.progressBar);
//            mProgressBar.setVisibility(View.VISIBLE);
//            mProgressBar.getProgress();
//        }

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
                    response = "Unable to download review, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * Uses JSON string response fetched from webservice to parse into list object.
         * @param result String to be parsed.
         */
        @Override
        protected void onPostExecute(String result){
            //mProgressBar.setVisibility(View.GONE);
            try{
                Log.e("Response ", result);

                // Commented out JSONObject conversion, changed check and parseBreweryJson parameter to match generic String results
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getBoolean("success") == true){
                    mReviewList = Review.parseReviewJson(resultObject.getString("names"));

                    if(!mReviewList.isEmpty()){
                        Toast.makeText(getActivity(), mReviewList.get(0).getBreweryName(), Toast.LENGTH_SHORT)
                        .show();
                        Log.e("mReviewList: ", mReviewList.toString());
                    }

                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "No reviews found", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }//end DownloadBrewSearch



}
