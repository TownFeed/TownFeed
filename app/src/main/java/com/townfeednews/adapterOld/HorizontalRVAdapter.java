package com.townfeednews.adapterOld;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.townfeednews.IOClickListener.CategoryListHoriClickListener;
import com.townfeednews.R;
import com.townfeednews.model.rv_list.CategoryData;
import com.townfeednews.utils.AppConstant;

import java.util.ArrayList;

public class HorizontalRVAdapter extends RecyclerView.Adapter<HorizontalRVAdapter.ViewHolder> {
    private ArrayList<CategoryData> categoryDataList;
    private Context mContext;
    private CategoryListHoriClickListener categoryListHoriClickListener;

    public HorizontalRVAdapter(Context mContext, ArrayList<CategoryData> categoryDataList, CategoryListHoriClickListener categoryListHoriClickListener) {
        this.categoryDataList = categoryDataList;
        this.mContext = mContext;
        this.categoryListHoriClickListener = categoryListHoriClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.hori_row_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.categoryNameTextView.setText(categoryDataList.get(position).getCategoryName());

        if (AppConstant.selectedPositionHorizontalCategoryList == position) {
            holder.categoryNameTextView.setBackgroundResource(R.drawable.hori_row_item_active);
            holder.categoryNameTextView.setTextColor(Color.WHITE);
            holder.itemView.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.categoryNameTextView.setBackgroundResource(R.drawable.hori_row_item_inactive);
            holder.categoryNameTextView.setTextColor(ContextCompat.getColor(mContext, R.color.dsgn_color_blue));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryListHoriClickListener.onHoriCategoryClick(categoryDataList.get(position).getCategoryId(), position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryNameTextView = itemView.findViewById(R.id.categoryNameTextView);
        }
    }
}
