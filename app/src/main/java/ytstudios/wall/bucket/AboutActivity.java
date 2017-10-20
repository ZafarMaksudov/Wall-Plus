package ytstudios.wall.bucket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Yugansh Tyagi on 14-10-2017.
 */

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView github,gmail,linkedIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        toolbar = findViewById(R.id.aboutToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About");
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        github = findViewById(R.id.github);
        gmail = findViewById(R.id.gmail);
        linkedIn = findViewById(R.id.linkedIn);

        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.githubLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getResources().getString(R.string.gmail)});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Wall Bucket");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No Email App Found!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){}

            }
        });

        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(getResources().getString(R.string.linkedinLink));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

}
