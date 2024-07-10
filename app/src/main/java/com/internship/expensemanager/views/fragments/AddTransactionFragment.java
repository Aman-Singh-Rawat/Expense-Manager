package com.internship.expensemanager.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.internship.expensemanager.R;
import com.internship.expensemanager.adapters.AccountsAdapter;
import com.internship.expensemanager.adapters.CategoryAdapter;
import com.internship.expensemanager.databinding.FragmentAddTransactionBinding;
import com.internship.expensemanager.databinding.ListDialogBinding;
import com.internship.expensemanager.models.Account;
import com.internship.expensemanager.models.Category;
import com.internship.expensemanager.models.Transaction;
import com.internship.expensemanager.util.Constants;
import com.internship.expensemanager.util.Helper;
import com.internship.expensemanager.views.activities.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class AddTransactionFragment extends BottomSheetDialogFragment implements CategoryAdapter.CategoryClickListener, AccountsAdapter.AccountClickListener {
    FragmentAddTransactionBinding binding = null;
    AlertDialog categoryDialog, accountDialog;
    Transaction transaction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentAddTransactionBinding.inflate(inflater, container, false);

        listenerFunctionality(inflater);
        return binding.getRoot();
    }

    private void listenerFunctionality(LayoutInflater inflater) {
        transaction = new Transaction();
        btnFunctionality();
        binding.etDate.setOnClickListener(listener -> {
            selectDate();
        });
        binding.etCategory.setOnClickListener(listener -> {
            openCategoryDialog(inflater);
        });

        binding.btnSaveTransaction.setOnClickListener(listener -> {
            try {
                if (binding.etAmount.getText() != null && binding.etNote.getText() != null) {
                    double amount = Double.parseDouble(binding.etAmount.getText().toString());
                    String note = binding.etNote.getText().toString();

                    if (transaction.getType().equals(Constants.EXPENSE)) {
                        transaction.setAmount(amount * -1);
                    } else {
                        transaction.setAmount(amount);
                    }
                    transaction.setNote(note);
                } else {
                    Toast.makeText(requireContext(), "Please input values", Toast.LENGTH_LONG).show();
                }

                ((MainActivity) requireActivity()).viewModel.addTransactions(transaction);
                ((MainActivity) requireActivity()).getTransaction();
                dismiss();
            } catch (Exception e) {
                Toast.makeText(requireContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

        });

        binding.etAccount.setOnClickListener(listener -> {
            ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
            accountDialog = new AlertDialog.Builder(requireContext()).create();

            accountDialog.setView(dialogBinding.getRoot());
            ArrayList<Account> accounts = new ArrayList<>();
            accounts.add(new Account(0, "Cash"));
            accounts.add(new Account(0, "Bank"));
            accounts.add(new Account(0, "Paytm"));
            accounts.add(new Account(0, "EasyPaisa"));
            accounts.add(new Account(0, "Other"));

            AccountsAdapter accountsAdapter = new AccountsAdapter(getContext(), accounts, this);
            dialogBinding.rvListDialog.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.rvListDialog.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            dialogBinding.rvListDialog.setAdapter(accountsAdapter);

            accountDialog.show();
        });
    }

    private void openCategoryDialog(LayoutInflater inflater) {
        ListDialogBinding dialogBinding = ListDialogBinding.inflate(inflater);
        categoryDialog = new AlertDialog.Builder(requireContext()).create();

        categoryDialog.setView(dialogBinding.getRoot());
        CategoryAdapter categoryAdapter = new CategoryAdapter(getContext(), Constants.categories, this);
        dialogBinding.rvListDialog.setLayoutManager(new GridLayoutManager(getContext(), 3));
        dialogBinding.rvListDialog.setAdapter(categoryAdapter);

        categoryDialog.show();
    }

    private void selectDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext());
        datePickerDialog.setOnDateSetListener((datePicker, i, i1, i2) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
            calendar.set(Calendar.MONTH, datePicker.getMonth());
            calendar.set(Calendar.YEAR, datePicker.getYear());

            //SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy");
            String dateToShow = Helper.formatDate(calendar.getTime());
            binding.etDate.setText(dateToShow);

            transaction.setDate(calendar.getTime());
            transaction.setId(calendar.getTime().getTime());
            Log.d("dubbing", String.valueOf(calendar.getTime().getTime()));
        });
        datePickerDialog.show();
    }

    private void btnFunctionality() {
        binding.tvIncome.setOnClickListener(listener -> {
            binding.tvExpense.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.tvIncome.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.tvExpense.setTextColor(getContext().getColor(R.color.textColor));
            binding.tvIncome.setTextColor(getContext().getColor(R.color.greenColor));

            transaction.setType(Constants.INCOME);
        });

        binding.tvExpense.setOnClickListener(listener -> {
            binding.tvExpense.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.tvIncome.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.tvExpense.setTextColor(getContext().getColor(R.color.redColor));
            binding.tvIncome.setTextColor(getContext().getColor(R.color.textColor));

            transaction.setType(Constants.EXPENSE);
        });
    }

    @Override
    public void onCategoryClicked(Category category) {
        binding.etCategory.setText(category.getCategoryName());
        transaction.setCategory(category.getCategoryName());
        if (categoryDialog.isShowing())
            categoryDialog.dismiss();
    }

    @Override
    public void onAccountSelected(Account account) {
        binding.etAccount.setText(account.getAccountName());
        transaction.setAccount(account.getAccountName());
        if (accountDialog.isShowing())
            accountDialog.dismiss();
    }
}