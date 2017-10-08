package ytstudios.wall.bucket;

import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import static ytstudios.wall.bucket.FavDatabaseHelper.DATABASE_NAME;

public class MainActivity extends AppCompatActivity {

    BottomBar bottomBar;
    public ViewPager viewPager;
    Toolbar toolbar;
    Categories_Fragment categories_fragment;
    Search_Fragment search_fragment;
    Downloaded_Fragment downloaded_fragment;
    Home_Fragment home_fragment;
    FavoriteFragment favorite_fragment;

    private AdView bannerAd;
    CardView disableAdBlock;

    public static FavDatabaseHelper favDatabaseHelper;

    public static  String DATABASE_FULL_PATH = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Explore");

        try {
            DATABASE_FULL_PATH = MainActivity.this.getDatabasePath(DATABASE_NAME).toString();
            Log.i("PATH", DATABASE_FULL_PATH);
        }catch (Exception e){

        }
        boolean checkDb = checkDataBase();

        favDatabaseHelper = new FavDatabaseHelper(MainActivity.this);

        if(!checkDb){
            Log.i("DATABASE ", "DOES'T EXIST");
            favDatabaseHelper = new FavDatabaseHelper(MainActivity.this);
            Log.i("DATABASE ", "CREATED");
        }

//        disableAdBlock = findViewById(R.id.disableAdBlock);
//        MobileAds.initialize(MainActivity.this, getResources().getString(R.string.FULLSCREEN_BANNER_ID));
//        bannerAd = new AdView(MainActivity.this);
//        bannerAd = findViewById(R.id.bannerAdView);
//        final AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("02147518DD550E863FFAA08EA49B5F41")
//                .addTestDevice("4F18060E4B4A11E00C6E6C3B8EEF6353")
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//
//        bannerAd.loadAd(adRequest);
//        bannerAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(int i) {
//                disableAdBlock.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                disableAdBlock.setVisibility(View.GONE);
//            }
//        });

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new setViewAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(2);
        viewPager.setOffscreenPageLimit(4);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setActionBarTitle("Search");
                    toolbar.setVisibility(View.GONE);
                    bottomBar.selectTabAtPosition(0, true);
                } else if (position == 1) {
                    setActionBarTitle("Categories");
                    toolbar.setVisibility(View.VISIBLE);
                    bottomBar.selectTabAtPosition(1, true);
                } else if (position == 2) {
                    setActionBarTitle("Explore");
                    toolbar.setVisibility(View.VISIBLE);
                    bottomBar.selectTabAtPosition(2, true);
                } else if (position == 3) {
                    setActionBarTitle("Favorites");
                    toolbar.setVisibility(View.VISIBLE);
                    bottomBar.selectTabAtPosition(3, true);
                } else if (position == 4) {
                    setActionBarTitle("Downloaded");
                    toolbar.setVisibility(View.VISIBLE);
                    bottomBar.selectTabAtPosition(4, true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.setDefaultTab(R.id.tab_home);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_search:
                        viewPager.setCurrentItem(0,true);
                        return;
                    case R.id.tab_categories:
                        viewPager.setCurrentItem(1,true);
                        return;
                    case R.id.tab_home:
                        viewPager.setCurrentItem(2,true);
                        return;
                    case R.id.tab_fav:
                        viewPager.setCurrentItem(3,true);
                        return;
                    case R.id.tab_downloaded:
                        viewPager.setCurrentItem(4,true);
                        return;
                }
            }
        });

    }

    public class setViewAdapter extends FragmentPagerAdapter {
        public setViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (search_fragment == null) {
                    search_fragment = new Search_Fragment();
                    return search_fragment;
                }
                return search_fragment;
            }
            else if (position == 1) {
                if (categories_fragment == null) {
                    categories_fragment = new Categories_Fragment();
                    return categories_fragment;
                }
                return categories_fragment;
            }
            else if (position == 2) {
                if (home_fragment == null) {
                    home_fragment = new Home_Fragment();
                    return home_fragment;
                }
                return home_fragment;
            }
            else if (position == 3) {
                if (favorite_fragment == null) {
                    favorite_fragment = new FavoriteFragment();
                    return favorite_fragment;
                }
                return favorite_fragment;
            }
            else if (position == 4) {
                if (downloaded_fragment == null) {
                    downloaded_fragment = new Downloaded_Fragment();
                    return downloaded_fragment;
                }
                return new Downloaded_Fragment();
            }
            else
                return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_FULL_PATH, null, SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
            Log.i("DATABASE", "EXISTS");
        } catch (SQLiteException e) {
            // database doesn't exist yet.
            Log.i("DATABASE", "DOES'T EXISTS");
        }
        return checkDB != null;
    }

}
