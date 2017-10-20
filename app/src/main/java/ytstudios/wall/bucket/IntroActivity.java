package ytstudios.wall.bucket;

import android.Manifest;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.view.View;
import android.view.WindowManager;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

/**
 * Created by Yugansh Tyagi on 13-10-2017.
 */

public class IntroActivity extends MaterialIntroActivity {

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
                        .image(R.drawable.searchwall)
                        .title("Wall Bucket")
                        .description("The best wallpaper downloader app for Android!")
                        .build());
//                new MessageButtonBehaviour(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showMessage("We provide solutions to make you love your work");
//                    }
//                }, "Work with love"));

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.color2)
                .buttonsColor(R.color.translucentBlackColor)
                .image(R.drawable.img1)
                .title(getResources().getString(R.string.explore))
                .description("More than 50000 wallpapers curated for your device")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.color3)
                .buttonsColor(R.color.translucentBlackColor)
                .image(R.drawable.img2)
                .title("Search")
                .description("Find the wallpapers that fits your taste")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.color4)
                .buttonsColor(R.color.translucentBlackColor)
                .image(R.drawable.img3)
                .title("Categories")
                .description("Explore the categories for finding precise wallpapers")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.color5)
                .buttonsColor(R.color.translucentBlackColor)
                .image(R.drawable.img4)
                .title("Add to Favorites")
                .description("Loved a wallpaper? Double Tap to Add them to Favorites so you never lose them")
                .build());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.translucentBlackColor)
                .image(R.drawable.searchwall)
                .title("Ready to Dive in ?")
                .neededPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE})
                .build());

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

