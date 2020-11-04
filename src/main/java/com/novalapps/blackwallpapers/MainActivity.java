package com.novalapps.blackwallpapers;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.FirebaseApp;
import com.infideap.drawerbehavior.Advance3DDrawerLayout;
import com.novalapps.blackwallpapers.tabsFragment.AllWallpapers;
import com.novalapps.blackwallpapers.tabsFragment.MostViewed;
import com.novalapps.blackwallpapers.utils.Utility;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.home,
            R.drawable.eye
    };

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseApp.initializeApp(this);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorBlack));
        }
        Advance3DDrawerLayout drawer = (Advance3DDrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setViewRotation(Gravity.START, 15);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        drawer.setViewScale(Gravity.START, 0.96f);
        drawer.setRadius(Gravity.START, 20);
        drawer.setViewElevation(Gravity.START, 8);
        drawer.setViewRotation(Gravity.START, 15);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setSelectedTabIndicatorColor(Color.YELLOW);
        tabLayout.setupWithViewPager(viewPager);
        setTabIcons();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial));
        AdRequest adInterstitialRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adInterstitialRequest);


    }

    private void setTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllWallpapers(), "");
        adapter.addFragment(new MostViewed(), "");
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onBackPressed() {
        Advance3DDrawerLayout drawer = (Advance3DDrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        }*/
        if (id == R.id.nav_privacy_policy) {

            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                mInterstitialAd.show();
                // mainscreenloadtime = 0;
            } else {
                Utility.launchActivity(this, PrivacyPolicy.class, false, null);
            }
            if (mInterstitialAd != null) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        Utility.launchActivity(MainActivity.this, PrivacyPolicy.class, false, null);
                        AdRequest adInterstitialRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(adInterstitialRequest);
                    }
                });
            }

        } else if (id == R.id.nav_share) {

            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                mInterstitialAd.show();
                // mainscreenloadtime = 0;
            } else {
                shareApp();
            }
            if (mInterstitialAd != null) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        shareApp();
                        AdRequest adInterstitialRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(adInterstitialRequest);
                    }
                });
            }

        } else if (id == R.id.nav_feedback) {

            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                mInterstitialAd.show();
                // mainscreenloadtime = 0;
            } else {
                launchMarket();
            }
            if (mInterstitialAd != null) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        launchMarket();
                        AdRequest adInterstitialRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(adInterstitialRequest);
                    }
                });
            }

        } else if (id == R.id.nav_rateUs) {

            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                mInterstitialAd.show();
                // mainscreenloadtime = 0;
            } else {
                launchMarket();
            }
            if (mInterstitialAd != null) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        launchMarket();
                        AdRequest adInterstitialRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(adInterstitialRequest);
                    }
                });
            }

        } else if (id == R.id.nav_moreApps) {

            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                mInterstitialAd.show();
                // mainscreenloadtime = 0;
            } else {
                moreApps();
            }
            if (mInterstitialAd != null) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        moreApps();
                        AdRequest adInterstitialRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(adInterstitialRequest);
                    }
                });
            }

        } else if (id == R.id.nav_exit) {

            if (mInterstitialAd.isLoaded() && mInterstitialAd != null) {
                mInterstitialAd.show();
                // mainscreenloadtime = 0;
            } else {
                exitFromApp();
            }
            if (mInterstitialAd != null) {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        exitFromApp();
                        AdRequest adInterstitialRequest = new AdRequest.Builder().build();
                        mInterstitialAd.loadAd(adInterstitialRequest);
                    }
                });
            }


        }

        Advance3DDrawerLayout drawer = (Advance3DDrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void moreApps() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://developer?id=" + "Novel Apps")));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/developer?id=" + "Novel Apps")));


        }
    }

    public void launchMarket() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public void exitFromApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        builder.setMessage("Are you sure you want to Exit?");
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        });
        AlertDialog d = builder.create();
        d.show();
    }

    public void shareApp() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Black HD Wallpapers");
            String shareMessage = "Unique and colorful collection of HD Wallpapers available to decorate your mobile screens! This app offering a Unique verity of wallpapers for your fun. Download Best HD Wallpapers now and get 1000+ attractive wallpapers.\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
            //e.toString();
        }
    }
}
