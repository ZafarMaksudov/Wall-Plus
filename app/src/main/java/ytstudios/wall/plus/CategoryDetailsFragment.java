package ytstudios.wall.plus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by Yugansh Tyagi on 26-09-2017.
 */

public class CategoryDetailsFragment extends AppCompatActivity {

    ImageView categoryType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_details_fragment);
        categoryType = findViewById(R.id.categoryType);
    }
}
