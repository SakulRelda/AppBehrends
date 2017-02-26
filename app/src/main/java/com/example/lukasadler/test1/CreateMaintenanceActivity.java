package com.example.lukasadler.test1;

import android.content.Intent;
import android.media.Image;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

import database.FirebaseHandler;
import logical.Machine;
import logical.RepairHistory;

public class CreateMaintenanceActivity extends AppCompatActivity {


    private ImageView imgViewMaintenance;
    private TextView txtRepairDate;
    private EditText txtRepairDesc;
    private Button fab;
    private FloatingActionButton fabCamera;
    private FloatingActionButton fabBarCode;
    private Button saveMaintenance;
    private FirebaseHandler handler;
    private Machine machine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_maintenance);

        //CHECK THE GIVEN INTENT
        Intent intent = getIntent();
        if(intent!= null){
            machine = (Machine) intent.getSerializableExtra("Machine");
        }else{
            return;
        }

        //GET VIEWS
        imgViewMaintenance = (ImageView) findViewById(R.id.imageViewNewMaintenance);
        txtRepairDate = (TextView) findViewById(R.id.txtRepairDate);
        txtRepairDesc = (EditText) findViewById(R.id.txtRepairDescription);
        fabCamera = (FloatingActionButton) findViewById(R.id.fabOpenCameraMaintenance);
        fabBarCode = (FloatingActionButton) findViewById(R.id.fabBarcodeScannerMaintenance);
        saveMaintenance = (Button) findViewById(R.id.btnCreateMaintenance);

        //INIT FIREBASE HANDLER
        handler = FirebaseHandler.getInstance();


        //SNAPSHOT FROM THE MAINTENANCE
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: CREATE SNAPSHOT LOGIC
            }
        });

        //SNAPSHOT FROM THE BARCODE
        fabBarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: CREATE BARCODE LOGIC
            }
        });

        //SAVE A NEW MAINTENANCE ENTRY
        saveMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepairHistory histo = new RepairHistory();
                histo.setS_author(machine.getI_uID());
                histo.setS_Description(txtRepairDesc.getText().toString());
                histo.setS_machineID(machine.getI_ID());
                handler.saveMaintenance(histo);
            }
        });

    }
}
