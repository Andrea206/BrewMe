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

    private SharedPreferences mSharedPreferences;
    private Button loginBtn;
    private Button logoutBtn;


    /**
     * Checks for login, automatically logs in using SharedPreferences.
     * @param savedInstanceState a Bundle object
     */
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

    /**
     * Launches the login dialog.
     * @param v a View object
     */
    public void onClickLoginBtn(View v) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SignInDialogFragment signInDialogFragment = new SignInDialogFragment();
        signInDialogFragment.show(fragmentTransaction, "Sign in");
    }

    /**
     * Logs the user out of the application.
     * @param v a View object
     */
    public void onClickLogoutBtn(View v) {
        mSharedPreferences.edit()
                .putBoolean(getString(R.string.LOGGEDIN), false)
                .putString(getString(R.string.EMAIL), null)
                .putString(getString(R.string.PASSWORD), null)
                .commit();

        logoutBtn.setVisibility(Button.GONE);
        loginBtn.setVisibility(Button.VISIBLE);
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called by the search button to launch the SearchActivity.
     * @param view a View object
     */
    public void launchSearchActivity(View view){
        Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(myIntent);
    }

    public void login(String email, String pwd) {
        boolean isLoggedIn = false;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SignInDialogFragment signInDialogFragment = new SignInDialogFragment();
        if (TextUtils.isEmpty(email) || !email.equals("a@m.com")) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT)
                    .show();
            signInDialogFragment.show(fragmentTransaction, "Sign in");

        } else if (TextUtils.isEmpty(pwd) || !pwd.equals("abc123")) {
            Bundle bundle = new Bundle();
            bundle.putString(SignInDialogFragment.SIGN_IN_EMAIL, email);

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

