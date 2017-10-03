package ytstudios.wall.bucket;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

import static android.widget.Toast.makeText;

/**
 * Created by Yugansh Tyagi on 29-09-2017.
 */

public class DownloadFragmentAdapter extends RecyclerView.Adapter {

    private ArrayList<String> paths;
    private ArrayList<String> names;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Log.i("Position  ", paths.get(position));
        int width = displayMetrics.widthPixels / 2;
        int height = 220;

        Uri uriImage = Uri.fromFile(new File(paths.get(position)));
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .override(width, height);
        Glide.with(context).load(uriImage).apply(myOptions).into(((DownloadsHolder) holder).downloadedImage);

        ((DownloadsHolder) holder).deleteDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
                Animation animation1 = AnimationUtils.loadAnimation(context, R.anim.fadeout);

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Log.i("ANIMATION", "END");
                        File fdelete = new File(paths.get(position));
                        if (fdelete.exists()) {
                            fdelete.delete();
                        }
                        Intent mediaScanIntent = new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(fdelete); //out is your file you saved/deleted/moved/copied
                        mediaScanIntent.setData(contentUri);
                        context.sendBroadcast(mediaScanIntent);
                        Intent intent = new Intent("Refresh");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                ((DownloadsHolder) holder).downloadedImage.startAnimation(animation);
                ((DownloadsHolder) holder).deleteDownload.startAnimation(animation1);
                ((DownloadsHolder) holder).setAsWall.startAnimation(animation1);
                ((DownloadsHolder) holder).view.startAnimation(animation1);
            }
        });

        ((DownloadsHolder) holder).setAsWall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new setWall(context, position).execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        try {
            Log.i("Returned ", String.valueOf(paths.size()));
            return paths.size();
        } catch (Exception e) {
            Log.i("NO Downloads", "0");
        }
        return 0;
    }

    public static class DownloadsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView downloadedImage, deleteDownload, setAsWall;
        View view;
        Context context;
        ArrayList<String> paths;

        public DownloadsHolder(View itemView, Context context, ArrayList<String> paths) {
            super(itemView);
            this.context = context;
            downloadedImage = itemView.findViewById(R.id.downloadImage);
            this.paths = paths;
            itemView.setOnClickListener(this);
            this.deleteDownload = itemView.findViewById(R.id.deleteDownload);
            this.setAsWall = itemView.findViewById(R.id.setaswall);
            this.view = itemView.findViewById(R.id.viewBar);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            makeText(context, paths.get(position), Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.parse(paths.get(position)), "image/*");
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(paths.get(position)));
//            this.context.startActivity(intent);

        }
    }

    class setWall extends AsyncTask<Void, Void, Void> {

        Context context;
        Bitmap result;
        int position;

        public setWall(Context context, int position) {
            this.context = context;
            this.result = null;
            this.position = position;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            result = BitmapFactory.decodeFile(paths.get(position));
            return null;
        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(context, "Applying Wallpaper", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            try {
                wallpaperManager.setBitmap(result);
                Toast.makeText(context, "Wallpaper Applied Successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, "Error applying wallpaper!", Toast.LENGTH_SHORT);
            }
        }
    }
}
