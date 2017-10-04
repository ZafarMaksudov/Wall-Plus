package ytstudios.wall.bucket;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 15-09-2017.
 */

public class FullWallpaperViewActivity extends AppCompatActivity {

    SimpleDraweeView full_imageView;
    String encodedUrlFull, encodedUrlThumb, fileType;
    int wallId;
    Window window;
    CharSequence options[] = new CharSequence[]{"Small - 1024x1024", "Medium - 2048x2048", "Large - 2732x2732"};

    Button downloadWallBtn, setWallBtn;
    CardView disableAdBlock;

    private AdView bannerAd;

    Toolbar toolbar;

    private String ActivityCaller;

    private boolean visibility = true;
    private boolean adblock = false;

    Dialog alertDialog;
    ProgressBar progressBar;

    ViewPager viewPager;
    FullScreenSwipeAdapter fullScreenSwipeAdapter;
    ArrayList<WallpapersModel> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fullscreen_activity_view);

        viewPager = findViewById(R.id.view_pager);

        arrayList = getIntent().getParcelableArrayListExtra("array");
        fullScreenSwipeAdapter = new FullScreenSwipeAdapter(FullWallpaperViewActivity.this, arrayList);

        viewPager.setAdapter(fullScreenSwipeAdapter);

    }
}

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.full_wallpaper_view_activity);
//
//        ActivityCaller = getIntent().getStringExtra("caller");
//
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(ContextCompat.getColor(FullWallpaperViewActivity.this, R.color.white));
//        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
//        toolbar.setBackgroundColor(ContextCompat.getColor(FullWallpaperViewActivity.this, R.color.translucentBlackColor));
//        toolbar.setNavigationIcon(R.drawable.back_arrow);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
//        String title = "Wallpaper " + String.valueOf(getIntent().getIntExtra("id", 0));
//        getSupportActionBar().setTitle(title);
//
//        downloadWallBtn = findViewById(R.id.downloadWallBtn);
//        setWallBtn = findViewById(R.id.setWallBtn);
//        disableAdBlock = findViewById(R.id.disableAdBlock);
//
////        window = getWindow();
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////            window.getDecorView().setSystemUiVisibility(
////                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
////            Log.i("TRANS", "STATUS");
////        }
////        else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
////            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////        }
//
//        MobileAds.initialize(FullWallpaperViewActivity.this, getResources().getString(R.string.FULLSCREEN_BANNER_ID));
//        bannerAd = new AdView(FullWallpaperViewActivity.this);
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
//                adblock = true;
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                adblock = false;
//                disableAdBlock.setVisibility(View.GONE);
//            }
//        });
//
//        encodedUrlFull = getIntent().getStringExtra("fullUrl");
//        encodedUrlThumb = getIntent().getStringExtra("thumbUrl");
//
//        wallId = getIntent().getIntExtra("id", 0);
//        fileType = "." + getIntent().getStringExtra("file_type");
//
//
//        setWallBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new setWall(getApplicationContext()).execute();
//            }
//        });
//
//        downloadWallBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                switch (ActivityCaller) {
//                    case "Category":
//                        new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId) + fileType);
//                        makeText(getApplicationContext(), "Downloading " + "Wallpaper " + String.valueOf(wallId) + fileType, Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case "Search":
//                        new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId) + fileType);
//                        makeText(getApplicationContext(), "Downloading " + "Wallpaper " + String.valueOf(wallId) + fileType, Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case "Home":
//                        try {
//                            AlertDialog.Builder builder = new AlertDialog.Builder(FullWallpaperViewActivity.this, R.style.MyDialogTheme);
//                            builder.setCancelable(false);
//                            builder.setTitle("Select your option:");
//                            builder.setItems(options, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    switch (which) {
//                                        case 0:
//                                            makeText(getApplicationContext(), "Downloading Wallpaper " + String.valueOf(wallId) + "(S)" +fileType, Toast.LENGTH_SHORT).show();
//                                            new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId) + fileType);
//                                            Log.i("ENCODEDURL", encodedUrlFull);
//                                            break;
//                                        case 1:
//                                            makeText(getApplicationContext(), "Downloading Wallpaper " + String.valueOf(wallId) + "(M)" +fileType, Toast.LENGTH_SHORT).show();
//                                            encodedUrlFull = encodedUrlFull.replace("-6-", "-8-");
//                                            new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId) + fileType);
//                                            Log.i("ENCODEDURL", encodedUrlFull);
//                                            break;
//                                        case 2:
//                                            makeText(getApplicationContext(), "Downloading Wallpaper " + String.valueOf(wallId) + "(L)" +fileType, Toast.LENGTH_SHORT).show();
//                                            encodedUrlFull = encodedUrlFull.replace("-6-", "-40-");
//                                            new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId) + fileType);
//                                            Log.i("ENCODEDURL", encodedUrlFull);
//                                            break;
//                                    }
//                                }
//                            });
//                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            });
//                            builder.show();
//                        } catch (Exception e) {
//                            Log.i("EXCEPTION ", e.toString());
//                        }
//                        break;
//                }
//            }
//        });
//
//        Fresco.initialize(this);
//
//        //avLoadingIndicatorView = findViewById(R.id.avi);
//        full_imageView = findViewById(R.id.full_image_view);
//        full_imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (visibility == true) {
//                    downloadWallBtn.setVisibility(View.GONE);
//                    setWallBtn.setVisibility(View.GONE);
//                    bannerAd.setVisibility(View.GONE);
//                    toolbar.setVisibility(View.GONE);
//                    disableAdBlock.setVisibility(View.GONE);
//                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//                    visibility = false;
//                } else if (visibility == false) {
//                    downloadWallBtn.setVisibility(View.VISIBLE);
//                    setWallBtn.setVisibility(View.VISIBLE);
//                    if(adblock){
//                        disableAdBlock.setVisibility(View.VISIBLE);
//                    }
//                    else disableAdBlock.setVisibility(View.GONE);
//                    bannerAd.setVisibility(View.VISIBLE);
//                    toolbar.setVisibility(View.VISIBLE);
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//                    visibility = true;
//                }
//
//
//            }
//        });
//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                new loadWall().execute(encodedUrlFull);
//            }
//        });
//
//    }
//
//    @Override
//    public void onPause() {
//        if (bannerAd != null) {
//            bannerAd.pause();
//        }
//        super.onPause();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (bannerAd != null) {
//            bannerAd.resume();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        if (bannerAd != null) {
//            bannerAd.destroy();
//        }
//        super.onDestroy();
//    }
//
//
//    class loadWall extends AsyncTask<String, Void, Uri> {
//
//        @Override
//        protected Uri doInBackground(String... params) {
//            Uri uri = Uri.parse(params[0]);
//            return uri;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            //avLoadingIndicatorView.show();
//        }
//
//        @Override
//        protected void onPostExecute(Uri url) {
//            //avLoadingIndicatorView.show();
//            full_imageView.setImageURI(url);
//        }
//
//    }
//
//    class setWall extends AsyncTask<Void, Void, Void> {
//
//        Context context;
//        Bitmap result;
//
//        public setWall(Context context) {
//            this.context = context;
//            this.result = null;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            try {
//                result = Picasso.with(FullWallpaperViewActivity.this)
//                        .load(encodedUrlFull)
//                        .get();
//            } catch (Exception e) {
//                Log.i("PICASSO EXCEPTION", e.toString());
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            alertDialog = new Dialog(FullWallpaperViewActivity.this);
//            alertDialog.setCancelable(false);
//            alertDialog.setContentView(R.layout.setwall_dialog_layout);
//            progressBar = alertDialog.findViewById(R.id.settingWall);
//            progressBar.setIndeterminate(true);
//            alertDialog.show();
//
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullWallpaperViewActivity.this);
//            try {
//                wallpaperManager.setBitmap(result);
//                Toast toast = Toast.makeText(context, "Wallpaper Applied Successfully", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.BOTTOM, 0, 220);
//                toast.show();
//                alertDialog.hide();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Toast toast = makeText(context, "Error applying wallpaper!", Toast.LENGTH_SHORT);
//                toast.setGravity(Gravity.BOTTOM, 0 ,220);
//                toast.show();
//                alertDialog.hide();
//            }
//        }
//    }
//
//
//    public int getStatusBarHeight() {
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0) {
//            result = getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
//
//}
