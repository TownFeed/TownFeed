package com.townfeednews.adapterOld;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.townfeednews.IOClickListenerOld.CategoryItemClickListener;
import com.townfeednews.R;
import com.townfeednews.model.category.CategoryData;
import com.townfeednews.utils.AppConstant;
import com.townfeednews.utils.AppPrefs;

import java.util.ArrayList;

public class CategoriesItemsAdapter extends RecyclerView.Adapter<CategoriesItemsAdapter.ViewHolder> {

    private ArrayList<CategoryData> categoryData;
    private Context mContext;
    private CategoryItemClickListener categoryItemClickListener;

    public CategoriesItemsAdapter(ArrayList<CategoryData> categoryData, Context mContext, CategoryItemClickListener categoryItemClickListener) {
        this.categoryData = categoryData;
        this.mContext = mContext;
        this.categoryItemClickListener = categoryItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.category_view, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.categoryItemView.setText(categoryData.get(position).getCat_name());
        Log.d("TAG", "onBindViewHolder: Category Data " + position);
        if (categoryData.get(position).getCat_id().equals("18") || categoryData.get(position).getCat_id().equals("9")) {
            if (AppPrefs.getAdultChecked(mContext)) {
                if (categoryData.get(position).getStatus().equals("0")) {
                    holder.categoryItemView.setBackgroundResource(R.drawable.gender_item_bg_unselected);
                    holder.categoryItemView.setTextColor(Color.BLACK);
                    AppConstant.selectedCategories.remove(categoryData.get(position).getCat_id());
                } else {
                    holder.categoryItemView.setBackgroundResource(R.drawable.gender_item_bg_selected);
                    holder.categoryItemView.setTextColor(Color.WHITE);
                    AppConstant.selectedCategories.add(categoryData.get(position).getCat_id());
                }
                holder.categoryItemView.setEnabled(true);
            } else {
                Log.d("TAG", "onBindViewHolder: adult category found");
                holder.categoryItemView.setBackgroundResource(R.drawable.category_item_bg_disabled);
                holder.categoryItemView.setTextColor(Color.WHITE);
                holder.categoryItemView.setEnabled(false);
                AppConstant.selectedCategories.remove(categoryData.get(position).getCat_id());
            }
        } else {
            if (categoryData.get(position).getStatus().equals("0")) {
                holder.categoryItemView.setBackgroundResource(R.drawable.gender_item_bg_unselected);
                holder.categoryItemView.setTextColor(Color.BLACK);
                AppConstant.selectedCategories.remove(categoryData.get(position).getCat_id());
            } else {
                holder.categoryItemView.setBackgroundResource(R.drawable.gender_item_bg_selected);
                holder.categoryItemView.setTextColor(Color.WHITE);
                AppConstant.selectedCategories.add(categoryData.get(position).getCat_id());
            }
        }

        holder.categoryItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: Category Clicked... " + AppConstant.selectedCategories.size());
                categoryItemClickListener.onItemClick(categoryData.get(position).getCat_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryItemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryItemView = itemView.findViewById(R.id.categoryItemView);
        }
    }
}
