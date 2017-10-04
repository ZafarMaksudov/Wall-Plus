package ytstudios.wall.bucket;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 20-09-2017.
 */

class FullScreenSwipeAdapter extends PagerAdapter {

    Activity activity;
    ArrayList<WallpapersModel> arrayList;
    LayoutInflater layoutInflater;

    public FullScreenSwipeAdapter(Activity activity, ArrayList<WallpapersModel> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        Fresco.initialize(activity);
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        SimpleDraweeView draweeView;
        Button downloadBtn,setWallBtn;

        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.fullscreen_view_layout, container, false);

        draweeView = v.findViewById(R.id.full_image_view);
        downloadBtn = v.findViewById(R.id.downloadWallBtn);
        setWallBtn = v.findViewById(R.id.setWallBtn);


        draweeView.setImageURI(arrayList.get(position).getWallpaperFullURL());

        downloadBtn.setText("DOWNLOAD");
        setWallBtn.setText("SET");

        container.addView(v);

        return v;


    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CoordinatorLayout)object);
    }
}

