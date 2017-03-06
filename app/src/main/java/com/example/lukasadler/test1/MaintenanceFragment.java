package com.example.lukasadler.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import logical.Machine;
import logical.RepairHistory;

/**
 * Created by Wickersheim on 28.02.2017.
 */
public class MaintenanceFragment extends android.app.Fragment {

    private Machine detailedMachine;
    private FloatingActionButton fab;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        Bundle b = this.getArguments();
        detailedMachine = (Machine) b.getSerializable("Machine");
        accessFields(v);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maschinance.firebaseio.com/Maintenance");
        Query refQuery = databaseReference.orderByChild("s_machineID").equalTo(detailedMachine.getI_ID());
        FirebaseListAdapter<RepairHistory> firebaseListAdapter = new FirebaseListAdapter<RepairHistory>(
                getActivity(),
                RepairHistory.class,
                R.layout.maintenance_list_item,
                refQuery) {
            @Override
            protected void populateView(View v, RepairHistory model, int position) {
                TextView txtView = (TextView) v.findViewById(R.id.listItemMaintenanceTitle);
                android.text.format.DateFormat df = new android.text.format.DateFormat();
                txtView.setText("Wartung: "+df.format("dd-MM-yyyy",model.getD_repairDate()));

            }
        };
        listView.setAdapter(firebaseListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if(item instanceof RepairHistory){
                    RepairHistory rep = (RepairHistory) item;
                    Intent intent = new Intent(getActivity(),MaintenanceActivity.class);
                    intent.putExtra("Repair",rep);
                    getActivity().startActivity(intent);
                }
            }
        });
        return v;
    }

    /**
     * Access all Field from the View
     * @param v - Current View
     */
    private void accessFields(View v){
        listView = (ListView) v.findViewById(R.id.listMaintenance);
        fab = (FloatingActionButton) v.findViewById(R.id.fabAddMaintenance);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CreateMaintenanceActivity.class);
                intent.putExtra("Machine", detailedMachine);
                startActivity(intent);
            }
        });
    }
}
