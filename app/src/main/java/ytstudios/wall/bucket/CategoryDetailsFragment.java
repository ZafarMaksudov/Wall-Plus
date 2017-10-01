package ytstudios.wall.bucket;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yugansh Tyagi on 26-09-2017.
 */

public class CategoryDetailsFragment extends Activity {

    Toolbar toolbar;
    String categoryName;
    RecyclerView recyclerView;
    CategoryDetailsFragmentAdapter categoryDetailsFragmentAdapter;
    GridLayoutManager gridLayoutManager;
    ArrayList<WallpapersModel> wallpapersModels;

    private Handler handler;
    private int pageCount = 1;

    private String categorySearchUrl;

    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories_details_fragment);

        wallpapersModels = new ArrayList<>();
        handler = new Handler();
        context = getApplicationContext();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        categoryName = getIntent().getStringExtra("categoryName");
        toolbar.setTitle(categoryName);
        switch (categoryName) {
            case "Abstract":
                categorySearchUrl = "https://mobile.alphacoders.com/by-resolution/1/Abstract/1080x1920-Wallpapers/?page=";
                break;
            case "Animal":
                categorySearchUrl = "https://mobile.alphacoders.com/by-resolution/1/Animal/1080x1920-Wallpapers/?page=";
                break;
            case "Amoled":
                categorySearchUrl = "https://mobile.alphacoders.com/by-resolution/1/Amoled/1080x1920-Wallpapers/?page=";
                //Todo : Find wallpaper site for Amoled walls!
                break;
            case "Anime":
                categorySearchUrl = "https://mobile.alphacoders.com/by-resolution/1/Anime/1080x1920-Wallpapers/?page=";
                break;
            case "Cityscape":
                categorySearchUrl = "https://mobile.alphacoders.com/by-resolution/1/Man-Made/1080x1920-Wallpapers/?page=";
                break;
            case "Minimal":
                categorySearchUrl = "https://mobile.alphacoders.com/by-resolution/1/Minimal/1080x1920-Wallpapers/?page=";
                break;
            case "Nature":
                categorySearchUrl = "https://mobile.alphacoders.com/by-resolution/1/Earth/1080x1920-Wallpapers/?page=";
                break;
            case "Vehicles":
                categorySearchUrl = "https://mobile.alphacoders.com/by-resolution/1/Vehicles/1080x1920-Wallpapers/?page=";
                break;
        }
        getCategoryWallpaper(categorySearchUrl + pageCount);

        recyclerView = findViewById(R.id.detailsCategory_rv);
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        categoryDetailsFragmentAdapter = new CategoryDetailsFragmentAdapter(wallpapersModels, getApplicationContext(), recyclerView);
        categoryDetailsFragmentAdapter.setOnLoadMoreListener(new onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pageCount++;
                //add null , so the adapter will check view_type and show progress bar at bottom
                wallpapersModels.add(null);
                Log.i("INSERTED", "NULL");
                categoryDetailsFragmentAdapter.notifyItemInserted(wallpapersModels.size() - 1);
                //Log.i("SIZE ", String.valueOf(wallpapersModelArrayList.size()));

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wallpapersModels.remove(wallpapersModels.size() - 1);
                        categoryDetailsFragmentAdapter.notifyItemRemoved(wallpapersModels.size());
                        Log.i("REMOVED", "NULL");
                        //add items one by one
                        Log.i("INIT", "DATA");
                        new loadMore().execute(categorySearchUrl + pageCount);
                        categoryDetailsFragmentAdapter.setLoaded();
                        Log.i("INIT", "FINISHED");
                    }
                }, 700);
            }
        });

        recyclerView.setAdapter(categoryDetailsFragmentAdapter);
        recyclerView.addItemDecoration(new RecyclerItemDecoration(2));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class loadMore extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Document document = Jsoup.connect(params[0]).get();
                Element wall = document.select("div.thumb-container").first();
                Elements img = wall.getElementsByAttribute("src");
                Elements widList = wall.getElementsByAttribute("alt");
                List list = img.eachAttr("src");
                List id = widList.eachAttr("alt");

                for (int i = 0; i < list.size(); i++) {
                    String wallUrl = list.get(i).toString();
                    String wallId = id.get(i).toString();
                    String sep[] = wallId.split("Wallpaper ");
                    wallpapersModels.add(wallpapersModels.size() - 1, new WallpapersModel(
                            wallUrl,///
                            wallUrl.replace("thumb-", ""),
                            "jpg",
                            Integer.valueOf(sep[1])
                    ));
                }
            } catch (Exception e) {
                Log.i("ERROR 2", "E");
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            categoryDetailsFragmentAdapter.notifyItemInserted(wallpapersModels.size());
        }
    }

    private void getCategoryWallpaper(String url) {
        new Read().execute(url);
    }

    class Read extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {
            try {
                Document document = Jsoup.connect(params[0]).get();
                Element wall = document.select("div.thumb-container").first();
                Elements img = wall.getElementsByAttribute("src");
                Elements widList = wall.getElementsByAttribute("alt");
                List list = img.eachAttr("src");
                List id = widList.eachAttr("alt");
//                Log.i("LIST ", list.toString());
//                Log.i("ID", id.toString());


                for (int i = 0; i < list.size(); i++) {
                    String wallUrl = list.get(i).toString();
                    String wallId = id.get(i).toString();
                    String sep[] = wallId.split("Wallpaper ");
//
//                    Log.i("URL ", wallUrl);
//                    Log.i("WALLID ", String.valueOf(sep[1]));
                    wallpapersModels.add(new WallpapersModel(
                            wallUrl,///
                            wallUrl.replace("thumb-", ""),
                            "jpg",
                            Integer.valueOf(sep[1])
                    ));
                }
            } catch (Exception e) {
                Log.i("ERROR 1", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            categoryDetailsFragmentAdapter.notifyDataSetChanged();
        }
    }
}
