package com.leanza.xpresseat;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.leanza.xpresseat.Data.Commuter;
import com.leanza.xpresseat.Data.Route;
import com.leanza.xpresseat.Data.Terminal;

public class MainActivity extends Activity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if(googleServiceAvailable()) {
            //Toast.makeText(this, "perfect!", Toast.LENGTH_SHORT).show();
        }

        Firebase.setAndroidContext(this);

        mAuth = FirebaseAuth.getInstance();

        //GET FIREBASE INSTANCE
        final String DB_URL = "https://xpresseat-180f4.firebaseio.com/Route";
        final Firebase fire = new Firebase(DB_URL);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) { //user is logged in
                    Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                    intent.putExtra("userEmail", mAuth.getCurrentUser().getEmail());
                    startActivity(intent);
                }
            }
        };

    }

    @Override
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    public void logInPopUp(View view) {
        Intent intent = new Intent(MainActivity.this, LoginUserActivity.class);
        intent.putExtra("role", "Commuter");
        startActivity(intent);
    }

    public void goToLoginUserActivity(View view) {
        Intent intent = new Intent(MainActivity.this, LoginUserActivity.class);
        startActivity(intent);
    }

    public void goToLoginDriverActivity(View view) {
        Intent intent = new Intent(MainActivity.this, LoginUserActivity.class);
        startActivity(intent);
    }

    public void goToSignUpActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }



    public boolean googleServiceAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
        } else {
            Toast.makeText(this, "Can't connect to play services", Toast.LENGTH_SHORT).show();
        }
        return false;
    }


}