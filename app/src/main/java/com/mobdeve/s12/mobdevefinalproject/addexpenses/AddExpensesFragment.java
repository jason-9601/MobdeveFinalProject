package com.mobdeve.s12.mobdevefinalproject.addexpenses;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    private ArrayList<String> expenseIdList;
    private ArrayList<String> expenseTitleList;
    private ArrayList<String> expenseYearList;
    private ArrayList<String> expenseMonthList;
    private ArrayList<String> expenseDayList;
    private ArrayList<String> expenseCategoryList;
    private ArrayList<String> expenseAmountList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddExpensesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddExpensesFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        this.addExpensesAdapter = new AddExpensesAdapter(expenseIdList, expenseTitleList,
                expenseYearList, expenseMonthList,
                expenseDayList, expenseAmountList);
        this.rvAddExpenses.setAdapter(addExpensesAdapter);
    }

    private void initArrayLists() {
        expenseIdList = new ArrayList<>();
        expenseTitleList = new ArrayList<>();
        expenseYearList = new ArrayList<>();
        expenseMonthList = new ArrayList<>();
        expenseDayList = new ArrayList<>();;
        expenseCategoryList = new ArrayList<>();;
        expenseAmountList = new ArrayList<>();;
    }

    private void clearArrayLists() {
        expenseIdList.clear();
        expenseTitleList.clear();
        expenseYearList.clear();
        expenseMonthList.clear();
        expenseDayList.clear();
        expenseCategoryList.clear();
        expenseAmountList.clear();
    }

    private void readAllExpenseTable() {
        Cursor cursor = dbHelper.readAllUserExpenseTable(loggedInUser);
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                expenseIdList.add(cursor.getString(0));
                expenseTitleList.add(cursor.getString(1));
                expenseYearList.add(cursor.getString(2));
                expenseMonthList.add(cursor.getString(3));
                expenseDayList.add(cursor.getString(4));
                expenseAmountList.add(cursor.getString(5));
                expenseCategoryList.add(cursor.getString(6));
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
}
