package edu.uw.tacoma.group7.brewme.authenticate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import edu.uw.tacoma.group7.brewme.R;

/**
 * Dialog fragment that requests the users information to register a new user.
 * Once the user has registered, they're account will be stored online and they will be logged in
 * on the device.
 */
public class RegisterDialogFragment extends DialogFragment {

    private RegisterListenerInterface mListener;
    public static final String SIGN_IN_EMAIL = "email";
    public static final String REGISTER_USERNAME = "Username";
    public static final String FIRST_NAME = "first";
    public static final String LAST_NAME = "last";

    @NonNull
    @Override
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText firstNameText = new EditText(getActivity());
        firstNameText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        firstNameText.setHint(R.string.first_name_hint);

        final EditText lastNameText = new EditText(getActivity());
        lastNameText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        lastNameText.setHint(R.string.last_name_hint);

        final EditText usernameText = new EditText(getActivity());
        usernameText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        usernameText.setHint(R.string.USERNAME);

        final EditText emailText = new EditText(getActivity());
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailText.setHint(R.string.email_hint);
        Bundle bundle = getArguments();
        if(bundle != null) {
            String firstName = bundle.getString(FIRST_NAME);
            String lastName = bundle.getString(LAST_NAME);
            String email = bundle.getString(SIGN_IN_EMAIL);
            String username = bundle.getString(REGISTER_USERNAME);
            firstNameText.setText(firstName);
            lastNameText.setText(lastName);
            emailText.setText(email);
            usernameText.setText(username);
        }

        final EditText pwdText = new EditText(getActivity());
        pwdText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwdText.setHint(R.string.pwd_hint);

        final EditText pwdReenterText = new EditText(getActivity());
        pwdReenterText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwdReenterText.setHint(R.string.pwd_reenter_hint);

        linearLayout.addView(firstNameText);
        linearLayout.addView(lastNameText);
        linearLayout.addView(usernameText);
        linearLayout.addView(emailText);
        linearLayout.addView(pwdText);
        linearLayout.addView(pwdReenterText);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(linearLayout)
                .setMessage(R.string.register_message)
                .setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener = (RegisterListenerInterface) getActivity();
                        mListener.register(firstNameText.getText().toString(),
                                lastNameText.getText().toString(),
                                usernameText.getText().toString(),
                                emailText.getText().toString(),
                                pwdText.getText().toString(),
                                pwdReenterText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nothing here.
                    }
                });
        return builder.create();
    }

    public interface RegisterListenerInterface {
        void register(String firstName, String lastName, String userName, String email, String pwd, String pwd2);
    }
}
