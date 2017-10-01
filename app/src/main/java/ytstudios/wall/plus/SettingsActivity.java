package ytstudios.wall.plus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Yugansh Tyagi on 15-09-2017.
 */

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsPreference()).commit();
    }

}
