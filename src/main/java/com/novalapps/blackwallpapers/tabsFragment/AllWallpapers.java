package com.novalapps.blackwallpapers.tabsFragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.novalapps.blackwallpapers.FullViewImage;
import com.novalapps.blackwallpapers.R;
import com.novalapps.blackwallpapers.adapters.AllWallpapersAdapter;
import com.novalapps.blackwallpapers.interfaces.OnImageClickListner;
import com.novalapps.blackwallpapers.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class AllWallpapers extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefresh;
    AdView mAdView;
    AllWallpapersAdapter adapter;
    RecyclerView mRecyclerView1;
    List<String> myGallery1 = new ArrayList<>();

    StorageReference mStorageRef;
    InterstitialAd mInterstitialAd;

    public AllWallpapers() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getContext());
        mStorageRef = FirebaseStorage.getInstance().getReference();        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.all_wallpapers_layout, container, false);
        mRecyclerView1 = myView.findViewById(R.id.recyclerView1);
        mSwipeRefresh = myView.findViewById(R.id.mSwipeRefresh);
        mSwipeRefresh.setOnRefreshListener(this);

        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
                getPicturesFromFirebase();
            }
        });

        mAdView = myView.findViewById(R.id.adView);
        String id = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        //MobileAds.initialize(this, getString(R.string.bannerappid));
        MobileAds.initialize(getContext(), getString(R.string.bannerappid));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(id)
                .build();
        adRequest.isTestDevice(getContext());
        boolean istestdeviice = adRequest.isTestDevice(getContext());
        mAdView.loadAd(adRequest);
        boolean shown = mAdView.isShown();

        mInterstitialAd = new InterstitialAd(getContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial));
        AdRequest adInterstitialRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adInterstitialRequest);

        mRecyclerView1.setLayoutManager(new GridLayoutManager(getContext(), 3));
        /*getPicturesFromFirebase();*/
        return myView;


    }

    private void getPicturesFromFirebase() {

        if (Utility.isConnectingToInternet(getContext())) {
            //showLoadingDialog();
            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference messagesRef = rootRef.child("Wallpapers");
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String key = ds.getKey();

                        DatabaseReference keyRef = rootRef.child("Wallpapers").child("AllWallpapers");

                        ValueEventListener valueEventListener = new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                mSwipeRefresh.setRefreshing(false);
                                myGallery1.clear();
                                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                                    myGallery1.add(ds1.getValue(String.class));
                                }
                                adapter = new AllWallpapersAdapter(getContext(), myGallery1, new OnImageClickListner() {
                                    @Override
                                    public void OnImageClick(final String image) {

                                        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                                            mInterstitialAd.show();
                                            // mainscreenloadtime = 0;
                                        } else {
                                            Intent intent = new Intent(getContext(), FullViewImage.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("image_Object", image);
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }
                                        if (mInterstitialAd != null) {
                                            mInterstitialAd.setAdListener(new AdListener() {
                                                @Override
                                                public void onAdClosed() {
                                                    Intent intent = new Intent(getContext(), FullViewImage.class);
                                                    Bundle bundle = new Bundle();
                                                    bundle.putSerializable("image_Object", image);
                                                    intent.putExtras(bundle);
                                                    startActivity(intent);
                                                    AdRequest adInterstitialRequest = new AdRequest.Builder().build();
                                                    mInterstitialAd.loadAd(adInterstitialRequest);
                                                }
                                            });
                                        }


                                    }
                                });
                                mRecyclerView1.setAdapter(adapter);
                            }


                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                mSwipeRefresh.setRefreshing(false);
                            }
                        };
                        keyRef.addListenerForSingleValueEvent(valueEventListener);
                        myGallery1.clear();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mSwipeRefresh.setRefreshing(false);
                }
            };
            messagesRef.addListenerForSingleValueEvent(eventListener);
        } else {
            Toast.makeText(getContext(), "No internet available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
            mInterstitialAd.show();
        } else {
            getPicturesFromFirebase();
        }
        if (mInterstitialAd != null) {
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    getPicturesFromFirebase();
                    AdRequest adInterstitialRequest = new AdRequest.Builder().build();
                    mInterstitialAd.loadAd(adInterstitialRequest);
                }
            });
        }

     //   getPicturesFromFirebase();
    }
}