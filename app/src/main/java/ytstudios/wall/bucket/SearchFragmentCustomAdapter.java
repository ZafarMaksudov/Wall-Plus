package ytstudios.wall.bucket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 15-09-2017.
 */

public class SearchFragmentCustomAdapter extends RecyclerView.Adapter<SearchFragmentCustomAdapter.ViewHolder> {

    ArrayList<WallpapersModel> wallpapersModels;
    Context context;
    String query;

    public SearchFragmentCustomAdapter(ArrayList<WallpapersModel> wallpapersModels, Context context, String query) {
        this.wallpapersModels = wallpapersModels;
        this.context = context;
        this.query = query;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Fresco.initialize(context);

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_wallpaper_item,parent,false);
        return new ViewHolder(itemView, context, wallpapersModels);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Uri uri = Uri.parse(wallpapersModels.get(position).getWallpaperURL());

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(320, 320))
                .build();
        holder.displayWallpaper.setController(
                Fresco.newDraweeControllerBuilder()
                        .setOldController(holder.displayWallpaper.getController())
                        .setImageRequest(request)
                        .build());

        //Glide.with(context).load(wallpapersModels.get(position).getWallpaperURL()).into(holder.displayWallpaper);
        //Picasso.with(context).load(wallpapers.get(position).getWallpaperUrl()).placeholder(R.drawable.load_animation).into(holder.wallpaper);
    }

    @Override
    public int getItemCount() {
        return wallpapersModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        SimpleDraweeView displayWallpaper;
        ArrayList<WallpapersModel> wallpapersModels;
        Context context;

        public ViewHolder(View itemView, Context context, ArrayList<WallpapersModel> arrayList) {
            super(itemView);
            displayWallpaper = itemView.findViewById(R.id.fresco_wall);
            this.context = context;
            this.wallpapersModels = arrayList;
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            //Toast.makeText(context, this.wallpapersModels.get(position).getWallpaperURL(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this.context, FullWallpaperViewActivity.class);
            //ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(.context, R.id.fresco_wall, "sharedTransition");
            intent.putExtra("fullUrl", wallpapersModels.get(position).getWallpaperFullURL());
            intent.putExtra("file_type", wallpapersModels.get(position).getFileType());
            intent.putExtra("id", wallpapersModels.get(position).getWallId());
            intent.putExtra("caller", "Search");
            this.context.startActivity(intent);
        }
    }
}
