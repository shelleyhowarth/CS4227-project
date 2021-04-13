package com.example.cs4227_project.interceptorPattern.interceptors;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs4227_project.R;
import com.example.cs4227_project.interceptorPattern.Context;
import com.example.cs4227_project.interceptorPattern.Interceptor;
import com.example.cs4227_project.interceptorPattern.contextObjects.LogInContext;
import com.example.cs4227_project.user.LogInFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LogInAuthenticationInterceptor implements Interceptor {

    public void execute(LogInContext context) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            View currentApplicationView = context.getView();
            Fragment fragment = new LogInFragment();
            AppCompatActivity activity = (AppCompatActivity) currentApplicationView.getContext();
            FragmentManager manager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = manager.beginTransaction().replace(R.id.content, fragment);
            fragmentTransaction.commit();
        }
        else {

        }
    }

    @Override
    public void execute(Context context) {
        System.out.println();
    }
}
