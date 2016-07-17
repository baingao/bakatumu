package com.bakatumu.bakatumu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bakatumu.bakatumu.R;
import com.bakatumu.bakatumu.adapter.OrdersAdapter;
import com.bakatumu.bakatumu.app.EndPoints;
import com.bakatumu.bakatumu.app.MyApplication;
import com.bakatumu.bakatumu.gcm.NotificationUtils;
import com.bakatumu.bakatumu.helper.SimpleDividerItemDecoration;
import com.bakatumu.bakatumu.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {

    private String TAG = OrderHistoryActivity.class.getSimpleName();
    private ArrayList<Order> ordersArrayList;
    private OrdersAdapter mAdapter;
    private RecyclerView recyclerView;
    private String userId;
    private FloatingActionButton btnOk, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orderhistory);

        userId = MyApplication.getInstance().getPrefManager().getUser().getId();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        btnOk = (FloatingActionButton) findViewById(R.id.btn_ok);
        btnCancel = (FloatingActionButton) findViewById(R.id.btn_cancel);

        ordersArrayList = new ArrayList<>();
        mAdapter = new OrdersAdapter(this, ordersArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // set OrdersAdapter.java ke dalam recyclerView yang ada di orders_list_row.xml
        recyclerView.setAdapter(mAdapter);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().menuActivity();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().menuActivity();
            }
        });

        recyclerView.addOnItemTouchListener(new OrdersAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new OrdersAdapter.ClickListener() {

            @Override
            public void onClick(View view, int position) {
                 // benahi untuk menampilkan detail order
//                Order order = ordersArrayList.get(position);
//                Intent intent = new Intent(OrderHistoryActivity.this, PMActivity.class);
//                intent.putExtra("id", order.getId());
//                intent.putExtra("idCustomer", order.getIdCustomer());
//                intent.putExtra("idAset", order.getIdAset());
//                intent.putExtra("pesan", order.getPesan());
//                intent.putExtra("antar", order.getAntar());
//                intent.putExtra("terima", order.getTerima());
//                intent.putExtra("keterangan", order.getKeterangan());
//                intent.putExtra("jumlah", order.getJumlah());
//                intent.putExtra("harga", order.getHarga());
//                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchOrdersList();
    }

    private void updateRow(String id, String idAset, String alamat, String pesan, String antar,
                           String terima, String jumlah, String harga) {
        for (Order orderArray: ordersArrayList) {
            if (orderArray.getId().equals(id)) {
                int index = ordersArrayList.indexOf(orderArray);

                orderArray.setId(id);
                orderArray.setIdAset(idAset);
                orderArray.setAlamat(alamat);
                orderArray.setPesan(pesan);
                orderArray.setAntar(antar);
                orderArray.setTerima(terima);
                orderArray.setJumlah(jumlah);
                orderArray.setHarga(harga);

                ordersArrayList.remove(index);
                ordersArrayList.add(index, orderArray);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
    }


    private void fetchOrdersList() {
        final String endPoint = EndPoints.ORDER_HISTORY.replace("_ID_", userId);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray usersArray = obj.getJSONArray("orders"); // check rest web service /order/history/:id
                        for (int i = 0; i < usersArray.length(); i++) {
                            JSONObject ordersObj = (JSONObject) usersArray.get(i);
                            Order order = new Order();
                            order.setId(ordersObj.getString("id"));
                            order.setIdCustomer(ordersObj.getString("idcustomer"));
                            order.setIdAset(ordersObj.getString("idaset"));
                            order.setAlamat(ordersObj.getString("alamat"));
                            order.setPesan(ordersObj.getString("pesan"));
                            order.setAntar(ordersObj.getString("antar"));
                            order.setTerima(ordersObj.getString("terima"));
                            order.setKeterangan(ordersObj.getString("keterangan"));
                            order.setJumlah(ordersObj.getString("jumlah"));
                            order.setHarga(ordersObj.getString("harga"));

                            // masukkan array object order ke dalam recyclerView di orders_list_row.xml
                            ordersArrayList.add(order);
                        }

                    } else {
                        // error in fetching orders
                        Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                mAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(OrderHistoryActivity.this, LoginBaruActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationUtils.clearNotifications();
    }

}
