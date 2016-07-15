package com.bakatumu.bakatumu.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bakatumu.bakatumu.R;
import com.bakatumu.bakatumu.adapter.ChatRoomsAdapter;
import com.bakatumu.bakatumu.app.Config;
import com.bakatumu.bakatumu.app.EndPoints;
import com.bakatumu.bakatumu.app.MyApplication;
import com.bakatumu.bakatumu.gcm.GcmIntentService;
import com.bakatumu.bakatumu.gcm.NotificationUtils;
import com.bakatumu.bakatumu.model.ChatRoom;
import com.bakatumu.bakatumu.model.Message;
import com.bakatumu.bakatumu.model.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private ArrayList<ChatRoom> chatRoomArrayList;
    private ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;
    private TextView btnOrder, btnHistory, btnPesan, btnProfil;
    private TextView welcomeMessage, welcomeHint;
    private FloatingActionButton btnLogout;
//    private Button btnOrder, btnHistory, btnPesan, btnProfil, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cek login, kalo belum login, arahkan ke LoginActivity
        if (MyApplication.getInstance().getPrefManager().getUser() == null) {
            //Toast.makeText(MainActivity.this, MyApplication.getInstance().getPrefManager().getUser().getName(), Toast.LENGTH_SHORT).show();
            launchLoginActivity();
        }

        setContentView(R.layout.activity_menu);

        Typeface ubuntuRegular = Typeface.createFromAsset(getAssets(),
                "Ubuntu-Regular.ttf");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        welcomeMessage = (TextView) findViewById(R.id.welcome_message);
        welcomeHint = (TextView) findViewById(R.id.welcome_hint);
        btnOrder = (TextView) findViewById(R.id.btn_order);
        btnHistory = (TextView) findViewById(R.id.btn_history);
        btnPesan = (TextView) findViewById(R.id.btn_pesan);
        btnProfil = (TextView) findViewById(R.id.btn_profil);
        btnLogout = (FloatingActionButton) findViewById(R.id.btn_keluar);

        welcomeMessage.setTypeface(ubuntuRegular);
        welcomeHint.setTypeface(ubuntuRegular);
        btnOrder.setTypeface(ubuntuRegular);
        btnHistory.setTypeface(ubuntuRegular);
        btnPesan.setTypeface(ubuntuRegular);
        btnProfil.setTypeface(ubuntuRegular);

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuActivity.this, "Order Baru", Toast.LENGTH_SHORT).show();
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuActivity.this, "History Order", Toast.LENGTH_SHORT).show();
            }
        });

        btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuActivity.this, "Kirim Pesan", Toast.LENGTH_SHORT).show();
            }
        });

        btnProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MenuActivity.this, "Lihat Profil", Toast.LENGTH_SHORT).show();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.getInstance().logout();
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    subscribeToGlobalTopic();

                } else if (intent.getAction().equals(Config.SENT_TOKEN_TO_SERVER)) {
                    // gcm registration id is stored in our server's MySQL
                    Log.e(TAG, "GCM registration id is sent to our server");

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    handlePushNotification(intent);
                }
            }
        };

        if (checkPlayServices()) {
            registerGCM();
        }
    }

    private void handlePushNotification(Intent intent) {
        int type = intent.getIntExtra("type", -1);

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_CHATROOM) {
            Message message = (Message) intent.getSerializableExtra("message");
            String chatRoomId = intent.getStringExtra("chat_room_id");

            if (message != null && chatRoomId != null) {
                // updateRow(chatRoomId, message);
                //setNotifCount(1);
            }
        } else if (type == Config.PUSH_TYPE_USER) {
            // push belongs to user alone
            // just showing the message in a toast
            // kalau dapat push message, jalankan fungsi di sini
            Message message = (Message) intent.getSerializableExtra("message");
            Toast.makeText(getApplicationContext(), message.getUser().getName() + ": " + message.getMessage(), Toast.LENGTH_LONG).show();
            newPMDialog(message.getUser().getName(), message.getMessage());
        }
    }

    private void subscribeToGlobalTopic() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
        intent.putExtra(GcmIntentService.TOPIC, Config.TOPIC_GLOBAL);
        startService(intent);
    }

    // Subscribing to all chat room topics
    // each topic name starts with `topic_` followed by the ID of the chat room
    // Ex: topic_1, topic_2
    private void subscribeToAllTopics() {
        for (ChatRoom cr : chatRoomArrayList) {

            Intent intent = new Intent(this, GcmIntentService.class);
            intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE);
            intent.putExtra(GcmIntentService.TOPIC, "topic_" + cr.getId());
            startService(intent);
        }
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(MenuActivity.this, LoginBaruActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

//        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clearing the notification tray
        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    // starting the service to register with GCM
    private void registerGCM() {
        Intent intent = new Intent(this, GcmIntentService.class);
        intent.putExtra("key", "register");
        startService(intent);
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported. Google Play Services not installed!");
                Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_logout:
                MyApplication.getInstance().logout();
                break;

            case R.id.action_user:
                MyApplication.getInstance().userActivity();
                break;

            case R.id.action_chat_room:
                MyApplication.getInstance().mainActivity();
                break;

            case R.id.action_aer:
                MyApplication.getInstance().aerActivity();
                break;

            /* untuk personal chat
            case R.id.action_chat:
                MyApplication.getInstance().chatActivity();
                break;
            */
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void newPMDialog(String user, String pesan) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Pesan baru");

        // pesan harus dibatasi 160 karakter
        alert.setMessage(user + ": " + pesan + "\n\n" + "Buka pesan?");

        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                MyApplication.getInstance().userActivity();
            }
        });

        alert.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // kalau tidak, isi fungsi di sini
                    }
                });

        alert.show();

    }

    // FUNGSI LAMA
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
