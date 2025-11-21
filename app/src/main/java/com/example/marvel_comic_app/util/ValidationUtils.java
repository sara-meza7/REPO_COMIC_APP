package com.example.marvel_comic_app.util;

import android.content.Context;
import android.widget.Toast;


public class ValidationUtils {
    public static boolean validateRegister(String name, String email,
                                           String pass, String date, Context c) {

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || date.isEmpty()) {
            Toast.makeText(c, "All fields are required", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(c, "Invalid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pass.length() < 5) {
            Toast.makeText(c, "Password too short", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
