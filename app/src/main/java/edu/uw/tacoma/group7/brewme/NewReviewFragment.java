/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import edu.uw.tacoma.group7.brewme.model.Review;

/**
 * NewReviewFragment takes the review input from the user and puts it in a database.
 * Activities that contain this fragment must implement the
 * {@link NewReviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewReviewFragment extends Fragment {

    private static final String BREWERY_ID_PARAM = "breweryId";
    private static final String BREWERY_NAME_PARAM = "breweryName";

    private String mBreweryId;
    private String mBreweryName;
    private Review mReview;
    private SharedPreferences mSharedPreferences;

    private OnFragmentInteractionListener mListener;


    public NewReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment NewReviewFragment.
     */
    public static NewReviewFragment newInstance() {
        NewReviewFragment fragment = new NewReviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Do nothing
        }
    }

    /**
     * Creates view for user to write a new review.
     * @param inflater LayoutInflater.
     * @param container ViewGroup.
     * @param savedInstanceState Bundle.
     * @return View object.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_new_review, container, false);
        mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        Bundle bundle = this.getArguments();
        mBreweryId = bundle.getString(BREWERY_ID_PARAM);
        mBreweryName = bundle.getString(BREWERY_NAME_PARAM);
        Log.e("mBreweryName: ", mBreweryId);
        TextView mBreweryNameTextView = view.findViewById(R.id.brewery_name_display);
        mBreweryNameTextView.setText(mBreweryName);
        final EditText reviewTitle = view.findViewById(R.id.title_edit_text);
        final RatingBar reviewRating = view.findViewById(R.id.rating_bar);
        final EditText review = view.findViewById(R.id.review_edit_text);
        Button postButton = view.findViewById(R.id.post_review_button);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Put review values into Review object to pass into webservice interaction
                mReview = new Review(Integer.parseInt(mBreweryId), mBreweryName,
                        mSharedPreferences.getString("Email", null), reviewTitle.getText().toString(),
                        (double)reviewRating.getRating(), review.getText().toString());
                // *** Debugging ***
                //Log.e("Review object: ", mReview.getReview());
                mListener.onNewReviewFragmentInteraction(mReview);
            }
        });
        return view;


    }

    public void onButtonPressed(Review review) {
        if (mListener != null) {
            mListener.onNewReviewFragmentInteraction(review);
        }
    }

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
     * onNewReviewFragmentInteraction accepts a Review object.
     */
    public interface OnFragmentInteractionListener {
        void onNewReviewFragmentInteraction(Review review);
    }
}
