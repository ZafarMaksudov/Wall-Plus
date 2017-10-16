package ytstudios.wall.bucket;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yugansh Tyagi on 10-09-2017.
 */

public class Search_Fragment extends Fragment {

    EditText searchBar;
    CardView searchView;
    ImageView imageView;
    TextView searchNet, searchQueryText, searchQuery;
    LottieAnimationView animationView;

    ArrayList<WallpapersModel> wallpapersModels;
    RecyclerView recyclerView;
    SearchFragmentCustomAdapter searchFragmentCustomAdapter;

    String query;

    Handler handler;

    GridLayoutManager gridLayoutManager;

    private static int numPages, currPg = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        wallpapersModels = new ArrayList<>();

        View view = inflater.inflate(R.layout.search_fragment, null);

        searchView = view.findViewById(R.id.search_cardView);

        handler = new Handler();

        recyclerView = view.findViewById(R.id.searchFragment_rv);
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        searchBar = view.findViewById(R.id.search_bar);

//        disableAdBlock = view.findViewById(R.id.disableAdBlock);
//        disableAdBlock.bringToFront();
//        MobileAds.initialize(getActivity(), getResources().getString(R.string.CATEGORY_BANNED_ID));
//        bannerAd = new AdView(getActivity());
//        bannerAd = view.findViewById(R.id.bannerAdView);
//        bannerAd.bringToFront();
//        final AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("02147518DD550E863FFAA08EA49B5F41")
//                .addTestDevice("4F18060E4B4A11E00C6E6C3B8EEF6353")
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .build();
//
//        bannerAd.loadAd(adRequest);
//        bannerAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdFailedToLoad(int i) {
//                disableAdBlock.setVisibility(View.VISIBLE);
//                isAdblock = true;
//                Log.i("Searc fragment", "NOT LOADED");
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                isAdblock = false;
//                disableAdBlock.setVisibility(View.GONE);
//                Log.i("Searc fragment", "ADLOADED");
//            }
//        });

        searchFragmentCustomAdapter = new SearchFragmentCustomAdapter(wallpapersModels, getContext(), searchBar.getText().toString(), recyclerView);
        recyclerView.setAdapter(searchFragmentCustomAdapter);
        recyclerView.addItemDecoration(new RecyclerItemDecoration(2));

        searchFragmentCustomAdapter.setOnLoadMoreListener(new onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.i("IN LOADMORE", "WITHOUT IF");
                if (currPg <= numPages) {
                    Log.i("CURRENT SET LOAD", String.valueOf(currPg));
                    Log.i("NUM OF PAGES SET LOAD", String.valueOf(numPages));
                    wallpapersModels.add(null);
                    searchFragmentCustomAdapter.notifyItemInserted(wallpapersModels.size() - 1);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            wallpapersModels.remove(wallpapersModels.size() - 1);
                            searchFragmentCustomAdapter.notifyItemRemoved(wallpapersModels.size());
                            Log.i("REMOVED", "NULL");
                            //add items one by one
                            Log.i("INIT SEARCH", "DATA");
                            new loadMore().execute("https://mobile.alphacoders.com/by-resolution/1/1080x1920-Wallpapers/?search=" + query + "&page=" + currPg);
                            searchFragmentCustomAdapter.setLoaded();
                            Log.i("CURRENT ", String.valueOf(currPg));
                            Log.i("NUM OF PAGES  ", String.valueOf(numPages));
                            Log.i("LOAD MORE", "https://mobile.alphacoders.com/by-resolution/1/1080x1920-Wallpapers/?search=" + query + "&page=" + currPg);
                        }
                    }, 700);
                }
            }
        });

        imageView = view.findViewById(R.id.search_wall_placeholder);
        searchNet = view.findViewById(R.id.search_net);
        searchQueryText = view.findViewById(R.id.searching_query);
        animationView = view.findViewById(R.id.animation_view);
        searchQuery = view.findViewById(R.id.query_name);

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                query = searchBar.getText().toString();
                currPg = 1;
                Log.i("CURRENT ", String.valueOf(currPg));

                if (i == EditorInfo.IME_ACTION_SEARCH && query.length() > 2) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //https://wall.alphacoders.com/api2.0/get.php?auth="+API_KEY+"&method=highest_rated&page=10&info_level=2
                            //https://wall.alphacoders.com/api2.0/get.php?auth=" + API_KEY + "&method=search&term="+ query
                            new ReadJSON().execute("https://mobile.alphacoders.com/by-resolution/1/1080x1920-Wallpapers?search=" + query);
                        }
                    });

                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                    return true;
                }

                return false;
            }
        });


        searchBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int DRAWABLE_RIGHT = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (searchBar.getRight() - searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        animationView.pauseAnimation();
                        animationView.setVisibility(View.INVISIBLE);
                        searchQueryText.setVisibility(View.INVISIBLE);
                        searchQuery.setVisibility(View.INVISIBLE);

                        searchNet.setText(getResources().getString(R.string.search));
                        searchNet.setVisibility(View.VISIBLE);
                        imageView.setImageResource(R.drawable.searchwall);
                        imageView.setVisibility(View.VISIBLE);

                        searchBar.setText("");

                        recyclerView.setVisibility(View.INVISIBLE);
                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        return;
    }

    class loadMore extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                Log.i("IN LOAD MORE", "TRUE");
                Document document = Jsoup.connect(params[0]).get();
                Element wall = document.select("div.thumb-container").first();
                Elements img = wall.getElementsByAttribute("src");
                Elements widList = wall.getElementsByAttribute("alt");
                List list = img.eachAttr("src");
                List id = widList.eachAttr("alt");

                if (currPg <= numPages) {
                    Log.i("IN IF CONDN", "TRUE");
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
                } else return null;
            } catch (Exception e) {
                Log.i("ERROR 2", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            searchFragmentCustomAdapter.notifyDataSetChanged();
            currPg++;
        }
    }


    class ReadJSON extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {
            if (isNetworkAvailable()) {
                try {
                    currPg = 1;
                    Log.i("NUM OF PAGES ", String.valueOf(numPages));
                    Document document = Jsoup.connect(params[0]).get();
                    Element wall = document.select("div.thumb-container").first();
                    //Log.i("RESULT", wall.toString());
                    Elements img = wall.getElementsByAttribute("src");
                    Elements widList = wall.getElementsByAttribute("alt");
                    Element page = document.select("ul.pagination.pagination").first();
                    Element result = document.select("div.searchTitle.alert.alert-warning").first();
                    if (result == null) {
                        if (page != null) {
                            Elements pageNum = page.getElementsByAttribute("href");
                            List pageText = pageNum.eachText();
                            String temp = pageText.get(pageText.size() - 2).toString();
                            numPages = Integer.parseInt(temp);
                            Log.i("IN READJSON CURRENT", String.valueOf(currPg));
                            Log.i("IN READJSON", String.valueOf(numPages));
                        } else numPages = 1;

                        List list = img.eachAttr("src");
                        List id = widList.eachAttr("alt");
                        Log.i("ARRAYLIST", list.toString());
                        Log.i("ID", id.toString());

                        for (int i = 0; i < list.size(); i++) {
                            String wallUrl = list.get(i).toString();
                            String wallId = id.get(i).toString();
                            String sep[] = wallId.split("Wallpaper ");
                            wallpapersModels.add(new WallpapersModel(
                                    wallUrl,///
                                    wallUrl.replace("thumb-", ""),
                                    "jpg",
                                    Integer.valueOf(sep[1])
                            ));
                        }
                    } else {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageResource(R.drawable.searchwall404);
                                imageView.setVisibility(View.VISIBLE);
                                searchNet.setText(getResources().getString(R.string.search404) + query);
                                searchNet.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.i("ERROR LOLO", e.toString());
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageResource(R.drawable.nonetwork);
                        imageView.setVisibility(View.VISIBLE);
                        searchNet.setText("No Internet connection!");
                        searchNet.setVisibility(View.VISIBLE);
                    }
                });
            }
            //Log.i("RESULT", result.toString());
            Log.i("DATA OF SITE ", wallpapersModels.toString());
            return null;
        }

        @Override
        protected void onPreExecute() {
            wallpapersModels.clear();
            recyclerView.setVisibility(View.INVISIBLE);

            animationView.setVisibility(View.VISIBLE);

            imageView.setVisibility(View.INVISIBLE);
            searchNet.setVisibility(View.INVISIBLE);

            searchQueryText.setVisibility(View.VISIBLE);
            searchQueryText.setText(getResources().getString(R.string.search_query));
            searchQuery.setText(query.toUpperCase());
            searchQuery.setVisibility(View.VISIBLE);

            animationView.setAnimation("wallFind.json");
            animationView.loop(true);
            animationView.playAnimation();
        }

        @Override
        protected void onPostExecute(String content) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.scrollToPosition(0);
            animationView.pauseAnimation();
            animationView.setVisibility(View.INVISIBLE);
            searchQueryText.setVisibility(View.INVISIBLE);
            searchQuery.setVisibility(View.INVISIBLE);
//            try {
//                JSONObject jsonObject = new JSONObject(content);
//                JSONArray jsonArray =  jsonObject.getJSONArray("wallpapers");
//
//                for(int i =0;i<jsonArray.length(); i++){
//                    JSONObject wallpaperObject = jsonArray.getJSONObject(i);
//                    wallpapersModels.add(new WallpapersModel(
//                            wallpaperObject.getString("url_thumb"),
//                            wallpaperObject.getString("url_image"),
//                            wallpaperObject.getString("file_type"),
//                            wallpaperObject.getInt("id")
//
//                    ));
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            searchFragmentCustomAdapter.notifyDataSetChanged();
            currPg++;
            Log.i("POST EXECUTE", "ME HUN MAI");
            searchFragmentCustomAdapter.setLoaded();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

