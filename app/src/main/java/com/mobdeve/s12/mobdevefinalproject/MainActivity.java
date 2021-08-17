package com.mobdeve.s12.mobdevefinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load ToDoFragment as default home page //
        this.loadFragment(new ToDoFragment());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.navigation_home:
                        loadFragment(new ToDoFragment());
                        return true;

                    case R.id.navigation_notes:
                        loadFragment(new NotesFragment());
                        return true;

                    case R.id.navigation_add_expenses:
                        loadFragment(new AddExpensesFragment());
                        return true;

                    case R.id.navigation_total_expenses:
                        loadFragment(new TotalExpensesFragment());
                        return true;
                }

                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}