package com.example.lukasadler.test1;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import database.FirebaseHandler;
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

    //HANDLER
    private FirebaseHandler handler;


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

    private void accessFields(View v, Machine m){
        txtMachineName = (TextView) v.findViewById(R.id.lblMachineName);
        txtMachineLocation = (TextView) v.findViewById(R.id.lblMachineLocation);
        txtMachineType = (TextView) v.findViewById(R.id.lblMachineTyp);
        imgOverview = (ImageView) v.findViewById(R.id.imgViewOverview);

        txtMachineName.setText(m.getS_Name());
        txtMachineLocation.setText(m.getS_MachineLocation());
        txtMachineType.setText(m.getS_MachineType());
    }

    public void downloadImage(Machine m){
        try{
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference picRef = storageReference.child("MachinePhotos/"+m.getI_ID());
            final long byteVal = 1024 * 1024;
            final byte[][] vals = new byte[1][];
            picRef.getBytes(byteVal).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try {
                        File t = new File(getContext().getFilesDir()+"/temp.jpg");
                        if(t.exists()){
                            t.delete();
                        }

                        File test = new File(getContext().getFilesDir(), "temp.jpg");
                        FileOutputStream st = new FileOutputStream(test.getAbsolutePath());
                        st.write(bytes);
                        Picasso.with(getActivity().getApplicationContext()).load(test).into(imgOverview);
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
