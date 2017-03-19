package com.com.activites.logic;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lukasadler.test1.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import database.FirebaseHandler;
import logical.Machine;

/**
 *
 * @author Lukas Adler / Artur Stalbaum
 */
public class SummaryActivity extends AppCompatActivity {

    protected FloatingActionButton floatingButton;
    protected LinearLayout linearLayout;
    protected ListView list;
    protected FirebaseHandler handler;
    protected ProgressDialog progressBar;
    protected FirebaseUser user;

    /**
     * Lifecycle Method for the Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Window w = this.getWindow();
        w.setStatusBarColor(this.getResources().getColor(R.color.colorTeal));
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        accessFields();
        addTimer();

        //LIFETIME LISTENER FOR THE DATABASE
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maschinance.firebaseio.com/Machine");
        Query queryRef = databaseReference.orderByChild("i_uID").equalTo(user.getUid());
        FirebaseListAdapter<Machine> firebaseListAdapter = new FirebaseListAdapter<Machine>(
                this,
                Machine.class,
                R.layout.list_item,
                queryRef) {
            @Override
            protected void populateView(View v, final Machine model, int position) {
                TextView txtView = (TextView) v.findViewById(R.id.listItemTitle);
                txtView.setText(model.getS_Name());
                final ImageView imgPic = (ImageView) v.findViewById(R.id.imageViewItems);
                ImageView imgViewDelete = (ImageView) v.findViewById(R.id.listItemDelete);

                imgViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //DELETE LOGIC
                        AlertDialog.Builder dia = new AlertDialog.Builder(v.getRootView().getContext());
                        dia.setTitle(R.string.delete_machine);
                        dia.setMessage(R.string.delete_machine_text);
                        dia.setPositiveButton(R.string.ja_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                //DELETE PRESS YES
                                Toast.makeText(SummaryActivity.this, model.getS_Name(), Toast.LENGTH_SHORT).show();
                                FirebaseHandler h = FirebaseHandler.getInstance();
                                h.deleteMachine(model);
                            }
                        });
                        dia.setNegativeButton(R.string.nein_text, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                //DELETE PRESS NO
                                Toast.makeText(SummaryActivity.this, "DELETED", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dia.show();
                    }
                });
                progressBar.dismiss();
                downloadImage(model, imgPic);

            }
        };

        list.setAdapter(firebaseListAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if (item instanceof Machine) {
                    Machine m = (Machine) item;
                    //Intent intent = new Intent(getApplicationContext(), MachineMaintenanceActivity.class);
                    Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                    intent.putExtra("Machine", m);
                    startActivity(intent);
                }
            }
        });


        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createMachineIntent = new Intent(SummaryActivity.this, CreateMachineActivity.class);
                startActivityForResult(createMachineIntent, RESULT_CANCELED);

            }
        });
    }

    private boolean isUserLoggedIn(){
        return (handler.getFirebaseUser().isAnonymous()||handler.getFirebaseUser()==null);
    }


    /**
     * Implements a Timer for the Exception that no Machines are available
     */
    private void addTimer(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                progressBar.dismiss();
            }
        },5000);
    }

    /**
     * Access the View Fields
     */
    private void accessFields(){
        //GET VIEWS
        floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        linearLayout = (LinearLayout) findViewById(R.id.linearMachineLayout);
        list = (ListView) findViewById(R.id.listen);

        handler = FirebaseHandler.getInstance();
        user = handler.getFirebaseUser();
        progressBar = new ProgressDialog(this);
        progressBar.setMessage("Machine downloading...");
        progressBar.setCancelable(true);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
    }

    /**
     * Lifecycle of the Activity
     * --> If the Activity gets a Result
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast t;
        int barcodeRes = -1;
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case SummaryActivity.RESULT_OK:
                if(requestCode==49374){
                    IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                    if (scanningResult != null) {
                        String scanContent = scanningResult.getContents();
                        String scanFormat = scanningResult.getFormatName();
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maschinance.firebaseio.com/Machine");
                        databaseReference.orderByChild("s_BarcodeValue").equalTo(scanContent).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                Machine scannedMachine = (Machine) dataSnapshot.getValue(Machine.class);
                                if(scannedMachine!=null){
                                    Intent intent = new Intent(getApplicationContext(), TabActivity.class);
                                    intent.putExtra("Machine", scannedMachine);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                //DO NOTHING
                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                //DO NOTHING
                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                                //DO NOTHING
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                System.out.println("CANCELED");
                            }
                        });
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }else{
                    t = Toast.makeText(this, R.string.machineCreated, Toast.LENGTH_SHORT);
                    t.show();
                }
                break;
            case SummaryActivity.RESULT_CANCELED:
                int toastText = R.string.machineAborted;
                if(data != null) {
                    toastText = data.getIntExtra("result", R.string.machineAborted);
                }
                t = Toast.makeText(this, toastText, Toast.LENGTH_SHORT);
                t.show();
                break;

            default:
                break;
        }
    }

    /**
     * Menu Create Event
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Item Selected Events for the Menu
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logOutMenu:
                FirebaseHandler h = FirebaseHandler.getInstance();
                boolean logout = h.logOutUser();
                if (logout) {
                    Intent intent = new Intent(SummaryActivity.this
                            , MainActivity.class);
                    startActivity(intent);
                }
                return true;

            case R.id.searchBarcodeMenu:
                if(hasCamera()){
                    barcodeSnap();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
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
     * Does a Barcode Snap
     */
    private void barcodeSnap(){
        IntentIntegrator scanIntegrator = new IntentIntegrator(SummaryActivity.this);
        scanIntegrator.initiateScan();
    }

    /**
     * Downloads a Image for the Machine if it exists
     * @param model
     * @param imgPic
     */
    private void downloadImage(Machine model, final ImageView imgPic){
        try {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference picRef = storageReference.child("MachinePhotos/" + model.getI_ID());
            final long byteVal = 1024 * 1024;
            final byte[][] vals = new byte[1][];
            picRef.getBytes(byteVal).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try {
                        File outputDir = SummaryActivity.this.getCacheDir();
                        File outputFile = File.createTempFile("temp.jpg",null,outputDir);

                        FileOutputStream st = new FileOutputStream(outputFile.getAbsolutePath());
                        st.write(bytes);
                        Picasso.with(SummaryActivity.this).load(outputFile).into(imgPic);
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
     * Disable the Back Button Event
     */
    @Override
    public void onBackPressed() {
        //DO NOTHING
        AlertDialog.Builder alertLog = new AlertDialog.Builder(this);
        alertLog.setTitle(R.string.action_logout);
        alertLog.setMessage(R.string.logout_question);
        alertLog.setPositiveButton(R.string.ja_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseHandler h = FirebaseHandler.getInstance();
                boolean logout = h.logOutUser();
                if (logout) {
                    Intent intent = new Intent(SummaryActivity.this
                            , MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        alertLog.setNegativeButton(R.string.nein_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DO NOTHING
            }
        });
        AlertDialog alert = alertLog.create();
        alert.show();
    }
}
