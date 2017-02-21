package com.example.lukasadler.test1;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import database.FirebaseHandler;
import logical.Machine;
import styleviews.StyledTextView;

public class SummaryActivity extends AppCompatActivity {

    protected FloatingActionButton floatingButton;
    protected LinearLayout linearLayout;

    final ArrayList<Machine> machines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Window w = this.getWindow();
        w.setStatusBarColor(this.getResources().getColor(R.color.colorTeal));

        floatingButton = (FloatingActionButton) findViewById(R.id.fab);
        linearLayout = (LinearLayout) findViewById(R.id.linearMachineLayout);

        final FirebaseHandler h = FirebaseHandler.getInstance();
        final FirebaseUser u = h.getFirebaseUser();
        Log.d("Summary","Done");
        ArrayList<Machine> m = h.readMachines();
        MachineAdapter machines = new MachineAdapter(this,m);
        ListView list = (ListView) findViewById(R.id.listen);
        list.setAdapter(machines);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if(item instanceof Machine){
                    Machine m = (Machine) item;
                    Intent intent = new Intent(getApplicationContext(), MachineMaintenanceActivity.class);
                    intent.putExtra("Machine",m);
                    startActivity(intent);

                    //Toast.makeText(getApplicationContext(),m.getS_Name(),Toast.LENGTH_LONG);
                }
            }
        });

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, CreateMachineActivity.class);
                startActivity(intent);
                //startActivity(createMachine);
                //Machine m = new Machine();
                //m.setI_uID(u.getUid());
                //m.setS_Name("Maschinen Versuch");
                //m.setS_Machinentyp("Fraesmaschine");
                //h.saveMachine(m);

            }
        });
        Log.d("Summary","Done");
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
                if(logout){
                    Intent intent = new Intent(SummaryActivity.this
                            ,MainActivity.class);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class MachineAdapter extends ArrayAdapter {


        private Context context;
        private int resource;
        public Activity activity;
        private List list;

        public MachineAdapter(Context context, int resource, Activity act) {
            super(context, resource);
            this.context = context;
            this.resource = resource;
            this.activity = act;

        }

        /**
         *
         * @param context - Context like the Main Activity or something else
         * @param list - List with Items for the Adapter
         */
        public MachineAdapter(Context context, List list){
            //0--> Kein vordefiniertes Layout
            super(context,0,list);
            this.context = context;
            this.resource = 0;
            this.list = list;

        }

        @NonNull
        @Override
        public View getView(int pos, View convertView, final ViewGroup parent){
            if(convertView==null){
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);
            }
            TextView txtView = (TextView) convertView.findViewById(R.id.listItemTitle);
            final Machine m = (Machine) getItem(pos);
            txtView.setText(m.getS_Name());
            ImageView imgViewEdit = (ImageView) convertView.findViewById(R.id.listItemEdit);
            ImageView imgViewDelete = (ImageView) convertView.findViewById(R.id.listItemDelete);

            imgViewEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //EDIT LOGIC
                }
            });

            imgViewDelete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //DELETE LOGIC
                    AlertDialog.Builder dia = new AlertDialog.Builder(v.getRootView().getContext());
                    dia.setTitle(R.string.delete_machine);
                    dia.setMessage(R.string.delete_machine_text);
                    dia.setPositiveButton(R.string.ja_text,new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface arg0, int arg1){
                            //DELETE PRESS YES
                            Toast.makeText(SummaryActivity.this,m.getS_Name(),Toast.LENGTH_SHORT).show();
                            FirebaseHandler h = FirebaseHandler.getInstance();
                            h.deleteMachine(m);
                        }
                    });
                    dia.setNegativeButton(R.string.nein_text,new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface arg0, int arg1){
                            //DELETE PRESS NO
                            Toast.makeText(SummaryActivity.this,m.getS_Name(),Toast.LENGTH_SHORT).show();
                        }
                    });
                    dia.show();
                }
            });

            return convertView;

        }


    }


}
