package com.example.cs4227_project.interceptorPattern;

public interface Dispatcher {
    public void attach(Interceptor i);

    public void detach(Interceptor i);

    public void dispatchInterceptors(InterceptorContext request);
}
