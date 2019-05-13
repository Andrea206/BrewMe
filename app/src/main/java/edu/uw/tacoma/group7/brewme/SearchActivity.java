package edu.uw.tacoma.group7.brewme;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;

import edu.uw.tacoma.group7.brewme.model.Brewery;

public class SearchActivity extends AppCompatActivity implements Serializable, View.OnClickListener, SearchListFragment.OnListFragmentInteractionListener {

    private String BY_CITY = "by_city";
    private String BY_STATE = "by_state";
    private String BY_NAME = "by_name";
    private static String mBrewText;
    private SearchListFragment.OnListFragmentInteractionListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText searchIput = findViewById(R.id.editText);
        final Button searchText = findViewById(R.id.button);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBrewText = searchText.getText().toString();
                mListener.onBrewListFragmentInteraction("by_state", mBrewText);
            }
        });



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


//        public void onButtonClick(String searchInput, String searchType) {
//            if (mListener != null) {
//                mListener.onFragmentInteraction(
//
//                );
//            }
//        }

    }




    @Override
    public void onClick(View v) {

    }


    @Override
    public void onBrewListFragmentInteraction(String search_key, String search_value) {
        
    }
}
