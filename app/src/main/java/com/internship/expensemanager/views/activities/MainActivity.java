package com.internship.expensemanager.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.tabs.TabLayout;
import com.internship.expensemanager.R;
import com.internship.expensemanager.adapters.TransactionAdapter;
import com.internship.expensemanager.databinding.ActivityMainBinding;
import com.internship.expensemanager.models.Transaction;
import com.internship.expensemanager.util.Constants;
import com.internship.expensemanager.util.Helper;
import com.internship.expensemanager.viewmodels.MainViewModel;
import com.internship.expensemanager.views.fragments.AddTransactionFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    Calendar calendar;
    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpViews();
    }

    private void setUpViews() {
        setSupportActionBar(binding.materialToolbar);
        getSupportActionBar().setTitle("Transactions");

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Constants.setCategory();
        calendar = Calendar.getInstance();
        updateDate();

        binding.btnNextDate.setOnClickListener(listener -> {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, 1);
            } else if(Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.btnPreviousDate.setOnClickListener(listener -> {
            if (Constants.SELECTED_TAB == Constants.DAILY) {
                calendar.add(Calendar.DATE, -1);
            } else if(Constants.SELECTED_TAB == Constants.MONTHLY){
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });
        binding.floatingActionButton.setOnClickListener(listener -> {
            new AddTransactionFragment().show(getSupportFragmentManager(), null);
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")) {
                    Constants.SELECTED_TAB = 1;
                    updateDate();
                } else if (tab.getText().equals("Daily")) {
                    Constants.SELECTED_TAB = 0;
                    updateDate();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.rvTransaction.setLayoutManager(new LinearLayoutManager(this));
        viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {
                TransactionAdapter transactionAdapter =
                        new TransactionAdapter(MainActivity.this, transactions);

                binding.rvTransaction.setAdapter(transactionAdapter);
                if (transactions.size() > 0) {
                    binding.imgEmptyState.setVisibility(View.GONE);
                } else {
                    binding.imgEmptyState.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.totalAmount.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.tvTotal.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.tvExpense.setText(String.valueOf(aDouble));
            }
        });

        viewModel.totalIncome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.tvIncome.setText(String.valueOf(aDouble));
            }
        });
        viewModel.getTransactions(calendar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void updateDate() {
        if (Constants.SELECTED_TAB == Constants.DAILY) {
            binding.tvCurrentDate.setText(Helper.formatDate(calendar.getTime()));
        } else if (Constants.SELECTED_TAB == Constants.MONTHLY) {
            binding.tvCurrentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
        viewModel.getTransactions(calendar);
    }

    public void getTransaction() {
        viewModel.getTransactions(calendar);
    }


}