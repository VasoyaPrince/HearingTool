package com.pesonal.adsdk;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.ads.AbstractAdListener;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.startapp.sdk.ads.nativead.NativeAdDetails;
import com.startapp.sdk.ads.nativead.NativeAdPreferences;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.mediation.IUnityAdsExtendedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static android.content.ContentValues.TAG;


public class AppManage {

    public static String ADMOB = "Admob";
    public static String FACEBOOK = "Facebookaudiencenetwork";
    public static String STARTAPP = "StartApp";
    public static String UNITY = "Unity";

    public static int count_banner = -1;
    public static int count_native = -1;
    public static int count_click = -1;
    public static int count_click_for_alt = -1;

    public static String app_privacyPolicyLink = "";
    public static String app_accountLink = "";
    public static int app_updateAppDialogStatus = 0;
    public static int app_dialogBeforeAdShow = 0;
    public static int app_redirectOtherAppStatus = 0;
    public static int app_adShowStatus = 1;
    public static int app_mainClickCntSwAd = 0;
    public static int app_innerClickCntSwAd = 0;


    public static String ADMOB_APPID = "";
    public static String[] ADMOB_B = {"", "", "", "", ""};
    public static String[] ADMOB_I = {"", "", "", "", ""};
    public static String[] ADMOB_N = {"", "", "", "", ""};


    public static String[] FACEBOOK_I = {"", "", "", "", ""};
    public static String[] FACEBOOK_B = {"", "", "", "", ""};
    public static String[] FACEBOOK_NB = {"", "", "", "", ""};
    public static String[] FACEBOOK_N = {"", "", "", "", ""};


    public static StartAppAd startAppAd;
    public static String STARTAPP_APPID = "";
    public static String UNITY_APPID = "";
    public static int admob_AdStatus = 0;
    public static int facebook_AdStatus = 0;
    public static int startapp_AdStatus = 0;
    public static int unity_AdStatus = 0;

    public static int admob_loadAdIdsType = 0;
    public static int facebook_loadAdIdsType = 0;
    public static SharedPreferences mysharedpreferences;
    public static int ad_dialog_time_in_second = 2;
    static Context activity;
    static MyCallback myCallback;
    private static AppManage mInstance;

    public static boolean preloadNative_AdStatus = true;
    public static String state_admobNative = "Start";
    NativeAd admobNativeAd_preLoad = null;
    public static String state_fbNative = "Start";
    com.facebook.ads.NativeAd fbNativeAd_preLoad = null;

    private InterstitialAd mInterstitialAd;
    private String google_i_pre = "", facebook_i_pre = "";
    String admob_b, facebook_nb, facebook_b;
    String admob_n, facebook_n;
    ArrayList<String> banner_sequence = new ArrayList<>();
    ArrayList<String> native_sequence = new ArrayList<>();
    ArrayList<String> interstitial_sequence = new ArrayList<>();
    private com.facebook.ads.InterstitialAd fbinterstitialAd1;
    private Dialog dialog;



    public AppManage(Context activity) {
        AppManage.activity = activity;
        mysharedpreferences = activity.getSharedPreferences(activity.getPackageName(), Context.MODE_PRIVATE);
        getResponseFromPref();

    }

    public static AppManage getInstance(Context activity) {
        AppManage.activity = activity;
        if (mInstance == null) {
            mInstance = new AppManage(activity);
        }
        return mInstance;
    }

    public static boolean hasActiveInternetConnection(Context context) {
        @SuppressLint("WrongConstant") NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void getResponseFromPref() {
        String response1 = mysharedpreferences.getString("response", "");

        SharedPreferences.Editor editor_count = mysharedpreferences.edit();
        editor_count.putInt("count_admob_B", 0);
        editor_count.putInt("count_admob_I", 0);
        editor_count.putInt("count_admob_N", 0);
        editor_count.putInt("count_facebook_B", 0);
        editor_count.putInt("count_facebook_NB", 0);
        editor_count.putInt("count_facebook_I", 0);
        editor_count.putInt("count_facebook_N", 0);
        editor_count.apply();

        if (!response1.isEmpty()) {
            try {


                JSONObject response = new JSONObject(response1);
                JSONObject settingsJsonObject = response.getJSONObject("APP_SETTINGS");

                app_accountLink = settingsJsonObject.getString("app_accountLink");
                app_privacyPolicyLink = settingsJsonObject.getString("app_privacyPolicyLink");
                app_updateAppDialogStatus = settingsJsonObject.getInt("app_updateAppDialogStatus");
                app_redirectOtherAppStatus = settingsJsonObject.getInt("app_redirectOtherAppStatus");
                app_dialogBeforeAdShow = settingsJsonObject.getInt("app_dialogBeforeAdShow");
                app_adShowStatus = settingsJsonObject.getInt("app_adShowStatus");
                app_mainClickCntSwAd = settingsJsonObject.getInt("app_mainClickCntSwAd");
                app_innerClickCntSwAd = settingsJsonObject.getInt("app_innerClickCntSwAd");
                boolean app_AppOpenAdStatus;
                if (settingsJsonObject.getInt("app_AppOpenAdStatus") == 1) {
                    app_AppOpenAdStatus = true;
                } else {
                    app_AppOpenAdStatus = false;
                }

                SharedPreferences.Editor editor = mysharedpreferences.edit();
                editor.putString("app_name", settingsJsonObject.getString("app_name"));
                editor.putString("app_logo", settingsJsonObject.getString("app_logo"));
                editor.putString("app_privacyPolicyLink", app_privacyPolicyLink);
                editor.putInt("app_updateAppDialogStatus", app_updateAppDialogStatus);
                editor.putString("app_versionCode", settingsJsonObject.getString("app_versionCode"));
                editor.putInt("app_redirectOtherAppStatus", app_redirectOtherAppStatus);
                editor.putString("app_newPackageName", settingsJsonObject.getString("app_newPackageName"));
                editor.putInt("app_adShowStatus", app_adShowStatus);

                editor.putInt("app_howShowAdInterstitial", settingsJsonObject.getInt("app_howShowAdInterstitial"));
                editor.putString("app_adPlatformSequenceInterstitial", settingsJsonObject.getString("app_adPlatformSequenceInterstitial"));
                editor.putString("app_alernateAdShowInterstitial", settingsJsonObject.getString("app_alernateAdShowInterstitial"));

                editor.putInt("app_howShowAdNative", settingsJsonObject.getInt("app_howShowAdNative"));
                editor.putString("app_adPlatformSequenceNative", settingsJsonObject.getString("app_adPlatformSequenceNative"));
                editor.putString("app_alernateAdShowNative", settingsJsonObject.getString("app_alernateAdShowNative"));

                editor.putInt("app_howShowAdBanner", settingsJsonObject.getInt("app_howShowAdBanner"));
                editor.putString("app_adPlatformSequenceBanner", settingsJsonObject.getString("app_adPlatformSequenceBanner"));
                editor.putString("app_alernateAdShowBanner", settingsJsonObject.getString("app_alernateAdShowBanner"));

                editor.putInt("app_mainClickCntSwAd", app_mainClickCntSwAd);
                editor.putInt("app_innerClickCntSwAd", app_innerClickCntSwAd);
                editor.putBoolean("app_AppOpenAdStatus", app_AppOpenAdStatus);
                editor.commit();

                JSONObject AdmobJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("Admob");
                admob_AdStatus = AdmobJsonObject.getInt("ad_showAdStatus");
                admob_loadAdIdsType = AdmobJsonObject.getInt("ad_loadAdIdsType");
                ADMOB_APPID = AdmobJsonObject.getString("AppID");
                ADMOB_B[0] = AdmobJsonObject.getString("Banner1");
                ADMOB_I[0] = AdmobJsonObject.getString("Interstitial1");
                ADMOB_N[0] = AdmobJsonObject.getString("Native1");

                SharedPreferences.Editor editor1 = mysharedpreferences.edit();
                editor1.putString("AppOpenID", AdmobJsonObject.getString("AppOpen1"));
                editor1.commit();

                JSONObject FBJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("Facebookaudiencenetwork");
                facebook_AdStatus = FBJsonObject.getInt("ad_showAdStatus");
                facebook_loadAdIdsType = FBJsonObject.getInt("ad_loadAdIdsType");
                FACEBOOK_B[0] = FBJsonObject.getString("Banner1");
                FACEBOOK_NB[0] = FBJsonObject.getString("NativeBanner1");
                FACEBOOK_I[0] = FBJsonObject.getString("Interstitial1");
                FACEBOOK_N[0] = FBJsonObject.getString("Native1");

                JSONObject SAJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("StartApp");
                startapp_AdStatus = SAJsonObject.getInt("ad_showAdStatus");
                STARTAPP_APPID = SAJsonObject.getString("AppID");


                JSONObject UNJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("Unity");
                unity_AdStatus = UNJsonObject.getInt("ad_showAdStatus");
                UNITY_APPID = UNJsonObject.getString("AppID");



            } catch (Exception e) {
            }

        }

    }

    private static void initAd() {
        if (admob_AdStatus == 1) {
            MobileAds.initialize(activity, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
        }

        if (facebook_AdStatus == 1) {
            AudienceNetworkAds.initialize(activity);

            AdSettings.addTestDevice("HASHED ID");
        }


        if (startapp_AdStatus == 1 && !STARTAPP_APPID.isEmpty()) {
            StartAppSDK.init(activity, STARTAPP_APPID, false);
            StartAppAd.disableSplash();
            startAppAd = new StartAppAd(activity);
            startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);
        }

        if (unity_AdStatus == 1 && !UNITY_APPID.isEmpty()) {
            UnityAds.initialize(activity, UNITY_APPID);
        }


    }

    private static boolean checkUpdate(int cversion) {


        if (mysharedpreferences.getInt("app_updateAppDialogStatus", 0) == 1) {
            String versions = mysharedpreferences.getString("app_versionCode", "");
            String str[] = versions.split(",");

            try {


                if (Arrays.asList(str).contains(cversion + "")) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return false;
    }



    public List<MoreApp_Data> get_SPLASHMoreAppData() {
        List<MoreApp_Data> data = new ArrayList<>();
        SharedPreferences preferences = activity.getSharedPreferences("ad_pref", 0);
        try {

            JSONArray array = new JSONArray(preferences.getString("MORE_APP_SPLASH", ""));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                data.add(new MoreApp_Data(object.getString("app_id"), object.getString("app_name"), object.getString("app_packageName"), object.getString("app_logo"), object.getString("app_status")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public List<MoreApp_Data> get_EXITMoreAppData() {
        List<MoreApp_Data> data = new ArrayList<>();
        SharedPreferences preferences = activity.getSharedPreferences("ad_pref", 0);
        try {

            JSONArray array = new JSONArray(preferences.getString("MORE_APP_EXIT", ""));
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                data.add(new MoreApp_Data(object.getString("app_id"), object.getString("app_name"), object.getString("app_packageName"), object.getString("app_logo"), object.getString("app_status")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public void getResponseFromPref(getDataListner listner, int cversion) {
        String response1 = mysharedpreferences.getString("response", "");

        SharedPreferences.Editor editor_count = mysharedpreferences.edit();
        editor_count.putInt("count_admob_B", 0);
        editor_count.putInt("count_admob_I", 0);
        editor_count.putInt("count_admob_N", 0);
        editor_count.putInt("count_facebook_B", 0);
        editor_count.putInt("count_facebook_NB", 0);
        editor_count.putInt("count_facebook_I", 0);
        editor_count.putInt("count_facebook_N", 0);
        editor_count.commit();

        if (!response1.isEmpty()) {
            try {


                JSONObject response = new JSONObject(response1);
                JSONObject settingsJsonObject = response.getJSONObject("APP_SETTINGS");

                app_accountLink = settingsJsonObject.getString("app_accountLink");
                app_privacyPolicyLink = settingsJsonObject.getString("app_privacyPolicyLink");
                app_updateAppDialogStatus = settingsJsonObject.getInt("app_updateAppDialogStatus");
                app_redirectOtherAppStatus = settingsJsonObject.getInt("app_redirectOtherAppStatus");
                app_dialogBeforeAdShow = settingsJsonObject.getInt("app_dialogBeforeAdShow");
                app_adShowStatus = settingsJsonObject.getInt("app_adShowStatus");
                app_mainClickCntSwAd = settingsJsonObject.getInt("app_mainClickCntSwAd");
                app_innerClickCntSwAd = settingsJsonObject.getInt("app_innerClickCntSwAd");
                boolean app_AppOpenAdStatus;
                if (settingsJsonObject.getInt("app_AppOpenAdStatus") == 1) {
                    app_AppOpenAdStatus = true;
                } else {
                    app_AppOpenAdStatus = false;
                }

                SharedPreferences.Editor editor = mysharedpreferences.edit();
                editor.putString("app_name", settingsJsonObject.getString("app_name"));
                editor.putString("app_logo", settingsJsonObject.getString("app_logo"));
                editor.putString("app_privacyPolicyLink", app_privacyPolicyLink);
                editor.putInt("app_updateAppDialogStatus", app_updateAppDialogStatus);
                editor.putString("app_versionCode", settingsJsonObject.getString("app_versionCode"));
                editor.putInt("app_redirectOtherAppStatus", app_redirectOtherAppStatus);
                editor.putString("app_newPackageName", settingsJsonObject.getString("app_newPackageName"));
                editor.putInt("app_adShowStatus", app_adShowStatus);

                editor.putInt("app_howShowAdInterstitial", settingsJsonObject.getInt("app_howShowAdInterstitial"));
                editor.putString("app_adPlatformSequenceInterstitial", settingsJsonObject.getString("app_adPlatformSequenceInterstitial"));
                editor.putString("app_alernateAdShowInterstitial", settingsJsonObject.getString("app_alernateAdShowInterstitial"));

                editor.putInt("app_howShowAdNative", settingsJsonObject.getInt("app_howShowAdNative"));
                editor.putString("app_adPlatformSequenceNative", settingsJsonObject.getString("app_adPlatformSequenceNative"));
                editor.putString("app_alernateAdShowNative", settingsJsonObject.getString("app_alernateAdShowNative"));

                editor.putInt("app_howShowAdBanner", settingsJsonObject.getInt("app_howShowAdBanner"));
                editor.putString("app_adPlatformSequenceBanner", settingsJsonObject.getString("app_adPlatformSequenceBanner"));
                editor.putString("app_alernateAdShowBanner", settingsJsonObject.getString("app_alernateAdShowBanner"));

                editor.putInt("app_mainClickCntSwAd", app_mainClickCntSwAd);
                editor.putInt("app_innerClickCntSwAd", app_innerClickCntSwAd);
                editor.putBoolean("app_AppOpenAdStatus", app_AppOpenAdStatus);
                editor.commit();

                JSONObject AdmobJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("Admob");
                admob_AdStatus = AdmobJsonObject.getInt("ad_showAdStatus");
                admob_loadAdIdsType = AdmobJsonObject.getInt("ad_loadAdIdsType");
                ADMOB_APPID = AdmobJsonObject.getString("AppID");
                ADMOB_B[0] = AdmobJsonObject.getString("Banner1");
                ADMOB_I[0] = AdmobJsonObject.getString("Interstitial1");
                ADMOB_N[0] = AdmobJsonObject.getString("Native1");

                SharedPreferences.Editor editor1 = mysharedpreferences.edit();
                editor1.putString("AppOpenID", AdmobJsonObject.getString("AppOpen1"));
                editor1.commit();

                JSONObject FBJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("Facebookaudiencenetwork");
                facebook_AdStatus = FBJsonObject.getInt("ad_showAdStatus");
                facebook_loadAdIdsType = FBJsonObject.getInt("ad_loadAdIdsType");
                FACEBOOK_B[0] = FBJsonObject.getString("Banner1");
                FACEBOOK_NB[0] = FBJsonObject.getString("NativeBanner1");
                FACEBOOK_I[0] = FBJsonObject.getString("Interstitial1");
                FACEBOOK_N[0] = FBJsonObject.getString("Native1");


                JSONObject SAJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("StartApp");
                startapp_AdStatus = SAJsonObject.getInt("ad_showAdStatus");
                STARTAPP_APPID = SAJsonObject.getString("AppID");


                JSONObject UNJsonObject = response.getJSONObject("PLACEMENT").getJSONObject("Unity");
                unity_AdStatus = UNJsonObject.getInt("ad_showAdStatus");
                UNITY_APPID = UNJsonObject.getString("AppID");


                try {
                    listner.onGetExtradata(response.getJSONObject("EXTRA_DATA"));
                    Log.e("EXTRA_DATA", response.getJSONObject("EXTRA_DATA").toString());
                } catch (Exception e) {
                    Log.e("extradata_error", e.getMessage());
                }


            } catch (Exception e) {
            }

            if (app_redirectOtherAppStatus == 1) {
                String redirectNewPackage = mysharedpreferences.getString("app_newPackageName", "");
                listner.onRedirect(redirectNewPackage);
            } else if (app_updateAppDialogStatus == 1 && checkUpdate(cversion)) {
                listner.onUpdate("https://play.google.com/store/apps/details?id=" + activity.getPackageName());
            } else {
                listner.onSuccess();
                initAd();
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        }
    }

    private String implode(String[] placementList) {

        String str_ads = "";
        for (int i = 1; i < placementList.length; i++) {
            if (!placementList[i].isEmpty()) {
                if (str_ads.isEmpty()) {
                    str_ads = placementList[i];
                } else {
                    str_ads = str_ads + "," + placementList[i];
                }

            }
        }
        return str_ads;
    }

    private String getHigheCPMAdId(String platform, String adFormat, String whichOne) {
        String adId = "";

        if (whichOne.equals("First")) {
            SharedPreferences.Editor editor = mysharedpreferences.edit();
            if (platform.equals(ADMOB)) {
                if (adFormat.equals("I")) {
                    adId = ADMOB_I[0];
                    String ADMOB_I_list = implode(ADMOB_I);
                    editor.putString("ADMOB_I", ADMOB_I_list);
                }

            } else if (platform.equals(FACEBOOK)) {
                if (adFormat.equals("I")) {
                    adId = FACEBOOK_I[0];
                    String FACEBOOK_I_list = implode(FACEBOOK_I);
                    editor.putString("FACEBOOK_I", FACEBOOK_I_list);
                }
            }

            editor.commit();
        } else if (whichOne.equals("Next")) {
            SharedPreferences.Editor editor = mysharedpreferences.edit();
            if (platform.equals(ADMOB)) {
                if (adFormat.equals("I")) {

                    String ADMOB_I_list = mysharedpreferences.getString("ADMOB_I", "");
                    if (!ADMOB_I_list.isEmpty())
                    {
                        String intA_list[] = ADMOB_I_list.split(",");
                        adId = intA_list[0];
                        ADMOB_I_list = implode(intA_list);
                        editor.putString("ADMOB_I", ADMOB_I_list);
                    }

                }

            } else if (platform.equals(FACEBOOK)) {
                if (adFormat.equals("I")) {
                    String FACEBOOK_I_list = mysharedpreferences.getString("FACEBOOK_I", "");
                    if (!FACEBOOK_I_list.isEmpty())
                    {
                        String intF_list[] = FACEBOOK_I_list.split(",");
                        adId = intF_list[0];
                        FACEBOOK_I_list = implode(intF_list);
                        editor.putString("FACEBOOK_I", FACEBOOK_I_list);
                    }
                }
            }

            editor.commit();
        }


        return adId;
    }


    public void loadInterstitialAd(Activity activity, String google_i, String facebook_i) {
        if (admob_loadAdIdsType == 2) {
            google_i = getHigheCPMAdId(ADMOB, "I", "First");
        }
        if (facebook_loadAdIdsType == 2) {
            facebook_i = getHigheCPMAdId(FACEBOOK, "I", "First");
        }
        turnLoadInterstitialAd(activity, google_i, facebook_i);
    }


    public void loadInterstitialAd(Activity activity) {

        String google_i = "";
        String facebook_i = "";

        if (admob_loadAdIdsType == 2) {
            google_i = getHigheCPMAdId(ADMOB, "I", "First");
        } else {
            google_i = getRandomPlacementId(ADMOB, "I");
        }
        if (facebook_loadAdIdsType == 2) {
            facebook_i = getHigheCPMAdId(FACEBOOK, "I", "First");
        } else {
            facebook_i = getRandomPlacementId(FACEBOOK, "I");
        }

        turnLoadInterstitialAd(activity, google_i, facebook_i);
    }

    public void turnLoadInterstitialAd(Activity activity, String google_i, String facebook_i) {

        if (app_adShowStatus == 0) {
            return;
        }

        if (admob_AdStatus == 1 && !google_i.isEmpty() && !google_i_pre.equals(google_i)) {
            loadAdmobInterstitial(activity, google_i);
        }


        if (facebook_AdStatus == 1 && !facebook_i.isEmpty() && !facebook_i_pre.equals(facebook_i)) {
            loadFacebookInterstitial(activity, facebook_i);
        }


    }


    private void loadFacebookInterstitial(final Activity activity, String facebook_i) {


        if (facebook_loadAdIdsType == 0) {
            facebook_i = getRandomPlacementId(FACEBOOK, "I");
        }
        facebook_i_pre = facebook_i;

        fbinterstitialAd1 = new com.facebook.ads.InterstitialAd(activity, facebook_i);
        fbinterstitialAd1.loadAd(fbinterstitialAd1.buildLoadAdConfig().withAdListener(new AbstractAdListener() {
            @Override
            public void onError(Ad ad, AdError error) {
                super.onError(ad, error);

                if (facebook_loadAdIdsType == 2) {
                    facebook_i_pre = getHigheCPMAdId(FACEBOOK, "I", "Next");
                    if (!facebook_i_pre.isEmpty()) {
                        loadFacebookInterstitial(activity, facebook_i_pre);
                    }
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                super.onAdLoaded(ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                super.onInterstitialDismissed(ad);
//                fbinterstitialAd1.loadAd();
                if (facebook_loadAdIdsType == 2) {
                    facebook_i_pre = getHigheCPMAdId(FACEBOOK, "I", "First");
                }
                loadFacebookInterstitial(activity, facebook_i_pre);
                interstitialCallBack();
            }
        }).build());
    }

    private void loadAdmobInterstitial(final Activity activity, String google_i) {

        if (admob_loadAdIdsType == 0) {
            google_i = getRandomPlacementId(ADMOB, "I");
        }
        this.google_i_pre = google_i;

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(activity, google_i, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
                Log.i(TAG, "onAdLoaded");

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when fullscreen content is dismissed.
                        Log.d("TAG", "The ad was dismissed.");
                        if (admob_loadAdIdsType == 2) {
                            google_i_pre = getHigheCPMAdId(ADMOB, "I", "First");
                        }
                        loadAdmobInterstitial(activity, google_i_pre);
                        interstitialCallBack();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                        // Called when fullscreen content failed to show.
                        Log.d("TAG", "The ad failed to show.");
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when fullscreen content is shown.
                        // Make sure to set your reference to null so you don't
                        // show it a second time.
                        mInterstitialAd = null;
                        Log.d("TAG", "The ad was shown.");
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.i(TAG, loadAdError.getMessage());
                mInterstitialAd = null;

                if (admob_loadAdIdsType == 2) {
                    google_i_pre = getHigheCPMAdId(ADMOB, "I", "Next");
                    if (!google_i_pre.isEmpty()) {
                        loadAdmobInterstitial(activity, google_i_pre);
                    }
                }
            }
        });


    }

    private String getRandomPlacementId(String platform, String adFormat) {
        String return_adId = "";


        SharedPreferences.Editor editor_count = mysharedpreferences.edit();
        int count = 0;
        String[] platform_Format_Ids = {"", "", "", "", ""};
        if (platform.equals(ADMOB)) {
            if (adFormat.equals("B")) {
                platform_Format_Ids = ADMOB_B;

                count = mysharedpreferences.getInt("count_admob_B", 0) + 1;
                editor_count.putInt("count_admob_B", count);
            } else if (adFormat.equals("I")) {
                platform_Format_Ids = ADMOB_I;

                count = mysharedpreferences.getInt("count_admob_I", 0) + 1;
                editor_count.putInt("count_admob_I", count);
            } else if (adFormat.equals("N")) {
                platform_Format_Ids = ADMOB_N;

                count = mysharedpreferences.getInt("count_admob_N", 0) + 1;
                editor_count.putInt("count_admob_N", count);
            }

        } else if (platform.equals(FACEBOOK)) {
            if (adFormat.equals("B")) {
                platform_Format_Ids = FACEBOOK_B;

                count = mysharedpreferences.getInt("count_facebook_B", 0) + 1;
                editor_count.putInt("count_facebook_B", count);
            } else if (adFormat.equals("I")) {
                platform_Format_Ids = FACEBOOK_I;

                count = mysharedpreferences.getInt("count_facebook_I", 0) + 1;
                editor_count.putInt("count_facebook_I", count);
            } else if (adFormat.equals("N")) {
                platform_Format_Ids = FACEBOOK_N;

                count = mysharedpreferences.getInt("count_facebook_N", 0) + 1;
                editor_count.putInt("count_facebook_N", count);
            } else if (adFormat.equals("NB")) {
                platform_Format_Ids = FACEBOOK_NB;

                count = mysharedpreferences.getInt("count_facebook_NB", 0) + 1;
                editor_count.putInt("count_facebook_NB", count);
            }
        }
        editor_count.commit();

        ArrayList<String> placemnt_Ids = new ArrayList<String>();
        for (int i = 0; i < platform_Format_Ids.length; i++) {
            if (!platform_Format_Ids[i].isEmpty()) {
                placemnt_Ids.add(platform_Format_Ids[i]);
            }
        }

        if (placemnt_Ids.size() != 0) {
            if (count % placemnt_Ids.size() == 0) {
                return_adId = placemnt_Ids.get(0);
            } else if (count % placemnt_Ids.size() == 1) {
                return_adId = placemnt_Ids.get(1);
            } else if (count % placemnt_Ids.size() == 2) {
                return_adId = placemnt_Ids.get(2);
            } else if (count % placemnt_Ids.size() == 3) {
                return_adId = placemnt_Ids.get(3);
            } else if (count % placemnt_Ids.size() == 4) {
                return_adId = placemnt_Ids.get(4);
            }
        }
        return return_adId;
    }



    public void showInterstitialAd(Context context, MyCallback myCallback) {
        turnInterstitialAd(context, myCallback, 0, "");
    }

    public void showInterstitialAd(Context context, MyCallback myCallback, String specific_platform) {
        turnInterstitialAd(context, myCallback, 0, specific_platform);
    }

    public void showInterstitialAd(Context context, MyCallback myCallback, int how_many_clicks) {
        turnInterstitialAd(context, myCallback, how_many_clicks, "");
    }


    public void showInterstitialAd(Context context, MyCallback myCallback, int how_many_clicks, String specific_platform) {
        turnInterstitialAd(context, myCallback, how_many_clicks, specific_platform);
    }

    public void showInterstitialAd(Context context, MyCallback myCallback, String specific_platform, int how_many_clicks) {
        turnInterstitialAd(context, myCallback, how_many_clicks, specific_platform);
    }

    public void turnInterstitialAd(Context context, MyCallback myCallback2, int how_many_clicks, String specific_platform) {
        this.myCallback = myCallback2;

        count_click++;

        if (app_adShowStatus == 0) {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
            return;
        }
        if (how_many_clicks != 0) {
            if (count_click % how_many_clicks != 0) {
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
                return;
            }
        }

        count_click_for_alt++;


        int app_howShowAd = mysharedpreferences.getInt("app_howShowAdInterstitial", 0);
        String adPlatformSequence = mysharedpreferences.getString("app_adPlatformSequenceInterstitial", "");
        String alernateAdShow = mysharedpreferences.getString("app_alernateAdShowInterstitial", "");

        if (!specific_platform.isEmpty()) {
            app_howShowAd = 0;
            adPlatformSequence = specific_platform;
        }


        interstitial_sequence = new ArrayList<String>();
        if (app_howShowAd == 0 && !adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");

            for (int i = 0; i < adSequence.length; i++) {
                interstitial_sequence.add(adSequence[i]);
            }

        } else if (app_howShowAd == 1 && !alernateAdShow.isEmpty()) {
            String alernateAd[] = alernateAdShow.split(",");

            int index = 0;
            for (int j = 0; j <= 10; j++) {
                if (count_click_for_alt % alernateAd.length == j) {
                    index = j;
                    interstitial_sequence.add(alernateAd[index]);
                }
            }

            String adSequence[] = adPlatformSequence.split(",");
            for (int j = 0; j < adSequence.length; j++) {
                if (interstitial_sequence.size() != 0) {
                    if (!interstitial_sequence.get(0).equals(adSequence[j])) {
                        interstitial_sequence.add(adSequence[j]);
                    }
                }

            }
        } else {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }

        if (interstitial_sequence.size() != 0) {
            displayInterstitialAd(interstitial_sequence.get(0), context);
        }

    }

    private void displayInterstitialAd(String platform, final Context context) {
        dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.ad_progress_dialog, null);
        dialog.setContentView(view);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        if (platform.equals(ADMOB) && admob_AdStatus == 1) {
            if (mInterstitialAd != null) {
                if (app_dialogBeforeAdShow == 1) {
                    dialog.show();

                    new CountDownTimer(ad_dialog_time_in_second * 1000, 10) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            double time = (millisUntilFinished / 10) / ad_dialog_time_in_second;

                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                            mInterstitialAd.show((Activity) context);
                        }
                    }.start();

                } else {
                    mInterstitialAd.show((Activity) context);
                }
            } else {
                if (admob_loadAdIdsType == 2) {
                    google_i_pre = getHigheCPMAdId(ADMOB, "I", "First");
                }
                if (!google_i_pre.isEmpty()) {
                    loadAdmobInterstitial((Activity) context, google_i_pre);
                }

                nextInterstitialPlatform(context);
            }
        } else if (platform.equals(FACEBOOK) && facebook_AdStatus == 1 && fbinterstitialAd1 != null) {

            if (fbinterstitialAd1.isAdLoaded()) {
                if (app_dialogBeforeAdShow == 1) {

                    dialog.show();

                    new CountDownTimer(ad_dialog_time_in_second * 1000, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            double time = (millisUntilFinished / 10) / ad_dialog_time_in_second;
                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                            fbinterstitialAd1.show();
                        }
                    }.start();

                } else {
                    fbinterstitialAd1.show();
                }
            } else {
//                fbinterstitialAd1.loadAd();
                if (facebook_loadAdIdsType == 2) {
                    facebook_i_pre = getHigheCPMAdId(FACEBOOK, "I", "First");
                }
                loadFacebookInterstitial((Activity) context, facebook_i_pre);
                nextInterstitialPlatform(context);
            }

        } else if (platform.equals(UNITY) && unity_AdStatus == 1) {
            if (UnityAds.isReady()) {
                if (app_dialogBeforeAdShow == 1) {

                    dialog.show();

                    new CountDownTimer(ad_dialog_time_in_second * 1000, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            double time = (millisUntilFinished / 10) / ad_dialog_time_in_second;
                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                            UnityAds.show((Activity) activity);
                        }
                    }.start();

                } else {
                    UnityAds.show((Activity) activity);
                }
                UnityAds.setListener(new IUnityAdsExtendedListener() {
                    @Override
                    public void onUnityAdsClick(String placementId) {

                    }

                    @Override
                    public void onUnityAdsPlacementStateChanged(String placementId, UnityAds.PlacementState oldState, UnityAds.PlacementState newState) {

                    }

                    @Override
                    public void onUnityAdsReady(String placementId) {

                    }

                    @Override
                    public void onUnityAdsStart(String placementId) {

                    }

                    @Override
                    public void onUnityAdsFinish(String placementId, UnityAds.FinishState result) {
                        interstitialCallBack();
                        UnityAds.initialize(activity, UNITY_APPID);
                    }

                    @Override
                    public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
                        nextInterstitialPlatform(context);
                        UnityAds.initialize(activity, UNITY_APPID);
                    }

                });
            } else {
                nextInterstitialPlatform(context);
            }

        } else if (platform.equals(STARTAPP) && startapp_AdStatus == 1) {
            startAppAd.showAd(new AdDisplayListener() {

                @Override
                public void adHidden(com.startapp.sdk.adsbase.Ad ad) {
                    interstitialCallBack();
                }

                @Override
                public void adDisplayed(com.startapp.sdk.adsbase.Ad ad) {

                }

                @Override
                public void adClicked(com.startapp.sdk.adsbase.Ad ad) {

                }

                @Override
                public void adNotDisplayed(com.startapp.sdk.adsbase.Ad ad) {
                    nextInterstitialPlatform(context);
                }
            });
        } else {

            nextInterstitialPlatform(context);

        }
    }

    private void nextInterstitialPlatform(Context context) {

        if (interstitial_sequence.size() != 0) {
            interstitial_sequence.remove(0);

            if (interstitial_sequence.size() != 0) {
                displayInterstitialAd(interstitial_sequence.get(0), context);
            } else {
                interstitialCallBack();
            }

        } else {
            interstitialCallBack();

        }
    }

    public void interstitialCallBack() {

        if (myCallback != null) {
            myCallback.callbackCall();
            AppManage.this.myCallback = null;
        }
    }

    public void showBanner(ViewGroup banner_container, String admob_b, String facebook_b) {

        turnShowBanner(banner_container, admob_b, facebook_b);
    }

    public void showBanner(ViewGroup banner_container) {

        turnShowBanner(banner_container, "random", "random");
    }

    public void turnShowBanner(ViewGroup banner_container, String admob_b, String facebook_b) {
        this.admob_b = admob_b;
        this.facebook_b = facebook_b;


        if (!hasActiveInternetConnection(activity)) {
            return;
        }

        if (app_adShowStatus == 0) {
            return;
        }

        count_banner++;


        int app_howShowAd = mysharedpreferences.getInt("app_howShowAdBanner", 0);
        String adPlatformSequence = mysharedpreferences.getString("app_adPlatformSequenceBanner", "");
        String alernateAdShow = mysharedpreferences.getString("app_alernateAdShowBanner", "");


        banner_sequence = new ArrayList<String>();
        if (app_howShowAd == 0 && !adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            for (int i = 0; i < adSequence.length; i++) {
                banner_sequence.add(adSequence[i]);
            }

        } else if (app_howShowAd == 1 && !alernateAdShow.isEmpty()) {
            String alernateAd[] = alernateAdShow.split(",");

            int index = 0;
            for (int j = 0; j <= 10; j++) {
                if (count_banner % alernateAd.length == j) {
                    index = j;
                    banner_sequence.add(alernateAd[index]);
                }
            }

            String adSequence[] = adPlatformSequence.split(",");
            for (int j = 0; j < adSequence.length; j++) {
                if (banner_sequence.size() != 0) {
                    if (!banner_sequence.get(0).equals(adSequence[j])) {
                        banner_sequence.add(adSequence[j]);
                    }
                }
            }
        }

        if (banner_sequence.size() != 0) {
            displayBanner(banner_sequence.get(0), banner_container);
        }


    }

    public void displayBanner(String platform, ViewGroup banner_container) {
        if (platform.equals(ADMOB) && admob_AdStatus == 1) {
            if ((admob_loadAdIdsType == 0 || admob_loadAdIdsType == 2 || admob_b.equals("random")) && !admob_b.isEmpty()) {
                admob_b = getRandomPlacementId(ADMOB, "B");
            }

            showAdmobBanner(banner_container);
        } else if (platform.equals(FACEBOOK) && facebook_AdStatus == 1) {

            if ((facebook_loadAdIdsType == 0 || facebook_loadAdIdsType == 2 || facebook_b.equals("random")) && !facebook_b.isEmpty()) {
                facebook_b = getRandomPlacementId(FACEBOOK, "B");
            }
            showFacebookBanner(banner_container);
        } else if (platform.equals(STARTAPP) && startapp_AdStatus == 1) {
            showStartAppBanner(banner_container);
        } else {
            nextBannerPlatform(banner_container);
        }
    }

    private void nextBannerPlatform(ViewGroup banner_container) {
        if (banner_sequence.size() != 0) {
            banner_sequence.remove(0);
            if (banner_sequence.size() != 0) {
                displayBanner(banner_sequence.get(0), banner_container);
            }
        }
    }


    private void showFacebookBanner(final ViewGroup banner_container) {
        if (facebook_b.isEmpty() || facebook_AdStatus == 0) {
            nextBannerPlatform(banner_container);
            return;
        }

        final com.facebook.ads.AdView adView = new com.facebook.ads.AdView(activity, facebook_b, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                banner_container.removeAllViews();
                nextBannerPlatform(banner_container);

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
                banner_container.removeAllViews();
                banner_container.addView(adView);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        };

        // Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());


    }

    private void showAdmobBanner(final ViewGroup banner_container) {
        if (admob_b.isEmpty() || admob_AdStatus == 0) {
            nextBannerPlatform(banner_container);
            return;
        }

        final AdView mAdView = new AdView(activity);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(admob_b);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                banner_container.removeAllViews();
                nextBannerPlatform(banner_container);

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                banner_container.removeAllViews();
                banner_container.addView(mAdView);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

    }

    private void showStartAppBanner(final ViewGroup banner_container) {
        if (STARTAPP_APPID.isEmpty() || startapp_AdStatus == 0) {
            nextNativeBannerPlatform(banner_container);
            return;
        }
        final StartAppNativeAd startAppNativeAd = new StartAppNativeAd(activity);
        AdEventListener adListener = new AdEventListener() {
            @Override
            public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                ArrayList<NativeAdDetails> ads = startAppNativeAd.getNativeAds();    // get NativeAds list
                new Inflate_ADS(activity).inflate_NB_STARTAPP(ads, banner_container);
            }

            @Override
            public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                nextBannerPlatform(banner_container);
            }

        };
        NativeAdPreferences preferences = new NativeAdPreferences();
        preferences.setAutoBitmapDownload(true);
        preferences.setPrimaryImageSize(4);
        startAppNativeAd.loadAd(preferences, adListener);
    }

    public void showNativeBanner(ViewGroup banner_container, String admobB, String facebookNB) {

        turnShowNativeBanner(banner_container, admobB, facebookNB);
    }

    public void showNativeBanner(ViewGroup banner_container) {

        turnShowNativeBanner(banner_container, "random", "random");
    }

    public void turnShowNativeBanner(ViewGroup banner_container, String admobB, String facebookNB) {
        this.admob_b = admobB;
        this.facebook_nb = facebookNB;
        if (app_adShowStatus == 0) {
            return;
        }

        count_banner++;
        int app_howShowAd = mysharedpreferences.getInt("app_howShowAdBanner", 0);
        String adPlatformSequence = mysharedpreferences.getString("app_adPlatformSequenceBanner", "");
        String alernateAdShow = mysharedpreferences.getString("app_alernateAdShowBanner", "");


        banner_sequence = new ArrayList<String>();
        if (app_howShowAd == 0 && !adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            for (int i = 0; i < adSequence.length; i++) {
                banner_sequence.add(adSequence[i]);
            }

        } else if (app_howShowAd == 1 && !alernateAdShow.isEmpty()) {
            String alernateAd[] = alernateAdShow.split(",");

            int index = 0;
            for (int j = 0; j <= 10; j++) {
                if (count_banner % alernateAd.length == j) {
                    index = j;
                    banner_sequence.add(alernateAd[index]);
                }
            }

            String adSequence[] = adPlatformSequence.split(",");
            for (int j = 0; j < adSequence.length; j++) {
                if (banner_sequence.size() != 0) {
                    if (!banner_sequence.get(0).equals(adSequence[j])) {
                        banner_sequence.add(adSequence[j]);
                    }
                }
            }
        }

        if (banner_sequence.size() != 0) {
            displayNativeBanner(banner_sequence.get(0), banner_container);
        }
    }

    public void displayNativeBanner(String platform, ViewGroup banner_container) {
        if (platform.equals(ADMOB) && admob_AdStatus == 1) {
            if ((admob_loadAdIdsType == 0 || admob_loadAdIdsType == 2 || admob_b.equals("random")) && !admob_b.isEmpty()) {
                admob_b = getRandomPlacementId(ADMOB, "B");
            }

            showNativeAdmobBanner(banner_container);
        } else if (platform.equals(FACEBOOK) && facebook_AdStatus == 1) {
            if ((facebook_loadAdIdsType == 0 || facebook_loadAdIdsType == 2 || facebook_nb.equals("random")) && !facebook_nb.isEmpty()) {
                facebook_nb = getRandomPlacementId(FACEBOOK, "NB");
            }

            showNativeFacebookBanner(banner_container);
        } else if (platform.equals(STARTAPP) && startapp_AdStatus == 1) {
            showNativeStartAppBanner(banner_container);
        } else {
            nextNativeBannerPlatform(banner_container);
        }
    }

    private void nextNativeBannerPlatform(ViewGroup banner_container) {
        if (banner_sequence.size() != 0) {
            banner_sequence.remove(0);
            if (banner_sequence.size() != 0) {
                displayNativeBanner(banner_sequence.get(0), banner_container);
            }
        }
    }


    private void showNativeStartAppBanner(final ViewGroup banner_container) {
        if (STARTAPP_APPID.isEmpty() || startapp_AdStatus == 0) {
            nextNativeBannerPlatform(banner_container);
            return;
        }
        final StartAppNativeAd startAppNativeAd = new StartAppNativeAd(activity);
        AdEventListener adListener = new AdEventListener() {
            @Override
            public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                ArrayList<NativeAdDetails> ads = startAppNativeAd.getNativeAds();    // get NativeAds list
                new Inflate_ADS(activity).inflate_NB_STARTAPP(ads, banner_container);

            }

            @Override
            public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                nextNativeBannerPlatform(banner_container);
            }
        };
        NativeAdPreferences preferences = new NativeAdPreferences();
        preferences.setAutoBitmapDownload(true);
        preferences.setPrimaryImageSize(4);
        startAppNativeAd.loadAd(preferences, adListener);
    }


    private void showNativeFacebookBanner(final ViewGroup container) {
        if (facebook_nb.isEmpty() || facebook_AdStatus == 0) {
            nextNativeBannerPlatform(container);
            return;
        }

        final NativeBannerAd nativeAd1 = new NativeBannerAd(activity, facebook_nb);
        nativeAd1.loadAd(nativeAd1.buildLoadAdConfig().withAdListener(new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                container.removeAllViews();
                container.setVisibility(View.VISIBLE);
                new Inflate_ADS(activity).inflate_NB_FB(nativeAd1, container);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                container.removeAllViews();
                nextNativeBannerPlatform(container);


            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd1 == null || nativeAd1 != ad) {
                    return;
                }
                nativeAd1.downloadMedia();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        }).build());

    }

    private void showNativeAdmobBanner(final ViewGroup banner_container) {

        if (admob_b.isEmpty() || admob_AdStatus == 0) {
            nextNativeBannerPlatform(banner_container);
            return;
        }

        final AdView mAdView = new AdView(activity);
        mAdView.setAdSize(AdSize.SMART_BANNER);
        mAdView.setAdUnitId(admob_b);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                banner_container.removeAllViews();

                nextNativeBannerPlatform(banner_container);

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                banner_container.removeAllViews();
                banner_container.addView(mAdView);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });

    }

    public void showNative(ViewGroup nativeAdContainer, String admobN, String facebookN) {
        turnShowNative(nativeAdContainer, admobN, facebookN);
    }

    public void showNative(ViewGroup nativeAdContainer) {

        turnShowNative(nativeAdContainer, "random", "random");
    }

    public void turnShowNative(ViewGroup nativeAdContainer, String admobN, String facebookN) {
        this.admob_n = admobN;
        this.facebook_n = facebookN;
        if (app_adShowStatus == 0) {
            return;
        }

        count_native++;
        int app_howShowAd = mysharedpreferences.getInt("app_howShowAdNative", 0);
        String adPlatformSequence = mysharedpreferences.getString("app_adPlatformSequenceNative", "");
        String alernateAdShow = mysharedpreferences.getString("app_alernateAdShowNative", "");


        native_sequence = new ArrayList<String>();
        if (app_howShowAd == 0 && !adPlatformSequence.isEmpty()) {
            String adSequence[] = adPlatformSequence.split(",");
            for (int i = 0; i < adSequence.length; i++) {
                native_sequence.add(adSequence[i]);
            }

        } else if (app_howShowAd == 1 && !alernateAdShow.isEmpty()) {
            String alernateAd[] = alernateAdShow.split(",");

            int index = 0;
            for (int j = 0; j <= 10; j++) {
                if (count_native % alernateAd.length == j) {
                    index = j;
                    native_sequence.add(alernateAd[index]);
                }
            }

            String adSequence[] = adPlatformSequence.split(",");
            for (int j = 0; j < adSequence.length; j++) {
                if (native_sequence.size() != 0) {
                    if (!native_sequence.get(0).equals(adSequence[j])) {
                        native_sequence.add(adSequence[j]);
                    }
                }
            }
        }

        if (native_sequence.size() != 0) {
            displayNative(native_sequence.get(0), nativeAdContainer);
        }

    }

    private void displayNative(String platform, ViewGroup nativeAdContainer) {
        if (platform.equals(ADMOB) && admob_AdStatus == 1) {

            if ((admob_loadAdIdsType == 0 || admob_loadAdIdsType == 2 || admob_n.equals("random")) && !admob_n.isEmpty()) {
                admob_n = getRandomPlacementId(ADMOB, "N");
            }

            showAdmobNative(nativeAdContainer);
        } else if (platform.equals(FACEBOOK) && facebook_AdStatus == 1) {
            if ((facebook_loadAdIdsType == 0 || facebook_loadAdIdsType == 2 || facebook_n.equals("random")) && !facebook_n.isEmpty()) {
                facebook_n = getRandomPlacementId(FACEBOOK, "N");
            }

            showFacebookNative(nativeAdContainer);
        } else if (platform.equals(STARTAPP) && startapp_AdStatus == 1) {
            showStartappNative(nativeAdContainer);
        } else {
            nextNativePlatform(nativeAdContainer);
        }
    }

    private void nextNativePlatform(ViewGroup nativeAdContainer) {
        if (native_sequence.size() != 0) {
            native_sequence.remove(0);
            if (native_sequence.size() != 0) {
                displayNative(native_sequence.get(0), nativeAdContainer);
            }
        }
    }


    private void showFacebookNative(final ViewGroup nativeAdContainer) {
        if (facebook_n.isEmpty() || facebook_AdStatus == 0) {
            nextNativePlatform(nativeAdContainer);
            return;
        }

        if(fbNativeAd_preLoad ==null)
        {
            final com.facebook.ads.NativeAd nativeAd = new com.facebook.ads.NativeAd(activity, facebook_n);

            nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    nextNativePlatform(nativeAdContainer);


                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }

                    inflate_NATIV_FB(nativeAd, nativeAdContainer);
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            }).build());
        }
        else
        {
            state_fbNative = "Start";
            inflate_NATIV_FB(fbNativeAd_preLoad, nativeAdContainer);
        }

    }

    public void inflate_NATIV_FB(com.facebook.ads.NativeAd nativeAd, ViewGroup card) {

        card.setVisibility(View.VISIBLE);
        nativeAd.unregisterView();
        LayoutInflater inflater = LayoutInflater.from(activity);
        View adView = inflater.inflate(R.layout.ads_nativ_fb, null);

        card.removeAllViews();
        card.addView(adView);

        NativeAdLayout nativeAdLayout = adView.findViewById(R.id.nativview);

        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);

        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        com.facebook.ads.MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        TextView nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdBody);
        clickableViews.add(nativeAdCallToAction);
        clickableViews.add(nativeAdIcon);
        clickableViews.add(nativeAdSocialContext);

        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);


        fbNativeAd_preLoad = null;

        if (preloadNative_AdStatus==true && (state_fbNative.equals("Start")) || state_fbNative.equals("Fail")) {

            if ((facebook_loadAdIdsType == 0 || facebook_loadAdIdsType == 2 || facebook_n.equals("random")) && !facebook_n.isEmpty()) {
                facebook_n = getRandomPlacementId(FACEBOOK, "N");
            }
            if (facebook_n.isEmpty()) {
                return;
            }
            state_fbNative = "Loading";

            final com.facebook.ads.NativeAd nativeAd1 = new com.facebook.ads.NativeAd(activity, facebook_n);

            nativeAd1.loadAd(nativeAd1.buildLoadAdConfig().withAdListener(new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {

                }

                @Override
                public void onError(Ad ad, AdError adError) {

                    state_fbNative = "Fail";
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (nativeAd1 == null || nativeAd1 != ad) {
                        return;
                    }
                    fbNativeAd_preLoad = nativeAd1;
                    state_fbNative = "Loaded";
                }

                @Override
                public void onAdClicked(Ad ad) {

                }

                @Override
                public void onLoggingImpression(Ad ad) {

                }
            }).build());
        }
        else
        {
            Log.e("fb_state","proccess");
        }
    }

    private void showAdmobNative(final ViewGroup nativeAdContainer) {
        if (admob_n.isEmpty() || admob_AdStatus == 0) {
            nextNativePlatform(nativeAdContainer);
            return;
        }


        if(admobNativeAd_preLoad ==null)
        {
            final AdLoader adLoader = new AdLoader.Builder(activity, admob_n)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.

                            inflate_NATIV_ADMOB(nativeAd, nativeAdContainer);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.

                            nextNativePlatform(nativeAdContainer);
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());
        }
        else
        {
            state_admobNative = "Start";
            inflate_NATIV_ADMOB(admobNativeAd_preLoad, nativeAdContainer);
        }




    }


    public void inflate_NATIV_ADMOB(com.google.android.gms.ads.nativead.NativeAd nativeAd, ViewGroup cardView) {

        cardView.setVisibility(View.VISIBLE);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = (View) inflater
                .inflate(R.layout.ads_nativ_admob, null);
        cardView.removeAllViews();
        cardView.addView(view);

        NativeAdView adView = (NativeAdView) view.findViewById(R.id.uadview);


        adView.setMediaView((com.google.android.gms.ads.nativead.MediaView) adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((TextView) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

        admobNativeAd_preLoad = null;

        if (preloadNative_AdStatus==true && (state_admobNative.equals("Start")) || state_admobNative.equals("Fail")) {

            if ((admob_loadAdIdsType == 0 || admob_loadAdIdsType == 2 || admob_n.equals("random")) && !admob_n.isEmpty()) {
                admob_n = getRandomPlacementId(ADMOB, "N");
            }
            if(admob_n.isEmpty())
            {
                return;
            }
            state_admobNative = "Loading";
            final AdLoader adLoader = new AdLoader.Builder(activity, admob_n)
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd nativeAd) {
                            // Show the ad.
                                admobNativeAd_preLoad = nativeAd;
                                state_admobNative = "Loaded";
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            // Handle the failure by logging, altering the UI, and so on.
                            state_admobNative = "Fail";
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();
            adLoader.loadAd(new AdRequest.Builder().build());


        }
        else
        {
            Log.e("admob_state","proccess");
        }


    }


    private void showStartappNative(final ViewGroup nativeAdContainer) {
        if (STARTAPP_APPID.isEmpty() || startapp_AdStatus == 0) {
            nextNativePlatform(nativeAdContainer);
            return;
        }

        final StartAppNativeAd startAppNativeAd = new StartAppNativeAd(activity);

        AdEventListener adListener = new AdEventListener() {
            @Override
            public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                ArrayList<NativeAdDetails> ads = startAppNativeAd.getNativeAds();
                new Inflate_ADS(activity).inflate_NATIV_STARTAPP(ads, nativeAdContainer);
            }

            @Override
            public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                nextNativePlatform(nativeAdContainer);
            }

        };

        NativeAdPreferences preferences = new NativeAdPreferences();
        preferences.setAutoBitmapDownload(true);
        preferences.setPrimaryImageSize(4);
        startAppNativeAd.loadAd(preferences, adListener);
    }

    public interface MyCallback {
        void callbackCall();
    }


}
