package ytstudios.wall.bucket;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

/**
 * Created by Yugansh Tyagi on 13-10-2017.
 */

public class IntroActivity extends MaterialIntroActivity {

//    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//    DatabaseReference databaseReference = firebaseDatabase.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        enableLastSlideAlphaExitTransition(false);

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.color1)
                .buttonsColor(R.color.translucentBlackColor)
                .image(R.drawable.introimg)
                .title(getResources().getString(R.string.app_name))
                .description(getResources().getString(R.string.app_desc))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.color2)
                .buttonsColor(R.color.translucentBlackColor)
                .image(R.drawable.img1)
                .title(getResources().getString(R.string.explore_intro))
                .description(getResources().getString(R.string.explore_intro_desc))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.color3)
                .buttonsColor(R.color.translucentBlackColor)
                .image(R.drawable.img2)
                .title(getResources().getString(R.string.search_intro))
                .description(getResources().getString(R.string.search_intro_desc))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.color4)
                .buttonsColor(R.color.translucentBlackColor)
                .image(R.drawable.img3)
                .title(getResources().getString(R.string.category_intro))
                .description(getResources().getString(R.string.category_intro_desc))
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.color5)
                .buttonsColor(R.color.translucentBlackColor)
                .image(R.drawable.img4)
                .title(getResources().getString(R.string.fav_intro))
                .description(getResources().getString(R.string.fav_intro_desc))
                .build());

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.colorPrimary)
                        .buttonsColor(R.color.translucentBlackColor)
                        .image(R.drawable.introimg)
                        .title(getResources().getString(R.string.ready_ask))
                        .neededPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.press_tick), Toast.LENGTH_SHORT).show();
                    }
                }, getResources().getString(R.string.ready_rock)));

    }

    @Override
    public void onFinish() {
        SharedPreferences settings = getSharedPreferences(getResources().getString(R.string.preferencesName), MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstRun", false);
        editor.apply();
        super.onFinish();
    }
}

