package com.leanza.xpresseat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class DriverProfileActivity extends Activity{

    TextView textView;
    Query queryRef;
    String van = "";
    String name = "";
    String email = "";

    //final String DB_URL = "https://xpresseat-180f4.firebaseio.com/";
    //final Firebase fire = new Firebase(DB_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_driver_profile);


        //GET THE TERMINAL NAME AND STORE IN "route" -- done
        //Bundle bundle = getIntent().getExtras();
        //van = bundle.getString("van");
        //name = bundle.getString("name");
        //email = bundle.getString("email");

        //textView = (TextView) findViewById(R.id.infoTextView);

        //textView.setText(name + "\n" + email + "\n" + van);
    }




}

