package com.leanza.xpresseat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leanza.xpresseat.Data.Commuter;
import com.leanza.xpresseat.Data.Driver;
import com.leanza.xpresseat.Data.Van;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VanActivity extends Activity {
    //final static String DB_URL = "https://xpresseat-180f4.firebaseio.com/Van/";

    ListView van_lv;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String route;
    //String capacity, deptime, driver, reserved, vanroute, vacant;

    private RecyclerView mVanList;
    private DatabaseReference mDatabase2;

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
    String passengerName ="";
    String lastKey="";
    String condition="";

    final String DB_URL = "https://xpresseat-180f4.firebaseio.com/";
    final Firebase fire = new Firebase(DB_URL);

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_van);

        //Firebase.setAndroidContext(this);

        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Van");
        mDatabase2.keepSynced(true);

        mVanList=(RecyclerView)findViewById(R.id.recycle2);
        mVanList.setHasFixedSize(true);
        mVanList.setLayoutManager(new LinearLayoutManager(this));
        //van_lv = (ListView) findViewById(R.id.vanLv);
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        //van_lv.setAdapter(adapter);

        //GET THE TERMINAL NAME AND STORE IN "route" -- done
        Bundle bundle = getIntent().getExtras();
        route = bundle.getString("route");

        System.out.println("routeeee:" + route);

    }//end of onCreate

    @Override
    protected void onStart(){
        super.onStart();

        FirebaseRecyclerAdapter<Van, VanViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Van, VanViewHolder>
                (Van.class, R.layout.van_items, VanViewHolder.class, mDatabase2) {


            @Override
            protected void populateViewHolder(VanViewHolder viewHolder, final Van model, int position) {

                System.out.println("\n\nmodel: " + model.getRoute());
                System.out.println("route: " + route);

                String temp = model.getRoute();
                int vs_count = Integer.parseInt(model.getVacantseat());
                String status = model.getStatus();

                System.out.println("temp: " + temp);

                if(temp.equals(route) && vs_count != 0 && status.equals("ON")) {

                    System.out.println("van here inside if");
                    viewHolder.setName(model.getName());
                    viewHolder.setDriver(model.getDriver());
                    viewHolder.setRoute(model.getRoute());
                    viewHolder.setDeparturetime(model.getDeparturetime());
                    viewHolder.setCapacity(model.getCapacity());
                    viewHolder.setVacantseat(model.getVacantseat());
                    //viewHolder.setReservedseat(model.getReservedseat());




                    viewHolder.mView.setVisibility(View.VISIBLE);
                    viewHolder.mView.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    viewHolder.mView.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;

                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) viewHolder.mView.getLayoutParams();
                    lp.setMargins(12,12,12,12);
                    viewHolder.mView.setLayoutParams(lp);

                    viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(RouteActivity.this, model.getName(), Toast.LENGTH_SHORT).show();

                            //GO TO THE LIST OF ROUTES AVAILABLE ON THE SELECTED TERMINAL
                            String van = model.getName();

                            //Toast.makeText(VanActivity.this, route, Toast.LENGTH_SHORT).show();

                            /*Intent intent = new Intent(VanActivity.this, ReservationActivity.class);
                            intent.putExtra("van", van);
                            startActivity(intent);*/


                            // GET VALUES FOR UPDATING DATABASE WHEN RESERVED
                            reserved = model.getReservedseat();  //string
                            vacant = model.getVacantseat();  //string
                            van = model.getName();  //plate number
                            deptime = model.getDeparturetime();


                            promptConfirmation(view, van, reserved, vacant, deptime);
                        }
                    });

                } else {
                    viewHolder.mView.setVisibility(View.GONE);
                    viewHolder.mView.getLayoutParams().height = 0;
                    viewHolder.mView.getLayoutParams().width = 0;
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) viewHolder.mView.getLayoutParams();
                    lp.setMargins(0,0,0,0);
                    viewHolder.mView.setLayoutParams(lp);
                }


            }
        };
        mVanList.setAdapter(firebaseRecyclerAdapter);
    }


    public static class VanViewHolder extends  RecyclerView.ViewHolder {
        View mView;
        CardView parentLayout;

        public VanViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            parentLayout = itemView.findViewById(R.id.van_item);
        }

        public void setDriver(String driver) {
            TextView name_tv = (TextView)mView.findViewById(R.id.vi_driver);
            name_tv.setText(driver);
        }

        public void setName(String name) { //plate number
            TextView name_tv = (TextView)mView.findViewById(R.id.vi_plate);
            name_tv.setText(name);
        }

        public void setRoute(String route) {
            TextView route_tv = (TextView)mView.findViewById(R.id.vi_route);
            route_tv.setText(route);
        }

        public void setDeparturetime(String departuretime) { //plate number
            TextView time_tv = (TextView)mView.findViewById(R.id.vi_time);
            time_tv.setText("Departure Time: " + departuretime);
        }
        public void setCapacity(String capacity) {
            TextView cap_tv = (TextView)mView.findViewById(R.id.vi_capacity);
            cap_tv.setText("Capacity: " + capacity);
        }

        public void setVacantseat(String vacant) { //plate number
            TextView vac_tv = (TextView)mView.findViewById(R.id.vi_vacant);
            vac_tv.setText("Vacant Seats: " + vacant);
        }



    }

    public void promptConfirmation(final View view2, final String van2, final String reserved2, final String vacant2, final String deptime2) {
        //SHOW DIALOG BOX
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(VanActivity.this);
        alertDialog.setTitle("RESERVE SEAT IN " + van2 + "?");

        alertDialog.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                reserveSeat(view2, van2, reserved2, vacant2, deptime2);
            }
        });

        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alertDialog.show();  //<-- See This!
    }


    public void reserveSeat(View view, final String van, String reserved, String vacant, final String deptime) {
        condition = "true";
        updatedreserved = Integer.parseInt(reserved) + 1;
        updatedvacant = Integer.parseInt(vacant) - 1;

        updatereserved = String.valueOf(updatedreserved);
        updatevacant = String.valueOf(updatedvacant);

        queryRef = fire.child("Van").orderByChild("name").equalTo(van);
        Toast.makeText(VanActivity.this, "you clicked " + van, Toast.LENGTH_SHORT);

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


            //ADD NAME IN PASSENGER'S LIST OF VAN
            mAuth = FirebaseAuth.getInstance();
            String currentUser = mAuth.getCurrentUser().getEmail();

            System.out.println("CURRENT USER: " + currentUser);

            //GET THE FULL NAME OF COMMUTER
            Query query = fire.child("Commuter").orderByChild("emailaddress").equalTo(currentUser); //select user from commuters

            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String passFname = (String) dataSnapshot.getValue(Commuter.class).getFirstname();
                    String passLname = (String) dataSnapshot.getValue(Commuter.class).getLastname();
                    passengerName = passFname + " " + passLname;

                    System.out.println("PASSENGER NAME: " + passengerName);

                    //CREATE CHILD NODE OF VAN FOR PASSENGERS
                    final Query queryRef3;
                    queryRef3 = fire.child("Van").orderByChild("name").equalTo(van);

                    queryRef3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey(); // this key is `K1NRz9l5PU_0CFDtgXz`

                            //System.out.print("KEY32: " + key);
                            //String path = "/" + dataSnapshot.getKey() + "/" + key;

                            //DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            //mDatabase.child("Van").child(key).child("passenger").push().setValue(passengerName);


                            DatabaseReference root = FirebaseDatabase.getInstance().getReference();
                            final DatabaseReference groupsRef = root.child("Van").child(key).child("passenger");


                            groupsRef.orderByKey().limitToLast(1).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
                                @Override
                                public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                                    lastKey = dataSnapshot.getKey();
                                    System.out.print("LAST KEY: " + dataSnapshot.getKey());

                                    int lastKeyInt = Integer.parseInt(lastKey);
                                    lastKeyInt = lastKeyInt + 1;
                                    String lastStr = String.valueOf(lastKeyInt);

                                    if(condition.equals("true")) {
                                        groupsRef.child(lastStr).setValue(passengerName);
                                        condition = "false";
                                    }


                                }

                                @Override
                                public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

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


        //SHOW DIALOG BOX
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(VanActivity.this);
        alertDialog.setTitle("SEAT RESERVED");

        alertDialog.setMessage("Please arrive at the terminal before the departure time to avoid cancellation of the reserved seat.\n\nDeparture time: " + deptime);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();

                Intent intent = new Intent(VanActivity.this, UserProfile2Activity.class);
                intent.putExtra("plate", van);
                intent.putExtra("route", route);
                intent.putExtra("deptime", deptime);
                startActivity(intent);
            }
        });

        alertDialog.show();  //<-- See This!
    }
}
