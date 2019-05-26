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
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * SearchFieldFragment allows the user to choose what type of search and input search text.
 * Activities that contain this fragment must implement the
 * {@link SearchFieldFragment.OnSearchFieldFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchFieldFragment extends Fragment {

    private static final String SEARCH_FIELD_PARAM = "searchfieldparam";

    private String BY_CITY = "by_city";
    private String BY_STATE = "by_state";
    private String BY_NAME = "by_name";
    private static String mSearchText;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton;
    private Button mSearchButton;


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
        SearchFieldFragment fragment = new SearchFieldFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates view for search input that includes OnClickListener for search button. When search button
     * is clicked the input text from user and search type is captured in a Bundle object and passed to
     * a new SearchListFragment.
     * @param inflater LayoutInflater.
     * @param container ViewGroup.
     * @param savedInstanceState Bundle.
     * @return View object.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] exampleArray = {"example0", "example1", "example2", "example3", "example4"};


        final View view = inflater.inflate(R.layout.fragment_search_field, container, false);
        final AutoCompleteTextView searchInputTextView = view.findViewById(R.id.autoCompleteSearchInput);

        ArrayAdapter<String> searchHistoryAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, exampleArray);
        searchInputTextView.setAdapter(searchHistoryAdapter);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);
        floatingActionButton.hide();

        mSearchButton = view.findViewById(R.id.button);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText = searchInputTextView.getText().toString();

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
            }
        });
        return view;
    }

    /**
     * Passes uri object to OnSearchFieldFragmentInteractionListener.
     * @param uri Uri object.
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSearchFieldFragmentInteraction(uri);
        }
    }

    /**
     * Attaches required OnSearchFieldFragmentInteractionListener, throws error if listener not implemented.
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
     * @return search 'key' string.
     */
    private String getSearchKey() {
        String result;
        mRadioGroup = (RadioGroup) getActivity().findViewById(R.id.radio_group);
        int selectedId = mRadioGroup.getCheckedRadioButtonId();
        mRadioButton = (RadioButton) getActivity().findViewById(selectedId);
        String selectedText = mRadioButton.getText().toString();
        Log.e("Selected button", selectedText);
        if(selectedText.equals("Search by city")) {
            result = BY_CITY;
        } else if (selectedText.equals("Search by state")) {
            result = BY_STATE;
        } else {
            result = BY_NAME;
        }
        return result;
    }
}
