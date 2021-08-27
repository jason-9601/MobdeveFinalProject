package com.mobdeve.s12.mobdevefinalproject.totalexpenses;

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
import android.widget.Toast;

import com.mobdeve.s12.mobdevefinalproject.R;
import com.mobdeve.s12.mobdevefinalproject.addexpenses.AddExpensesAdapter;
import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TotalExpensesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TotalExpensesFragment extends Fragment {

    private String loggedInUser;
    private DatabaseHelper dbHelper;

    // Shared preferences //
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;

    private RecyclerView rvTotalExpenses;
    private RecyclerView.LayoutManager lmManager;
    private TotalExpensesAdapter totalExpensesAdapter;
    private ArrayList<String> totalExpenseCategoryList;
    private ArrayList<String> totalExpenseExpensesList;
    private ArrayList<String> totalExpenseProfitsList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TotalExpensesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TotalExpensesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TotalExpensesFragment newInstance(String param1, String param2) {
        TotalExpensesFragment fragment = new TotalExpensesFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_total_expenses, container, false);

        initHelpers();

        loggedInUser = sp.getString("KEY_USERNAME_LOGGED_IN", "N/A");
        Log.d("Logged in user: ", loggedInUser);

        initArrayLists();
        readAllCategoryExpenses();
        readAllCategoryProfits();
        initRecyclerView(view);

        return view;
    }

    private void initRecyclerView(View view) {
        this.rvTotalExpenses = view.findViewById(R.id.rv_total_expenses);
        this.lmManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        this.rvTotalExpenses.setLayoutManager(this.lmManager);
        this.totalExpensesAdapter = new TotalExpensesAdapter(totalExpenseCategoryList,
                totalExpenseExpensesList, totalExpenseProfitsList);
        this.rvTotalExpenses.setAdapter(totalExpensesAdapter);
    }

    private void initArrayLists() {
        totalExpenseCategoryList = new ArrayList<>();
        totalExpenseExpensesList = new ArrayList<>();
        totalExpenseProfitsList = new ArrayList<>();
    }

    private void initHelpers() {
        dbHelper = new DatabaseHelper(getActivity());
        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        spEditor = this.sp.edit();
    }

    private void readAllCategoryProfits() {
        Cursor cursor = dbHelper.getAllCategoryProfits(loggedInUser);
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                totalExpenseCategoryList.add(cursor.getString(0));
                totalExpenseProfitsList.add(Float.toString(cursor.getFloat(1)));
                totalExpenseExpensesList.add("0");
            }
        }
    }

    private void readAllCategoryExpenses() {
        Cursor cursor = dbHelper.getAllCategoryExpenses(loggedInUser);
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                totalExpenseCategoryList.add(cursor.getString(0));
                Log.d("myerror", cursor.getString(0));
                totalExpenseExpensesList.add(Float.toString(cursor.getFloat(1)));
                Log.d("myerror", cursor.getString(1));
                totalExpenseProfitsList.add("0");
            }
        }
    }

}
