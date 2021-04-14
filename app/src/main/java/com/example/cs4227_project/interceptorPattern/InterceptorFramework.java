package com.example.cs4227_project.interceptorPattern;

public class InterceptorFramework {
    Dispatcher dispatcher;

    public InterceptorFramework(Target target) {
        dispatcher = new Dispatcher(target);
    }

    public void addInterceptor(Interceptor i) {
        dispatcher.attach(i);
    }

    public void removeInterceptor(Interceptor i) {
        dispatcher.detach(i);
    }

    public void executeRequest(InterceptorContext request) {
        dispatcher.dispatchInterceptors(request);
    }
}
