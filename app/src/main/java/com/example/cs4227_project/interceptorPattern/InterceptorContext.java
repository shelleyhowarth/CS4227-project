package com.example.cs4227_project.interceptorPattern;

public class InterceptorContext {
    protected String message;
    protected Object data;

    public InterceptorContext(String message) {
        this.message = message;
    }

    public InterceptorContext(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
