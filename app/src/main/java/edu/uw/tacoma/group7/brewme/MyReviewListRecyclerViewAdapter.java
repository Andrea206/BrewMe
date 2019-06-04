package edu.uw.tacoma.group7.brewme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.uw.tacoma.group7.brewme.ReviewListFragment.OnListFragmentInteractionListener;
import edu.uw.tacoma.group7.brewme.model.Review;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Review} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyReviewListRecyclerViewAdapter extends RecyclerView.Adapter<MyReviewListRecyclerViewAdapter.ViewHolder> {

    private final List<Review> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyReviewListRecyclerViewAdapter(List<Review> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_reviewlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getBreweryName());
        holder.mContentView.setText(mValues.get(position).getUsername());
//        holder.mContentView.setText(mValues.get(position).getTitle());
//        //***Rating bar goes here ***
//        holder.mContentView.setText(mValues.get(position).getReview());


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Review mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
