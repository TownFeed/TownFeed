package com.townfeednews.adapterOld;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
//import com.townfeednews.IOInterface.SpeakText;
import com.townfeednews.R;
import com.townfeednews.model.newsOld.News;
import com.townfeednews.utils.AppPrefs;

import java.util.ArrayList;

/**
 * Created by rizvan on 12/13/16.
 */

public class VerticlePagerAdapter extends PagerAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<News> newsDataList;
    private boolean isPlaying = false;
//    private SpeakText speakText;

//    public VerticlePagerAdapter(Context context, ArrayList<News> newsDataList, SpeakText speakText) {
//        mContext = context;
//        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.newsDataList = newsDataList;
//        this.speakText = speakText;
//    }

    @Override
    public int getCount() {
        return newsDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.content_main, container, false);

        TextView newsTitleTextView = itemView.findViewById(R.id.newsTitleTextView);
        if (AppPrefs.getNightMode(mContext)) {
            newsTitleTextView.setTextColor(Color.WHITE);
//            AppPrefs.setNightMode(mContext, true);
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            newsTitleTextView.setTextColor(Color.BLACK);
//            AppPrefs.setNightMode(mContext, false);
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        TextView newsDetailsTextView = itemView.findViewById(R.id.newsDetailsTextView);
        TextView newsSourceAndTimeTextView = itemView.findViewById(R.id.newsSourceAndTimeTextView);
        ImageView imageView = itemView.findViewById(R.id.newsContentImageView);
        ImageView shareAppImageView = itemView.findViewById(R.id.shareAppImageView);
        final ImageView ttsImageView = itemView.findViewById(R.id.ttsImageView);
        ImageView bookmarkNewsImageView = itemView.findViewById(R.id.bookmarkNewsImageView);

        Glide.with(mContext).load(newsDataList.get(position).getImage()).placeholder(R.drawable.place_holder_img).into(imageView);

        newsTitleTextView.setText(newsDataList.get(position).getTitle());
        newsDetailsTextView.setText(newsDataList.get(position).getDetails());
        String setSourceAndTime = newsDataList.get(position).getSource() + newsDataList.get(position).getDatetime();
        newsSourceAndTimeTextView.setText(setSourceAndTime);

        container.addView(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG", "onClick: itemView Clicked...");
            }
        });

        shareAppImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri shortLink = null;
                try {
                    ;
                    String shareLink = "https://townfeednews.page.link/?" +
                            "link=https://townfeed.in/" +
                            "&apn=com.townfeednews" +
                            "&st=News Content title" +
                            "&sd="+newsDataList.get(position).getNews_id();  //News Content title will be get from the system.

                    Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                            .setLink(Uri.parse(shareLink))
                            .setDomainUriPrefix("https://townfeednews.page.link")
//                            .setDomainUriPrefix("https://townfeed.in")
//                            .setDomainUriPrefix("Some Text")
                            .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.townfeednews")
                                    .setMinimumVersion(1)
                                    .build())
                            .setSocialMetaTagParameters(
                                    new DynamicLink.SocialMetaTagParameters.Builder()
                                            .setTitle("TownFeed Daily news app")
                                            .setDescription("This link works whether the app is installed or not!")
                                            .build())
                            // Set parameters
                            // ...
                            .buildShortDynamicLink()
                            .addOnCompleteListener( new OnCompleteListener<ShortDynamicLink>() {
                                @Override
                                public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                    if (task.isSuccessful()) {
                                        // Short link created
                                        Uri shortLink = task.getResult().getShortLink();
                                        Uri flowchartLink = task.getResult().getPreviewLink();
                                        Intent shareIntent = new Intent(Intent.ACTION_SEND);

                                        shareIntent.setType("text/plain");
                                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.app_name));
                                        String shareMessage = "\n Let me recommend you this application \n\n";

                                        shareMessage = shareMessage + shortLink;
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                                        mContext.startActivity(Intent.createChooser(shareIntent, "choose one"));

                                    } else {
                                        // Error
                                        // ...
                                        Log.d("TAG", "onComplete: ");
                                    }
                                }
                            });

                } catch (Exception e) {
                    Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ttsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    ttsImageView.setImageResource(R.mipmap.ic_read_news);
                    isPlaying = false;
//                    speakText.stopSpeakText();
                } else {
                    ttsImageView.setImageResource(R.mipmap.ic_stop_play);
                    isPlaying = true;
//                    speakText.startSpeakText(position);
                }
            }
        });

        bookmarkNewsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 10-10-2019 save news in bookmark db with date. show aler to user that news will be remove
                // TODO: 10-10-2019 from db after a limited time period (Like one week or three days)
            }
        });


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
//        container.removeView((FrameLayout) object);
    }

}
