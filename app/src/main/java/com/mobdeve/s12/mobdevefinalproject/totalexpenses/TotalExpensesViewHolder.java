package com.mobdeve.s12.mobdevefinalproject.totalexpenses;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s12.mobdevefinalproject.R;

import org.jetbrains.annotations.NotNull;

public class TotalExpensesViewHolder extends RecyclerView.ViewHolder {

    private TextView tvRvTotalExpensesCategory;
    private TextView tvRvTotalExpensesExpenses;
    private TextView tvRvTotalExpensesProfits;

    public TotalExpensesViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        tvRvTotalExpensesCategory = itemView.findViewById(R.id.tv_rv_total_expenses_category);
        tvRvTotalExpensesExpenses = itemView.findViewById(R.id.tv_rv_total_expenses_expenses);
        tvRvTotalExpensesProfits = itemView.findViewById(R.id.tv_rv_total_expenses_profits);
    }

    public void setTvRvTotalExpensesCategory(String text) {
        tvRvTotalExpensesCategory.setText(text);
    }

    public void setTvRvTotalExpensesExpenses(String text) {
        tvRvTotalExpensesExpenses.setText(text);
    }

    public void setTvRvTotalExpensesProfits(String text) {
        tvRvTotalExpensesProfits.setText(text);
    }

    public void setTvRvTotalExpensesExpensesVisibility() {
        tvRvTotalExpensesExpenses.setVisibility(View.GONE);
    }

    public void setTvRvTotalProfitsVisibility() {
        tvRvTotalExpensesProfits.setVisibility(View.GONE);
    }
}
