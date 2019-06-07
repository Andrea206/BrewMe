/*
TCSS450 Spring 2019
BrewMe app
Group 7: Gabriel Nieman, Andrea Moncada, James Schlaudraff
*/

package edu.uw.tacoma.group7.brewme.authenticate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import edu.uw.tacoma.group7.brewme.R;

/**
 * Dialog fragment that asks the users credentials. If the credentials are authorized, the user is
 * logged in.
 */
public class SignInDialogFragment extends DialogFragment {

    public static final String SIGN_IN_EMAIL = "email";

    private SignInListenerInterface mListener;

    /**
     * Dialog that pops up for users to log in. Email and password input boxes and
     * associated buttons with OnClickListeners are included in this method.
     * @param savedInstanceState Bundle.
     * @return Dialog object.
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final EditText emailText = new EditText(getActivity());
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailText.setHint(R.string.email_hint);
        Bundle bundle = getArguments();
        if(bundle != null) {
            String email = bundle.getString(SIGN_IN_EMAIL);
            emailText.setText(email);
        }

        final EditText pwdText = new EditText(getActivity());
        pwdText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwdText.setHint(R.string.pwd_hint);
        linearLayout.addView(emailText);
        linearLayout.addView(pwdText);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(linearLayout)
                .setMessage(R.string.login_message)
                .setPositiveButton(R.string.login, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener = (SignInListenerInterface) getActivity();
                        mListener.login(emailText.getText().toString(), pwdText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing here.
                    }
                })
                .setNeutralButton(R.string.register, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        RegisterDialogFragment registerDialogFragment = new RegisterDialogFragment();
                        registerDialogFragment.show(fragmentTransaction, "Register a new account");
                    }
                });
        return builder.create();
    }

    /**
     * Custom listener interface that passes the string parameters of a user's Email and Password.
     */
    public interface SignInListenerInterface {
        void login(String email, String pwd);
    }
}
