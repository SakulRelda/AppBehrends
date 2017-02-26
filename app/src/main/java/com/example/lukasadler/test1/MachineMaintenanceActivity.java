package com.example.lukasadler.test1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import database.FirebaseHandler;
import logical.Machine;
import logical.RepairHistory;

public class MachineMaintenanceActivity extends AppCompatActivity {

    //MACHINE
    private Machine detailedMachine;

    //VIEWS
    private TextView txtMachineName;
    private TextView txtMachineLocation;
    private TextView txtMachineType;
    private ListView listView;
    private FloatingActionButton fab;

    //HANDLER
    private FirebaseHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_maintenance);

        //READ INTENT VALUE
        Intent intent = getIntent();
        if(intent!=null){
            detailedMachine = (Machine) intent.getSerializableExtra("Machine");
        }else{
            return;
        }

        //GET VIEWS
        txtMachineLocation = (TextView) findViewById(R.id.lblMachineLocation);
        txtMachineName = (TextView) findViewById(R.id.lblMachineName);
        txtMachineType = (TextView) findViewById(R.id.lblMachineTyp);
        listView = (ListView) findViewById(R.id.listMaintenance);
        fab = (FloatingActionButton) findViewById(R.id.fabAddMaintenance);

        //SET VALUES FOR THE MACHINE VIEW
        setValues(detailedMachine);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maschinance.firebaseio.com/Maintenance");
        FirebaseListAdapter<RepairHistory> firebaseListAdapter = new FirebaseListAdapter<RepairHistory>(
                this,
                RepairHistory.class,
                R.layout.maintenance_list_item,
                databaseReference) {
            @Override
            protected void populateView(View v, RepairHistory model, int position) {
                if(detailedMachine.getI_ID().equals(model.getS_machineID())){
                    TextView txtView = (TextView) v.findViewById(R.id.listItemMaintenanceTitle);
                    txtView.setText(model.getD_repairDate().toString());
                }

            }
        };
        listView.setAdapter(firebaseListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if(item instanceof RepairHistory){
                    RepairHistory rep = (RepairHistory) item;
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MachineMaintenanceActivity.this,CreateMaintenanceActivity.class);
                intent.putExtra("Machine", detailedMachine);
                startActivity(intent);
            }
        });


    }

    /**
     * Adds the Values to the View
     * @param m
     */
    public void setValues(Machine m){
        txtMachineName.setText(m.getS_Name());
        txtMachineLocation.setText(m.getS_MachineLocation());
        txtMachineType.setText(m.getS_Machinentyp());
    }

}
