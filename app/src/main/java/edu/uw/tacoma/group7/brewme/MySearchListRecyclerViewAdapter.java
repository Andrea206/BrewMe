package edu.uw.tacoma.group7.brewme;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import edu.uw.tacoma.group7.brewme.SearchListFragment.OnListFragmentInteractionListener;
import edu.uw.tacoma.group7.brewme.model.*;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Brewery} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MySearchListRecyclerViewAdapter extends RecyclerView.Adapter<MySearchListRecyclerViewAdapter.ViewHolder> {

    private final List<Brewery> mValues;
    private final OnListFragmentInteractionListener mListener;

    /**
     * Constructor for MySearchListRecyclerViewAdapter, requires a List<Brewery> object and OnListFragmentInteractionListener.
     * @param items List<Brewery> object.
     * @param listener OnListFragmentInteractionListener.
     */
    public MySearchListRecyclerViewAdapter(List<Brewery> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    /**
     * View holder method that calls parent view and returns created ViewHolder.
     * @param parent ViewGroup.
     * @param viewType integer id.
     * @return ViewHolder.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_brewery, parent, false);
        return new ViewHolder(view);
    }


    /**
     * Populates the Viewholder with formatted list data, onClickListener listens for item clicks.
     * @param holder ViewHolder.
     * @param position integer value.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
        holder.mContentView.setText(mValues.get(position).getCity());
        //holder.mContentView.setText(mValues.get(position).getState());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onBrewListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Returns the number of items in brewery list object.
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
        public final TextView mIdView;
        public final TextView mContentView;
        public Brewery mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.name);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
