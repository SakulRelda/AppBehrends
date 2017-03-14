package com.example.lukasadler.test1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

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
        listViewHandling();
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
                startActivityForResult(intent, Activity.RESULT_CANCELED);
            }
        });
    }

    /**
     * Handels the List View Data and the Events
     * Async loading Progress for the Maintenance Entrys
     */
    private void listViewHandling(){
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
    }

    /**
     * Downloads a Image for the Machine if it exists
     * @param model
     * @param imgPic
     */
    private void downloadImage(RepairHistory model, final ImageView imgPic){
        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference picRef = storageReference.child("MachinePhotos/" + model.getS_ID());
            final long byteVal = 1024 * 1024;
            final byte[][] vals = new byte[1][];
            picRef.getBytes(byteVal).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try {
                        File outputDir = getContext().getCacheDir();
                        File outputFile = File.createTempFile("temp.jpg",null,outputDir);

                        FileOutputStream st = new FileOutputStream(outputFile.getAbsolutePath());
                        st.write(bytes);
                        Picasso.with(getActivity().getApplicationContext()).load(outputFile).into(imgPic);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    System.out.println("HERE");
                }
            });
        } catch (Exception ex) {
            Log.d("Download ERROR", ex.toString());
        };
    }


    /**
     * Lifecycle of the Activity
     * --> If the Activity gets a Result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast t;
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case SummaryActivity.RESULT_OK:
                t = Toast.makeText(getActivity(), R.string.maintenanceCreated, Toast.LENGTH_SHORT);
                t.show();
                break;
            case SummaryActivity.RESULT_CANCELED:
                int toastText = R.string.maintenanceAborted;
                if(data != null) {
                    toastText = data.getIntExtra("result", R.string.maintenanceAborted);
                }
                t = Toast.makeText(getActivity(), toastText, Toast.LENGTH_SHORT);
                t.show();
                break;
            default:
                break;
        }
    }
}
