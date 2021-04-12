package com.example.cs4227_project.interceptorPattern;

import java.util.ArrayList;
import java.util.List;

public class Dispatcher {
    private List<Interceptor> interceptors;
    private Class contextType;

    public Dispatcher(Context context) {
        interceptors = new ArrayList<>();
        contextType = context.getClass();
    }

    public Class getContextType() {
        return this.contextType;
    }

    public void attach(Interceptor i) {
        interceptors.add(i);
    }

    public void detach(Interceptor i) {
        interceptors.remove(i);
    }

    public void dispatchInterceptors(Context context) {
        // validate context type matches dispatcher type
        if(contextType == context.getClass()) {
            for (Interceptor i : interceptors) {
                i.execute(context);
            }
        }
    }

}
