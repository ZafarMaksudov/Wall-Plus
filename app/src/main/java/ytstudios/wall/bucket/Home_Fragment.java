package ytstudios.wall.bucket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yugansh Tyagi on 10-09-2017.
 */

public class Home_Fragment extends Fragment {

    ArrayList<WallpapersModel> wallpapersModelArrayList;
    RecyclerView recyclerView;
    HomeFragmentCustomAdapter homeFragmentCustomAdapter;
    ImageView noNetImage;
    TextView noNetText;
    Button connectBtn;

    GridLayoutManager gridLayoutManager;

    boolean isNetworkConnected;

    public static int pageCount = 2;

    protected Handler handler;

    public static int spanCount = 3;

    public static String API_KEY;

    public static int wallpaperNumber = 0;

    private int numPages;

    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home_fragment, null);

        spanCount = 3;

        API_KEY = getResources().getString(R.string.API_KEY);

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(loadMoreBroadcastReceiver,
                new IntentFilter("LoadMore"));
//        LocalBroadcastManager.getInstance(getContext()).registerReceiver(initDataBroadcastReceiver,
//                new IntentFilter("InitData"));

        progressBar = view.findViewById(R.id.progressBar);


        noNetImage = view.findViewById(R.id.noNet);
        noNetText = view.findViewById(R.id.noNetText);
        connectBtn = view.findViewById(R.id.connectBtn);
        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
            }
        });


        wallpapersModelArrayList = new ArrayList<>();
        handler = new Handler();

        initData();

        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView = view.findViewById(R.id.homeFragment_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        homeFragmentCustomAdapter = new HomeFragmentCustomAdapter(wallpapersModelArrayList, getContext(), recyclerView);
        homeFragmentCustomAdapter.setOnLoadMoreListener(new onLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (pageCount < numPages) {
                    Log.i("CURRENT PAGE ", String.valueOf(pageCount));
                    Log.i("NUMBER OF  PAGE ", String.valueOf(numPages));
                    wallpapersModelArrayList.add(null);
                    Log.i("INSERTED", "NULL");
                    homeFragmentCustomAdapter.notifyItemInserted(wallpapersModelArrayList.size() - 1);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            wallpapersModelArrayList.remove(wallpapersModelArrayList.size() - 1);
                            homeFragmentCustomAdapter.notifyItemRemoved(wallpapersModelArrayList.size());
                            Log.i("REMOVED", "NULL");
                            //add items one by one
                            Log.i("INIT", "DATA");
                            new loadMore().execute("http://papers.co/android/page/" + pageCount + "/");
                            homeFragmentCustomAdapter.setLoaded();
                            Log.i("INIT", "FINISHED");
                        }
                    }, 900);
                }
            }
        });

        recyclerView.setAdapter(homeFragmentCustomAdapter);
        recyclerView.addItemDecoration(new RecyclerItemDecoration(2));

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            boolean n = isNetworkAvailable();
            if (n) {
                initData();
            }
        }
    }


    public void initData() {
        isNetworkConnected = isNetworkAvailable();
        if (isNetworkConnected) {
            noNetImage.setVisibility(View.INVISIBLE);
            noNetText.setVisibility(View.INVISIBLE);
            connectBtn.setVisibility(View.GONE);

            loadFromInternet("http://papers.co/android/");
            //https://spliffmobile.com/mobile-wallpapers/hd/wallpapers-for-mobile-2-1-16.html
            //http://papers.co/android/
            //http://wallpaperscraft.com/all/1080x1920

        } else {

            noNetImage.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nonetwork));
            noNetImage.setVisibility(View.VISIBLE);
            noNetText.setVisibility(View.VISIBLE);
            connectBtn.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "No Internet Connected!", Toast.LENGTH_SHORT).show();

        }
    }

    public class loadMore extends AsyncTask<String, Integer, String> {

        List list;
        List id;

        @Override
        protected String doInBackground(String... params) {
            try {
                Document document = Jsoup.connect(params[0]).get();
                Element wall = document.select("ul.postul").first();
                //Log.i("LIST ", wall.toString());
                Elements url = wall.getElementsByAttribute("src");
                list = url.eachAttr("src");

                if(pageCount <= numPages) {

                    for (int i = 0; i < list.size(); i++) {
                        wallpaperNumber++;
                        String string = list.get(i).toString();
                        String sep[] = string.split("http://");
                        sep[1] = sep[1].replace("android/wp-content/uploads", "wallpaper");
                        String septemp[] = sep[1].split("wallpaper-");
                        //Log.i("SEPARATION ", septemp[1]);
                        if(!septemp[1].contains("250x400.jpg")){
                            //Log.i("YES ", "It will be fixed!");
                            septemp[0] = septemp[0] + "wallpaper-" + septemp[1] + "wallpaper.jpg?download=true";
                            sep[1] = "http://" + septemp[0];
                        }
                        else {
                            Log.i("String ", septemp[0]);
                            septemp[0] = septemp[0] + "wallpaper.jpg?download=true";
                            sep[1] = "http://" + septemp[0];
                            //Log.i("String ", septemp[0]);
                        }
                        //Log.i("URL", string);
                        wallpapersModelArrayList.add(wallpapersModelArrayList.size() - 1, new WallpapersModel(
                                string,///
                                sep[1],
                                "jpg",
                                wallpaperNumber
                        ));
                    }
                }
            } catch (Exception e) {
                Log.i("ERROR", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.i("COUNT ", String.valueOf(count));
            pageCount++;
            homeFragmentCustomAdapter.notifyItemInserted(wallpapersModelArrayList.size());
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.grid_two:
                spanCount = 2;
                gridLayoutManager.setSpanCount(spanCount);
                recyclerView.setLayoutManager(gridLayoutManager);
                return true;
            case R.id.grid_three:
                spanCount = 3;
                gridLayoutManager.setSpanCount(spanCount);
                recyclerView.setLayoutManager(gridLayoutManager);
                return true;
            case R.id.refresh:
                pageCount = 2;
                recyclerView.setVisibility(View.INVISIBLE);
                initData();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void loadFromInternet(final String url) {

        if (isNetworkConnected) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wallpapersModelArrayList.clear();
                    //new ReadJSON().execute(url);
                    new ReadHTML().execute(url);
                }
            });
        }
    }

    class ReadHTML extends AsyncTask<String, Integer, String> {

        List list;


        @Override
        protected String doInBackground(String... params) {

            //***************PAPERS
            try {

                Document document = Jsoup.connect(params[0]).get();
                Element wall = document.select("ul.postul").first();
                //Log.i("LIST ", wall.toString());
                Elements url = wall.getElementsByAttribute("src");
                Element page = document.select("a.page-numbers").last();
                numPages = Integer.parseInt(page.text());
                Log.i("PAGE NUMBER ", String.valueOf(numPages));
                list = url.eachAttr("src");

                for (int i = 0; i < list.size(); i++) {
                    wallpaperNumber++;
                    String string = list.get(i).toString();
                    String sep[] = string.split("http://");
                    sep[1] = sep[1].replace("android/wp-content/uploads", "wallpaper");
                    String septemp[] = sep[1].split("wallpaper-");
                    septemp[0] = septemp[0] + "wallpaper.jpg?download=true";
                    sep[1] = "http://" + septemp[0];
                    Log.i("String ", sep[1]);
                    //Log.i("URL", string);
                    wallpapersModelArrayList.add(new WallpapersModel(
                            string,///
                            sep[1],
                            "jpg",
                            wallpaperNumber,
                            0
                    ));
                }
            } catch (Exception e) {
                Log.i("ERROR LOADING WEBSITE ", e.toString());
            }
            return null;
        }
        //************************************
        //Elements url = wall.getElementsByAttribute("src");
        //Log.i("URL 1  ", url.toString());
//                list = url.eachAttr("src");
//
//                for (int i = 0; i < list.size(); i++) {
//                    wallpaperNumber++;
//                    String string = list.get(i).toString();
//                    String sep[] = string.split("http://");
//                    sep[1] = sep[1].replace("android/wp-content/uploads", "wallpaper");
//                    //sep[1] = sep[1].replace("-6-", "-6-");
//                    sep[1] = sep[1].replace("-250x400.jpg", ".jpg?download=true");
//                    sep[1] = "http://" + sep[1];
//                    Log.i("String ", sep[1]);
//                    Log.i("URL", string);
//                    wallpapersModelArrayList.add(new WallpapersModel(
//                            string,///
//                            sep[1],
//                            "jpg",
//                            wallpaperNumber
//                    ));
//                }


//                //FOR WALLPAPERSCRAFT
//                Document document = Jsoup.connect(params[0]).get();
//                Element wall = document.select("div.wallpapers").first();
//                //Log.i("WALL  ", wall.toString());
//                Elements url = wall.getElementsByAttribute("src");
//                //Log.i("ELEMENTS   ", url.toString());
//                list = url.eachAttr("src");
//
//                for(int i = 0; i < list.size(); i++){
//                    String string = list.get(i).toString();
//                    String[] sep = string.split("wallpaperscraft.com");
//                    ///Log.i("URL ", sep[1]);
//                    wallpapersModelArrayList.add(new WallpapersModel(
//                            "https:/www.wallpaperscraft.com"+sep[1].replace("168x300", "320x480"),///
//                            "https:/www.wallpaperscraft.com"+sep[1].replace("168x300", "1080x1920"),
//                            "jpg",
//                            1
//                    ));
//                }

        @Override
        protected void onPreExecute() {
            Toast.makeText(getContext(), "Loading Wallpapers!", Toast.LENGTH_SHORT).show();
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.i("COUNT ", String.valueOf(count));
            homeFragmentCustomAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    class ReadJSON extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPostExecute(String content) {
            try {
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray = jsonObject.getJSONArray("wallpapers");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject wallpaperObject = jsonArray.getJSONObject(i);
//                    if(wallpaperObject.getString("success").length() <= 4){
                    wallpapersModelArrayList.add(new WallpapersModel(
                            wallpaperObject.getString("url_thumb"),
                            wallpaperObject.getString("url_image"),
                            wallpaperObject.getString("file_type"),
                            wallpaperObject.getInt("id")
                    ));
//                    }
//                    else {
//                        imageView.setVisibility(View.VISIBLE);
//                        return;
//                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            homeFragmentCustomAdapter.notifyDataSetChanged();
        }
    }

    private static String readURL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);
            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();
            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private BroadcastReceiver loadMoreBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            new loadMore().execute("http://papers.co/android/page/" + pageCount + "/");
            Log.i("BroadCast LoadMore", "RECEIVED");
            homeFragmentCustomAdapter.notifyDataSetChanged();
        }
    };

//    private BroadcastReceiver initDataBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            initData();
//            Log.i("BroadCast InitData", "RECEIVED");
//            homeFragmentCustomAdapter.notifyDataSetChanged();
//        }
//    };
}
