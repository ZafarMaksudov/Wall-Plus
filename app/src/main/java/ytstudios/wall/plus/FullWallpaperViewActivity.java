package ytstudios.wall.plus;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.like.LikeButton;
import com.like.OnLikeListener;

/**
 * Created by Yugansh Tyagi on 15-09-2017.
 */

public class FullWallpaperViewActivity extends AppCompatActivity{

    SimpleDraweeView full_imageView;
    String encodedUrlFull,encodedUrlThumb,fileType;
    int wallId;
    AppBarLayout appBarLayout;
    Window window;
    FloatingActionButton floatingActionButton;
    LikeButton favToggleBtn;
    ViewPager fullScreenViewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_wallpaper_view_activity);
        supportPostponeEnterTransition();

        fullScreenViewPager = findViewById(R.id.fullscreen_view_pager);

        favToggleBtn = findViewById(R.id.fav_toggle);
        favToggleBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
//        favToggleBtn.setChecked(false);
//        favToggleBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fav_white_border));
//        favToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked)
//                    favToggleBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.fav_white_full));
//                else
//                    favToggleBtn.setBackgroundDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.fav_white_border));
//            }
//        });


        encodedUrlFull = getIntent().getStringExtra("fullUrl");
        encodedUrlThumb = getIntent().getStringExtra("thumbUrl");

        wallId = getIntent().getIntExtra("id", 0);
        fileType = "." + getIntent().getStringExtra("file_type");


        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               DownloadHandler downloadHandler = new DownloadHandler();
                new DownloadHandler.ImageDownloadAndSave(getApplicationContext()).execute(encodedUrlFull, "Wallpaper " + String.valueOf(wallId)+fileType);
//                downloadHandler.ImageDownloadAndSave.execute(encodedUrl, "Wallpaper " + String.valueOf(wallId)+fileType);
                String fileName = String .valueOf(wallId)+fileType;
                Toast.makeText(getApplicationContext(), "Downloading "+ fileName, Toast.LENGTH_SHORT).show();
            }
        });

        window = getWindow();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            window.setStatusBarColor(getColor(R.color.transparentColor));
        }
        else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT){
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        Fresco.initialize(this);
        appBarLayout = findViewById(R.id.app_bar);

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

     class loadWall extends AsyncTask<String, Void, Uri>{

         @Override
        protected Uri doInBackground(String ... params) {
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

}
