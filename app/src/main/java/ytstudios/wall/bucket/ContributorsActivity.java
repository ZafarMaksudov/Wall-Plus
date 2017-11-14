package ytstudios.wall.bucket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 13-11-2017.
 */

public class ContributorsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<Contributors> contributors;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contributors_activity);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.contributors));
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        contributors = new ArrayList<>();
        contributors.add(new Contributors(getResources().getString(R.string.myname), "Indie Developer with a craze for Technology.", "Italian | Spanish",getResources().getString(R.string.githubLink)));

        recyclerView = findViewById(R.id.contributors_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(ContributorsActivity.this));
        recyclerView.setAdapter(new ContributorsAdapter(ContributorsActivity.this, contributors));

    }
}
