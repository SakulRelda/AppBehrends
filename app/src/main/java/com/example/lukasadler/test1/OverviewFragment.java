package com.example.lukasadler.test1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import database.FirebaseHandler;
import logical.Machine;

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
    private FloatingActionButton fab;

    //HANDLER
    private FirebaseHandler handler;


    public static OverviewFragment newInstance(Machine machine) {
        OverviewFragment fragment = new OverviewFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("Machine", machine);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        detailedMachine = (Machine) getArguments().getSerializable("Machine");
        return inflater.inflate(R.layout.fragment_overview, container, false);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*txtMachineLocation = (TextView) getActivity().findViewById(R.id.lblMachineLocation);
        txtMachineName = (TextView) getActivity().findViewById(R.id.lblMachineName);
        txtMachineType = (TextView) getActivity().findViewById(R.id.lblMachineTyp);
        imgOverview = (ImageView) getActivity().findViewById(R.id.imgViewOverview);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fabAddMaintenance);

        txtMachineLocation.setText(detailedMachine.getS_MachineLocation());
        txtMachineName.setText(detailedMachine.getS_Name());
        txtMachineType.setText(detailedMachine.getS_Machinentyp());*/



    }


}
