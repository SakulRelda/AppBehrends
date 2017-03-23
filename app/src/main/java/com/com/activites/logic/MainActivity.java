package com.com.activites.logic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lukasadler.test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Start Activity with the Login Possibility
 * and the possibility to start the SignUp Activity
 */
public class MainActivity extends AppCompatActivity {

    protected EditText emailEditText;
    protected EditText passwordEditText;
    protected Button logInButton;
    protected TextView signUpTextView;
    protected ProgressDialog progressBar;
    private FirebaseAuth mFirebaseAuth;

    /**
     * Activity Lifecycle
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        accessFields();
        checkIfUserLoggedIn();
        setupStatusbar();

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpHandler();
            }
        });
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            logInHandler();
            }
        });

    }

    /**
     * set color of statusbar
     */
    private void setupStatusbar(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }


    /**
     * Method for the SignUp-Textfield Event
     */
    public void signUpHandler(){
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    /**
     * Log In a User by its mail and pwd
     */
    private void logInHandler(){
        progressBar.show();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        email = email.trim();
        password = password.trim();
        logInUserWithMailAndPwd(email, password);
    }

    /**
     * Method for the Login-Event
     * @param email
     * @param password
     */
    private void logInUserWithMailAndPwd(String email, String password){
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

    /**
     * Access the View Fields
     */
    private void accessFields(){
        //Ini for the Buttons, Fields...
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        emailEditText = (AutoCompleteTextView) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        logInButton = (Button) findViewById(R.id.email_sign_in_button);


        emailEditText.setText("sakulrelda@aol.com");
        passwordEditText.setText("71292al");

        progressBar = new ProgressDialog(this);
        String s_auth = getResources().getString(R.string.authentication);
        progressBar.setTitle(s_auth);
        String s_authMessage = getResources().getString(R.string.login_progress);
        progressBar.setMessage(s_authMessage);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    /**
     * Check if a User is currently Logged In
     */
    private void checkIfUserLoggedIn(){
        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser()!=null){
            finish();
            startActivity(new Intent(this, SummaryActivity.class));
        }
    }

    /**
     * Disable the Back Button Event
     */
    @Override
    public void onBackPressed() {
        //DO NOTHING
    }
}




