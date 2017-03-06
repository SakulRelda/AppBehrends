package com.example.lukasadler.test1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Start Activity with the Login Possibility
 */
public class MainActivity extends AppCompatActivity {

    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button logInButton;
    protected TextView signUpTextView;
    protected ProgressDialog progressBar;
    private FirebaseAuth mFirebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ini for the Buttons, Fields...
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        emailEditText = (AutoCompleteTextView) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        logInButton = (Button) findViewById(R.id.email_sign_in_button);


        emailEditText.setText("sakulrelda@aol.com");
        passwordEditText.setText("71292al");


        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this, SummaryActivity.class));
        }

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpHandler();
            }
        });

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setMessage("Login...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressBar.show();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                email = email.trim();
                password = password.trim();

                loginHandler(email, password);
                Log.d("MainAct","LoggedIn");
            }
        });

    }


    /**
     * Method for the SignUp-Textfield Event
     */
    public void signUpHandler(){
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    /**
     * Method for the Login-Event
     * @param email
     * @param password
     */
    public void loginHandler(String email, String password){
        //If a required field is empty--> Error
        if (email.isEmpty() || password.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(R.string.login_error_message)
                    .setTitle(R.string.login_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            //Sign in with Mail and Password
            mFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if(task==null){
                                return;
                            }
                            //If login is successfull--> then...
                            if (task.isSuccessful()) {
                                progressBar.dismiss();
                                Intent intent = new Intent(MainActivity.this, SummaryActivity.class);
                                startActivity(intent);
                            } else {
                                //Login is incorrect --> Error
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setMessage(task.getException().getMessage())
                                        .setTitle(R.string.login_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
        }
    }




}




