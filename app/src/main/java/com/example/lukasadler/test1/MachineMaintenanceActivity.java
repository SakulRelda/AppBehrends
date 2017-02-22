package com.example.lukasadler.test1;

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

import java.util.ArrayList;
import java.util.List;

import database.FirebaseHandler;
import logical.Machine;
import logical.RepairHistory;

public class MachineMaintenanceActivity extends AppCompatActivity {

    private Machine detailedMachine;

    private TextView txtMachineName;
    private TextView txtMachineLocation;
    private TextView txtMachineType;
    private ListView listView;
    private FloatingActionButton fab;
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

        //FIREBASE HANDLER
        handler = FirebaseHandler.getInstance();
        ArrayList<RepairHistory> repHisto = handler.readMaintenance(detailedMachine.getI_ID());
        MaintenanceAdapter adapter = new MaintenanceAdapter(this, repHisto);
        listView.setAdapter(adapter);
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


    private class MaintenanceAdapter extends ArrayAdapter
    {

        public MaintenanceAdapter(Context context, List list){
            super(context,0);
        }

        public MaintenanceAdapter(Context context, int resource) {
            super(context, resource);
        }

        @NonNull
        @Override
        public View getView(int pos, View convertView, final ViewGroup parent){
            if(convertView==null){
                convertView = getLayoutInflater().inflate(R.layout.maintenance_list_item, null);
            }
            TextView txtView = (TextView) convertView.findViewById(R.id.listItemMaintenanceTitle);
            final RepairHistory m = (RepairHistory) getItem(pos);
            txtView.setText(m.getD_repairDate().toString());
            return convertView;
        }


    }
}
