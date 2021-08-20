package com.mobdeve.s12.mobdevefinalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddExpensesAdapter extends RecyclerView.Adapter<AddExpensesViewHolder> {

    private ArrayList<String> expenseTitleList;
    private ArrayList<String> expenseYearList;
    private ArrayList<String> expenseMonthList;
    private ArrayList<String> expenseDayList;
    private ArrayList<String> expenseAmountList;

    public AddExpensesAdapter(ArrayList<String> expenseTitleList,
                              ArrayList<String> expenseYearList,
                              ArrayList<String> expenseMonthList,
                              ArrayList<String> expenseDayList,
                              ArrayList<String> expenseAmountList) {
        this.expenseTitleList = expenseTitleList;
        this.expenseYearList = expenseYearList;
        this.expenseMonthList = expenseMonthList;
        this.expenseDayList = expenseDayList;
        this.expenseAmountList = expenseAmountList;
    }

    @NonNull
    @NotNull
    @Override
    public AddExpensesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.rv_template_add_expenses, parent, false);
        AddExpensesViewHolder addExpensesViewHolder = new AddExpensesViewHolder(itemView);
        return addExpensesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AddExpensesViewHolder holder, int position) {
        String currentExpenseTitle = this.expenseTitleList.get(position);
        String currentExpenseYear = this.expenseYearList.get(position);
        String currentExpenseMonth = this.expenseMonthList.get(position);
        String currentExpenseDay = this.expenseDayList.get(position);
        String currentExpenseAmount = this.expenseAmountList.get(position);

        holder.setTvExpenseTitle(currentExpenseTitle);
        holder.setTvExpenseDate(currentExpenseDay + " " + currentExpenseMonth);
        holder.setTvExpenseValue(currentExpenseAmount);
    }

    @Override
    public int getItemCount() {
        return expenseTitleList.size();
    }
}
