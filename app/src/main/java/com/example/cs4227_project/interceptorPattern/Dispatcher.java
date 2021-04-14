package com.example.cs4227_project.interceptorPattern;

import java.util.ArrayList;
import java.util.List;

public class Dispatcher {
    private List<Interceptor> interceptors;
    private Target target;

    public Dispatcher(Target target) {
        interceptors = new ArrayList<>();
        this.target = target;
    }

    public void attach(Interceptor i) {
        interceptors.add(i);
    }

    public void detach(Interceptor i) {
        interceptors.remove(i);
    }

    public void dispatchInterceptors(InterceptorContext request) {
        for (Interceptor i : interceptors) {
            i.execute(request);
        }
        target.execute(request);
    }

}
