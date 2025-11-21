package com.example.marvel_comic_app.network;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class ApiClient {

    private static ApiClient instance;
    private RequestQueue requestQueue;
    private static Context ctx;

    // URL base del servidor (cambia según tu configuración)
    public static final String BASE_URL = "http://10.0.2.2:8080/MicroservicioPasajero";

    private ApiClient(Context context) {
        ctx = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiClient getInstance(Context context) {
        if (instance == null) {
            instance = new ApiClient(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static String buildUrl(String endpoint) {
        return BASE_URL + endpoint;
    }
}