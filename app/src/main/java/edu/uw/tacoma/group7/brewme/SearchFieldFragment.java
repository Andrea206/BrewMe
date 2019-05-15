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
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFieldFragment.OnSearchFieldFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFieldFragment#getSearchFieldFragment()} factory method to
 * create an instance of this fragment.
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
     *
     * @return A new instance of fragment SearchFieldFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFieldFragment getSearchFieldFragment() {
        SearchFieldFragment fragment = new SearchFieldFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_field, container, false);
        final TextView searchInputTextView = view.findViewById(R.id.editText);

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSearchFieldFragmentInteraction(uri);
        }
    }

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSearchFieldFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchFieldFragmentInteraction(Uri uri);
    }

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
