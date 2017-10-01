package ytstudios.wall.plus;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 29-09-2017.
 */

public class DownloadFragmentAdapter extends RecyclerView.Adapter {

    private  ArrayList<String > paths;
    private  ArrayList<String> names;
    Context context;
    DisplayMetrics displayMetrics;
    public static String uri;

    public DownloadFragmentAdapter(Context context, ArrayList<String> paths, ArrayList<String> names) {
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
        viewHolder = new DownloadsHolder(v, context, paths);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        //refreshDownloads(paths, position, (displayMetrics.widthPixels/2), 220, context, holder);

        Log.i("Position  ", paths.get(position));
        int width = displayMetrics.widthPixels/2;
        int height = 220;

        Uri uriImage = Uri.fromFile(new File(paths.get(position)));
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .override(width, height);

        //((DownloadsHolder)holder).downloadedImage.setImageBitmap(scaled);
        //Picasso.with(context).load(uri).into(((DownloadsHolder)holder).downloadedImage);
        Glide.with(context).load(uriImage).apply(myOptions).into(((DownloadsHolder)holder).downloadedImage);
    }

    @Override
    public int getItemCount() {
        try{
            Log.i("Returned ", String.valueOf(paths.size()));
            return paths.size();
        }catch (Exception e){
            Log.i("NO Downloads", "0");
        }
        return 0;
    }

    public static class DownloadsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView downloadedImage;
        Context context;
        ArrayList<String> paths;

        public DownloadsHolder(View itemView, Context context, ArrayList<String> paths) {
            super(itemView);
            this.context = context;
            downloadedImage = itemView.findViewById(R.id.downloadImage);
            this.paths = paths;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            //          Toast.makeText(this.context, paths[position], Toast.LENGTH_SHORT).show();
            //this.context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(paths[position])));
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(paths.get(position)), "image/*");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(paths.get(position)));
            this.context.startActivity(intent);
        }
    }
//
//    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
//    {
//        int width = bm.getWidth();
//        int height = bm.getHeight();
//        float scaleWidth = ((float) newWidth) / width;
//        float scaleHeight = ((float) newHeight) / height;
//        // create a matrix for the manipulation
//        Matrix matrix = new Matrix();
//        // resize the bit map
//        matrix.postScale(scaleWidth, scaleHeight);
//        // recreate the new Bitmap
//        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
//        return resizedBitmap;
//    }
}
