package com.internship.expensemanager.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.internship.expensemanager.R;
import com.internship.expensemanager.databinding.RowTransactionBinding;
import com.internship.expensemanager.models.Category;
import com.internship.expensemanager.models.Transaction;
import com.internship.expensemanager.util.Constants;
import com.internship.expensemanager.util.Helper;
import com.internship.expensemanager.views.activities.MainActivity;

import java.util.ArrayList;

import io.realm.RealmResults;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    
    Context context;
    RealmResults<Transaction> transactions;

    public TransactionAdapter(Context context, RealmResults<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.row_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.binding.tvTransactionAmount.setText(String.valueOf(transaction.getAmount()));
        holder.binding.tvAccountLabel.setText(transaction.getAccount());
        holder.binding.tvTransactionDate.setText(Helper.formatDate(transaction.getDate()));
        holder.binding.tvTransactionCategory.setText(transaction.getCategory());

        Category transactionCategory = Constants.getCategory(transaction.getCategory());
        if (transactionCategory != null) {
            holder.binding.imgCategory.setImageResource(transactionCategory.getCategoryImage());
            holder.binding.imgCategory.setBackgroundTintList(context.getColorStateList(transactionCategory.getCategoryColor()));

            holder.binding.tvAccountLabel.setBackgroundTintList(context.getColorStateList(
                    Constants.getAccountColor(transaction.getAccount())
            ));

            if (transaction.getType().equals(Constants.INCOME)) {
                holder.binding.tvTransactionAmount.setTextColor(context.getColor(R.color.greenColor));
            } else if (transaction.getType().equals(Constants.EXPENSE)) {
                holder.binding.tvTransactionAmount.setTextColor(context.getColor(R.color.redColor));
            }
        } else {
            Toast.makeText(context, "Something gonna wrong", Toast.LENGTH_LONG).show();
        }
        holder.itemView.setOnLongClickListener(listener -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Delete Transaction");
            alertDialog.setMessage("Are you sure to delete this transaction?");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", (dialogInterface, i) -> {
                ((MainActivity)context).viewModel.deleteTransactions(transaction);
            } );
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", ((dialog, which) -> {
                alertDialog.dismiss();
            }));
            alertDialog.show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        RowTransactionBinding binding;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = RowTransactionBinding.bind(itemView);
        }
    }
}
