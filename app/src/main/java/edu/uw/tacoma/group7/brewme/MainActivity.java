/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/
package edu.uw.tacoma.group7.brewme;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import edu.uw.tacoma.group7.brewme.authenticate.RegisterDialogFragment;
import edu.uw.tacoma.group7.brewme.authenticate.SignInDialogFragment;
import edu.uw.tacoma.group7.brewme.data.SearchHistoryDB;

/**
 * MainActivity launches at start up of app, and is parent to login and register fragments.
 */
public class MainActivity extends AppCompatActivity
implements SignInDialogFragment.SignInListenerInterface,
        RegisterDialogFragment.RegisterListenerInterface {

    public static String EMAIL = "email";
    public static String PASSWORD = "password";
    public static String USERNAME = "username";
    public static String FIRST_NAME = "first";
    public static String LAST_NAME = "last";
    private SharedPreferences mSharedPreferences;
    private Button loginBtn;
    private Button logoutBtn;
    private Button mFavsButton;
    private String mEmail;
    private String mPassword;
    private JSONObject mArguments;
    private JSONObject mRegisterArguments;
    private SearchHistoryDB mSearchHistoryDB;


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

        mSearchHistoryDB = new SearchHistoryDB(this);
        //If History table has values from previous session, delete table
        if (!mSearchHistoryDB.isTableEmpty()) {
            mSearchHistoryDB.deleteHistory();   // New table created in SearchFieldFragment
        }

        loginBtn = (Button) findViewById(R.id.login_btn);
        logoutBtn = (Button) findViewById(R.id.logout_btn);
        mFavsButton = findViewById(R.id.angry_btn);

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

    /**
     * Called by the search button to launch the FavoritesActivity.
     * @param view a View object
     */
    public void launchFavoritesActivity(View view) {
        if (mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            Intent myIntent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(myIntent);
        } else {
            Toast.makeText(this, "Must be logged in to view favorites", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void login(String email, String pwd) {
            this.checkLoginInfo(email, pwd);
    }

    /**
     * Checks if register input text is complete (no fields missing) and passwords match.
     * @param firstName String.
     * @param lastName String.
     * @param userName String.
     * @param email String.
     * @param pwd String.
     * @param pwd2 String.
     */
    @Override
    public void register(String firstName, String lastName, String userName, String email, String pwd, String pwd2) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        RegisterDialogFragment registerDialogFragment = new RegisterDialogFragment();

        //Check if the passwords match.
        if(!pwd.equals(pwd2)) {
            Toast.makeText(this, "Password must match", Toast.LENGTH_SHORT)
                    .show();
            Bundle bundle = new Bundle();
            bundle.putString(RegisterDialogFragment.FIRST_NAME, firstName);
            bundle.putString(RegisterDialogFragment.LAST_NAME, lastName);
            bundle.putString(RegisterDialogFragment.REGISTER_USERNAME, userName);
            bundle.putString(RegisterDialogFragment.SIGN_IN_EMAIL, email);
            registerDialogFragment.setArguments(bundle);
            registerDialogFragment.show(fragmentTransaction, "Register");

            //Check if not all the fields are filled.
        } else if(firstName.equals("") || lastName.equals("") || userName.equals("") ||
                email.equals("") || pwd.equals("") || pwd2.equals("")) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT)
                    .show();
            Bundle bundle = new Bundle();
            bundle.putString(RegisterDialogFragment.FIRST_NAME, firstName);
            bundle.putString(RegisterDialogFragment.LAST_NAME, lastName);
            bundle.putString(RegisterDialogFragment.REGISTER_USERNAME, userName);
            bundle.putString(RegisterDialogFragment.SIGN_IN_EMAIL, email);
            registerDialogFragment.setArguments(bundle);
            registerDialogFragment.show(fragmentTransaction, "Register");
        } else {
            registerLoginInfo(firstName, lastName, userName, email, pwd);
            login(email, pwd);
        }
    }

    /**
     * Confirm that the email and password are in the online db.
     * @param email String.
     * @param password String.
     */
    private void checkLoginInfo(String email, String password) {
//        boolean b = false;
        StringBuilder url = new StringBuilder(getString(R.string.post_login));

        mEmail = email;
        mPassword = password;

        mArguments = new JSONObject();
        try {
            mArguments.put(EMAIL, email);
            mArguments.put(PASSWORD, password);
            new CheckLoginAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "error making json object: " + e.getMessage(),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Attempt to register a new account to the db.
     *
     */
    private void registerLoginInfo(String firstName, String lastName, String userName, String email,
                                   String password) {
        StringBuilder url = new StringBuilder(getString(R.string.post_register));
        mEmail = email;
        mPassword = password;
        mRegisterArguments = new JSONObject();
        try {
            mRegisterArguments.put(FIRST_NAME, firstName);
            mRegisterArguments.put(LAST_NAME, lastName);
            mRegisterArguments.put(USERNAME, userName);
            mRegisterArguments.put(EMAIL, email);
            mRegisterArguments.put(PASSWORD, password);
            new RegisterAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this,"error making json object: " + e.getMessage(),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Private class to access db and add a new account.
     */
    private class RegisterAsyncTask extends AsyncTask<String, Void, String> {
        private String Tag = "Register Tag";

        /**
         * Connects to registered user database, attempts to write to database with POST request.
         * @param urls database url endpoint.
         * @return String result of POST request.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for(String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    Log.i(Tag, mRegisterArguments.toString());
                    wr.write(mRegisterArguments.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while((s = buffer.readLine()) != null) {
                        response += s;
                    }


                } catch (MalformedURLException e) {
                    response = "Unable to reach database";
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        return response;
        }

        /**
         * Checks if adding user to registered user database was successful, displays Toast notification of status (Registered, Unable to register, or error code).
         * @param result JSON string returned from webservice.
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getBoolean("success") == true) {

                    Toast.makeText(getApplicationContext(), "Registered " + mEmail, Toast.LENGTH_SHORT)
                            .show();

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to register your account" +
                            resultObject.toString(), Toast.LENGTH_LONG)
                            .show();
                    Log.i("debug tag", resultObject.toString());

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Private class to access db and check for authenticity.
     */
    private class CheckLoginAsyncTask extends AsyncTask<String, Void, String> {

        private String Tag = "tag you're it";

        /**
         * Connects to registered user database to check if user exists.
         * @param urls database url endpoint.
         * @return String result of POST request.
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for(String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-type", "application/json");
                    urlConnection.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    Log.i(Tag, mArguments.toString());
                    wr.write(mArguments.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while((s = buffer.readLine()) != null) {
                        response += s;
                    }


                } catch (MalformedURLException e) {
                    response = "Unable to reach database";
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            }

            return response;
        }

        /**
         * Checks results of login attempt, saves login status to Shared Preferences.
         * @param result String.
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject resultObject = new JSONObject(result);
                if(resultObject.getBoolean("success") == true) {

                    mSharedPreferences
                            .edit()
                            .putBoolean(getString(R.string.LOGGEDIN), true)
                            .putString(getString(R.string.EMAIL), mEmail.toString())
                            .putString(getString(R.string.PASSWORD), mPassword.toString())
                            .commit();

                    loginBtn.setVisibility(Button.GONE);
                    logoutBtn.setVisibility(Button.VISIBLE);

                    Toast.makeText(getApplicationContext(), "Signed in as :" + mEmail, Toast.LENGTH_SHORT)
                            .show();

                } else {
                    Toast.makeText(getApplicationContext(), "Unable to sign in", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


}

