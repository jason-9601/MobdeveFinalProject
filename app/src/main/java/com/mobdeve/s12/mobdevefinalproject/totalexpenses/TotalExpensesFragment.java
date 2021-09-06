package com.mobdeve.s12.mobdevefinalproject.totalexpenses;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mobdeve.s12.mobdevefinalproject.R;
import com.mobdeve.s12.mobdevefinalproject.addexpenses.AddExpensesAdapter;
import com.mobdeve.s12.mobdevefinalproject.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    private ArrayList<String> totalYearMonthCategories;
    private ArrayList<String> totalYearMonthExpenses;

    private Spinner spTotalExpensesMonth;
    private Spinner spTotalExpensesYear;

    private ArrayAdapter<String> spTotalExpensesYearAdapter;
    private ArrayAdapter<CharSequence> spTotalExpensesMonthAdapter;

    private TextView tvTotalExpensesExpenses;
    private TextView tvTotalExpensesProfits;
    private PieChart pieChartTotalExpenses;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public TotalExpensesFragment() {

    }

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

        spTotalExpensesMonth = view.findViewById(R.id.sp_total_expenses_month);
        spTotalExpensesYear = view.findViewById(R.id.sp_total_expenses_year);
        tvTotalExpensesExpenses = view.findViewById(R.id.tv_total_expenses_expenses);
        tvTotalExpensesProfits = view.findViewById(R.id.tv_total_expenses_profits);
        pieChartTotalExpenses = view.findViewById(R.id.pie_chart_total_expenses);

        populateMonthSpinner();
        populateYearSpinner();

        initArrayLists();
        readAllCategoryExpenses();
        readAllCategoryProfits();
        initRecyclerView(view);
        initSpinnerListeners();

        setupPieChartExpenses();
        loadPieChartExpenses();

        tvTotalExpensesExpenses.setText("Expenses: " + Float.toString(getYearMonthTotalExpenses()));
        tvTotalExpensesProfits.setText("Profits: " + Float.toString(getYearMonthTotalProfits()));

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
        totalYearMonthExpenses = new ArrayList<>();
        totalYearMonthCategories = new ArrayList<>();
    }

    private void clearArrayLists() {
        totalExpenseCategoryList.clear();
        totalExpenseExpensesList.clear();
        totalExpenseProfitsList.clear();
        totalYearMonthExpenses.clear();
        totalYearMonthCategories.clear();
    }

    private void initHelpers() {
        dbHelper = new DatabaseHelper(getActivity());

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        spEditor = this.sp.edit();

        loggedInUser = sp.getString("KEY_USERNAME_LOGGED_IN", "N/A");
        Log.d("Logged in user: ", loggedInUser);
    }

    private void readAllCategoryProfits() {
        String yearSelected = spTotalExpensesYear.getSelectedItem().toString();
        String monthSelected = spTotalExpensesMonth.getSelectedItem().toString();
        Cursor cursor = dbHelper.getYearMonthCategoryProfits(loggedInUser, yearSelected, monthSelected);
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
        String yearSelected = spTotalExpensesYear.getSelectedItem().toString();
        String monthSelected = spTotalExpensesMonth.getSelectedItem().toString();
        Cursor cursor = dbHelper.getYearMonthCategoryExpenses(loggedInUser, yearSelected, monthSelected);
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                totalExpenseCategoryList.add(cursor.getString(0));
                totalExpenseExpensesList.add(Float.toString(cursor.getFloat(1)));
                totalExpenseProfitsList.add("0");
            }
        }
    }

    private void updateRecyclerViewData() {
        readAllCategoryExpenses();
        readAllCategoryProfits();
        totalExpensesAdapter.notifyDataSetChanged();
    }

    private void readAllYearMonthCategoryExpenses() {
        String yearSelected = spTotalExpensesYear.getSelectedItem().toString();
        String monthSelected = spTotalExpensesMonth.getSelectedItem().toString();
        Cursor cursor = dbHelper.getYearMonthCategoryExpenses(loggedInUser, yearSelected, monthSelected);

        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                totalYearMonthCategories.add(cursor.getString(0));
                totalYearMonthExpenses.add(Float.toString(cursor.getFloat(1)));
            }
        }
    }

    private float getYearMonthTotalExpenses() {
        float totalExpenses = -1;

        String yearSelected = spTotalExpensesYear.getSelectedItem().toString();
        String monthSelected = spTotalExpensesMonth.getSelectedItem().toString();

        Cursor cursor = dbHelper.getTotalExpensesOfYearMonth(loggedInUser, yearSelected, monthSelected);

        if(cursor.moveToFirst()) {
            totalExpenses = cursor.getFloat(0);
        }

        return totalExpenses;
    }

    private float getYearMonthTotalProfits() {
        float totalProfits = -1;

        String yearSelected = spTotalExpensesYear.getSelectedItem().toString();
        String monthSelected = spTotalExpensesMonth.getSelectedItem().toString();

        Cursor cursor = dbHelper.getTotalProfitsOfYearMonth(loggedInUser, yearSelected, monthSelected);

        if(cursor.moveToFirst()) {
            totalProfits = cursor.getFloat(0);
        }

        return totalProfits;
    }

    private void setupPieChartExpenses() {
        pieChartTotalExpenses.setDrawHoleEnabled(true);
        pieChartTotalExpenses.setUsePercentValues(true);
        pieChartTotalExpenses.setEntryLabelTextSize(12);
        pieChartTotalExpenses.setEntryLabelColor(Color.BLACK);
        pieChartTotalExpenses.setCenterTextSize(20);
        pieChartTotalExpenses.getDescription().setEnabled(false);
        pieChartTotalExpenses.getLegend().setEnabled(false);
    }

    private void loadPieChartExpenses() {
        ArrayList<PieEntry> chartEntries = new ArrayList<>();

        readAllYearMonthCategoryExpenses();

        for (int i = 0; i < totalYearMonthExpenses.size(); i++) {
            String label = totalYearMonthCategories.get(i);
            chartEntries.add(new PieEntry(Float.parseFloat(totalYearMonthExpenses.get(i)) * -1,
                    label));
        }

        PieDataSet dataSet = new PieDataSet(chartEntries, "Expenses");

        dataSet.setColors(R.color.orange_1, R.color.blue_1, R.color.light_orange, R.color.blue_2);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChartTotalExpenses));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        if (totalYearMonthExpenses.size() != 0) {
            pieChartTotalExpenses.setCenterText("Total Expenses");
        } else {
            pieChartTotalExpenses.setCenterText("No Data Available");
        }

        pieChartTotalExpenses.setData(data);
        pieChartTotalExpenses.invalidate();
    }

    private void populateYearSpinner() {
        List<String> yearsList = dbHelper.getYearsOfExpenses(loggedInUser);

        // If user has no data, add current year to year spinner //
        if (yearsList.size() == 0) {
            String currentYear = Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
            yearsList.add(currentYear);
        }

        spTotalExpensesYearAdapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, yearsList);

        spTotalExpensesYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTotalExpensesYear.setAdapter(spTotalExpensesYearAdapter);
    }

    private void populateMonthSpinner() {
        spTotalExpensesMonthAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.months, android.R.layout.simple_spinner_item);
        spTotalExpensesMonthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTotalExpensesMonth.setAdapter(spTotalExpensesMonthAdapter);
    }

    private void initSpinnerListeners() {
        spTotalExpensesMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                clearArrayLists();
                tvTotalExpensesExpenses.setText("Expenses: " + Float.toString(getYearMonthTotalExpenses()));
                tvTotalExpensesProfits.setText("Profits: " + Float.toString(getYearMonthTotalProfits()));
                loadPieChartExpenses();
                updateRecyclerViewData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spTotalExpensesYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clearArrayLists();
                tvTotalExpensesExpenses.setText("Expenses: " + Float.toString(getYearMonthTotalExpenses()));
                tvTotalExpensesProfits.setText("Profits: " + Float.toString(getYearMonthTotalProfits()));
                loadPieChartExpenses();
                updateRecyclerViewData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               
            }
        });
    }

}
