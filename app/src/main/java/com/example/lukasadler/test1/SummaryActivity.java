package com.example.lukasadler.test1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import database.FirebaseHandler;
import logical.Machine;

public class SummaryActivity extends AppCompatActivity {

    protected FloatingActionButton floatingButton;
    protected LinearLayout linearLayout;
    protected ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Window w = this.getWindow();
        w.setStatusBarColor(this.getResources().getColor(R.color.colorTeal));

        //GET VIEWS
        floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        linearLayout = (LinearLayout) findViewById(R.id.linearMachineLayout);
        list = (ListView) findViewById(R.id.listen);

        //LIFETIME LISTENER FOR THE DATABASE
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://maschinance.firebaseio.com/Machine");
        FirebaseListAdapter<Machine> firebaseListAdapter = new FirebaseListAdapter<Machine>(
                this,
                Machine.class,
                R.layout.list_item,
                databaseReference) {
            @Override
            protected void populateView(View v, final Machine model, int position) {
                TextView txtView = (TextView) v.findViewById(R.id.listItemTitle);
                txtView.setText(model.getS_Name());
                ImageView imgViewEdit = (ImageView) v.findViewById(R.id.listItemEdit);
                ImageView imgViewDelete = (ImageView) v.findViewById(R.id.listItemDelete);

                imgViewEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //EDIT LOGIC
                    }
                });

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
            }
        };
        list.setAdapter(firebaseListAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                 if(item instanceof Machine) {
                     Machine m = (Machine) item;
                     Intent intent = new Intent(getApplicationContext(), MachineMaintenanceActivity.class);
                     intent.putExtra("Machine", m);
                     startActivity(intent);
                 }
            }
        });


        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, CreateMachineActivity.class);
                startActivityForResult(intent,0);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            Toast t = Toast.makeText(this, "Maschine erstellt",Toast.LENGTH_SHORT);
            t.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
