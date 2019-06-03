package edu.uw.tacoma.group7.brewme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FavoritesActivity extends AppCompatActivity implements FavoritesListFragment.OnFavoritesListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_favorites_container, new FavoritesListFragment())
                .commit();
    }

    @Override
    public void onFavoritesListFragmentInteraction(Integer brewery) {

    }
}
