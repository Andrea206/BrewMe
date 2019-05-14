package edu.uw.tacoma.group7.brewme;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import edu.uw.tacoma.group7.brewme.authenticate.SignInDialogFragment;

public class MainActivity extends AppCompatActivity
        implements SignInDialogFragment.SignInListenerInterface {

<<<<<<< HEAD
public class MainActivity extends AppCompatActivity
        implements SearchListFragment.OnListFragmentInteractionListener,
        SearchDetailFragment.OnFragmentInteractionListener{

    private SearchDetailFragment mDetailFragment;
    private JSONObject mArguments;
=======
    private SharedPreferences mSharedPreferences;
    private Button loginBtn;
    private Button logoutBtn;
>>>>>>> origin/master


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginBtn = (Button) findViewById(R.id.login_btn);
        logoutBtn = (Button) findViewById(R.id.logout_btn);

        //Automatically log in if prefs are saved to device.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        if(mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            login(mSharedPreferences.getString(getString(R.string.EMAIL), null),
                    mSharedPreferences.getString(getString(R.string.PASSWORD), null));
        }
    }

    public void onClickLoginBtn(View v) {
        //Launch the login dialog.
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SignInDialogFragment signInDialogFragment = new SignInDialogFragment();
        signInDialogFragment.show(fragmentTransaction, "Sign in");
    }

    public void onClickLogoutBtn(View v) {
        //TODO logout of your account.
        mSharedPreferences.edit()
                .putBoolean(getString(R.string.LOGGEDIN), false)
                .putString(getString(R.string.EMAIL), null)
                .putString(getString(R.string.PASSWORD), null)
                .commit();

        logoutBtn.setVisibility(Button.GONE);
        loginBtn.setVisibility(Button.VISIBLE);
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
    }

    public void launchSearchActivity(View view){
        Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(myIntent);
    }

    @Override
<<<<<<< HEAD
    public void onBrewListFragmentInteraction(Brewery item) {
        mDetailFragment = SearchDetailFragment.getSearchDetailFragment(item);

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.brewery_fragment_container, mDetailFragment)
                .addToBackStack(null);

        transaction.commit();
=======
    public void login(String email, String pwd) {
        boolean isLoggedIn = false;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SignInDialogFragment signInDialogFragment = new SignInDialogFragment();
        //TODO compare email with database of emails.
        if (TextUtils.isEmpty(email) || !email.equals("a@m.com")) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT)
                    .show();
            signInDialogFragment.show(fragmentTransaction, "Sign in");

            //TODO compare email with database password. Include hashing.
        } else if (TextUtils.isEmpty(pwd) || !pwd.equals("abc123")) {
            Bundle bundle = new Bundle();
            bundle.putString(SignInDialogFragment.SIGN_IN_EMAIL, email);
            signInDialogFragment.setArguments(bundle);
>>>>>>> origin/master

            signInDialogFragment.setArguments(bundle);
            Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT)
                    .show();
            signInDialogFragment.show(fragmentTransaction, "Sign in");
        } else {
            mSharedPreferences
                    .edit()
                    .putBoolean(getString(R.string.LOGGEDIN), true)
                    .putString(getString(R.string.EMAIL), email.toString())
                    .putString(getString(R.string.PASSWORD), pwd.toString())
                    .commit();

            loginBtn.setVisibility(Button.GONE);
            logoutBtn.setVisibility(Button.VISIBLE);

            isLoggedIn = true;
            Toast.makeText(this, "Signed in as: " + email.toString(), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}

