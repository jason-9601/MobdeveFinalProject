package com.mobdeve.s12.mobdevefinalproject.totalexpenses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s12.mobdevefinalproject.R;
import com.mobdeve.s12.mobdevefinalproject.addexpenses.AddExpensesViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TotalExpensesAdapter extends RecyclerView.Adapter<TotalExpensesViewHolder> {

    private ArrayList<String> totalExpenseCategoryList;
    private ArrayList<String> totalExpenseExpensesList;
    private ArrayList<String> totalExpenseProfitsList;

    public TotalExpensesAdapter(ArrayList<String> totalExpenseCategoryList,
            ArrayList<String> totalExpenseExpensesList,
            ArrayList<String> totalExpenseProfitsList) {
        this.totalExpenseCategoryList = totalExpenseCategoryList;
        this.totalExpenseExpensesList = totalExpenseExpensesList;
        this.totalExpenseProfitsList = totalExpenseProfitsList;
    }

    @NonNull
    @NotNull
    @Override
    public TotalExpensesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.rv_template_total_expenses, parent, false);
        TotalExpensesViewHolder totalExpensesViewHolder = new TotalExpensesViewHolder(itemView);
        return totalExpensesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TotalExpensesViewHolder holder, int position) {
        String currentCategoryName = this.totalExpenseCategoryList.get(position);
        String currentExpenseAmount = this.totalExpenseExpensesList.get(position);
        String currentProfitAmount = this.totalExpenseProfitsList.get(position);

        holder.setTvRvTotalExpensesCategory(currentCategoryName);

        if (currentExpenseAmount.equals("0")) {
            holder.setTvRvTotalExpensesExpensesVisibility();
        } else {
            holder.setTvRvTotalExpensesExpenses(currentExpenseAmount);
        }

        if (currentProfitAmount.equals("0")) {
            holder.setTvRvTotalProfitsVisibility();
        } else {
            holder.setTvRvTotalExpensesProfits("+" + currentProfitAmount);
        }
        
    }

    @Override
    public int getItemCount() {
        return totalExpenseCategoryList.size();
    }

}
