package com.mobdeve.s12.mobdevefinalproject.addexpenses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;
import com.mobdeve.s12.mobdevefinalproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddExpensesFragment extends Fragment {

    private String loggedInUser;

    // Shared preferences //
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private RecyclerView rvAddExpenses;
    private RecyclerView.LayoutManager lmManager;
    private AddExpensesAdapter addExpensesAdapter;
    private FloatingActionButton fabAddExpense;
    private TextView tvExpensesAmount;
    private TextView tvProfitsAmount;

    private DatabaseHelper dbHelper;
    private ArrayList<Expenses> expenseList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AddExpensesFragment() {

    }

    public static AddExpensesFragment newInstance(String param1, String param2) {
        AddExpensesFragment fragment = new AddExpensesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        clearArrayLists();
        readAllExpenseTable();
        setTotalAmountTextViews();
        addExpensesAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_expenses, container, false);

        fabAddExpense = view.findViewById(R.id.fab_add_expense);
        tvExpensesAmount = view.findViewById(R.id.tv_expenses_amount);
        tvProfitsAmount = view.findViewById(R.id.tv_profits_amount);

        dbHelper = new DatabaseHelper(getActivity());
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        spEditor = this.sp.edit();

        loggedInUser = sp.getString("KEY_USERNAME_LOGGED_IN", "N/A");
        Log.d("Logged in user: ", loggedInUser);

        fabAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddExpensesInput.class);
                startActivity(intent);
            }
        });

        initArrayLists();
        readAllExpenseTable();
        initRecyclerView(view);
        setTotalAmountTextViews();

        return view;
    }

    private void initRecyclerView(View view) {
        this.rvAddExpenses = view.findViewById(R.id.rv_add_expenses);
        this.lmManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        this.rvAddExpenses.setLayoutManager(this.lmManager);
        this.addExpensesAdapter = new AddExpensesAdapter(expenseList);
        this.rvAddExpenses.setAdapter(addExpensesAdapter);
    }

    private void initArrayLists() {
        expenseList = new ArrayList<>();
    }

    private void clearArrayLists() {
        expenseList.clear();
    }

    private void readAllExpenseTable() {
        Cursor cursor = dbHelper.readAllUserExpenseTable(loggedInUser);
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {

                String expenseId = cursor.getString(0);
                String expenseTitle = cursor.getString(1);
                String expenseYear = cursor.getString(2);
                String expenseMonth = cursor.getString(3);
                String expenseDay = cursor.getString(4);
                String expenseAmount = cursor.getString(5);
                String expenseCategory = cursor.getString(6);

                for (int i = 0; i < 7; i++) {
                    String temptemp = cursor.getString(i);
                    Log.d("expensething", temptemp);
                }

                Expenses currentExpense = new Expenses(expenseId, expenseTitle,
                        expenseYear, expenseMonth, expenseDay,
                        expenseCategory, expenseAmount);

                expenseList.add(currentExpense);

            }
        }
    }

    private float getTotalExpenses() {
        float totalExpenses = -1;
        Cursor cursor = dbHelper.getUserTotalExpenses(loggedInUser);
        if(cursor.moveToFirst()) {
            totalExpenses = cursor.getFloat(0);
        }
        return totalExpenses;
    }

    private float getTotalProfits() {
        float totalProfits = -1;
        Cursor cursor = dbHelper.getUserTotalProfits(loggedInUser);
        if(cursor.moveToFirst()) {
            totalProfits = cursor.getFloat(0);
        }
        return totalProfits;
    }

    private void setTotalAmountTextViews() {
        tvExpensesAmount.setText(Float.toString(getTotalExpenses()));
        tvProfitsAmount.setText(Float.toString(getTotalProfits()));
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };

}
