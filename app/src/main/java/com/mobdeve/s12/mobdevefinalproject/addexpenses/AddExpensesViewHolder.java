package com.mobdeve.s12.mobdevefinalproject.addexpenses;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mobdeve.s12.mobdevefinalproject.R;

import org.jetbrains.annotations.NotNull;

public class AddExpensesViewHolder extends RecyclerView.ViewHolder {

    private TextView tvExpenseTitle;
    private TextView tvExpenseDate;
    private TextView tvExpenseValue;

    public AddExpensesViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        tvExpenseTitle = itemView.findViewById(R.id.tv_expense_title);
        tvExpenseDate = itemView.findViewById(R.id.tv_expense_date);
        tvExpenseValue = itemView.findViewById(R.id.tv_expense_value);
    }

    public void setTvExpenseTitle(String text) {
        this.tvExpenseTitle.setText(text);
    }

    public void setTvExpenseDate(String text) {
        this.tvExpenseDate.setText(text);
    }

    public void setTvExpenseValue(String text) {
        this.tvExpenseValue.setText(text);
    }

}
