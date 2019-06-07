/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import java.util.ArrayList;
import edu.uw.tacoma.group7.brewme.data.SearchHistoryDB;

/**
 * SearchFieldFragment allows the user to choose what type of search and input search text.
 * Activities that contain this fragment must implement the
 * {@link SearchFieldFragment.OnSearchFieldFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFieldFragment extends Fragment {

    private static String mSearchText;
    private AutoCompleteTextView mAutoCompleteTextView;
    private SearchHistoryDB mSearchHistoryDB;

    private OnSearchFieldFragmentInteractionListener mListener;

    public SearchFieldFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SearchFieldFragment.
     */
    public static SearchFieldFragment getSearchFieldFragment() {
        return new SearchFieldFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates view for search input that includes OnClickListener for search button. When search button
     * is clicked the input text from user and search type is captured in a Bundle object and passed to
     * a new SearchListFragment.
     *
     * @param inflater LayoutInflater.
     * @param container ViewGroup.
     * @param savedInstanceState Bundle.
     * @return View object.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_search_field, container, false);
        mAutoCompleteTextView = view.findViewById(R.id.autoCompleteSearchInput);

        if(mSearchHistoryDB == null){
            mSearchHistoryDB = new SearchHistoryDB(getActivity());
        }

        //Attach search history and adapter only if Sqlite db has values present
        if(!mSearchHistoryDB.isTableEmpty()){
            ArrayList<String> mHistoryArrayList = mSearchHistoryDB.getHistory();
            //This adapter displays the search history  autocomplete text
            //**Must pass a String array to this adapter
            ArrayAdapter<String> searchHistoryAdapter = new ArrayAdapter<String>(getContext(),
                    android.R.layout.simple_list_item_1, getStringArray(mHistoryArrayList));
            mAutoCompleteTextView.setAdapter(searchHistoryAdapter);
        }

        //Currently not using this button
        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        Button mSearchButton = view.findViewById(R.id.button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText = mAutoCompleteTextView.getText().toString();

                if(mSearchHistoryDB == null){
                    mSearchHistoryDB = new SearchHistoryDB(getActivity());
                }
                mSearchHistoryDB.insertSearchHistory(mSearchText);
                //***Debugging***
                //Log.e("INSERT INTO HISTORY", mSearchHistoryDB.getHistory().toString());

                //Pass search input and search type to SearchListFragment
                Bundle bundle = new Bundle();
                bundle.putString("searchKey", getSearchKey());
                bundle.putString("searchValue", mSearchText);

                SearchListFragment searchListFragment = new SearchListFragment();
                searchListFragment.setArguments(bundle);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_search_container, searchListFragment)
                        .addToBackStack(null);
                transaction.commit();

                //Clear text view so text does not remain on back navigation
                mAutoCompleteTextView.setText("");
            }
        });
        return view;
    }

    /**
     * Passes uri object to OnSearchFieldFragmentInteractionListener.
     *
     * @param uri Uri object.
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSearchFieldFragmentInteraction(uri);
        }
    }

    /**
     * Attaches required OnSearchFieldFragmentInteractionListener, throws error if listener not implemented.
     *
     * @param context Context object.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchFieldFragmentInteractionListener) {
            mListener = (OnSearchFieldFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Sets OnSearchFieldFragmentInteractionListener to null when fragment is detached.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * Custom onSearchFieldFragmentInteraction, with Brewery item parameter implemented in this interface.
     */
    public interface OnSearchFieldFragmentInteractionListener {
        void onSearchFieldFragmentInteraction(Uri uri);
    }

    /**
     * Toggles the search type string needed to correctly format the GET statement passed to the webservice.
     * For example to search for a brewery by name the search key must be "by_name" in the GET statement.
     *
     * @return search 'key' string.
     */
    private String getSearchKey() {
        String result;
        RadioGroup mRadioGroup = (RadioGroup) getActivity().findViewById(R.id.radio_group);
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        RadioButton mRadioButton = (RadioButton) getActivity().findViewById(selectedId);
        String selectedText = mRadioButton.getText().toString();
        Log.e("Selected button", selectedText);
        if(selectedText.equals("Search by city")) {
            String BY_CITY = "by_city";
            result = BY_CITY;
        } else if (selectedText.equals("Search by state")) {
            String BY_STATE = "by_state";
            result = BY_STATE;
        } else {
            String BY_NAME = "by_name";
            result = BY_NAME;
        }
        return result;
    }

    private String[] getStringArray(ArrayList<String> searchHistoryList) {
        String[] arr = new String[searchHistoryList.size()];
        for (int i = 0; i < searchHistoryList.size(); i++) {
            arr[i] = searchHistoryList.get(i);
        }
        return arr;
    }
}
