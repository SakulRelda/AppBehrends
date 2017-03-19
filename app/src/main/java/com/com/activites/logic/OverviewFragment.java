package com.com.activites.logic;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lukasadler.test1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import logical.Machine;

/**
 * Created by Wickersheim on 28.02.2017.
 */
public class OverviewFragment extends android.app.Fragment {

    //MACHINE
    private Machine detailedMachine;

    //VIEWS
    private TextView txtMachineName;
    private TextView txtMachineLocation;
    private TextView txtMachineType;
    private ImageView imgOverview;
    private FloatingActionButton fab;

    /**
     * Lifecycle of the Fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_overview,container,false);
        Bundle b = this.getArguments();
        detailedMachine = (Machine) b.getSerializable("Machine");
        accessFields(v,detailedMachine);
        downloadImage(detailedMachine);
        return v;
    }

    /**
     * Access Fields fro the View
     * @param v - View
     * @param m - Machine which should fill the data
     */
    private void accessFields(View v, Machine m){
        txtMachineName = (TextView) v.findViewById(R.id.lblMachineName);
        txtMachineLocation = (TextView) v.findViewById(R.id.lblMachineLocation);
        txtMachineType = (TextView) v.findViewById(R.id.lblMachineTyp);
        imgOverview = (ImageView) v.findViewById(R.id.imgViewOverview);
        fab = (FloatingActionButton) v.findViewById(R.id.fabAddMaintenance);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: ADD LOGIC FOR NEW MAINTENANCE
                Intent intent = new Intent(getActivity(),CreateMaintenanceActivity.class);
                intent.putExtra("Machine", detailedMachine);
                startActivityForResult(intent, Activity.RESULT_CANCELED);
            }
        });

        if(m!=null){
            txtMachineName.setText(m.getS_Name());
            txtMachineLocation.setText(m.getS_MachineLocation());
            txtMachineType.setText(m.getS_MachineType());
        }
    }

    /**
     * Try to download an Image for the Machine
     * @param m - Machine which Image should be downloaded
     */
    public void downloadImage(Machine m){
        if(m!=null){
            try{
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                StorageReference picRef = storageReference.child("MachinePhotos/"+m.getI_ID());
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
                            Picasso.with(getActivity().getApplicationContext()).load(outputFile).into(imgOverview);
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
            }catch(Exception ex){
                Log.d("DOWNLOAD ERROR", ex.toString());
            };
        }
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
