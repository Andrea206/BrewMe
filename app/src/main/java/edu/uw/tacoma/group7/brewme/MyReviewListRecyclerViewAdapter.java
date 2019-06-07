/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import edu.uw.tacoma.group7.brewme.ReviewListFragment.OnListFragmentInteractionListener;
import edu.uw.tacoma.group7.brewme.model.Review;
import java.util.List;

/**
 * MyReviewListRecyclerViewAdapter is used to create a list view of the reviews for a specific
 * Brewery, which is displayed in the ReviewListFragment {@link RecyclerView.Adapter} that can
 * display a {@link Review}.
 */
public class MyReviewListRecyclerViewAdapter extends RecyclerView.Adapter<MyReviewListRecyclerViewAdapter.ViewHolder> {

    private final List<Review> mValues;

    /**
     * Constructor for MyReviewListRecyclerViewAdapter, requires a List<Review> object and
     * OnListFragmentInteractionListener.
     *
     * @param items List<Review> object.
     * @param listener OnListFragmentInteractionListener.
     */
    public MyReviewListRecyclerViewAdapter(List<Review> items, OnListFragmentInteractionListener listener) {
        mValues = items;
    }

    /**
     * View holder method that calls parent view and returns created ViewHolder.
     *
     * @param parent ViewGroup.
     * @param viewType integer id.
     * @return ViewHolder.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reviewlist, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Populates the Viewholder with formatted list data.
     *
     * @param holder ViewHolder.
     * @param position integer value.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mBreweryName.setText(mValues.get(position).getBreweryName());
        holder.mReviewTitle.setText(mValues.get(position).getTitle());
        holder.mAuthorUsername.setText(mValues.get(position).getUsername());
        holder.mRatingBar.setRating((float) mValues.get(position).getRating());
        holder.mReviewContent.setText(mValues.get(position).getReview());

    }

    /**
     * Returns the number of items in review list object.
     *
     * @return integer.
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * ViewHolder class.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mBreweryName;
        public final TextView mReviewTitle;
        public final TextView mAuthorUsername;
        public final TextView mReviewContent;
        public final RatingBar mRatingBar;
        public Review mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mBreweryName = (TextView) view.findViewById(R.id.review_display_brewery_name);
            mReviewTitle = (TextView) view.findViewById(R.id.review_display_title);
            mAuthorUsername = (TextView) view.findViewById(R.id.review_display_author_username);
            mRatingBar = (RatingBar) view.findViewById(R.id.review_display_rating_bar);
            mReviewContent = (TextView) view.findViewById(R.id.review_display_review_content);


        }

        @Override
        public String toString() {
            return super.toString() + " '" + mBreweryName.getText() + " '" + mReviewTitle.getText() + "'" + " '" + mAuthorUsername.getText() + "'"
                    + " '" + mReviewContent.getText();
        }
    }
}
