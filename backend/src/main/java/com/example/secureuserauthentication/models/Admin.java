package com.example.secureuserauthentication.models;

import jakarta.persistence.Entity;

@Entity
public class Admin extends User {

    public Admin() {
    }

    public Admin(String username, String email, String password) {
        super(username, email, password);

    }


}
