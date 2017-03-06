package com.example.lukasadler.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import logical.RepairHistory;

public class MaintenanceActivity extends AppCompatActivity {

    private RepairHistory repHisto;
    private TextView txtMaintenanceDate;
    private TextView txtMaintenanceDescription;
    private ImageView imgMaintenanceOverview;

    //Database Handler
    private FirebaseHandler handler;

    /**
     * Lifecycle of the Activity
     * @param savedInstanceState - Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        readIntentVal();
        connectViews();
        addValuesToViews();
    }


    /**
     * Reads Intent Values
     */
    private void readIntentVal(){
        Intent intent = getIntent();
        if(intent!=null){
            repHisto = (RepairHistory) intent.getSerializableExtra("Repair");
        }else{
            return;
        }
    }

    /**
     * Finds the Views and adds them to the Fields
     */
    private void connectViews(){
        txtMaintenanceDate = (TextView) findViewById(R.id.lblMaintenanceDate);
        txtMaintenanceDescription = (TextView) findViewById(R.id.lblMaintenanceDescription);
        imgMaintenanceOverview = (ImageView) findViewById(R.id.imgMaintenanceOverview);
    }

    /**
     * Add Standard Values to the Views
     */
    private void addValuesToViews(){
        if(repHisto!=null){
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            txtMaintenanceDate.setText("Wartung: "+df.format("dd-MM-yyyy",repHisto.getD_repairDate()));
            txtMaintenanceDescription.setText(repHisto.getS_Description());
            downloadImage(repHisto);
        }
    }

    /**
     * Try to download an Image
     */
    private void downloadImage(RepairHistory repHisto){
        try{
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference picRef = storageReference.child("MaintenancePhotos/"+repHisto.getS_ID());
            final long byteVal = 1024 * 1024;
            final byte[][] vals = new byte[1][];
            picRef.getBytes(byteVal).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try {
                        File t = new File(getFilesDir()+"/temp.jpg");
                        if(t.exists()){
                            t.delete();
                        }

                        File test = new File(getFilesDir(), "temp.jpg");
                        FileOutputStream st = new FileOutputStream(test.getAbsolutePath());
                        st.write(bytes);
                        Picasso.with(MaintenanceActivity.this.getApplicationContext()).load(test).into(imgMaintenanceOverview);
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
