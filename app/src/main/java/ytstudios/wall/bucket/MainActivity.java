package ytstudios.wall.bucket;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;

import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;

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

    Context context;

    public static FavDatabaseHelper favDatabaseHelper;

    public static String DATABASE_FULL_PATH = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        String sdCard = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(sdCard + context.getResources().getString(R.string.downloadLocation));

        /*  if not exist create new */
        if (!myDir.exists()) {
            Log.i("CREATED DIR ", myDir.toString());
            myDir.mkdirs();
        }

        try {
            AppRate.with(MainActivity.this)
                    .setInstallDays(0) // default 10, 0 means install day.
                    .setLaunchTimes(9) // default 10
                    .setRemindInterval(2) // default 1
                    .setShowLaterButton(true) // default true
                    .setDebug(false) // default false
                    .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
                        @Override
                        public void onClickButton(int which) {
                            Log.d(MainActivity.class.getName(), Integer.toString(which));
                        }
                    })
                    .monitor();

            // Show a dialog if meets conditions
            AppRate.showRateDialogIfMeetsConditions(MainActivity.this);
        } catch (Exception e) {
            Log.i("PlayStore Exception", e.toString());
            Toast.makeText(MainActivity.this, getResources().getString(R.string.playstore_error), Toast.LENGTH_SHORT).show();
        }


        /* Check for app Updates */
        new checkForAppUpdates().execute();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.explore_intro));

        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.preferencesName), MODE_PRIVATE);

        boolean firstRun = preferences.getBoolean("firstRun", true);
        if (firstRun) {
            Log.i("Preferences", String.valueOf(preferences.getBoolean("firstRun", true)));
            // here run your first-time instructions, for example :
            Intent intro = new Intent(MainActivity.this, IntroActivity.class);
            startActivityForResult(intro, 1);

        }

        try {
            DATABASE_FULL_PATH = MainActivity.this.getDatabasePath(DATABASE_NAME).toString();
            Log.i("PATH", DATABASE_FULL_PATH);
        } catch (Exception e) {

        }
        boolean checkDb = checkDataBase();

        favDatabaseHelper = new FavDatabaseHelper(MainActivity.this);

        if (!checkDb) {
            Log.i("DATABASE ", "DOES'T EXIST");
            favDatabaseHelper = new FavDatabaseHelper(MainActivity.this);
            Log.i("DATABASE ", "CREATED");
        }

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new setViewAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(2);
        viewPager.setOffscreenPageLimit(5);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setActionBarTitle(getResources().getString(R.string.search_intro));
                    toolbar.setVisibility(View.GONE);
                    bottomBar.selectTabAtPosition(0, true);
                } else if (position == 1) {
                    setActionBarTitle(getResources().getString(R.string.category_intro));
                    toolbar.setVisibility(View.VISIBLE);
                    bottomBar.selectTabAtPosition(1, true);
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    } catch (NullPointerException e) {
                    }
                } else if (position == 2) {
                    setActionBarTitle(getResources().getString(R.string.explore_intro));
                    toolbar.setVisibility(View.VISIBLE);
                    bottomBar.selectTabAtPosition(2, true);
                } else if (position == 3) {
                    setActionBarTitle(getResources().getString(R.string.fav_intro));
                    toolbar.setVisibility(View.VISIBLE);
                    bottomBar.selectTabAtPosition(3, true);
                } else if (position == 4) {
                    setActionBarTitle(getResources().getString(R.string.downloaded));
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
                switch (tabId) {
                    case R.id.tab_search:
                        viewPager.setCurrentItem(0, true);
                        return;
                    case R.id.tab_categories:
                        viewPager.setCurrentItem(1, true);
                        return;
                    case R.id.tab_home:
                        viewPager.setCurrentItem(2, true);
                        return;
                    case R.id.tab_fav:
                        viewPager.setCurrentItem(3, true);
                        return;
                    case R.id.tab_downloaded:
                        viewPager.setCurrentItem(4, true);
                        return;
                }
            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("In Result ", "TRUE");
        if (requestCode == 1) {
            Log.i("In Result REQUEST CODE", "TRUE");
        }
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
            } else if (position == 1) {
                if (categories_fragment == null) {
                    categories_fragment = new Categories_Fragment();
                    return categories_fragment;
                }
                return categories_fragment;
            } else if (position == 2) {
                if (home_fragment == null) {
                    home_fragment = new Home_Fragment();
                    return home_fragment;
                }
                return home_fragment;
            } else if (position == 3) {
                if (favorite_fragment == null) {
                    favorite_fragment = new FavoriteFragment();
                    return favorite_fragment;
                }
                return favorite_fragment;
            } else if (position == 4) {
                if (downloaded_fragment == null) {
                    downloaded_fragment = new Downloaded_Fragment();
                    return downloaded_fragment;
                }
                return new Downloaded_Fragment();
            } else
                return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("Request COde ", String.valueOf(requestCode));
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

    private class checkForAppUpdates extends AsyncTask<Void, Void, Void> {

        float latestVersion;
        float currentVersion;
        String releaseNotes;

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("IN CHECK UPDATES", "TRUE");
            try {

                Document document = Jsoup.connect("http://play.google.com/store/apps/details?id=" + MainActivity.this.getPackageName()).get();
                latestVersion = Float.parseFloat(document.getElementsByAttributeValue
                        ("itemprop", "softwareVersion").first().text());
                releaseNotes = String.valueOf(document.select("div.recent-change").text());
                Log.d("Latest Version", String.valueOf(latestVersion));
                Log.d("Release Notes", releaseNotes);
            } catch (Exception e) {
                Log.i("Exception", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                currentVersion = Float.parseFloat(context.getPackageManager().getPackageInfo(
                        context.getApplicationInfo().packageName, 0).versionName);
                Log.d("Current Version", String.valueOf(currentVersion));

                if (latestVersion == currentVersion) {
                    if (!MainActivity.this.isFinishing()) {

                        new LovelyStandardDialog(MainActivity.this, R.style.MyDialogTheme)
                                .setTopColorRes(R.color.colorPrimary)
                                .setButtonsColorRes(R.color.white)
                                .setIcon(R.mipmap.ic_launcher)
                                .setTitle(getResources().getString(R.string.update_text) +" " + latestVersion)
                                .setMessage(getResources().getString(R.string.release_notes) +"\n" + releaseNotes)
                                .setCancelable(false)
                                .setPositiveButton(R.string.update, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Uri uri = Uri.parse("market://details?id=" + MainActivity.this.getPackageName());
                                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                        startActivity(goToMarket);
                                    }
                                })
                                .show();
                    }
                }

            } catch (PackageManager.NameNotFoundException e) {
            }
        }
    }
}
