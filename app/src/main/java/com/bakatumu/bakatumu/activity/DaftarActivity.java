package com.bakatumu.bakatumu.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bakatumu.bakatumu.R;
import com.bakatumu.bakatumu.app.EndPoints;
import com.bakatumu.bakatumu.app.MyApplication;
import com.bakatumu.bakatumu.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DaftarActivity extends AppCompatActivity {

    private String TAG = DaftarActivity.class.getSimpleName();
    private TextView welcomeMessage, welcomeHint;
    private EditText inputPhone, inputPassword, inputNama, inputEmail, inputAlamat;
    private TextInputLayout inputLayoutPhone, inputLayoutPassword, inputLayoutNama, inputLayoutEmail, inputLayoutAlamat;
    private FloatingActionButton btnOk, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daftar);

        Typeface ubuntuRegular = Typeface.createFromAsset(getAssets(),
                "Ubuntu-Regular.ttf");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        welcomeMessage = (TextView) findViewById(R.id.welcome_message);
        welcomeHint = (TextView) findViewById(R.id.welcome_hint);

        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_nohp);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutNama = (TextInputLayout) findViewById(R.id.input_layout_nama);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutAlamat = (TextInputLayout) findViewById(R.id.input_layout_alamat);

        inputPhone = (EditText) findViewById(R.id.input_phone);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputNama = (EditText) findViewById(R.id.input_nama);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputAlamat = (EditText) findViewById(R.id.input_alamat);

        btnOk = (FloatingActionButton) findViewById(R.id.btn_ok);
        btnCancel = (FloatingActionButton) findViewById(R.id.btn_cancel);

        welcomeMessage.setTypeface(ubuntuRegular);
        welcomeHint.setTypeface(ubuntuRegular);
        inputLayoutPhone.setTypeface(ubuntuRegular);
        inputLayoutPassword.setTypeface(ubuntuRegular);
        inputLayoutNama.setTypeface(ubuntuRegular);
        inputLayoutEmail.setTypeface(ubuntuRegular);
        inputLayoutAlamat.setTypeface(ubuntuRegular);
        inputPhone.setTypeface(ubuntuRegular);
        inputPassword.setTypeface(ubuntuRegular);
        inputNama.setTypeface(ubuntuRegular);
        inputEmail.setTypeface(ubuntuRegular);
        inputAlamat.setTypeface(ubuntuRegular);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daftar();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().logout();
            }
        });
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    // Validasi email
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_phone));
            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    // Validasi no hp
    private static boolean isValidPhone(String phone) {
        return !TextUtils.isEmpty(phone) && android.util.Patterns.PHONE.matcher(phone).matches();
    }

    private boolean validatePhone() {
        String phone = inputPhone.getText().toString().trim();

        if (phone.isEmpty() || !isValidPhone(phone)) {
            inputLayoutPhone.setError(getString(R.string.err_msg_phone));
            requestFocus(inputPhone);
            return false;
        } else {
            inputLayoutPhone.setErrorEnabled(false);
        }

        return true;
    }

    private void daftar() {

        if (!validatePhone()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        // nilai ini
        final String name = inputNama.getText().toString();
        final String phone = inputPhone.getText().toString();
        final String email = inputEmail.getText().toString();
        final String alamat = inputAlamat.getText().toString();
        final String password = inputPassword.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.USER_DAFTAR, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                login(phone, password);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            // dipakai di sini untuk POST Request body
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("phone", phone);
                params.put("email", email);
                params.put("alamat", alamat);
                params.put("password", password);

                // untuk debug, ingat hapus!
                Log.e(TAG, "Sent params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void login(final String phone, final String password) {

        if (!validatePhone()) {
            return;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.USER_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        // user successfully logged in

                        JSONObject userObj = obj.getJSONObject("user");
                        User user = new User(userObj.getString("user_id"),
                                userObj.getString("name"),
                                userObj.getString("alamat"),
                                userObj.getString("lat"),
                                userObj.getString("lng"),
                                userObj.getString("email"),
                                userObj.getString("phone"),
                                userObj.getString("api_key"),
                                null,
                                userObj.getString("created_at"), 0);

                        // storing user in shared preferences
                        MyApplication.getInstance().getPrefManager().storeUser(user);

                        // start main activity
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        finish();

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            // dipakai di sini untuk POST Request body
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("phone", phone);
                params.put("password", password); // cek lagi

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                5000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
