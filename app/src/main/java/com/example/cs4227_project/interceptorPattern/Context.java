package com.example.cs4227_project.interceptorPattern;

public class Context {
    protected String message;

    public Context() {}

    public Context(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
