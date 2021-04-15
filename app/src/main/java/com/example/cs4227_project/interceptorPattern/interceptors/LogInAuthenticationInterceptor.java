package com.example.cs4227_project.interceptorPattern.interceptors;

import android.os.Bundle;
import android.util.Log;

import com.example.cs4227_project.R;
import com.example.cs4227_project.interceptorPattern.InterceptorContext;
import com.example.cs4227_project.interceptorPattern.Interceptor;
import com.example.cs4227_project.misc.FragmentController;
import com.example.cs4227_project.misc.LogTags;
import com.example.cs4227_project.order.ViewCheckoutInputFragment;
import com.example.cs4227_project.user.LogInFragment;
import com.google.firebase.auth.FirebaseAuth;

public class LogInAuthenticationInterceptor implements Interceptor {

    @Override
    public void execute(InterceptorContext context) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            Log.d(LogTags.INTERCEPTOR, "User is not logged-in. Cannot start checkout process");
            FragmentController fragmentController = FragmentController.getInstance();
            Bundle bundle = new Bundle();
            bundle.putString("toast","You must be logged-in to purchase products!");
            fragmentController.startFragment(new LogInFragment(), R.id.content, "log_in", bundle);
        }
        else {
            Log.d(LogTags.INTERCEPTOR, "User is logged-in. Further action not required");
        }
    }
}
