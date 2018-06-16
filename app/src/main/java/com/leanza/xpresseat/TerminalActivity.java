package com.leanza.xpresseat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.leanza.xpresseat.Data.Terminal;


import java.util.ArrayList;

public class TerminalActivity extends Activity {
    final static String DB_URL = "https://xpresseat-180f4.firebaseio.com/Terminal/";

    ListView terminal_lv;
    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_terminal);
        terminal_lv = (ListView) findViewById(R.id.terminalLv);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        terminal_lv.setAdapter(adapter);

        final Firebase fire = new Firebase(DB_URL);

        final ChildEventListener childEventListener = fire.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String t = (String) dataSnapshot.getValue(Terminal.class).getName();
                list.add(t);
                adapter.notifyDataSetChanged();
                System.out.println(t);
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

        terminal_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //GO TO THE LIST OF ROUTES AVAILABLE ON THE SELECTED TERMINAL
                String terminal = (terminal_lv.getItemAtPosition(position)).toString();

                //Toast.makeText(TerminalActivity.this, terminal, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(TerminalActivity.this, RouteActivity.class);
                intent.putExtra("terminal", terminal);
                startActivity(intent);
            }
        });


    }
}
