package ytstudios.wall.bucket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 15-09-2017.
 */

public class SearchFragmentCustomAdapter extends RecyclerView.Adapter {

    ArrayList<WallpapersModel> wallpapersModels;
    Context context;
    String query;

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private final int VIEW_NATIVE_AD = 2;

    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private onLoadMoreListener onLoadMoreListener;

    int rvPosition;

    public SearchFragmentCustomAdapter(ArrayList<WallpapersModel> wallpapersModels, Context context, String query, RecyclerView recyclerView) {
        this.wallpapersModels = wallpapersModels;
        this.context = context;
        this.query = query;
        Fresco.initialize(context);

        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {

            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch (getItemViewType(position)) {
                        case VIEW_ITEM:
                            return 1;
                        case VIEW_PROG:
                            if (Home_Fragment.spanCount == 2) {
                                return 2;
                            } else if (Home_Fragment.spanCount == 3)
                                return 3;
                        default:
                            return 3;
                    }
                }
            });

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = gridLayoutManager.getItemCount();
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return wallpapersModels.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder recyclerVh;
        switch (viewType) {
            case VIEW_ITEM:
                View v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.home_wallpaper_item, parent, false);
                recyclerVh = new WallpapersViewHolder(v, context, wallpapersModels);
                return recyclerVh;
            case VIEW_PROG:
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.load_more_progress_bar, parent, false);
                recyclerVh = new ProgressViewHolder(v);
                return recyclerVh;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        Log.i("**********POSITION", String.valueOf(position));
        if (holder instanceof WallpapersViewHolder) {
            Uri uri = Uri.parse(wallpapersModels.get(position).getWallpaperURL());
            ((WallpapersViewHolder) holder).displayWallpaper.setImageURI(uri);
            rvPosition = holder.getAdapterPosition();
            Log.i("INSTANCE OF ", "WALLPAPER");
        }
        else if(holder instanceof ProgressViewHolder)
        {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
            Log.i("INSTANCE OF ", "PROGRESS");
        }
    }

    public void setLoaded(){
        loading = false;
    }

    public void setOnLoadMoreListener(onLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemCount() {
        return wallpapersModels.size();
    }

    public static class WallpapersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView displayWallpaper;
        ArrayList<WallpapersModel> wallpapersModels = new ArrayList<>();
        Context context;

        public WallpapersViewHolder(View itemView, Context context, ArrayList<WallpapersModel> arrayList) {
            super(itemView);
            this.context = context;
            this.wallpapersModels = arrayList;
            displayWallpaper = itemView.findViewById(R.id.fresco_wall);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            //Toast.makeText(context, this.wallpapersModels.get(position).getWallpaperURL(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this.context, FullWallpaperViewActivity.class);
            //ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this.context);
            intent.putExtra("fullUrl", wallpapersModels.get(position).getWallpaperFullURL());
            intent.putExtra("thumbUrl", wallpapersModels.get(position).getWallpaperURL());
            intent.putExtra("file_type", wallpapersModels.get(position).getFileType());
            intent.putExtra("id", wallpapersModels.get(position).getWallId());
            intent.putExtra("caller", "Category");
            intent.putExtra("position", position);
            intent.putParcelableArrayListExtra("array", wallpapersModels);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.context.startActivity(intent);
        }
    }
    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;
        public TextView textView;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar =  v.findViewById(R.id.progressBar);
            textView = v.findViewById(R.id.textview);
            textView.setPadding(0,0,0,0);
        }
    }
}
