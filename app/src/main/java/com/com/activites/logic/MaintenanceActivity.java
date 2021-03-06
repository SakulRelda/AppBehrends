package com.com.activites.logic;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lukasadler.test1.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import database.FirebaseHandler;
import logical.RepairHistory;

/**
 * @author Artur Stalbaum
 */

public class MaintenanceActivity extends AppCompatActivity {

    private RepairHistory repHisto;
    private TextView txtMaintenanceDate;
    private TextView txtMaintenanceDescription;
    private TextView txtMaintenanceShortDescription;
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
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        readIntentVal();
        connectViews();
        toolbarHandler();
        setupStatusbar();
        addValuesToViews();
    }

    /**
     * back navigation with toolbar
     */
    private void toolbarHandler(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setNavigationIcon(R.drawable.ic_nav_back);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * set color of statusbar
     */
    private void setupStatusbar(){
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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
        txtMaintenanceShortDescription = (TextView) findViewById(R.id.lblMaintenanceShort);
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
            txtMaintenanceShortDescription.setText(repHisto.getS_shortDescr());
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
                        File outputDir = MaintenanceActivity.this.getCacheDir();
                        File outputFile = File.createTempFile("temp.jpg",null,outputDir);

                        FileOutputStream st = new FileOutputStream(outputFile.getAbsolutePath());
                        st.write(bytes);
                        Picasso.with(MaintenanceActivity.this.getApplicationContext()).load(outputFile).into(imgMaintenanceOverview);
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
