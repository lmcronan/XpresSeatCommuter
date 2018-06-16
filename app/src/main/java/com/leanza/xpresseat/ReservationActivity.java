package com.leanza.xpresseat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.leanza.xpresseat.Data.Route;
import com.leanza.xpresseat.Data.Van;

import java.util.ArrayList;
import java.util.HashMap;

public class ReservationActivity extends Activity{


    TextView textView;
    Button confirmBtn;
    Query queryRef;
    String van = "";
    String capacity = "";
    String deptime = "";
    String driver = "";
    String reserved = "";
    String vanroute = "";
    String vacant = "";
    int updatedreserved = 0;
    int updatedvacant = 0;
    String updatereserved = "";
    String updatevacant = "";

    final String DB_URL = "https://xpresseat-180f4.firebaseio.com/";
    final Firebase fire = new Firebase(DB_URL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reservation);


        //GET THE TERMINAL NAME AND STORE IN "route" -- done
        Bundle bundle = getIntent().getExtras();
        van = bundle.getString("van");
        capacity = bundle.getString("capacity");
        deptime = bundle.getString("deptime");
        driver = bundle.getString("driver");
        reserved = bundle.getString("reserved");
        vanroute = bundle.getString("vanroute");
        vacant = bundle.getString("vacant");

        textView = (TextView) findViewById(R.id.infoTextView);
        confirmBtn = (Button) findViewById(R.id.confirmBtn);

        textView.setText(van + "\n" + driver + "\n" + deptime + "\n" + vanroute + "\n" + capacity + "\n" + reserved + "\n" + vacant);
    }

    public void reserveSeat(View view) {
        updatedreserved = Integer.parseInt(reserved) + 1;
        updatedvacant = Integer.parseInt(vacant) - 1;

        updatereserved = String.valueOf(updatedreserved);
        updatevacant = String.valueOf(updatedvacant);

        queryRef = fire.child("Van").orderByChild("name").equalTo(van);

        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`

                System.out.print("KEY: " + key);
                String path = "/" + dataSnapshot.getKey() + "/" + key;

                System.out.println("PATH: " + path);

                fire.child(path + "/reservedseat").setValue(updatereserved);
                fire.child(path + "/vacantseat").setValue(updatevacant);
                fire.child(path + "/notify").setValue("ON");
            }

            @Override
            public void onCancelled(FirebaseError databaseError) {
                //Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);
            }
        });

        //SHOW DIALOG BOX
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReservationActivity.this);
        alertDialog.setTitle("SEAT RESERVED");

        alertDialog.setMessage("Please arrive at the terminal before the departure time to avoid cancellation of the reserved seat.\n\nDeparture time: " + deptime);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                Intent intent = new Intent(ReservationActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alertDialog.show();  //<-- See This!
    }


}

