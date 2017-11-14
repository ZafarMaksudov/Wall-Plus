package ytstudios.wall.bucket;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 14-11-2017.
 */

public class ContributorsAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Contributors> contributors;

    public ContributorsAdapter(Context context, ArrayList<Contributors> contributors) {
        this.context = context;
        this.contributors = contributors;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contributors_item, parent, false);
        viewHolder = new ContributorsHolder(v, context, contributors);
        Log.d("CONTRI", "VIEW CREATED");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ContributorsHolder)holder).name.setText(contributors.get(position).getName());
        ((ContributorsHolder) holder).about.setText(contributors.get(position).getAbout());
        String temp = context.getResources().getString(R.string.translated_to) + contributors.get(position).getLanguage();
        ((ContributorsHolder) holder).language.setText(temp);
    }

    @Override
    public int getItemCount() {
        return contributors.size();
    }

    public static class ContributorsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name;
        TextView about;
        TextView language;
        Context context;
        ArrayList<Contributors> contributors;

        public ContributorsHolder(View itemView, Context context, ArrayList<Contributors> contributors) {
            super(itemView);
            this.context = context;
            this.contributors = contributors;
            this.name = itemView.findViewById(R.id.contri_name);
            this.about = itemView.findViewById(R.id.contri_about);
            this.language = itemView.findViewById(R.id.contri_trans);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.contributors.get(pos).getLink()));
            this.context.startActivity(intent);
        }
    }
}
