package ytstudios.wall.bucket;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 20-09-2017.
 */

class FullScreenSwipeAdapter extends PagerAdapter {

    Activity activity;
    ArrayList<WallpapersModel> arrayList;
    LayoutInflater layoutInflater;

    ArrayList<String> path;

    int position;

    SimpleDraweeView draweeView;

    public FullScreenSwipeAdapter(Activity activity, ArrayList<String> path, int position) {
        this.activity = activity;
        this.path = path;
        this.position = position;
    }

    public FullScreenSwipeAdapter(Activity activity, ArrayList<WallpapersModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        Fresco.initialize(activity);
    }

    @Override
    public int getCount() {
        if (arrayList != null) {
            return this.arrayList.size();
        } else {
            return path.size();
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.fullwallpaper_view_layout, container, false);

        draweeView = v.findViewById(R.id.full_image_view);

        if (arrayList != null) {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setLowResImageRequest(ImageRequest.fromUri(arrayList.get(position).getWallpaperURL()))
                    .setImageRequest(ImageRequest.fromUri(arrayList.get(position).getWallpaperFullURL()))
                    .setOldController(draweeView.getController())
                    .build();
            draweeView.setController(controller);
            draweeView.setController(controller);
        } else{
            Uri imageUri= Uri.fromFile(new File(path.get(position)));
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(imageUri)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build();

            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(draweeView.getController())
                    .build();
            ProgressBarDrawable progressBarDrawable = new ProgressBarDrawable();
            progressBarDrawable.setPadding(50);
            progressBarDrawable.setColor(R.color.colorBackground);
            draweeView.getHierarchy().setProgressBarImage(progressBarDrawable);
            draweeView.setController(controller);
            //draweeView.setImageURI(imageUri);
        }


        container.addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CoordinatorLayout) object);
    }
}

