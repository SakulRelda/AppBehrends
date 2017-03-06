package com.example.lukasadler.test1;

import android.content.Intent;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayOutputStream;

import database.FirebaseHandler;
import logical.Machine;

public class CreateMachineActivity extends AppCompatActivity {

    private FirebaseHandler handler;
    private EditText s_machineName;
    private EditText s_machineType;
    private EditText s_machineLocation;
    private ImageView img_Machine;
    private Button btn_Save;
    private FloatingActionButton fab_Camera;
    private FloatingActionButton fab_BarcodeScanner;
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private Machine machine = new Machine();
    private StorageReference mStorage;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_machine);

        //CREATE HANDLER
        handler = FirebaseHandler.getInstance();
        if(handler.checkLoggedIn()==false){
            finish();
            Intent intent = new Intent(CreateMachineActivity.this, MainActivity.class);
            startActivity(intent);
        }


        //GET VIEWS
        s_machineLocation = (EditText) findViewById(R.id.machineLocation);
        s_machineName = (EditText) findViewById(R.id.machineName);
        s_machineType = (EditText) findViewById(R.id.machineTyp);
        btn_Save = (Button) findViewById(R.id.btnCreateMachine);
        fab_BarcodeScanner = (FloatingActionButton) findViewById(R.id.fabBarcodeScannerMachine);
        fab_Camera = (FloatingActionButton) findViewById(R.id.fabOpenCameraMachine);
        img_Machine = (ImageView) findViewById(R.id.imageViewNewMachine);



        //CREATE STORAGE OBJECT
        mStorage = FirebaseStorage.getInstance().getReference();


        if(!hasCamera()){
            fab_Camera.setEnabled(false);
            fab_BarcodeScanner.setEnabled(false);
        }

        //CAMERA SNAP
        fab_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraSnap();
            }
        });

        //BARCODE SCANNER
        fab_BarcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeSnap();
            }
        });

        //SAVE LOGIC
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMachineToDatabase();
            }
        });
    }

    /**
     * Method which is called after a Result
     * @param requestCode - reqCode
     * @param resultCode - resCode
     * @param data - Intent Data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //IF A IMAGE IS CAPTURED
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap imageBit = (Bitmap) extra.get("data");
            img_Machine.setImageBitmap(imageBit);
            image = imageBit.createScaledBitmap(imageBit,20,20,true);
        }
        //IF A BARCODE IS CAPTURED -> BY 3rd PARTY LIB
        else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                machine.setS_BarcodeValue(scanContent);
            } else {
                Toast t = Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    /**
     * Has the System a Camera
     * @return true -> Camera / false -> No Camera
     */
    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
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
        }catch(Exception ex){
            Log.d("UPLOAD ERROR", ex.toString());
        };
    }

    /**
     * Does a Camera Snap
     */
    private void cameraSnap(){
        Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
    }

    /**
     * Does a Barcode Snap
     */
    private void barcodeSnap(){
        IntentIntegrator scanIntegrator = new IntentIntegrator(CreateMachineActivity.this);
        scanIntegrator.initiateScan();
    }

    /**
     * Saves a Machine to the Database
     */
    private void saveMachineToDatabase(){
        machine.setS_Machinentyp(s_machineType.getText().toString());
        machine.setS_Name(s_machineName.getText().toString());
        machine.setS_MachineLocation(s_machineLocation.getText().toString());
        FirebaseUser user = handler.getFirebaseUser();
        machine.setI_uID(user.getUid());
        handler.saveMachine(machine);
        uploadImage(machine.getI_ID());
        finishActivity(0);
        onBackPressed();
    }

}
