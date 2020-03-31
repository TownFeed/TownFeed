package com.townfeednews.adapterOld;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.townfeednews.IOClickListener.CategoryListHoriClickListener;
import com.townfeednews.R;
import com.townfeednews.model.rv_list.CategoryData;
import com.townfeednews.utils.AppPrefsMain;

import java.util.ArrayList;

public class DrawerCategoryAdapter extends RecyclerView.Adapter<DrawerCategoryAdapter.ViewHolder> {
    private ArrayList<CategoryData> categoryDataList;
    private Context mContext;
    private CategoryListHoriClickListener categoryListHoriClickListener;
    private static final String TAG = "DrawerCategoryAdapter";

    public DrawerCategoryAdapter(Context mContext, ArrayList<CategoryData> categoryDataList, CategoryListHoriClickListener categoryListHoriClickListener) {
        this.categoryDataList = categoryDataList;
        this.mContext = mContext;
        this.categoryListHoriClickListener = categoryListHoriClickListener;
    }

    @NonNull
    @Override
    public DrawerCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.grid_row_item_drawer, parent, false);

        return new DrawerCategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DrawerCategoryAdapter.ViewHolder holder, final int position) {
        Log.d("TAG", "onBindViewHolder: " + position + " Name " + categoryDataList.get(position).getCategoryName());
        //setCategorybackGround
        if (position < 11) {
//                    Log.d("TAG", "onClick: Position is less than 11");
            if (AppPrefsMain.isCategorySelected(mContext, String.valueOf(position))) {
                Log.d(TAG, "onClick: Position is less than 11 and got unselected.");
                holder.drawerCategoryItemLinearLayout.setBackgroundResource(R.drawable.category_item_bg_enabled);
                holder.drawerCategoryTitleTextView.setText(categoryDataList.get(position).getCategoryName());
                holder.drawerCategoryTitleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.dsgn_color_blue));
                holder.drawerCategoryIconImageView.setImageResource(categoryDataList.get(position).getEnableIcon());

            } else {
                Log.d(TAG, "onClick: Position is less than 11 and got selected");
                holder.drawerCategoryItemLinearLayout.setBackgroundResource(R.drawable.category_item_bg_disabled);
                holder.drawerCategoryTitleTextView.setText(categoryDataList.get(position).getCategoryName());
                holder.drawerCategoryTitleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.dsgn_color_grey));
                holder.drawerCategoryIconImageView.setImageResource(categoryDataList.get(position).getDisableIcon());
            }
        } else {
            if (!AppPrefsMain.isUserAdult(mContext)) {
                Log.d("TAG", "onClick: Position is 11 user 18 below");
                holder.drawerCategoryItemLinearLayout.setBackgroundResource(R.drawable.category_item_bg_inactive);
                holder.drawerCategoryTitleTextView.setText(categoryDataList.get(position).getCategoryName());
                holder.drawerCategoryTitleTextView.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
                holder.drawerCategoryIconImageView.setImageResource(categoryDataList.get(position).getDisableIcon());
                Log.d(TAG, "onClick: Position is equal 11 and got unselected.");

                // TODO: 30-01-2020 set Category deactive
//                Toast.makeText(mContext, "Please confirm your age 18+ (Adult)", Toast.LENGTH_SHORT).show();
            } else {
                if (AppPrefsMain.isCategorySelected(mContext, String.valueOf(position))) {
                    holder.drawerCategoryItemLinearLayout.setBackgroundResource(R.drawable.category_item_bg_enabled);
                    holder.drawerCategoryTitleTextView.setText(categoryDataList.get(position).getCategoryName());
                    holder.drawerCategoryTitleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.dsgn_color_blue));
                    holder.drawerCategoryIconImageView.setImageResource(categoryDataList.get(position).getEnableIcon());
                    Log.d(TAG, "onClick: Position is equal 11 and got unselected.");

                } else {
                    Log.d(TAG, "onClick: Position is equal 11 and got selected");
                    holder.drawerCategoryItemLinearLayout.setBackgroundResource(R.drawable.category_item_bg_disabled);
                    holder.drawerCategoryTitleTextView.setText(categoryDataList.get(position).getCategoryName());
                    holder.drawerCategoryTitleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.dsgn_color_grey));
                    holder.drawerCategoryIconImageView.setImageResource(categoryDataList.get(position).getDisableIcon());

                }
            }
        }

        holder.drawerCategoryItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPrefsMain.setAllSelected(mContext,false);
                if (position < 11) {
//                    Log.d("TAG", "onClick: Position is less than 11");
                    if (AppPrefsMain.isCategorySelected(mContext, String.valueOf(position))) {
                        AppPrefsMain.setCategorySelected(mContext, String.valueOf(position), false);

                        Log.d(TAG, "onClick: Position is less than 11 and got selected");
                        holder.drawerCategoryItemLinearLayout.setBackgroundResource(R.drawable.category_item_bg_disabled);
                        holder.drawerCategoryTitleTextView.setText(categoryDataList.get(position).getCategoryName());
                        holder.drawerCategoryTitleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.dsgn_color_grey));
                        holder.drawerCategoryIconImageView.setImageResource(categoryDataList.get(position).getDisableIcon());

                    } else {
                        AppPrefsMain.setCategorySelected(mContext, String.valueOf(position), true);
                        Log.d(TAG, "onClick: Position is less than 11 and got unselected.");
                        holder.drawerCategoryItemLinearLayout.setBackgroundResource(R.drawable.category_item_bg_enabled);
                        holder.drawerCategoryTitleTextView.setText(categoryDataList.get(position).getCategoryName());
                        holder.drawerCategoryTitleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.dsgn_color_blue));
                        holder.drawerCategoryIconImageView.setImageResource(categoryDataList.get(position).getEnableIcon());

                    }
                    categoryListHoriClickListener.onDrawerCategoryClick(categoryDataList.get(position).getCategoryId(), position);
                } else {
                    if (!AppPrefsMain.isUserAdult(mContext)) {
                        Log.d("TAG", "onClick: Position is 11 user 18 below");
                        Toast.makeText(mContext, "Please confirm your age 18+ (Adult)", Toast.LENGTH_SHORT).show();
                    } else {
                        if (AppPrefsMain.isCategorySelected(mContext, String.valueOf(position))) {
                            AppPrefsMain.setCategorySelected(mContext, String.valueOf(position), false);
                            Log.d(TAG, "onClick: Position is equal 11 and got selected");
                            holder.drawerCategoryItemLinearLayout.setBackgroundResource(R.drawable.category_item_bg_disabled);
                            holder.drawerCategoryTitleTextView.setText(categoryDataList.get(position).getCategoryName());
                            holder.drawerCategoryTitleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.dsgn_color_grey));
                            holder.drawerCategoryIconImageView.setImageResource(categoryDataList.get(position).getDisableIcon());

                        } else {
                            holder.drawerCategoryItemLinearLayout.setBackgroundResource(R.drawable.category_item_bg_enabled);
                            holder.drawerCategoryTitleTextView.setText(categoryDataList.get(position).getCategoryName());
                            holder.drawerCategoryTitleTextView.setTextColor(ContextCompat.getColor(mContext, R.color.dsgn_color_blue));
                            holder.drawerCategoryIconImageView.setImageResource(categoryDataList.get(position).getEnableIcon());

                            AppPrefsMain.setCategorySelected(mContext, String.valueOf(position), true);
                            Log.d(TAG, "onClick: Position is equal 11 and got unselected.");
                            categoryListHoriClickListener.onDrawerCategoryClick(categoryDataList.get(position).getCategoryId(), position);
                        }
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout drawerCategoryItemLinearLayout;
        private TextView drawerCategoryTitleTextView;
        private ImageView drawerCategoryIconImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            drawerCategoryIconImageView = itemView.findViewById(R.id.drawerCategoryIconImageView);
            drawerCategoryTitleTextView = itemView.findViewById(R.id.drawerCategoryTitleTextView);
            drawerCategoryItemLinearLayout = itemView.findViewById(R.id.categoryItemLinearLayout);
        }
    }
}
