package com.example.lukasadler.test1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lukasadler.test1.R;

import database.FirebaseHandler;
import logical.Machine;

import static com.example.lukasadler.test1.R.id.fab;

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
    private ListView listView;
    private ImageView imgOverview;
    private FloatingActionButton fab;

    //HANDLER
    private FirebaseHandler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*        Intent intent = getIntent();
        if(intent!=null){
            detailedMachine = (Machine) intent.getSerializableExtra("Machine");
        }else{
            return;
        }*/

        txtMachineLocation = (TextView) getActivity().findViewById(R.id.lblMachineLocation);
        txtMachineName = (TextView) getActivity().findViewById(R.id.lblMachineName);
        txtMachineType = (TextView) getActivity().findViewById(R.id.lblMachineTyp);
        listView = (ListView) getActivity().findViewById(R.id.listMaintenance);
        imgOverview = (ImageView) getActivity().findViewById(R.id.imgViewOverview);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fabAddMaintenance);

    }


}
