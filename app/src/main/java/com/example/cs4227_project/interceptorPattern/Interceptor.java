package com.example.cs4227_project.interceptorPattern;

public interface Interceptor<T extends Context> {
    public void execute(T context);
}
