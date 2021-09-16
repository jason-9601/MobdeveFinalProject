package com.mobdeve.s12.mobdevefinalproject.addexpenses;

import java.util.ArrayList;

public class Expenses {

    private String expenseId;
    private String expenseTitle;
    private String expenseYear;
    private String expenseMonth;
    private String expenseDay;
    private String expenseCategory;
    private String expenseAmount;
    
    public Expenses(String expenseId, String expenseTitle, String expenseYear,
                    String expenseMonth, String expenseDay, String expenseCategory,
                    String expenseAmount) {

        this.expenseId = expenseId;
        this.expenseTitle = expenseTitle;
        this.expenseYear = expenseYear;
        this.expenseMonth = expenseMonth;
        this.expenseDay = expenseDay;
        this.expenseCategory = expenseCategory;
        this.expenseAmount = expenseAmount;

    }

    public String getExpenseId() {
        return expenseId;
    }

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public String getExpenseYear() {
        return expenseYear;
    }

    public String getExpenseMonth() {
        return expenseMonth;
    }

    public String getExpenseDay() {
        return expenseDay;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

}
