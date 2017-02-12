package com.example.arkajitde.virasat;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arkajit De on 2/6/2017.
 */

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://virasat.pe.hu/Register.php";
    private Map<String, String> params;

    public RegisterRequest(String name, String phone, String college, String password, String email,String sex, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("name", name);
        params.put("phone", phone);
        params.put("college", college);
        params.put("email", email);
        params.put("password", password);
        params.put("sex",sex);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}