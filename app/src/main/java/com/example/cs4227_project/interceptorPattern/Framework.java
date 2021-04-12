package com.example.cs4227_project.interceptorPattern;

import java.util.List;

public class Framework {
    List<Dispatcher> dispatcherList;
    private static Framework instance;

    public static Framework getInstance() {
        if(instance == null) {
            instance = Application.buildFramework();
        }
        return instance;
    }

    public void handleRequest(Context context) {
        for (Dispatcher d : dispatcherList) {
            d.dispatchInterceptors(context);
        }
    }
}
