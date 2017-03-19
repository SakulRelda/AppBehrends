package com.com.activites.logic;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lukasadler.test1.R;

import logical.Machine;

/**
 * @author Nico Wickersheim
 */
public class TabActivity extends AppCompatActivity {

    private Machine detailedMachine;

	/**
	 * Lifecylce Method of the Activity
	 * @param savedInstanceState
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        readIntentValue();
		setupNavigationView();
	}

	/**
	 * Reads the Value which is passed into the Activity
	 */
	private void readIntentValue(){
		Intent intent = getIntent();
		if(intent!=null){
			detailedMachine = (Machine) intent.getSerializableExtra("Machine");
		}else{
			return;
		}
	}

	/**
	 * Setup Method which creates the Tab Bottom Menu
	 */
	private void setupNavigationView() {
		BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
		if (bottomNavigationView != null) {

			// Select first menu item by default and show Fragment accordingly.
			Menu menu = bottomNavigationView.getMenu();
			selectFragment(menu.getItem(0));

			// Set action to perform when any menu-item is selected.
			bottomNavigationView.setOnNavigationItemSelectedListener(
					new BottomNavigationView.OnNavigationItemSelectedListener() {
						@Override
						public boolean onNavigationItemSelected(@NonNull MenuItem item) {
							selectFragment(item);
							return false;
						}
					});
		}
	}

	/**
	 * Select the Fragment which should be displayed
	 * @param item
	 */
	protected void selectFragment(MenuItem item) {
		item.setChecked(true);
		BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
		Bundle bundle = new Bundle();
		bundle.putSerializable("Machine",detailedMachine);
		switch (item.getItemId()) {
			case R.id.fragment_overview:
				bottomNavigationView.getMenu().findItem(R.id.fragment_main).setChecked(false);
				OverviewFragment overviewFragment = new OverviewFragment();
				overviewFragment.setArguments(bundle);
				pushFragment(overviewFragment);
				break;
			case R.id.fragment_main:
				bottomNavigationView.getMenu().findItem(R.id.fragment_overview).setChecked(false);
				MaintenanceFragment mainFragment = new MaintenanceFragment();
				mainFragment.setArguments(bundle);
				pushFragment(mainFragment);
				break;
		}
	}


	/**
	 * Shows up a Fragment in the Activity
	 * @param fragment
	 */
	protected void pushFragment(Fragment fragment) {
		if (fragment == null)
			return;

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.frameLayout, fragment);
		ft.commit();


	}
}
