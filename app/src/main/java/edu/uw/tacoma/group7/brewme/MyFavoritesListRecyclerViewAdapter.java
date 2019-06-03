package edu.uw.tacoma.group7.brewme;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;
import java.util.List;

import edu.uw.tacoma.group7.brewme.model.Brewery;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Brewery} and makes a call to the
 * specified {@link FavoritesListFragment.OnFavoritesListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFavoritesListRecyclerViewAdapter extends RecyclerView.Adapter<MyFavoritesListRecyclerViewAdapter.ViewHolder> {

    private final List<String> mValues;
    private final FavoritesListFragment.OnFavoritesListFragmentInteractionListener mListener;

    public MyFavoritesListRecyclerViewAdapter(List<String> items, FavoritesListFragment.OnFavoritesListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_favoriteslist, parent, false);
        return new ViewHolder(view);
    }

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
