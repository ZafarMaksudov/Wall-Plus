package ytstudios.wall.bucket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 10-09-2017.
 */

public class Categories_Fragment extends Fragment {

    RecyclerView categories_rv;
    ArrayList<Categories_Model_Class> categories;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.categories_fragment, null);

        categories_rv = view.findViewById(R.id.categories_rv);
        categories_rv.setHasFixedSize(true);

        categories_rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        categories_rv.addItemDecoration(new RecyclerItemDecoration(2));

        initializeCategories();
        initializeAdapter();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu,menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_about:
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void initializeCategories() {
        categories = new ArrayList<>();
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_abstract), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/abstractimg.jpg?alt=media&token=622244ff-2675-4b54-9610-e3f864ab2c00"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_animal), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/animal.jpg?alt=media&token=60e0e0ae-6848-464e-9532-28e661d33179"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_anime), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/anime.jpg?alt=media&token=ba505dbd-5697-43ad-af8e-92e91ea6c347"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_city), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/city.jpg?alt=media&token=d8d3673b-f30e-492e-a9ad-1b25d634b070"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_comic), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/comics.jpg?alt=media&token=c77ad840-0ef5-4120-98eb-5e305dd49dcf"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_games), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/Games.jpg?alt=media&token=b2aaba9c-e3b8-431e-9d67-7e44b185e78e"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_movies), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/movies.jpg?alt=media&token=fbd54f1f-7afe-487e-bf9a-f0b0709d8d1c"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_nature), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/nature.jpg?alt=media&token=9614c034-a256-4fe3-8bab-f49bd8fb3ee2"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_patterns), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/patterns.jpg?alt=media&token=df85011d-2617-4ba8-9f59-4cb713689dc6"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_scifi), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/Sci-Fi.jpg?alt=media&token=264fc122-90a7-4cdf-9e94-e0aea7c0bea4"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_tv), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/tv.jpg?alt=media&token=0e872639-ec61-49c5-8caf-bc5035fb3b4f"));
        categories.add(new Categories_Model_Class(getResources().getString(R.string.cat_vehicles), "https://firebasestorage.googleapis.com/v0/b/wallbucket-1a592.appspot.com/o/cars.jpg?alt=media&token=70d17da0-0a4b-4fbb-82f5-af7c6659f50f"));

    }

    private void initializeAdapter() {
        Categories_Adapter categories_adapter = new Categories_Adapter(getContext(), categories);
        categories_rv.setAdapter(categories_adapter);
    }
}
