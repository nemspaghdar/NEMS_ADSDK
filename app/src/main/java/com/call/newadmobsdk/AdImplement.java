package com.call.newadmobsdk;

import android.app.Activity;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.ads.UnityAdsImplementation;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

public class AdImplement {

    static NativeAd nativeAd;
    static Boolean testMode = true;
    private static boolean enableLoad = true;
    public Activity activity;
    private static AdView adView;

    private static OnAdListner onAdListner;
    private static Onrewared onrewared;
    private static InterstitialAd mInterstitialAd;
    static RewardedAd rewardedAds;
    static boolean isLoading;
    private static boolean isRewarded = false;

    //    static CheckBox startVideoAdsMuted;
//    static TextView videoStatus;


    public AdImplement(Activity activity) {
        this.activity = activity;
        IronSource.init(activity, MyApplication.resources.getString(R.string.test_key), IronSource.AD_UNIT.OFFERWALL, IronSource.AD_UNIT.INTERSTITIAL, IronSource.AD_UNIT.REWARDED_VIDEO, IronSource.AD_UNIT.BANNER);

    }


    public static void BanneradLoad(Activity activity, RelativeLayout adlayout) {
        LoadBannerAd(activity, adlayout);
    }

    public interface OnAdListner {
        void OnClose();
    }

    public static void Interstitialadload(Activity activity) {
        LoadInterestialAd(activity);
    }

    public static void Nativead(Activity activity, FrameLayout fl_placeholder) {
        LoadAdNative(activity, fl_placeholder);
    }

    public static void LoadBannerAd(Activity activity, RelativeLayout adlayout) {
        adView = new AdView(activity);
        adView.setAdUnitId("ca-app-pub-3940256099942544/");
        adlayout.removeAllViews();
        adlayout.addView(adView);

        AdSize adSize = getAdSize(activity, adlayout);
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.

        adView.loadAd(adRequest);

        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                adlayout.removeAllViews();
                UnityBannerADLoad(activity, adlayout);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    public static AdSize getAdSize(Activity activity, RelativeLayout adlayout) {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = outMetrics.density;
        float adWidthPixels = adlayout.getWidth();
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public static void UnityBannerADLoad(Activity activity, RelativeLayout unitybannerads) {

        UnityAds.initialize(activity, "4295571", null, testMode, enableLoad);
        BannerView bannerView = new BannerView(activity, "Banner_Android", new UnityBannerSize(320, 50));
        bannerView.load();
        unitybannerads.addView(bannerView);

        BannerView.IListener iListener = new BannerView.IListener() {
            @Override
            public void onBannerLoaded(BannerView bannerView) {

            }

            @Override
            public void onBannerClick(BannerView bannerView) {

            }

            @Override
            public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {

            }

            @Override
            public void onBannerLeftApplication(BannerView bannerView) {

            }
        };
        bannerView.setListener(iListener);
    }


    //TODO INTERESTIAL ADS LOADED

    public static void InterLoad(Activity activity, OnAdListner onAdListner1) {
        onAdListner = onAdListner1;
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
        } else {
            LoadInterestialAd(activity);
            onAdListner.OnClose();
        }
    }

    private static void LoadInterestialAd(Activity activity) {

        AdRequest adRequest = new AdRequest.Builder().build();


        InterstitialAd.load(activity, "ca-app-pub-3940256099942544/10331737", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                Log.d("TAG", "The ad was dismissed.");
                                LoadInterestialAd(activity);
                                onAdListner.OnClose();
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {

                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                        UnityIntrestialAd(activity);
                        //onAdListner.OnClose();

                    }
                });

    }

    //TODO UNITY AD IMPEMTATION

    public static void UnityIntrestialAd(Activity activity) {

        if (UnityAds.isReady("Interstitial_Android")) {
            UnityAds.show(activity, "Interstitial_Android");
        }

    }


    public static void UnityInterestialLoad(Activity activity) {
        UnityAdsListener myAdsListener = new UnityAdsListener();
        UnityAds.addListener(myAdsListener);
        UnityAds.initialize(activity.getApplicationContext(), "4295571", testMode);

        UnityAds.load("Interstitial_Android", new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String s) {

            }

            @Override
            public void onUnityAdsFailedToLoad(String s, UnityAds.UnityAdsLoadError unityAdsLoadError, String s1) {

            }
        });


    }


    private static class UnityAdsListener implements IUnityAdsListener {

        @Override
        public void onUnityAdsReady(String adUnitId) {
            // Implement functionality for an ad being ready to show.
        }

        @Override
        public void onUnityAdsStart(String adUnitId) {
            // Implement functionality for a user starting to watch an ad.
        }

        @Override
        public void onUnityAdsFinish(String adUnitId, UnityAds.FinishState finishState) {
            // Implement functionality for a user finishing an ad.

            onAdListner.OnClose();

        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError error, String message) {
            // Implement functionality for a Unity Ads service error occurring.
            onAdListner.OnClose();

        }


    }

    public static void LoadAdNative(Activity activity, FrameLayout fl_placeholder) {


        AdLoader.Builder builder = new AdLoader.Builder(activity, "ca-app-pub-3940256099942544/2247696110");

        builder.forNativeAd(
                new NativeAd.OnNativeAdLoadedListener() {
                    // OnLoadedListener implementation.
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAds) {
                        // If this callback occurs after the activity is destroyed, you must call
                        // destroy and return or you may get a memory leak.
                        boolean isDestroyed = false;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            isDestroyed = activity.isDestroyed();
                        }
                        if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
                            nativeAds.destroy();
                            return;
                        }
                        // You must call destroy on old ads when you are done with them,
                        // otherwise you will have a memory leak.
                        if (nativeAd != null) {
                            nativeAd.destroy();
                        }
                        nativeAd = nativeAds;
                        FrameLayout frameLayout = fl_placeholder;
                        NativeAdView adView =
                                (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_ads_layout, null);
                        populateNativeAdView(nativeAds, adView);
                        frameLayout.removeAllViews();
                        frameLayout.addView(adView);
                    }
                });

//        VideoOptions videoOptions =
//                new VideoOptions.Builder().setStartMuted(startVideoAdsMuted.isChecked()).build();
//
//        NativeAdOptions adOptions =
//                new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
//
//        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {

            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

        //videoStatus.setText("");

    }

    private static void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        // Set the media view.
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline and mediaContent are guaranteed to be in every NativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every NativeAd, so it's important to
        // check before trying to display them.
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
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
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


    }


    //REWAREDED ADS

    public static void loadRewardedAd(Activity activity) {
        if (rewardedAds == null) {
            isLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(activity, "ca-app-pub-3940256099942544/52243549", adRequest,//ca-app-pub-3940256099942544/5224354917
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            rewardedAds = null;
                            isLoading = false;
                            //UnityRewaredAdShow(activity);
                            //UnityRewardedVideoAdLoad(activity);
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            rewardedAds = rewardedAd;

                            isLoading = false;
                        }
                    });
        }
    }


    public static void ShowRewardedVideo(Activity activity, Onrewared onrewared) {

        isRewarded = false;
        if (rewardedAds == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            return;
        }
        rewardedAds.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        rewardedAds = null;
                    }
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        //  onrewared.close();

                        rewardedAds = null;
                        if (isRewarded) {
                            onrewared.userearnsucess();
                        } else {
                            onrewared.userearnunsucess();
                        }
                        loadRewardedAd(activity);

                    }
                });
        Activity activityContext = activity;
        rewardedAds.show(
                activityContext,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        isRewarded = true;
                    }
                });
    }



    public interface Onrewared {
        void userearnsucess();

        void userearnunsucess();
    }

    public static void OndestroyBannerAd() {
        if (adView != null) {
            adView.destroy();
        }
    }

    public static void OnResume() {
        if (adView != null) {
            adView.resume();
        }
    }

    public static void OnPause() {
        if (adView != null) {
            adView.pause();
        }
    }
}
