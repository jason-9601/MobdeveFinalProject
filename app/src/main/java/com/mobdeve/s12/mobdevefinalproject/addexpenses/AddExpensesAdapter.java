package com.mobdeve.s12.mobdevefinalproject.addexpenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s12.mobdevefinalproject.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AddExpensesAdapter extends RecyclerView.Adapter<AddExpensesViewHolder> {

    private ArrayList<Expenses> expenseList;

    public AddExpensesAdapter(ArrayList<Expenses> expenseList) {
        this.expenseList = expenseList;
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
        String currentExpenseTitle = this.expenseList.get(position).getExpenseTitle();
        String currentExpenseYear = this.expenseList.get(position).getExpenseYear();
        String currentExpenseMonth = this.expenseList.get(position).getExpenseMonth();
        String currentExpenseDay = this.expenseList.get(position).getExpenseDay();
        String currentExpenseAmount = this.expenseList.get(position).getExpenseAmount();

        holder.setTvExpenseTitle(currentExpenseTitle);
        holder.setTvExpenseDate(currentExpenseYear + "/" + currentExpenseMonth + "/" + currentExpenseDay);
        holder.setTvExpenseValue(currentExpenseAmount);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

}
