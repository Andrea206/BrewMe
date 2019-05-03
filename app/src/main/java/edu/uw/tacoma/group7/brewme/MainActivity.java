package edu.uw.tacoma.group7.brewme;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import org.json.JSONObject;

import edu.uw.tacoma.group7.brewme.model.Brewery;

public class MainActivity extends AppCompatActivity implements SearchListFragment.OnListFragmentInteractionListener, SearchDetailFragment.OnFragmentInteractionListener{

    private SearchDetailFragment mDetailFragment;
    private JSONObject mArguments;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.brewery_fragment_container, new SearchListFragment())
                .commit();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.brewery_fragment_container, new SearchListFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBrewListFragmentInteraction(Brewery item) {
//        mDetailFragment = SearchDetailFragment.getCourseDetailFragment(item);
//
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.brewery_fragment_container, mDetailFragment)
//                .addToBackStack(null);
//
//        transaction.commit();

    }
}
