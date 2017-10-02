package ytstudios.wall.bucket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 15-09-2017.
 */

public class FullWallpaperViewActivity extends AppCompatActivity {

//    SimpleDraweeView full_imageView;
//    String encodedUrlFull, encodedUrlThumb, fileType;
//    int wallId;
//    Window window;
//    CharSequence options[] = new CharSequence[]{"Small - 1024x1024", "Medium - 2048x2048", "Large - 2732x2732"};
//
//    Button downloadWallBtn, setWallBtn;
//    CardView disableAdBlock;
//
//    private AdView bannerAd;

    ViewPager viewPager;
    FullScreenSwipeAdapter fullScreenSwipeAdapter;
    ArrayList<WallpapersModel> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fullscreen_activity_view);

        viewPager = findViewById(R.id.view_pager);

        //arrayList = getIntent().getParcelableArrayListExtra("array");
        fullScreenSwipeAdapter = new FullScreenSwipeAdapter(FullWallpaperViewActivity.this, arrayList);

        viewPager.setAdapter(fullScreenSwipeAdapter);

    }

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

//        Fresco.initialize(this);
//
//    //avLoadingIndicatorView = findViewById(R.id.avi);
//    full_imageView =
//
//    findViewById(R.id.full_image_view);
//    //full_imageView.getHierarchy().setPlaceholderImage();
//        this.
//
//    runOnUiThread(new Runnable() {
//        @Override
//        public void run () {
//            new loadWall().execute(encodedUrlFull);
//        }
//    });
//
//}
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
//class loadWall extends AsyncTask<String, Void, Uri> {
//
//    @Override
//    protected Uri doInBackground(String... params) {
//        Uri uri = Uri.parse(params[0]);
//        return uri;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        //avLoadingIndicatorView.show();
//    }
//
//    @Override
//    protected void onPostExecute(Uri url) {
//        //avLoadingIndicatorView.show();
//        full_imageView.setImageURI(url);
//    }
//
//}
//
//class setWall extends AsyncTask<Void, Void, Void> {
//
//    Context context;
//    Bitmap result;
//
//    public setWall(Context context) {
//        this.context = context;
//        this.result = null;
//    }
//
//    @Override
//    protected Void doInBackground(Void... voids) {
//
//        try {
//            result = Picasso.with(FullWallpaperViewActivity.this)
//                    .load(encodedUrlFull)
//                    .get();
//        } catch (Exception e) {
//            Log.i("PICASSO EXCEPTION", e.toString());
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        Toast.makeText(context, "Applying Wallpaper", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullWallpaperViewActivity.this);
//        try {
//            wallpaperManager.setBitmap(result);
//            Toast.makeText(context, "Wallpaper Applied Successfully", Toast.LENGTH_SHORT).show();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            Toast.makeText(context, "Cannot apply wallpaper!", Toast.LENGTH_SHORT).show();
//        }
//    }
//}
}
