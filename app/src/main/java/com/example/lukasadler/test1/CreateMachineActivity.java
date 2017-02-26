package com.example.lukasadler.test1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_machine);

        //GET VIEWS
        s_machineLocation = (EditText) findViewById(R.id.machineLocation);
        s_machineName = (EditText) findViewById(R.id.machineName);
        s_machineType = (EditText) findViewById(R.id.machineTyp);
        btn_Save = (Button) findViewById(R.id.btnCreateMachine);
        fab_BarcodeScanner = (FloatingActionButton) findViewById(R.id.fabBarcodeScannerMachine);
        fab_Camera = (FloatingActionButton) findViewById(R.id.fabOpenCameraMachine);
        img_Machine = (ImageView) findViewById(R.id.imageViewNewMachine);

        //CREATE HANDLER
        handler = FirebaseHandler.getInstance();

        if(!hasCamera()){
            fab_Camera.setEnabled(false);
            fab_BarcodeScanner.setEnabled(false);
        }

        //CAMERA SNAP
        fab_Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePicIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        //BARCODE SCANNER
        fab_BarcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(CreateMachineActivity.this);
                scanIntegrator.initiateScan();
            }
        });

        //SAVE LOGIC
        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Machine m = new Machine();
                m.setS_Machinentyp(s_machineType.getText().toString());
                m.setS_Name(s_machineName.getText().toString());
                m.setS_MachineLocation(s_machineLocation.getText().toString());
                FirebaseUser user = handler.getFirebaseUser();
                m.setI_uID(user.getUid());
                handler.saveMachine(m);
                finishActivity(0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extra = data.getExtras();
            Bitmap imageBit = (Bitmap) extra.get("data");
            img_Machine.setImageBitmap(imageBit);
        } else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                Log.d("Content Barcode", scanContent);
                Log.d("Format Barcode", scanFormat);
            } else {
                Toast t = Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT);
                t.show();
            }
        }
    }

    private boolean hasCamera(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }
}
