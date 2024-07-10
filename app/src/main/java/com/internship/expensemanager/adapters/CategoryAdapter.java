package com.internship.expensemanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.internship.expensemanager.R;
import com.internship.expensemanager.databinding.SampleCategoryItemsBinding;
import com.internship.expensemanager.models.Category;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context context;
    ArrayList<Category> categories;
    public interface CategoryClickListener {
        void onCategoryClicked(Category category);
    }
    CategoryClickListener listener;

    public CategoryAdapter(Context context, ArrayList<Category> categories, CategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }
    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sample_category_items, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.binding.tvCategory.setText(category.getCategoryName());
        holder.binding.imgCategory.setImageResource(category.getCategoryImage());
        holder.binding.tvCategory.setBackgroundTintList(context.getColorStateList(category.getCategoryColor()));
        holder.itemView.setOnClickListener(c -> {
            listener.onCategoryClicked(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        SampleCategoryItemsBinding binding;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = SampleCategoryItemsBinding.bind(itemView);
        }
    }
}
