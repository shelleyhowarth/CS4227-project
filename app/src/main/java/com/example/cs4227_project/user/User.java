package com.example.cs4227_project.user;

import com.example.cs4227_project.shop.Cart;

public class User {
    private String id;
    private String email;
    private boolean admin;
    public static User user;

    public User(){}

    public User(String id, String email, boolean admin){
        this.id = id;
        this.email = email;
        this.admin = admin;
    }

    public void setUser(User u){user = u;};

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public boolean isAdmin() { return admin; }

    public void setAdmin(boolean admin) { this.admin = admin; }
}
