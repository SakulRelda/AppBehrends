package com.example.lukasadler.test1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import database.FirebaseHandler;
import logical.Machine;

public class MaintenanceActivity extends AppCompatActivity {

    private Machine detailedMachine;
    private TextView txtMaintenanceDate;
    private TextView txtMaintenanceDescription;
    private ImageView imgMaintenanceOverview;

    //Database Handler
    private FirebaseHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        //READ INTENT VALUE
        Intent intent = getIntent();
        if(intent!=null){
            detailedMachine = (Machine) intent.getSerializableExtra("Machine");
        }else{
            return;
        }

        // GET VIEWS
        txtMaintenanceDate = (TextView) findViewById(R.id.lblMaintenanceDate);
        txtMaintenanceDescription = (TextView) findViewById(R.id.lblMaintenanceDescription);
        imgMaintenanceOverview = (ImageView) findViewById(R.id.imgMaintenanceOverview);


    }
}
