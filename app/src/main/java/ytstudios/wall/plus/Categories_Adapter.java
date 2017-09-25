package ytstudios.wall.plus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 11-09-2017.
 */

public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.ViewHolder> {

    ArrayList<Categories_Model_Class> categoriesArray;
    Context context;

    public Categories_Adapter(Context context, ArrayList<Categories_Model_Class> categoriesArray) {
        this.categoriesArray = categoriesArray;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return categoriesArray.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_cardview_model,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(context).load(categoriesArray.get(position).getCategory_image_id()).into(holder.image);
        holder.name.setText(categoriesArray.get(position).getCategory_name());
        //Picasso.with(context).load(categoriesArray.get(position).getCategory_image_id()).into(holder.image);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.category_name);
            image = view.findViewById(R.id.category_image);
        }
    }
}
