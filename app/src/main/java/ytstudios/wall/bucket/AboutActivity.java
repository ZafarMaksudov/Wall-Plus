package ytstudios.wall.bucket;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Yugansh Tyagi on 14-10-2017.
 */

public class AboutActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView github, gmail, linkedIn;
    CardView shareApp, rateApp, translate,contributors;
    SimpleDraweeView me, codingScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        toolbar = findViewById(R.id.aboutToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.menu_about));
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        me = findViewById(R.id.profile_image);
        me.setImageURI("https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/me.jpeg?alt=media&token=8b85122a-3010-420e-98e8-7d2e152fdbfc");
        codingScreen = findViewById(R.id.codingScreen);
        codingScreen.setImageURI("https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/codingscreen.jpg?alt=media&token=d5b5a2f6-85a5-447f-b2d9-47d63a359234");

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
                try {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.gmail)});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Regarding Wall Bucket");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Email App Found!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                }

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

        shareApp = findViewById(R.id.shareApp);
        rateApp = findViewById(R.id.rateApp);
        translate = findViewById(R.id.translate);

        shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.shareText) + AboutActivity.this.getPackageName());

                try {
                    startActivity(Intent.createChooser(intent, "Select an action"));
                } catch (android.content.ActivityNotFoundException ex) {
                    // (handle error)
                }
            }
        });

        rateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + AboutActivity.this.getPackageName());
                Log.i("URI RATE APP", uri.toString());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + AboutActivity.this.getPackageName())));
                }
            }
        });

        try {
            translate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AboutActivity.this, HelpTranslateActivity.class);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {

        }

        contributors = findViewById(R.id.contributors);
        contributors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutActivity.this, ContributorsActivity.class);
                startActivity(intent);
            }
        });

    }

}
