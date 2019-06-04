/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
 * {@link SearchDetailFragment.OnAddToFavoritesFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchDetailFragment extends Fragment{

    private static final String BREWERY_DETAILS_PARAM = "brewerydetailsparam";

    private Brewery mBrewery;
    private ImageView mBreweryImage;
    private TextView mDescription;
    private Button mWriteReviewButton;
    private Button mGoogleMapButton;
    private Button mShareButton;
    private FloatingActionButton mAddToFavsButton;
    private Button mUserReviewsButton;
    private SharedPreferences mSharedPreferences;
    private List<Review> mReviewList;
    private String mContactNumber;

    private final int PICK_CONTACT = 1;
    private final int REQUEST_READ_CONTACTS = 2;
    private final int REQUEST_SEND_SMS = 3;

    private OnAddToFavoritesFragmentInteractionListener mListener;
    private ReviewListFragment.OnListFragmentInteractionListener mReviewListener;



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

        mUserReviewsButton = view.findViewById(R.id.user_reviews_button);
        mUserReviewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadReviews().execute("https://jamess33-services-backend.herokuapp.com/reviews/reviewsId?brewery_id=" +
                        mBrewery.getBreweryId());
            }
        });


        mShareButton = view.findViewById(R.id.share_button);
        mShareButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                        android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);
                } else if(ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_CONTACTS)
                        == PackageManager.PERMISSION_DENIED) {
                    requestReadContactsPermission();

                } else if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_DENIED) {
                    requestSendSMSPermission();
                }
            }
        });
        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mListener.onAddToFavoritesFragmentInteraction(mBrewery.getBreweryId(), mBrewery.getName(), mBrewery.getCity(), mBrewery.getState());
            }
        });
        fab.setImageResource(R.drawable.add_to_favorites_icon);
        fab.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        SharedPreferences sp = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        if(sp.getBoolean(getString(R.string.LOGGEDIN), false)) {
            fab.show();
        } else {
            fab.hide();
        }

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

    /**
     * Opens a window requesting permissions to send SMS messages.
     */
    protected void requestReadContactsPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_CONTACTS},
                REQUEST_READ_CONTACTS);
    }

    /**
     * Opens a window requesting permissions to send SMS messages.
     */
    protected void requestSendSMSPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},
                REQUEST_SEND_SMS);
    }

    /**
     * Listens for the results of all permission requests.
     *
     * @param requestCode a number identifying which permission request was returned
     * @param permissions an array of Strings listing permissions
     * @param grantResults an array of numbers detailing the results of the request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);

                }
                return;
            }

            case REQUEST_SEND_SMS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);

                }
                return;
            }

        }
    }

    /**
     * Grabs the contact's phone number from the data returned by the previous Intent and then
     * prepares an SMS message with the details about the brewery to share and send an SMS message
     * to the contact that was chosen.
     *
     * @param requestCode a number identifying which Intent returned
     * @param resultCode a number identifying whether the actions was successful or not
     * @param data the data returned from the Intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_CONTACT){
            if(resultCode==Activity.RESULT_OK){

                Uri uri = data.getData();
                ContentResolver contentResolver = getActivity().getContentResolver();
                Cursor contentCursor = contentResolver.query(uri, null, null,null, null);

                if(contentCursor.moveToFirst()){
                    String id = contentCursor.getString(contentCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone =
                            contentCursor.getString(contentCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1"))
                    {
                        Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                        phones.moveToFirst();
                        mContactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Log.i("phoneNUmber", "The phone number is "+ mContactNumber);

                    } else {
                        mContactNumber = "";
                    }
                }
                super.onActivityResult(requestCode, resultCode, data);
            }
            try {
//                String message = "https://www.google.com/maps/search/?api=1&query="
//                        + mBrewery.getName() + "%2C" + mBrewery.getCity() + "%2C"
//                        + mBrewery.getState();
                String mapLink = "https://www.google.com/maps/search/?api=1&query="
                        + mBrewery.getName() + "%2C" + mBrewery.getCity() + "%2C"
                        + mBrewery.getState();
                mapLink = mapLink.replaceAll(" ", "%20");
                String message = "Check out this brewery I found...\n" + mBrewery.getName() +"\n"
                                 + mBrewery.getWebsite();

                SmsManager.getDefault().sendTextMessage(mContactNumber, null,
                        message,null, null);
                SmsManager.getDefault().sendTextMessage(mContactNumber, null,
                        mapLink,null, null);
                Toast.makeText(getContext(), "Text Sent Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(getContext(), "There was an error sending the text.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Formats a String of ten numbers into a nicely formatted String to print.
     *
     * @param num the phone number
     * @return a String representation of a phone number
     */
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
            mListener.onAddToFavoritesFragmentInteraction(null, null, null, null);
        }
    }

    /**
     * Attaches required OnFragmentInteractionListener, throws error if listener not implemented.
     * @param context Context object.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddToFavoritesFragmentInteractionListener) {
            mListener = (OnAddToFavoritesFragmentInteractionListener) context;
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


    public interface OnAddToFavoritesFragmentInteractionListener {
        void onAddToFavoritesFragmentInteraction(String id, String name, String city, String state);
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

//            Toast.makeText(getActivity(), result, Toast.LENGTH_LONG)
//                    .show();

            JSONObject resultObject = null;
            try {
                resultObject = new JSONObject(result);
                if(resultObject.getJSONArray("names").length() == 0 || resultObject.getJSONArray("names") == null
                || resultObject.getBoolean("success") == false){
                    Toast.makeText(getActivity(), "No review for this brewery yet!", Toast.LENGTH_LONG)
                            .show();
                }
                else {
                //Pass search input and search type to SearchListFragment
                Bundle bundle = new Bundle();
                bundle.putString("ReviewList", result);
                ReviewListFragment reviewListFragment = new ReviewListFragment();
                reviewListFragment.setArguments(bundle);

        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_search_container, reviewListFragment)
                .addToBackStack(null);
        transaction.commit();

                    //mReviewListener.onReviewListFragmentInteraction(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }//end DownloadBrewSearch



}


//            try{
////                Log.e("Response ", result);
////
////                // Commented out JSONObject conversion, changed check and parseBreweryJson parameter to match generic String results
////                JSONObject resultObject = new JSONObject(result);
////                if(resultObject.getBoolean("success") == true){
////                    mReviewList = Review.parseReviewJson(resultObject.getString("names"))
////
////                }
////            } catch (JSONException e) {
////                Toast.makeText(getContext(), "No reviews found", Toast.LENGTH_LONG)
////                        .show();
////            }