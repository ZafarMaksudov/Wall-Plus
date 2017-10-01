package ytstudios.wall.bucket;

import android.widget.ImageView;

/**
 * Created by Yugansh Tyagi on 15-09-2017.
 */

public interface WallpaperClickListener {

    void onWallpaperClick(int position, WallpapersModel wallpaperItem, ImageView imageView);
}
