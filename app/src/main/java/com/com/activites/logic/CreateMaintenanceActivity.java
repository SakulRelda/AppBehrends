package com.com.activites.logic;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lukasadler.test1.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import database.FirebaseHandler;
import logical.Machine;
import logical.RepairHistory;

public class CreateMaintenanceActivity extends AppCompatActivity {

    private ImageView imgViewMaintenance;
    private EditText txtRepairDesc;
    private EditText txtRepairShortDesc;
    private Button fab;
    private FloatingActionButton fabCamera;
    private Button saveMaintenance;
    private FirebaseHandler handler;
    private Machine machine;
    private Bitmap image;
    private StorageReference mStorage;
    private int finisherCode = 0;
    private final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * On Create Method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_maintenance);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //CHECK THE GIVEN INTENT
        Intent intent = getIntent();
        if(intent!= null){
            machine = (Machine) intent.getSerializableExtra("Machine");
        }else{
            return;
        }

        accessFields();

        //INIT FIREBASE HANDLER
        handler = FirebaseHandler.getInstance();

        //CREATE STORAGE OBJECT
        mStorage = FirebaseStorage.getInstance().getReference();

        if(!hasCamera()){
            fabCamera.setEnabled(false);
        }

        //SNAPSHOT FROM THE MAINTENANCE
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSnap();
            }
        });


        //SAVE A NEW MAINTENANCE ENTRY
        saveMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMaintenanceToDatabase();
            }
        });
    }

    /**
     * Method which is called after a Result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //IF A IMAGE IS CAPTURED
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap imageBit = (Bitmap) extra.get("data");
            imgViewMaintenance.setImageBitmap(imageBit);
            image = imageBit.createScaledBitmap(imageBit, 20, 20, true);
        }
    }

    /**
     * Access the View Fields
     */
    private void accessFields(){
        //GET VIEWS
        imgViewMaintenance = (ImageView) findViewById(R.id.imageViewNewMaintenance);
        txtRepairDesc = (EditText) findViewById(R.id.txtRepairDescription);
        fabCamera = (FloatingActionButton) findViewById(R.id.fabOpenCameraMaintenance);
        saveMaintenance = (Button) findViewById(R.id.btnCreateMaintenance);
        txtRepairShortDesc = (EditText) findViewById(R.id.txtShortDescription);
    }

    /**
     * Try to upload a Image with a specified ID
     * @param id - Image ID
     */
    private void uploadImage(String id){
        //TRY TO UPLOAD IMAGE
        try{
            if(image!=null){
                StorageReference ref = mStorage.child("MaintenancePhotos").child(id);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG,100,out);
                byte[] data = out.toByteArray();
                UploadTask uploader = ref.putBytes(data);
                uploader.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {}
                });
            }
        }catch(Exception ex){
            Log.d("UPLOAD ERROR", ex.toString());
        };
    }

    /**
     * Has the System a Camera
     * @return true -> Camera / false -> No Camera
     */
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    /**
     * Does a Camera Snap
     */
    private void cameraSnap(){
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
    }

    /**
     * Saves a Maintenance to the Database
     */
    private void saveMaintenanceToDatabase(){
        RepairHistory histo = new RepairHistory();
        histo.setS_author(machine.getI_uID());
        histo.setS_Description(txtRepairDesc.getText().toString());
        histo.setS_machineID(machine.getI_ID());
        histo.setS_shortDescr(txtRepairShortDesc.getText().toString());
        handler.saveMaintenance(histo);
        uploadImage(histo.getS_ID());
        finisherCode=1;
        onBackPressed();
    }

    /**
     * Checks if EditText is Empty
     * @param etText --> Edit Text
     * @return true --> EMPTY / false --> INSERT
     */
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    /**
     * Checks the Plausibility if all Files are exists
     * @return bool true -> Plausi CORRECT / false -> Plausi WRONG
     */
    private boolean checkPlausibility(){
        boolean retVal = true;
        StringBuilder builder = new StringBuilder();
        if(isEmpty(txtRepairDesc)){
            builder.append("EMPTY REPAIR DESCRIPTION");
            retVal=false;
        }
        if(isEmpty(txtRepairShortDesc)){
            builder.append("EMPTY REPAIR SHORT DESC");
            retVal=false;
        }
        if(retVal==false){
            Toast.makeText(this,builder.toString(),Toast.LENGTH_LONG).show();
        }
        return retVal;
    }

    @Override
    public void onBackPressed() {
        if(finisherCode==1){
            Intent retIntent = new Intent();
            setResult(RESULT_OK, retIntent);
            finish();
        }else{
            Intent retIntent = new Intent();
            setResult(RESULT_CANCELED, retIntent);
            finish();
        }
        super.onBackPressed();
    }

}
