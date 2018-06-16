package com.leanza.xpresseat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.vision.text.Line;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leanza.xpresseat.Data.Route;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RouteActivity extends Activity {
    final static String DB_URL = "https://xpresseat-180f4.firebaseio.com/Route/";

    ListView route_lv;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String terminal, route;

    private RecyclerView mRouteList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_route);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Route");
        mDatabase.keepSynced(true);

        mRouteList=(RecyclerView)findViewById(R.id.recycle);
        mRouteList.setHasFixedSize(true);
        mRouteList.setLayoutManager(new LinearLayoutManager(this));

        //GET THE TERMINAL NAME AND STORE IN "terminal" -- done
        Bundle bundle = getIntent().getExtras();
        terminal = bundle.getString("terminal");

        System.out.println("terminallll:" + terminal);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Route, RouteViewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Route, RouteViewHolder>
                (Route.class, R.layout.card_items, RouteViewHolder.class, mDatabase) {


            @Override
            protected void populateViewHolder(RouteViewHolder viewHolder, final Route model, int position) {

                if(model.getName().startsWith(terminal)) {
                    viewHolder.setName(model.getName());
                    viewHolder.setUrl(getApplicationContext(), model.getUrl());
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
                            route = model.getName();

                            //GET LATITUDE AND LONGTIUDE OF ROUTE
                            double originLat = model.getOriginLat();
                            double originLong = model.getOriginLong();
                            double destLat = model.getDestLat();
                            double destLong = model.getDestLong();
                            String origin = model.getOrigin();
                            String dest = model.getDest();

                            Toast.makeText(RouteActivity.this, route, Toast.LENGTH_SHORT).show();

                            /*Intent intent = new Intent(RouteActivity.this, VanActivity.class);
                            intent.putExtra("route", route);
                            startActivity(intent);*/



                            //String uri = "http://maps.google.com/maps?saddr=" + sourceLatitude + "," + sourceLongitude + "&daddr=" + destinationLatitude + "," + destinationLongitude;
                            Intent intent = new Intent(RouteActivity.this, MapActivity.class);
                            intent.putExtra("originLat", originLat);
                            intent.putExtra("originLong", originLong);
                            intent.putExtra("destLat", destLat);
                            intent.putExtra("destLong", destLong);
                            intent.putExtra("origin", origin);
                            intent.putExtra("dest", dest);
                            intent.putExtra("route", route);
                            startActivity(intent);
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
        mRouteList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class RouteViewHolder extends  RecyclerView.ViewHolder {
        View mView;
        CardView parentLayout;

        public RouteViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            parentLayout = itemView.findViewById(R.id.card_view);
        }
        public void setName(String name) {
            TextView name_tv = (TextView)mView.findViewById(R.id.routeNameTV);
            name_tv.setText(name);
        }
        public void setUrl(Context ctx, String url) {
            ImageView url_iv = (ImageView)mView.findViewById(R.id.routeImgView);
            Picasso.with(ctx).load(url).into(url_iv);
        }
    }







}
