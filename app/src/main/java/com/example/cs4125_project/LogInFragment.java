package com.example.cs4125_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private Button signInBtn;


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
        rootView.findViewById(R.id.goBack).setOnClickListener(this);
        rootView.findViewById(R.id.signIn).setOnClickListener(this);


        signInBtn = (Button) rootView.findViewById(R.id.signIn); // you have to use rootview object..
        signInBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Hello World", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    private void register(String email, String password) {
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
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getContext(), "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
    }

    public void onClick(View v) {
        int i = v.getId();
        /*
        if (i == R.id.signIn) {
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        */
        if (i == R.id.goBack) {
            register(mEmailField.getText().toString(), mPasswordField.getText().toString());
            Toast.makeText(getContext(), "Go back clicked",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
