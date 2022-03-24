package com.example.cata_adventures;

import android.widget.EditText;

public class User {

    public String fullname, username, email, password;

    public User(EditText userName, EditText email_1, EditText password) {

    }

    public User(String fullname, String username, String email, String password) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public User(EditText name, EditText user_name, EditText email_input, EditText password_input) {
    }
}

