package com.example.cs4227_project.interceptorPattern.interceptors;

import android.util.Log;

import com.example.cs4227_project.interceptorPattern.Interceptor;
import com.example.cs4227_project.interceptorPattern.InterceptorContext;
import com.example.cs4227_project.misc.LogTags;

public class LoggingInterceptor implements Interceptor {
    @Override
    public void execute(InterceptorContext context) {
        String message = context.getMessage();
        Log.d(LogTags.INTERCEPTOR, message);
    }
}
