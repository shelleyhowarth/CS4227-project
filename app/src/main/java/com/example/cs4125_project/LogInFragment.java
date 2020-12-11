package com.example.cs4125_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private RelativeLayout layout;

    public LogInFragment() {
        // Required empty public constructor
    }

    public static LogInFragment newInstance()
    {
        LogInFragment myFragment = new LogInFragment();
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);

        // Views
        mEmailField = rootView.findViewById(R.id.fieldEmail);
        mPasswordField = rootView.findViewById(R.id.fieldPassword);
        rootView.findViewById(R.id.register).setOnClickListener(this);
        rootView.findViewById(R.id.signIn).setOnClickListener(this);
        rootView.findViewById(R.id.goBack).setOnClickListener(this);
        layout = rootView.findViewById(R.id.fragment_log_in);

        return rootView;
    }

    private boolean validateForm(String email, String password){
        boolean valid = true;

        if(TextUtils.isEmpty(email)) {
            mEmailField.setError("Required");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmailField.setError("Invalid Format");
        } else {
            mEmailField.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Required.");
            valid = false;
        } else if(password.length()< 4){
            mPasswordField.setError("Must be greater than 4 characters");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return valid;
    }

    private void register(String email, String password) {
        if (!validateForm(email, password)) {
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void closeFragment(View v) {
        layout.setVisibility(v.INVISIBLE);
    }

    private void signIn(String email, String password, final View v) {
        Log.d(TAG, "signIn:" + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            closeFragment(v);
                            Toast.makeText(getContext(), "Authentication success.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed." + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signIn) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString(), v);
        }
        if (i == R.id.register) {
            register(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        if (i == R.id.goBack) {
            closeFragment(v);
        }
    }
}
