package ytstudios.wall.bucket;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fullscreen_view_layout);

        downloadWallBtn = findViewById(R.id.downloadWallBtn);
        setWallBtn = findViewById(R.id.setWallBtn);
        disableAdBlock = findViewById(R.id.disableAdBlock);

        window = getWindow();
        if (Build.VERSION.SDK_INT >= 21) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        MobileAds.initialize(FullWallpaperViewActivity.this, "ca-app-pub-3940256099942544/6300978111");
        bannerAd = new AdView(FullWallpaperViewActivity.this);
        bannerAd = findViewById(R.id.bannerAdView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("02147518DD550E863FFAA08EA49B5F41")
                .addTestDevice("4F18060E4B4A11E00C6E6C3B8EEF6353")
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        bannerAd.loadAd(adRequest);
        bannerAd.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                disableAdBlock.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                disableAdBlock.setVisibility(View.GONE);
            }
        });

        encodedUrlFull = getIntent().getStringExtra("fullUrl");
        encodedUrlThumb = getIntent().getStringExtra("thumbUrl");

        wallId = getIntent().getIntExtra("id", 0);
        fileType = "." + getIntent().getStringExtra("file_type");


        setWallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new setWall(getApplicationContext()).execute();
            }
        });

//        floatingActionButton = findViewById(R.id.fab);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String fileName = String .valueOf(wallId)+fileType;
//
//                try{
//                    AlertDialog.Builder builder = new AlertDialog.Builder(FullWallpaperViewActivity.this, R.style.MyDialogTheme);
//                    builder.setCancelable(false);
//                    builder.setTitle("Select your option:");
//                    builder.setItems(options, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            switch (which){
//                                case 0 :
//                                    Toast.makeText(getApplicationContext(), "Downloading Small "+ fileName, Toast.LENGTH_SHORT).show();
//                                    new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId)+fileType);
//                                    break;
//                                case 1 :
//                                    Toast.makeText(getApplicationContext(), "Downloading Medium"+ fileName, Toast.LENGTH_SHORT).show();
//                                    encodedUrlFull = encodedUrlFull.replace("-6-", "-8-");
//                                    new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId)+fileType);
//                                    break;
//                                case 2 :
//                                    Toast.makeText(getApplicationContext(), "Downloading Large"+ fileName, Toast.LENGTH_SHORT).show();
//                                    encodedUrlFull = encodedUrlFull.replace("-6-", "-40-");
//                                    new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId)+fileType);
//                                    break;
//                            }
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    });
//                    builder.show();
//                }catch (Exception e){
//                    Log.i("EXCEPTION ", e.toString());
//                }
//
//            }
//        });

        Fresco.initialize(this);

        //avLoadingIndicatorView = findViewById(R.id.avi);
        full_imageView = findViewById(R.id.full_image_view);
        //full_imageView.getHierarchy().setPlaceholderImage();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new loadWall().execute(encodedUrlFull);
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
    
    class loadWall extends AsyncTask<String, Void, Uri> {

        @Override
        protected Uri doInBackground(String... params) {
            Uri uri = Uri.parse(params[0]);
            return uri;
        }

        @Override
        protected void onPreExecute() {
            //avLoadingIndicatorView.show();
        }

        @Override
        protected void onPostExecute(Uri url) {
            //avLoadingIndicatorView.show();
            full_imageView.setImageURI(url);
        }

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

            try {
                result = Picasso.with(FullWallpaperViewActivity.this)
                        .load(encodedUrlFull)
                        .get();
            } catch (Exception e) {
                Log.i("PICASSO EXCEPTION", e.toString());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Applying Wallpaper", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullWallpaperViewActivity.this);
            try {
                wallpaperManager.setBitmap(result);
                Toast.makeText(context, "Wallpaper Applied Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, "Cannot apply wallpaper!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
