package ytstudios.wall.plus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Created by Yugansh Tyagi on 29-09-2017.
 */

public class DownloadFragmentAdapter extends RecyclerView.Adapter {

    private String[] paths;
    private String[] names;
    Context context;
    DisplayMetrics displayMetrics;

    public DownloadFragmentAdapter(Context context, String[] paths, String[] names) {
        this.paths = paths;
        this.names = names;
        //this.activity = activity;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        displayMetrics = new DisplayMetrics();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.downloaded_item, parent, false);
        viewHolder = new DownloadsHolder(v, context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Bitmap bitmap = BitmapFactory.decodeFile(paths[position]);
        int width = displayMetrics.widthPixels/2;
        int height = 220;
        Bitmap scaled = getResizedBitmap(bitmap, 320,220);

        Uri uri = Uri.fromFile(new File(paths[position]));

        Picasso.with(context).load(uri).into(((DownloadsHolder)holder).downloadedImage);
    }

    @Override
    public int getItemCount() {
        return paths.length;
    }

    public static class DownloadsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView downloadedImage;
        Context context;

        public DownloadsHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            downloadedImage = itemView.findViewById(R.id.downloadImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Toast.makeText(this.context, "TOUCHED", Toast.LENGTH_SHORT).show();

        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }
}
