package com.buildbox;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.progames.shark.R;

import org.cocos2dx.lib.Cocos2dxActivity;

import java.lang.ref.WeakReference;

public class AdIntegrator {
    public static native boolean rewardedVideoDidEnd();
    public static AdView mAdView;
    public static InterstitialAd mInterstitialAd;
    public static RewardedVideoAd mRewardedVideoAd;

    private static WeakReference<Cocos2dxActivity> activity;

    public static void initBridge(Cocos2dxActivity act){
        activity = new WeakReference<Cocos2dxActivity>(act);
    }

    public static void initAds(){
        activity.get().runOnUiThread( new Runnable() {
            public void run() {
                Log.d("my_test", "initAds");
                //Admob AppID
                MobileAds.initialize(activity.get(), getString(R.string.app_ad_mob_id));

                //banner
                FrameLayout frameLayout = (FrameLayout)activity.get().findViewById(android.R.id.content);
                RelativeLayout layout = new RelativeLayout( activity.get() );
                layout.setVerticalGravity(Gravity.BOTTOM);
                mAdView = new AdView( activity.get());
                mAdView.setAdSize(AdSize.SMART_BANNER);
                mAdView.setAdUnitId(getString(R.string.ad_banner_id));
                mAdView.setVisibility(View.INVISIBLE);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
                layout.addView(mAdView);
                frameLayout.addView(layout);

                //interstitial
                mInterstitialAd = new InterstitialAd(activity.get());
                mInterstitialAd.setAdUnitId(getString(R.string.ad_interstitial_id));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        // Load the next interstitial.
                        mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    }

                });

            }
        });


    }

    public static void showBanner(){
        activity.get().runOnUiThread( new Runnable() {
            public void run() {
                Log.d("my_test", "showBanner");
                mAdView.setVisibility(View.VISIBLE);
            }
        });
    }

    public static void hideBanner(){
        activity.get().runOnUiThread( new Runnable() {
            public void run() {
                Log.d("my_test", "hideBanner");
                mAdView.setVisibility(View.INVISIBLE);
            }
        });
    }

    public static boolean isBannerVisible(){
        Log.d("my_test", "isBannerVisible");
        return true;
    }


    public static void showInterstitial(){
        activity.get().runOnUiThread( new Runnable() {
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    Log.d("my_test", "showInterstitial");
                    mInterstitialAd.show();
                }
            }
        });
    }

    private static String getString(int id) {
        return activity.get().getResources().getString(id);
    }
}
