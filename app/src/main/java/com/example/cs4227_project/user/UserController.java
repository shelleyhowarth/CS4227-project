package com.example.cs4227_project.user;

public class UserController {
    private static User user;

    public static void setUser(User u){ user = u;}

    public static User getUser(){ return user; }
}
