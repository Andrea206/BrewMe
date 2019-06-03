/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/
package edu.uw.tacoma.group7.brewme;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import edu.uw.tacoma.group7.brewme.model.Brewery;

/**
 * SearchDetailFragment displays extended information about a brewery that is selected
 * from the search results list.
 * Activities that contain this fragment must implement the
 * {@link SearchDetailFragment.OnAddToFavoritesFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchDetailFragment extends Fragment {

    private static final String BREWERY_DETAILS_PARAM = "brewerydetailsparam";

    private Brewery mBrewery;

    private ImageView mBreweryImage;
    private TextView mDescription;
    private Button mWriteReviewButton;
    private Button mGoogleMapButton;
    private Button mShareButton;
    private FloatingActionButton mAddToFavsButton;
    private Button mUserReviewsButton;
    private String mContactNumber;

    private final int PICK_CONTACT = 1;
    private final int REQUEST_READ_CONTACTS = 2;
    private final int REQUEST_SEND_SMS = 3;

    private OnAddToFavoritesFragmentInteractionListener mListener;

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
//        String phoneFormatted = "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) +
//                                "-" + phone.substring(6);
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
                mListener.onAddToFavoritesFragmentInteraction("14", mBrewery.getName(), mBrewery.getCity(), mBrewery.getState());
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
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_CONTACTS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);

                } else {

                    // permission denied,Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case REQUEST_SEND_SMS: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                    startActivityForResult(intent, PICK_CONTACT);

                } else {

                    // permission denied,Disable the
                    // functionality that depends on this permission.
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
}
