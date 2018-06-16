package com.leanza.xpresseat;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginUserActivity extends Activity {

    private EditText emailField;
    private EditText pwordField;
    private Button loginBtn;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login_user);


        Bundle bundle = getIntent().getExtras();
        final String role= bundle.getString("role");
        mAuth = FirebaseAuth.getInstance();

        emailField = (EditText) findViewById(R.id.emailLogin);
        pwordField = (EditText) findViewById(R.id.pwordLogin);
        loginBtn = (Button) findViewById(R.id.loginUserBtn);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) { //user is logged in

                        System.out.println("entering user profile");
                        //String userEmail = mAuth.getCurrentUser().getEmail();

                        //System.out.println("emailz: "+ userEmail);

                        Intent intent = new Intent(LoginUserActivity.this, UserProfileActivity.class);
                        //intent.putExtra("userEmail", userEmail);
                        startActivity(intent);
                        //startActivity(new Intent(LoginUserActivity.this, UserProfileActivity.class));

                }
            }
        };
    }

    @Override
    protected void onStart(){
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    public void logInUser(View view){
        String email = emailField.getText().toString().trim();
        String pword = pwordField.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(pword)){
            Toast.makeText(LoginUserActivity.this, "Fields are empty.", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, pword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginUserActivity.this, "Log in problem", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(LoginUserActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
