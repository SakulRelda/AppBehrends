package com.example.lukasadler.test1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private TextView txtRepairDate;
    private EditText txtRepairDesc;
    private Button fab;
    private FloatingActionButton fabCamera;
    private Button saveMaintenance;
    private FirebaseHandler handler;
    private Machine machine;
    private Bitmap image;
    private StorageReference mStorage;
    private final int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * On Create Method
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_maintenance);

        //CHECK THE GIVEN INTENT
        Intent intent = getIntent();
        if(intent!= null){
            machine = (Machine) intent.getSerializableExtra("Machine");
        }else{
            return;
        }

        //GET VIEWS
        imgViewMaintenance = (ImageView) findViewById(R.id.imageViewNewMaintenance);
        txtRepairDate = (TextView) findViewById(R.id.txtRepairDate);
        txtRepairDesc = (EditText) findViewById(R.id.txtRepairDescription);
        fabCamera = (FloatingActionButton) findViewById(R.id.fabOpenCameraMaintenance);
        saveMaintenance = (Button) findViewById(R.id.btnCreateMaintenance);

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
     * Try to upload a Image with a specified ID
     * @param id - Image ID
     */
    private void uploadImage(String id){
        //TRY TO UPLOAD IMAGE
        try{
            if(image!=null){
                StorageReference ref = mStorage.child("MachinePhotos").child(id);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG,100,out);
                byte[] data = out.toByteArray();
                UploadTask uploader = ref.putBytes(data);
                uploader.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {}
                });
            }
        }catch(Exception ex){};
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
        handler.saveMaintenance(histo);
        uploadImage(histo.getS_ID());
    }

}
