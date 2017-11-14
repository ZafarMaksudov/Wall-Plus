package ytstudios.wall.bucket;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Yugansh Tyagi on 13-11-2017.
 */

public class HelpTranslateActivity extends AppCompatActivity {

    Toolbar toolbar;
    CardView step1,step3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_translate_activity_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.get_started));
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        step1 = findViewById(R.id.step1);
        step1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HelpTranslateActivity.this,"strings.xml",Toast.LENGTH_SHORT).show();
                new DownloadHandler.ImageDownloadAndSave(HelpTranslateActivity.this).execute("https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/strings.xml?alt=media&token=a426a4f5-96df-4b11-a0c5-b6e8969a57d5", "strings.xml");
            }
        });

        step3 = findViewById(R.id.step3);
        step3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {getResources().getString(R.string.gmail)});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Wall Bucket Translation");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "No Email App Found!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){}
            }
        });
    }
}
