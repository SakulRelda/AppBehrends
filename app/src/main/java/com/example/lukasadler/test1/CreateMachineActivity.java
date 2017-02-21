package com.example.lukasadler.test1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseUser;

import database.FirebaseHandler;
import logical.Machine;

public class CreateMachineActivity extends AppCompatActivity {

    private FirebaseHandler handler;


    private EditText s_machineName;
    private EditText s_machineType;
    private EditText s_machineLocation;
    private Button btn_Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_machine);

        //GET Fields
        s_machineLocation = (EditText) findViewById(R.id.machineLocation);
        s_machineName = (EditText) findViewById(R.id.machineName);
        s_machineType = (EditText) findViewById(R.id.machineTyp);
        btn_Save = (Button) findViewById(R.id.btnCreateMachine);

        //Create Handler
        handler = FirebaseHandler.getInstance();

        //SAVE LOGIC
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SAVE LOGIC FOR THE BUTTON
                Machine m = new Machine();
                m.setS_Machinentyp(s_machineType.getText().toString());
                m.setS_Name(s_machineName.getText().toString());
                m.setS_MachineLocation(s_machineLocation.getText().toString());
                FirebaseUser user = handler.getFirebaseUser();
                m.setI_uID(user.getUid());
                handler.saveMachine(m);
            }
        });



    }
}
