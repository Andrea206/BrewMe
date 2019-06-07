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
import android.widget.TextView;
import java.util.List;
import edu.uw.tacoma.group7.brewme.model.Brewery;

/**
 * MyFavoritesListRecyclerViewAdapter is used to create a list view of the user's favorite Breweries,
 * which is displayed in the FavoritesListFragment {@link RecyclerView.Adapter} that can display a
 * {@link Brewery} and makes a call to the specified
 * {@link edu.uw.tacoma.group7.brewme.FavoritesListFragment.OnFavoritesListFragmentInteractionListener}.
 */
public class MyFavoritesListRecyclerViewAdapter extends
        RecyclerView.Adapter<MyFavoritesListRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final FavoritesListFragment.OnFavoritesListFragmentInteractionListener mListener;

    /**
     * Constructor for MyFavoritesListRecyclerViewAdapter, requires a List<String> object and
     * OnFavoritesListFragmentInteractionListener.
     *
     * @param items List<String> object.
     * @param listener OnFavoritesListFragmentInteractionListener.
     */
    public MyFavoritesListRecyclerViewAdapter(List<String> items,
           FavoritesListFragment.OnFavoritesListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
                .inflate(R.layout.fragment_favoriteslist, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Populates the Viewholder with formatted list data, onClickListener listens for item clicks.
     *
     * @param holder ViewHolder.
     * @param position integer value.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onFavoritesListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Returns the number of items in brewery list object.
     *
     * @return integer.
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public String mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.fav_name);
        }

    }
}
