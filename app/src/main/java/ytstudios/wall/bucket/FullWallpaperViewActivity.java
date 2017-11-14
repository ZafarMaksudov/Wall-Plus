package ytstudios.wall.bucket;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import static android.widget.Toast.makeText;
import static ytstudios.wall.bucket.Home_Fragment.pageCount;

/**
 * Created by Yugansh Tyagi on 15-09-2017.
 */

public class FullWallpaperViewActivity extends AppCompatActivity {

    ViewPager viewPager;
    FullScreenSwipeAdapter fullScreenSwipeAdapter;
    ArrayList<WallpapersModel> arrayList;
    ArrayList<WallpapersModel> favModel;

    Toolbar toolbar;

    Button downloadWallBtn, setWallBtn;

    public String encodedUrlThumb, encodedUrlFull, fileType;
    int wallId;

    CharSequence options[];

    private boolean fullscreen = false;

    CardView disableAdBlock;
    AdView bannerAd;
    boolean isAdblock;

    Dialog alertDialog;
    ProgressBar progressBar;

    private String ActivityCaller;

    Toast toast;

    ArrayList<String> path;
    int pos;

    public static WallpaperManager wallpaperManager;

    ImageView zoomHeart;

    public DisplayMetrics dm;
    public static int height, width;

    String tempPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);

//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver,
//                new IntentFilter("UpdateHomeViewPager"));

        options = new CharSequence[]{getApplicationContext().getResources().getString(R.string.size_option_small), getApplicationContext().getResources().getString(R.string.size_option_med), getApplicationContext().getResources().getString(R.string.size_option_large)};

        dm = getResources().getDisplayMetrics();
        height = dm.heightPixels;
        width = dm.widthPixels;

        wallpaperManager = WallpaperManager.getInstance(FullWallpaperViewActivity.this);

        setContentView(R.layout.fullscreen_activity_view);

        zoomHeart = findViewById(R.id.zoom_heart);
        final Animation bounce = AnimationUtils.loadAnimation(FullWallpaperViewActivity.this, R.anim.bounce);
        final Animation zoomOut = AnimationUtils.loadAnimation(FullWallpaperViewActivity.this, R.anim.fadeout);
        final Animation breathe = AnimationUtils.loadAnimation(FullWallpaperViewActivity.this, R.anim.breathe);

        ActivityCaller = getIntent().getStringExtra("caller");
        pos = getIntent().getIntExtra("position", 0);
        Log.i("POSITION", String.valueOf(pos));

        downloadWallBtn = findViewById(R.id.downloadWallBtn);
        setWallBtn = findViewById(R.id.setWallBtn);
        disableAdBlock = findViewById(R.id.disableAdBlock);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);


        encodedUrlFull = getIntent().getStringExtra("fullUrl");
        encodedUrlThumb = getIntent().getStringExtra("thumbUrl");
        wallId = getIntent().getIntExtra("id", 0);
        fileType = getIntent().getStringExtra("file_type");


        if (ActivityCaller.equals("Downloads")) {
            path = getIntent().getStringArrayListExtra("paths");
            fullScreenSwipeAdapter = new FullScreenSwipeAdapter(FullWallpaperViewActivity.this, path, pos);
            Log.i("POSITION INSIDE", String.valueOf(pos));
            viewPager.setAdapter(fullScreenSwipeAdapter);
            viewPager.setCurrentItem(pos);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    String id[] = path.get(pos).split("Wallpaper");
                    String title = "Wallpaper " + id[1];
                    getSupportActionBar().setTitle(title);
                    toolbar.setTitleTextColor(ContextCompat.getColor(FullWallpaperViewActivity.this, R.color.white));
                    toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
                    toolbar.setBackgroundColor(ContextCompat.getColor(FullWallpaperViewActivity.this, R.color.translucentBlackColor));
                    toolbar.setNavigationIcon(R.drawable.back_arrow);
                    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
                }

                @Override
                public void onPageSelected(int position) {
                    if (pos > position) {
                        //left
                        pos--;
                        Log.i("POSITION LEFT", String.valueOf(pos));
                    } else if (pos < position) {
                        //right
                        pos++;
                        Log.i("POSITION RIGHT", String.valueOf(pos));
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            downloadWallBtn.setVisibility(View.GONE);
            setWallBtn.setVisibility(View.VISIBLE);
        } else {
            arrayList = getIntent().getParcelableArrayListExtra("array");
            fullScreenSwipeAdapter = new FullScreenSwipeAdapter(FullWallpaperViewActivity.this, arrayList);
            Log.i("ARRAYLIST PAGER SIZE", String.valueOf(arrayList.size()));

            viewPager.setAdapter(fullScreenSwipeAdapter);
            viewPager.setCurrentItem(pos);
            viewPager.setOnTouchListener(new View.OnTouchListener() {
                private float pointX;
                private float pointY;
                private int tolerance = 50;
                private long previousTouchTime;

                @Override
                public boolean onTouch(View v, final MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            return false; //This is important keep this False
                        case MotionEvent.ACTION_DOWN:
                            pointX = event.getX();
                            pointY = event.getY();
                            Log.i("DOWN", String.valueOf(pointX));
                            Log.i("DOWN", String.valueOf(pointY));
                            long temp = System.currentTimeMillis();
                            if (previousTouchTime != 0) {
                                Log.i("MyView", "Time Between Clicks=" + (temp - previousTouchTime));

                                if ((temp - previousTouchTime) < 250) {
                                    Log.i("DOUBLE TAP", "DETECTED");
                                    zoomHeart.setVisibility(View.VISIBLE);
                                    zoomHeart.startAnimation(bounce);
                                    Log.i("ANIMATION", "Bounce");
                                    bounce.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            zoomHeart.startAnimation(breathe);
                                            Log.i("ANIMATION", "Breathe");
                                            boolean exist = MainActivity.favDatabaseHelper.checkExist(arrayList.get(pos).getWallpaperFullURL());
                                            Log.i("EXIST", String.valueOf(exist));
                                            if (!exist) {
                                                boolean inserted = MainActivity.favDatabaseHelper.addFavToDatabase(
                                                        arrayList.get(pos).getWallpaperURL(),
                                                        arrayList.get(pos).getWallpaperFullURL(),
                                                        arrayList.get(pos).getFileType(),
                                                        arrayList.get(pos).getWallId());
                                                Log.i("VALUE OF POS", String.valueOf(pos));
                                                Log.i("IS INSERTED", String.valueOf(inserted));
                                                Cursor cursor = MainActivity.favDatabaseHelper.readFavFromDatabase();
                                                favModel = new ArrayList<>();
                                                try {
                                                    if (cursor.moveToNext()) {
                                                        Log.i("RUNNING", "CURSOR");
                                                        favModel.add(new WallpapersModel(
                                                                cursor.getString(0),
                                                                cursor.getString(1),
                                                                cursor.getString(2),
                                                                Integer.parseInt(cursor.getString(3))
                                                        ));
                                                    }
                                                } catch (Exception e) {
                                                    Log.i("DATABASE EXCEPTION", e.toString());
                                                }
                                                Intent intent = new Intent("ReadDatabase");
                                                LocalBroadcastManager.getInstance(FullWallpaperViewActivity.this).sendBroadcast(intent);
                                            }
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                    breathe.setAnimationListener(new Animation.AnimationListener() {
                                        @Override
                                        public void onAnimationStart(Animation animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animation animation) {
                                            zoomHeart.setAnimation(zoomOut);
                                            Log.i("ANIMATION", "FADE OUT");
                                        }

                                        @Override
                                        public void onAnimationRepeat(Animation animation) {

                                        }
                                    });
                                }
                            } else {
                                Log.i("MyView", "First Click");
                            }
                            previousTouchTime = temp;
                            break;
                        case MotionEvent.ACTION_UP:
                            boolean sameX = pointX + tolerance > event.getX() && pointX - tolerance < event.getX();
                            boolean sameY = pointY + tolerance > event.getY() && pointY - tolerance < event.getY();
                            Log.i("UP", String.valueOf(sameX));
                            Log.i("UP", String.valueOf(sameY));
                            if (sameX && sameY) {
                                //The user "clicked" certain point in the screen or just returned to the same position an raised the finger
                                if (fullscreen == false) {
                                    toolbar.setVisibility(View.INVISIBLE);
                                    disableAdBlock.setVisibility(View.INVISIBLE);
                                    bannerAd.setVisibility(View.INVISIBLE);
                                    downloadWallBtn.setVisibility(View.INVISIBLE);
                                    setWallBtn.setVisibility(View.INVISIBLE);
                                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                    fullscreen = true;
                                } else if (fullscreen == true) {
                                    toolbar.setVisibility(View.VISIBLE);
                                    if (isAdblock) {
                                        disableAdBlock.setVisibility(View.VISIBLE);
                                    }
                                    bannerAd.setVisibility(View.VISIBLE);
                                    downloadWallBtn.setVisibility(View.VISIBLE);
                                    setWallBtn.setVisibility(View.VISIBLE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                                    fullscreen = false;
                                }
                            }
                    }
                    return false;
                }
            });

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    try {
                        String title = "Wallpaper " + String.valueOf(arrayList.get(position).getWallId());
                        getSupportActionBar().setTitle(title);
                        toolbar.setTitleTextColor(ContextCompat.getColor(FullWallpaperViewActivity.this, R.color.white));
                        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
                        toolbar.setBackgroundColor(ContextCompat.getColor(FullWallpaperViewActivity.this, R.color.translucentBlackColor));
                        toolbar.setNavigationIcon(R.drawable.back_arrow);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                onBackPressed();
                            }
                        });
                    } catch (NullPointerException e) {
                    }
                }

                @Override
                public void onPageSelected(int position) {
                    if ((arrayList.size() - position <= 2)) {
                        Log.i("LOAD MORE", "PLEASE");
                        Log.i("PAGE COUNT", String.valueOf(pageCount));
                        Intent intent = new Intent("LoadMore");
                        LocalBroadcastManager.getInstance(FullWallpaperViewActivity.this).sendBroadcast(intent);
                        Log.i("BroadCast LoadMore", "SENT");
                        //viewPager.invalidate();
                    }
                    if (pos > position) {
                        //left
                        pos--;
                        Log.i("POSITION LEFT", String.valueOf(pos));
                    } else if (pos < position) {
                        //right
                        pos++;
                        Log.i("POSITION RIGHT", String.valueOf(pos));
                    }
                    try {
                        encodedUrlFull = arrayList.get(position).getWallpaperFullURL();
                        wallId = arrayList.get(position).getWallId();
                        Log.i("Position", String.valueOf(pos));
                        Log.i("URL", encodedUrlFull);
                    } catch (NullPointerException e) {
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }

        MobileAds.initialize(FullWallpaperViewActivity.this, getResources().getString(R.string.FULLSCREEN_BANNER_ID));
        bannerAd = new AdView(FullWallpaperViewActivity.this);
        bannerAd = findViewById(R.id.bannerAdView);
        final AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("02147518DD550E863FFAA08EA49B5F41")
                .addTestDevice("4F18060E4B4A11E00C6E6C3B8EEF6353")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        bannerAd.loadAd(adRequest);
        bannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int i) {
                if (i == 3 || !isNetworkAvailable()) {
                    disableAdBlock.setVisibility(View.GONE);
                } else {
                    disableAdBlock.setVisibility(View.VISIBLE);
                }
                isAdblock = true;
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                isAdblock = false;
                disableAdBlock.setVisibility(View.GONE);
            }
        });

        setWallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    new setWall(getApplicationContext()).execute();
                } else if (ActivityCaller.equals("Downloads")) {
                    new setWall(getApplicationContext()).execute();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_net_connection), Toast.LENGTH_SHORT).show();
                }
            }
        });

        downloadWallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (ActivityCaller) {
                    case "Category":
                        new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId) + fileType);
                        toast = makeText(getApplicationContext(), getResources().getString(R.string.downloading_wallpaper) + String.valueOf(wallId) + fileType, Toast.LENGTH_SHORT);
                        toast.show();
                        break;

                    case "Search":
                        new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId) + fileType);
                        toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.downloading_wallpaper) + String.valueOf(wallId) + fileType, Toast.LENGTH_SHORT);
                        toast.show();
                        break;

                    case "Home":
                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FullWallpaperViewActivity.this, R.style.MyDialogTheme);
                            builder.setCancelable(false);
                            builder.setTitle("Select your option:");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.downloading_wallpaper) + String.valueOf(wallId) + "(S)" + fileType, Toast.LENGTH_SHORT);
                                            //toast.setGravity(Gravity.BOTTOM, 0, 330);
                                            toast.show();
                                            Log.i("ENCODED URL BEFORE",encodedUrlFull);
                                            encodedUrlFull = encodedUrlFull.replace("-33-iphone6-", "-4-");
                                            new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId) + fileType);
                                            Log.i("ENCODEDURL", encodedUrlFull);
                                            break;
                                        case 1:
                                            toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.downloading_wallpaper) + String.valueOf(wallId) + "(M)" + fileType, Toast.LENGTH_SHORT);
                                            //toast.setGravity(Gravity.BOTTOM, 0, 330);
                                            toast.show();
                                            Log.i("ENCODED URL BEFORE",encodedUrlFull);
                                            new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId) + fileType);
                                            Log.i("ENCODEDURL", encodedUrlFull);
                                            break;
                                        case 2:
                                            toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.downloading_wallpaper) + String.valueOf(wallId) + "(L)" + fileType, Toast.LENGTH_SHORT);
                                            //toast.setGravity(Gravity.BOTTOM, 0, 330);
                                            toast.show();
                                            Log.i("ENCODED URL BEFORE",encodedUrlFull);
                                            encodedUrlFull = encodedUrlFull.replace("-33-iphone6-", "-34-iphone6-plus-");
                                            new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId) + fileType);
                                            Log.i("ENCODEDURL", encodedUrlFull);
                                            break;
                                    }
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        } catch (Exception e) {
                            Log.i("EXCEPTION ", e.toString());
                        }
                        break;

                    case "Favorites":
                        boolean isNetworkConnected = isNetworkAvailable();
                        if (isNetworkConnected) {
                            encodedUrlFull = FavoriteFragment.arrayList.get(pos).getWallpaperFullURL();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.downloading_wallpaper) + String.valueOf(wallId)  + FavoriteFragment.arrayList.get(pos).getFileType(), Toast.LENGTH_SHORT).show();
                            new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(FavoriteFragment.arrayList.get(pos).getWallId()) + FavoriteFragment.arrayList.get(pos).getFileType());
                        } else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_net_connection), Toast.LENGTH_SHORT).show();
                        }
                        break;

                }
            }
        });

    }

    @Override
    public void onPause() {
        if (bannerAd != null) {
            bannerAd.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerAd != null) {
            bannerAd.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (bannerAd != null) {
            bannerAd.destroy();
        }
        super.onDestroy();
    }

    class setWall extends AsyncTask<Void, Void, Void> {

        Context context;
        Bitmap result;

        public setWall(Context context) {
            this.context = context;
            this.result = null;
        }


        @Override
        protected Void doInBackground(Void... voids) {

            if (ActivityCaller.equals("Downloads")) {
                try {
                    result = BitmapFactory.decodeFile(path.get(pos));
                } catch (Exception e) {
                    Log.i("PICASSO EXCEPTION", e.toString());
                }
            } else {
                try {
                    Log.i("URL", encodedUrlFull);
                    result = Picasso.with(FullWallpaperViewActivity.this)
                            .load(encodedUrlFull)
                            .get();
                } catch (Exception e) {
                    Log.i("PICASSO EXCEPTION", e.toString());
                }
            }
            return null;
        }

        @Override
        @TargetApi(11)
        protected void onPreExecute() {
            alertDialog = new Dialog(FullWallpaperViewActivity.this);
            alertDialog.setCancelable(false);
            alertDialog.setContentView(R.layout.setwall_dialog_layout);
            progressBar = alertDialog.findViewById(R.id.settingWall);
            progressBar.setPadding(12, 12, 12, 12);
            alertDialog.setTitle(getResources().getString(R.string.applying));
            progressBar.setIndeterminate(true);
            alertDialog.show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                Intent in = new Intent(wallpaperManager.getCropAndSetWallpaperIntent(getImageUri(FullWallpaperViewActivity.this,result)));
                in.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                in.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                wallpaperManager.suggestDesiredDimensions(width, height);
                startActivityForResult(in, 1);
            } catch (Exception ex) {
                try{
                    wallpaperManager.setBitmap(result);
                    Toast.makeText(context, getResources().getString(R.string.apply_success), Toast.LENGTH_SHORT).show();
                    deleteSetImage();
                }catch (Exception e){
                    Log.d("Wallpaper Set Exception", ex.toString());
                    Toast.makeText(context, getResources().getString(R.string.apply_error), Toast.LENGTH_SHORT).show();
                }
            }
            alertDialog.hide();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == -1) {
            Log.d("CODE ", String.valueOf(resultCode));
            Toast toast = makeText(FullWallpaperViewActivity.this, getResources().getString(R.string.apply_success), Toast.LENGTH_SHORT);
            toast.show();
        }
        deleteSetImage();
    }

    private void deleteSetImage()
    {
        String tempUri = getRealPathFromUri(getApplicationContext(), Uri.parse(tempPath));
        File tempFile = new File(tempUri);
        Log.i("URI OF PATH DELETE", String.valueOf(tempFile.exists()));
        if (tempFile.exists()) {
            tempFile.delete();
        }
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(tempFile); //out is your file you saved/deleted/moved/copied
        mediaScanIntent.setData(contentUri);
        FullWallpaperViewActivity.this.sendBroadcast(mediaScanIntent);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        tempPath = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        Log.d("URI OF SET IMAGE", tempPath);
        ContentResolver cr = this.getContentResolver();
        Log.d("CONTENT TYPE: ", "IS: " + cr.getType(Uri.parse(tempPath)));
        return Uri.parse(tempPath);
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (NullPointerException e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

