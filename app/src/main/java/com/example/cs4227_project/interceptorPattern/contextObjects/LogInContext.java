package com.example.cs4227_project.interceptorPattern.contextObjects;

import android.view.View;

import com.example.cs4227_project.interceptorPattern.Context;

public class LogInContext extends Context {
    protected View view;

    public LogInContext() {
        super();
    }

    public LogInContext(String message, View view) {
        super(message);
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
