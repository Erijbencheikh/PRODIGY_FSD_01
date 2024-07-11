package com.example.secureuserauthentication.models;

import jakarta.persistence.Entity;

@Entity

public class Customer extends User{
    private String image;

    private String num ;

    public Customer(String image, String num) {
        this.image = image;
        this.num = num;
    }

    public Customer(String username, String email, String password) {
        super(username, email, password);
    }

    public Customer(String username, String email, String password, String image, String num) {
        super(username, email, password);
        this.image = image;
        this.num = num;
    }

    public Customer() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
