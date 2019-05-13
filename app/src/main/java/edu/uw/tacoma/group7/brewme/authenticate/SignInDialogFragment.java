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

public class SignInDialogFragment extends DialogFragment {

    private SignInListenerInterface mListener;
    public static final String SIGN_IN_EMAIL = "email";

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
                        //TODO login to your account
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
                        //TODO register a new account.
                        Toast.makeText(getContext(), "Not yet implemented.", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        return builder.create();
    }

    public interface SignInListenerInterface {
        void login(String email, String pwd);
    }
}
