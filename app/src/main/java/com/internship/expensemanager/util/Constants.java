package com.internship.expensemanager.util;

import com.internship.expensemanager.R;
import com.internship.expensemanager.models.Category;

import java.util.ArrayList;

public class Constants {
    public static final String INCOME = "Income";
    public static final String EXPENSE = "Expense";
    public static final int DAILY = 0;
    public static final int MONTHLY = 1;
    public static final int CALENDAR = 2;
    public static final int SUMMARY = 3;
    public static final int NOTES = 4;

    public static int SELECTED_TAB = 0;
    public static ArrayList<Category> categories;
    public static void setCategory() {
        categories = new ArrayList<>();
        categories.add(new Category("Salary", R.drawable.ic_salary, R.color.categoryOne));
        categories.add(new Category("Business", R.drawable.ic_business, R.color.categoryTwo));
        categories.add(new Category("Investment", R.drawable.ic_investment, R.color.categoryThree));
        categories.add(new Category("Loan", R.drawable.ic_loan, R.color.categoryFour));
        categories.add(new Category("Rent", R.drawable.ic_rent, R.color.categoryFive));
        categories.add(new Category("Other", R.drawable.ic_other, R.color.categorySix));
    }
    
    public static Category getCategory(String categoryName) {
        for (Category cat:
             categories) {
            if (cat.getCategoryName().equals(categoryName)) {
                return cat;
            }
        }
        return null;
    }

    public static int getAccountColor(String accountName) {
        switch (accountName) {
            case "Bank":
                return R.color.blank_color;
            case "Cash":
                return R.color.cash_color;
            case "Card":
                return R.color.card_color;
            default:
                return R.color.default_color;

        }
    }
}
