package com.leanza.xpresseat;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.leanza.xpresseat.Data.Commuter;
import com.leanza.xpresseat.Data.Route;

import static java.lang.Boolean.TRUE;

public class SignUpActivity extends Activity {

    EditText etFname, etLname, etUname;
    EditText etEmail, etPassword;
    Button registerBtn, testBtn;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_up);

        etFname = findViewById(R.id.fnameEditText);
        etLname = findViewById(R.id.lnameEditText);
        etUname = findViewById(R.id.unameEditText);
        etEmail = findViewById(R.id.emailEditText);
        etPassword = findViewById(R.id.pwordEditText);
        registerBtn = findViewById(R.id.registerBtn);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();



    }

    public void registerUser(View view) {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String fname = etFname.getText().toString().trim();
        final String lname = etFname.getText().toString().trim();
        final String uname = etUname.getText().toString().trim();

        if(fname.isEmpty()){
            etFname.setError("First name is required");
            etFname.requestFocus();
            return;
        }

        if(lname.isEmpty()){
            etLname.setError("Last name is required");
            etLname.requestFocus();
            return;
        }

        if(uname.isEmpty()){
            etUname.setError("Username is required");
            etUname.requestFocus();
            return;
        }

        if(email.isEmpty()){
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return;
        }

        if(password.length() < 8) {
            etPassword.setError("Password should be at least 8 characters.");
            etPassword.requestFocus();
            return;
        }

        final Intent intent = new Intent(this, UserProfileActivity.class);

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "User succesfully registered.", Toast.LENGTH_SHORT).show();

                    Commuter commuter = new Commuter();
                    commuter.setFirstname(fname);
                    commuter.setLastname(lname);
                    commuter.setUsername(uname);
                    commuter.setEmailaddress(email);
                    commuter.setPassword(password);

                    FirebaseDatabase.getInstance().getReference("Commuter").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(commuter).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Registration success", Toast.LENGTH_SHORT).show();
                            } else {
                                // not success
                            }
                        }
                    });

                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(2000); // As I am using LENGTH_LONG in Toast
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    thread.start();
                } else {
                    Toast.makeText(SignUpActivity.this, "Some error occured.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //INITIALIZE FIREBASE
    /*private  void initializeFirebase() {
        Firebase.setAndroidContext(this);
    }*/
}
