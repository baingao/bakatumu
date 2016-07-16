package com.bakatumu.bakatumu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class OrderActivity extends AppCompatActivity {

    private String TAG = OrderActivity.class.getSimpleName();
    private String latLng, alamatKirim;
    private TextView welcomeMessage, welcomeHint, alamatHint;
    private EditText inputJumlahOrder, inputAlamatSendiri, inputAlamatLain;
    private TextInputLayout inputLayoutJumlahOrder, inputLayoutAlamatSendiri, inputLayputAlamatLain;
    private RadioButton rbAlamatSendiri, rbAlamatLain;
    private FloatingActionButton btnOk, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MyApplication.getInstance().getPrefManager().getUser() == null) {
            launchLoginActivity();
        }
        setContentView(R.layout.activity_order);

        Typeface ubuntuRegular = Typeface.createFromAsset(getAssets(),
                "Ubuntu-Regular.ttf");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        welcomeMessage = (TextView) findViewById(R.id.welcome_message);
        welcomeHint = (TextView) findViewById(R.id.welcome_hint);
        alamatHint = (TextView) findViewById(R.id.alamat_hint);

        inputLayoutJumlahOrder = (TextInputLayout) findViewById(R.id.input_layout_jumlah_order);
        inputLayoutAlamatSendiri = (TextInputLayout) findViewById(R.id.input_layout_alamat_sendiri);
        inputLayputAlamatLain = (TextInputLayout) findViewById(R.id.input_layout_alamat_lain);
        rbAlamatSendiri = (RadioButton) findViewById(R.id.rb_alamat_sendiri);
        rbAlamatLain = (RadioButton) findViewById(R.id.rb_alamat_lain);

        inputJumlahOrder = (EditText) findViewById(R.id.input_jumlah_order);
        inputAlamatSendiri = (EditText) findViewById(R.id.input_alamat_sendiri);
        inputAlamatLain = (EditText) findViewById(R.id.input_alamat_lain);

        btnOk = (FloatingActionButton) findViewById(R.id.btn_ok);
        btnCancel = (FloatingActionButton) findViewById(R.id.btn_cancel);

        welcomeMessage.setTypeface(ubuntuRegular);
        welcomeHint.setTypeface(ubuntuRegular);
        alamatHint.setTypeface(ubuntuRegular);
        inputLayoutJumlahOrder.setTypeface(ubuntuRegular);
        inputLayoutAlamatSendiri.setTypeface(ubuntuRegular);
        inputLayputAlamatLain.setTypeface(ubuntuRegular);
        rbAlamatSendiri.setTypeface(ubuntuRegular);
        rbAlamatLain.setTypeface(ubuntuRegular);
        inputJumlahOrder.setTypeface(ubuntuRegular);
        inputAlamatSendiri.setTypeface(ubuntuRegular);
        inputAlamatLain.setTypeface(ubuntuRegular);

        if (MyApplication.getInstance().getPrefManager().getUser().getAlamat() != null) {
            inputAlamatSendiri.setText(MyApplication.getInstance().getPrefManager().getUser().getAlamat());
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rbAlamatSendiri.isChecked()) {
                    alamatKirim = inputAlamatSendiri.getText().toString();
                } else {
                    alamatKirim = inputAlamatLain.getText().toString();
                }
                orderQuery("Konfirmasi Order", "Jumlah order : " + inputJumlahOrder.getText().toString() + " ret\n\n" +
                        "Alamat antar :\n" + alamatKirim);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().menuActivity();
            }
        });
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(OrderActivity.this, LoginBaruActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void simpanOrder() {

        // nilai ini
        final String userId = MyApplication.getInstance().getPrefManager().getUser().getId();
        final String alamat;
        final String lat = MyApplication.getInstance().getPrefManager().getUser().getLat();
        final String lng = MyApplication.getInstance().getPrefManager().getUser().getLng();
        final String jumlahOrder = inputJumlahOrder.getText().toString();
        final String harga = "0";
        final String apiKey = MyApplication.getInstance().getPrefManager().getUser().getApiKey();

        if (rbAlamatSendiri.isChecked()) {
            alamat = inputAlamatSendiri.getText().toString();
        } else {
            alamat = inputAlamatLain.getText().toString();
        }


        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.USER_ORDER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);
                Toast.makeText(OrderActivity.this, "Order sudah terkirim.", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                finish();
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
                params.put("user_id", userId);
                params.put("alamat", alamat);
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("jumlah_order", jumlahOrder);
                params.put("api_key", apiKey);
                params.put("harga", harga);
                // untuk debug, ingat hapus!
                Log.e(TAG, "Sent params: " + params.toString());
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(strReq);

    }

    public void orderQuery(String judul, String pesan) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(judul);

        // pesan harus dibatasi 160 karakter
        alert.setMessage(pesan);

        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                simpanOrder();
            }
        });

        alert.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(OrderActivity.this, "Order dibatalkan", Toast.LENGTH_SHORT).show();
                    }
                });

        alert.show();
    }

}
