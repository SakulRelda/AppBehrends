package com.com.activites.logic;

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

import com.example.lukasadler.test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import database.FirebaseHandler;

/**
 * @author Artur Stalbaum
 * Sign Up Activity
 * Creating a new User
 */
public class SignUpActivity extends AppCompatActivity {

    protected EditText passwordEditText;
    protected AutoCompleteTextView emailEditText;
    protected Button signUpButton;
    private FirebaseHandler handler;
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
        accessFields();
        setupStatusbar();

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = passwordEditText.getText().toString();
                String email = emailEditText.getText().toString();

                password = password.trim();
                email = email.trim();
                signUpHandler(password, email);

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
     * Access the View Fields
     */
    private void accessFields(){
        passwordEditText = (EditText)findViewById(R.id.passwordField);
        emailEditText = (AutoCompleteTextView)findViewById(R.id.emailField);
        signUpButton = (Button)findViewById(R.id.signupButton);
        handler = FirebaseHandler.getInstance();
    }


    /**
     * Handels the Sign Up for the new User
     * @param password
     * @param email
     */
    private void signUpHandler(String password, String email){
        //If Mail / PW is Empty --> Error appears
        if (password.isEmpty() || email.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
            builder.setMessage(R.string.signup_error_message)
                    .setTitle(R.string.signup_error_title)
                    .setPositiveButton(android.R.string.ok, null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            //Create a new User with the Mail / Password at the Database
            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            //If creation was successfull --> Go back to the Main Activity
                            if (task.isSuccessful()) {
                                handler.logOutUser();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                //If creation wasn't successfull -> Error Appears
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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
