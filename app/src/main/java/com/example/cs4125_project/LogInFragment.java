package com.example.cs4125_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

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

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);

        // Views
        mEmailField = rootView.findViewById(R.id.fieldEmail);
        mPasswordField = rootView.findViewById(R.id.fieldPassword);

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
}