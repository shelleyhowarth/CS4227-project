package com.example.cs4227_project.interceptorPattern;

public class InterceptorFramework {
    Dispatcher dispatcher;

    public InterceptorFramework(Context context) {
        dispatcher = new Dispatcher(context);
    }

    public void addInterceptor(Interceptor i) {
        dispatcher.attach(i);
    }

    public void removeInterceptor(Interceptor i) {
        dispatcher.detach(i);
    }

    public void interceptorRequest(Context request) {
        dispatcher.dispatchInterceptors(request);
    }
}
