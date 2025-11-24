package com.example.marvel_comic_app.util;

import android.content.Context;
import android.widget.Toast;


public class ValidationUtils {
    public static boolean validateRegister(String name, String email,
                                           String pass, String date, Context c) {

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || date.isEmpty()) {
            Toast.makeText(c, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(c, "Email Invalido", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (pass.length() < 7) {
            Toast.makeText(c, "Contraseña muy corta", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
