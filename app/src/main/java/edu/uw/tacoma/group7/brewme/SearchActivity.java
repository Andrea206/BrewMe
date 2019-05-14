package edu.uw.tacoma.group7.brewme;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;

import edu.uw.tacoma.group7.brewme.model.Brewery;

public class SearchActivity extends AppCompatActivity implements Serializable, View.OnClickListener, SearchListFragment.OnListFragmentInteractionListener,
SearchDetailFragment.OnFragmentInteractionListener{

    private String BY_CITY = "by_city";
    private String BY_STATE = "by_state";
    private String BY_NAME = "by_name";
    private static String mSearchText;
    private SearchListFragment.OnListFragmentInteractionListener mListener;
    private SearchListFragment mSearchListFragment;
    private SearchDetailFragment mDetailFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final TextView searchInputTextView = findViewById(R.id.editText);
        Button searchButton = findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchText = searchInputTextView.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("searchKey", "by_state");
                bundle.putString("searchValue", mSearchText);

                mSearchListFragment = new SearchListFragment();
                mSearchListFragment.setArguments(bundle);

                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.search_main, mSearchListFragment)
                        .addToBackStack(null);

                transaction.commit();
            }
        });

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onBrewListFragmentInteraction(Brewery item) {
        mDetailFragment = SearchDetailFragment.getSearchDetailFragment(item);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.search_main, mDetailFragment)
                .addToBackStack(null);

        transaction.commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}//end SearchActivity
