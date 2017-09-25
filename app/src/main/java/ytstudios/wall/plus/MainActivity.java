package ytstudios.wall.plus;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    BottomBar bottomBar;
    public ViewPager viewPager;
    Toolbar toolbar;
    Categories_Fragment categories_fragment;
    Search_Fragment search_fragment;
    Downloaded_Fragment downloaded_fragment;
    Home_Fragment home_fragment;
    Favorite_Fragment favorite_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Explore");

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
                    setActionBarTitle("Search");
                    bottomBar.selectTabAtPosition(0, true);
                } else if (position == 1) {
                    setActionBarTitle("Categories");
                    bottomBar.selectTabAtPosition(1, true);
                } else if (position == 2) {
                    setActionBarTitle("Explore");
                    bottomBar.selectTabAtPosition(2, true);
                } else if (position == 3) {
                    setActionBarTitle("Favorites");
                    bottomBar.selectTabAtPosition(3, true);
                } else if (position == 4) {
                    setActionBarTitle("Downloaded");
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
                    favorite_fragment = new Favorite_Fragment();
                    return favorite_fragment;
                }
                return favorite_fragment;
            }
            else if (position == 4) {
                if (downloaded_fragment == null) {
                    downloaded_fragment = new Downloaded_Fragment();
                    return downloaded_fragment;
                }
                return downloaded_fragment;
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

}
