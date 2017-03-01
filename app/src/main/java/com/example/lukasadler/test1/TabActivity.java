package com.example.lukasadler.test1;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class TabActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.activity_tab);

			setupNavigationView();
	}

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

	protected void selectFragment(MenuItem item) {

		item.setChecked(true);

		switch (item.getItemId()) {
			case R.id.fragment_overview:
				// Action to perform when Home Menu item is selected.
				pushFragment(new OverviewFragment());
				break;
			case R.id.fragment_main:
				// Action to perform when Bag Menu item is selected.
				pushFragment(new MainFragment());
				break;
		}
	}

	protected void pushFragment(Fragment fragment) {
		if (fragment == null)
			return;

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(R.id.frameLayout, fragment);
		ft.addToBackStack(null);
		ft.commit();


	}
}
