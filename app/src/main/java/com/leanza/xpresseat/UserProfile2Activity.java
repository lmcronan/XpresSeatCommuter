package com.leanza.xpresseat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.leanza.xpresseat.Data.Commuter;

public class UserProfile2Activity extends Activity {
    final static String DB_URL = "https://xpresseat-180f4.firebaseio.com/Commuter/";
    final Firebase fire = new Firebase(DB_URL);



    TextView emailTv, unameTv, numberTv, vanTv, routeTv, deptimeTv, upcomingTv, fullnameTv;;
    String fname, lname, email, username, number, email2, van, route, deptime;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_user_profile2);

        Bundle bundle = getIntent().getExtras();
        van = bundle.getString("plate");
        route = bundle.getString("route");
        deptime = bundle.getString("deptime");

        upcomingTv = (TextView) findViewById(R.id.upcoming);
        upcomingTv.setText("UPCOMING TRIP: ");
        vanTv = (TextView) findViewById(R.id.upPlate);
        vanTv.setText(van);
        routeTv = (TextView) findViewById(R.id.upRoute);
        routeTv.setText(route);
        deptimeTv = (TextView) findViewById(R.id.upDeptime);
        deptimeTv.setText("Departure Time: " + deptime);

        //Bundle bundle = getIntent().getExtras();
        //email2 = bundle.getString("userEmail");

        //System.out.println("EMAIL222: " + email2);

        mAuth = FirebaseAuth.getInstance();
        final String userEmail = mAuth.getCurrentUser().getEmail();

        System.out.println("USEREMAILLL: " + userEmail);

        Query queryRef;
        queryRef = fire.orderByChild("emailaddress").equalTo(userEmail);


        queryRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fname = (String) dataSnapshot.getValue(Commuter.class).getFirstname();
                lname = (String) dataSnapshot.getValue(Commuter.class).getLastname();
                email = (String) dataSnapshot.getValue(Commuter.class).getEmailaddress();
                username = (String) dataSnapshot.getValue(Commuter.class).getUsername();
                //number = (String) dataSnapshot.getValue(Commuter.class).getDriver();

                fullnameTv = (TextView) findViewById(R.id.fullname);
                fullnameTv.setText(fname + " " + lname);
                emailTv = (TextView) findViewById(R.id.userEmail);
                emailTv.setText(email);
                unameTv = (TextView) findViewById(R.id.userName);
                unameTv.setText(username);
                //numberTv = (TextView) findViewById(R.id.userNumber);
                //numberTv.setText(number);

                System.out.println("UZER EMAIL: " + email);
                System.out.println("UZER NAME: " + username);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void goToTerminalActivity(View view) {
        Intent intent = new Intent(UserProfile2Activity.this, TerminalActivity.class);
        startActivity(intent);
    }

    public void logoutUser(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
        startActivity(new Intent(UserProfile2Activity.this, MainActivity.class));
    }

    public void changeReservation(View view) {
        Intent intent = new Intent(UserProfile2Activity.this, TerminalActivity.class);
        startActivity(intent);
    }

    public void cancelReservation(View view) {
        //SHOW DIALOG BOX
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UserProfile2Activity.this);
        alertDialog.setTitle("Are you sure you want to cancel your reservation?");

        alertDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                //reserveSeat(view2, van2, reserved2, vacant2, deptime2);
                //method for cancelling
                //remove name of commuter from the list of passengers

                //clear upcoming trip details
                upcomingTv = (TextView) findViewById(R.id.upcoming);
                upcomingTv.setText("UPCOMING TRIP: ");
                vanTv.setText("");
                routeTv.setText("No reservations made.");
                deptimeTv.setText("");

                Button change = (Button) findViewById(R.id.changeBtn);
                change.setVisibility(View.GONE);

                Button cancel = (Button) findViewById(R.id.cancelBtn);
                cancel.setVisibility(View.GONE);
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
