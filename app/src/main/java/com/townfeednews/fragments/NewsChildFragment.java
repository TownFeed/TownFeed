package com.townfeednews.fragments;


import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.townfeednews.R;
import com.townfeednews.utils.AppConstant;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsChildFragment extends Fragment {
    private ImageView newsImageView;
    private TextView mainTitleTextView, descriptionTextView, categoryTextView, sourceTextView, postedDateTextView;
    private int position;
    private ImageView shareNewsImageView;

    public NewsChildFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_child, container, false);
        newsImageView = view.findViewById(R.id.newsImageView);
        mainTitleTextView = view.findViewById(R.id.mainTitleTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        sourceTextView = view.findViewById(R.id.sourceTextView);
        postedDateTextView = view.findViewById(R.id.postedDateTextView);
        categoryTextView = view.findViewById(R.id.categoryTextView);
        shareNewsImageView = view.findViewById(R.id.shareNewsImageView);

        Bundle bundle = getArguments();

        position = bundle.getInt("position");

        Glide.with(this).load(AppConstant.newsResponseDataList.get(position).getHdImage())
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail(Glide.with(this).load(AppConstant.newsResponseDataList.get(position).getThumbnail()))
                .into(newsImageView);

        mainTitleTextView.setText(Html.fromHtml(AppConstant.newsResponseDataList.get(position).getTitle()));
        descriptionTextView.setText(Html.fromHtml(AppConstant.newsResponseDataList.get(position).getDetails()));
        sourceTextView.setText(AppConstant.newsResponseDataList.get(position).getSource());
        postedDateTextView.setText(AppConstant.newsResponseDataList.get(position).getDatetime());
        categoryTextView.setText(AppConstant.newsResponseDataList.get(position).getCategory());
        if (position == 0) {
            AppConstant.webViewFragment.setCurrentItem(AppConstant.newsResponseDataList.get(position).getPermalink());
        } else {
            AppConstant.webViewFragment.setCurrentItem(AppConstant.newsResponseDataList.get(position - 1).getPermalink());
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppConstant.mainViewPager.setCurrentItem(1);
            }
        });

        shareNewsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "Clicked..", Toast.LENGTH_SHORT).show();
            }
        });

        return view;

    }
}
