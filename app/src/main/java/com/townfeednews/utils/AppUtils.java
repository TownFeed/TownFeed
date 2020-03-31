package com.townfeednews.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.townfeednews.IOClickListenerOld.CategoryItemClickListener;
import com.townfeednews.activity.HomeActivityOld;
import com.townfeednews.activity.SplashScreen;
import com.townfeednews.connection.ApiInterface;
import com.townfeednews.connection.BaseUrlOld;
import com.townfeednews.connection.TownFeedBaseUrl;
import com.townfeednews.helper.UserProfile;
import com.townfeednews.model.category.CategoriesNameResponseData;
import com.townfeednews.model.category.CategoryData;
import com.townfeednews.model.newsOld.NewsResponseData;
import com.townfeednews.roomDBOld.Category;
import com.townfeednews.roomDBOld.MyDataAccessObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.townfeednews.utils.AppConstant.newsArrayList;

public class AppUtils {

    public static void updateUserProfileToDb(String cat_id, MyDataAccessObject myDataAccessObject, CategoryItemClickListener categoryItemClickListener) {
        new UpdateUserProfiletoDBAsynkTask(cat_id, myDataAccessObject,categoryItemClickListener).execute();
    }

    public static class UpdateUserProfiletoDBAsynkTask extends AsyncTask<Void, Void, Void> {
        private MyDataAccessObject mAsynkTaskDAO;
        private String cat_id;
        private CategoryItemClickListener categoryItemClickListener;

        UpdateUserProfiletoDBAsynkTask(String cat_id, MyDataAccessObject myDataAccessObject, CategoryItemClickListener categoryItemClickListener) {
            mAsynkTaskDAO = myDataAccessObject;
            this.cat_id = cat_id;
            this.categoryItemClickListener = categoryItemClickListener;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("TAG", "doInBackground: deleting all data from db");
            Category oldCategory = AppConstant.townFeedAppDB.myDataAccessObject().getCategoryStatus(cat_id);
            if (oldCategory.getStatus().equals("0")) {
                oldCategory.setStatus("1");
            } else {
                oldCategory.setStatus("0");
            }
            AppConstant.townFeedAppDB.myDataAccessObject().updateCategory(oldCategory);
            Log.d("TAG", "onPostExecute: updating all data in db again.");
            for (Category category : AppConstant.townFeedAppDB.myDataAccessObject().getCategories()) {
                Log.d("TAG", "User details from DB : " + category.getCat_name());
                CategoryData category1 = new CategoryData();
                category1.setCat_id(category.getCat_id());
                category1.setStatus(category.getStatus());
                category1.setCat_name(category.getCat_name());
                AppConstant.categoryData.add(category1);
                Log.d("TAG", "Category Data List Size: " + AppConstant.categoryData.size());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            categoryItemClickListener.onCategoryListUpdated();

        }
    }

    public static void updateUserProfileToServer(UserProfile userProfile, final Context context) {

        Log.d("TAG", "updateUserProfileToServer: " +
                "\n Date : " + userProfile.getDate() +
                "\n Device Id : " + userProfile.getDevice_id()+
                "\n Email :" + userProfile.getEmail() +
                "\n Gender : " + userProfile.getGender()+
                "\n Language : " + userProfile.getLang() +
                "\n Name : " + userProfile.getName());
        ApiInterface retrofit = TownFeedBaseUrl.getRetrofit().create(ApiInterface.class);
//        retrofit.updateUserProfile(userProfile.getName(), userProfile.getGender(), userProfile.getEmail(),
//                userProfile.getMobile(), userProfile.getSelect_cat(), userProfile.getDevice_id(), userProfile.getDate(),
//                userProfile.getLang()).enqueue(new Callback<ProfileResponseData>() {
//            @Override
//            public void onResponse(Call<ProfileResponseData> call, Response<ProfileResponseData> response) {
//                if (response.body().getData() != null) {
//                    UserProfilePrefs.setProfileUpdateToServer(context,false);
//                    AppConstant.selectedCategories.clear();
//                    Log.d("TAG", "onResponse update user profile: " + response.body().getData());
//                } else {
//                    Log.d("TAG", "onResponse update user profile went wrong from server: " + response.body().getData());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ProfileResponseData> call, Throwable t) {
//                Log.d("TAG", "onFailure: " + t.getMessage());
//            }
//        });
    }


    private static class LogOutAllUserInfo extends AsyncTask<Void, Void, Void> {
        private MyDataAccessObject mAsynkTaskDAO;
        private Activity activity;

        LogOutAllUserInfo(MyDataAccessObject myDataAccessObject, Activity activity) {
            mAsynkTaskDAO = myDataAccessObject;
            this.activity = activity;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsynkTaskDAO.logOutUser();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            activity.startActivity(new Intent(activity, SplashScreen.class));
            Toast.makeText(activity, "Logout Successfully.", Toast.LENGTH_SHORT).show();
            activity.finish();
        }
    }

    public static void logOutUser(Activity activity) {
        new LogOutAllUserInfo(AppConstant.townFeedAppDB.myDataAccessObject(), activity).execute();
    }

    private static class DeleteAllDataFromDBAsynkTask extends AsyncTask<Void, Void, Void> {
        private MyDataAccessObject mAsynkTaskDAO;

        DeleteAllDataFromDBAsynkTask(MyDataAccessObject myDataAccessObject) {
            mAsynkTaskDAO = myDataAccessObject;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsynkTaskDAO.deleteAllCategoryData();
            Log.d("TAG", "doInBackground: deleting all data from db");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("TAG", "onPostExecute: updating all data in db again.");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    for (int i = 0; i < AppConstant.categoryData.size(); i++) {
                        Category category = new Category();
                        category.setCat_id(AppConstant.categoryData.get(i).getCat_id());
                        category.setCat_name(AppConstant.categoryData.get(i).getCat_name());
                        category.setStatus(AppConstant.categoryData.get(i).getStatus());

                        AppConstant.townFeedAppDB.myDataAccessObject().addCategory(category);
                        Log.d("TAG", "Category added successfully");
                    }
                }
            }.start();
        }
    }

    public static void getCategoryInfo(String lang) {
        ApiInterface retrofit = TownFeedBaseUrl.getRetrofit().create(ApiInterface.class);
        retrofit.getCategoriesNamesFromServer(lang, AppConstant.deviceId).enqueue(new Callback<CategoriesNameResponseData>() {
            @Override
            public void onResponse(Call<CategoriesNameResponseData> call, Response<CategoriesNameResponseData> response) {
                AppConstant.categoryData.clear();
                if (response.isSuccessful()) {
                    AppConstant.categoryData = response.body().getData();
                    new DeleteAllDataFromDBAsynkTask(AppConstant.townFeedAppDB.myDataAccessObject()).execute();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            for (Category category : AppConstant.townFeedAppDB.myDataAccessObject().getCategories()) {
                                Log.d("TAG", "User details from DB : " + category.getCat_name());
                                CategoryData category1 = new CategoryData();
                                category1.setCat_id(category.getCat_id());
                                category1.setStatus(category.getStatus());
                                category1.setCat_name(category.getCat_name());
                                AppConstant.categoryData.add(category1);
                                Log.d("TAG", "Category Data List Size: " + AppConstant.categoryData.size());
                            }
                        }
                    }.start();
                }
            }

            @Override
            public void onFailure(Call<CategoriesNameResponseData> call, Throwable t) {
                AppConstant.categoryData.clear();
                Log.d("TAG", "getCategoryInfo: Data : " + t.getMessage());
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        for (Category category : AppConstant.townFeedAppDB.myDataAccessObject().getCategories()) {
                            Log.d("TAG", "User details from DB : " + category.getCat_name());
                            CategoryData category1 = new CategoryData();
                            category1.setCat_id(category.getCat_id());
                            category1.setStatus(category.getStatus());
                            category1.setCat_name(category.getCat_name());
                            AppConstant.categoryData.add(category1);
                            Log.d("TAG", "Category Data List Size: " + AppConstant.categoryData.size());
                        }
                    }
                }.start();
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static boolean hasInternetConnection() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("www.google.com", 80), 2000);
            return true;
        } catch (IOException e) {
            // Either we have a timeout or unreachable host or failed DNS lookup
            System.out.println(e);
            return false;
        }
    }

    public static void getNewsUpdateData(final Context context) {


    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    public static String getMstoHHMMSS(long millis) {
        if (TimeUnit.MILLISECONDS.toHours(millis) != 0) {
            return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        } else {
            return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        }
    }
}
