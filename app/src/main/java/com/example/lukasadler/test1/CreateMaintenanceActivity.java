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
    private FloatingActionButton fab;
    private Button saveMaintenance;
    private FirebaseHandler handler = FirebaseHandler.getInstance();
    private Machine machine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_maintenance);

        Intent intent = getIntent();
        if(intent!=null){
            machine = (Machine) intent.getSerializableExtra("Machine");
        }else{
            return;
        }



        //GET VIEWS
        imgViewMaintenance = (ImageView) findViewById(R.id.imageViewNewMaintenance);
        txtRepairDate = (TextView) findViewById(R.id.txtRepairDate);
        txtRepairDesc = (EditText) findViewById(R.id.txtRepairDescription);
        fab = (FloatingActionButton) findViewById(R.id.fabAddMaintenance);
        saveMaintenance = (Button) findViewById(R.id.btnCreateMaintenance);

        //INIT FIREBASE HANDLER

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OPEN CAMERA
            }
        });

        saveMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RepairHistory histo = new RepairHistory();
                histo.setD_repairDate(new Date());
                histo.setS_author(machine.getI_uID()); //NEEDED
                histo.setS_Description(txtRepairDesc.getText().toString());
                histo.setS_machineID(machine.getI_ID());//NEEDED
                handler.saveMaintenance(histo);
            }
        });

    }
}
