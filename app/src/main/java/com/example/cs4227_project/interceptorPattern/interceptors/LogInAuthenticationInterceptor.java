package com.example.cs4227_project.interceptorPattern.interceptors;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.cs4227_project.R;
import com.example.cs4227_project.interceptorPattern.InterceptorContext;
import com.example.cs4227_project.interceptorPattern.Interceptor;
import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.user.LogInFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LogInAuthenticationInterceptor implements Interceptor {

    @Override
    public void execute(InterceptorContext context) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.d(LogTags.INTERCEPTOR, "starting log-in fragment");
            FragmentActivity fragmentContext = (FragmentActivity) context.getData();
            Bundle bundle = new Bundle();
            LogInFragment fragment = new LogInFragment();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = fragmentContext.getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, fragment);
            transaction.addToBackStack("login");
            transaction.commit();
        }
    }
}
