package ytstudios.wall.plus;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import static ytstudios.wall.plus.Home_Fragment.API_KEY;

/**
 * Created by Yugansh Tyagi on 10-09-2017.
 */

public class Search_Fragment extends Fragment {

    EditText searchBar;
    ImageView imageView;
    TextView searchNet,searchQueryText,searchQuery;
    LottieAnimationView animationView;

    ArrayList<WallpapersModel> wallpapersModels;
    RecyclerView recyclerView;
    SearchFragmentCustomAdapter searchFragmentCustomAdapter;

    String query;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        wallpapersModels = new ArrayList<>();

        View view = inflater.inflate(R.layout.search_fragment, null);

        recyclerView = view.findViewById(R.id.searchFragment_rv);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        searchBar = view.findViewById(R.id.search_bar);

        searchFragmentCustomAdapter = new SearchFragmentCustomAdapter(wallpapersModels, getContext(), searchBar.getText().toString());
        recyclerView.setAdapter(searchFragmentCustomAdapter);
        recyclerView.addItemDecoration(new RecyclerItemDecoration(2));

        imageView = view.findViewById(R.id.search_wall_placeholder) ;
        searchNet = view.findViewById(R.id.search_net);
        searchQueryText = view.findViewById(R.id.searching_query);
        animationView = view.findViewById(R.id.animation_view);
        searchQuery = view.findViewById(R.id.query_name);

        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                query = searchBar.getText().toString();

                if (i == EditorInfo.IME_ACTION_SEARCH && query.length() > 2) {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //https://wall.alphacoders.com/api2.0/get.php?auth="+API_KEY+"&method=highest_rated&page=10&info_level=2
                            new ReadJSON().execute("https://wall.alphacoders.com/api2.0/get.php?auth=" + API_KEY + "&method=search&term="+ query );
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
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if(motionEvent.getRawX() >= (searchBar.getRight() - searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        animationView.pauseAnimation();
                        animationView.setVisibility(View.INVISIBLE);
                        searchQueryText.setVisibility(View.INVISIBLE);
                        searchQuery.setVisibility(View.INVISIBLE);

                        searchNet.setVisibility(View.VISIBLE);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.menu,menu);
        return;
    }

    class ReadJSON extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... params) {
            return readURL(params[0]);
        }

        @Override
        protected void onPreExecute() {
            recyclerView.setVisibility(View.INVISIBLE);

            animationView.setVisibility(View.VISIBLE);
            searchQueryText.setVisibility(View.VISIBLE);

            imageView.setVisibility(View.INVISIBLE);
            searchNet.setVisibility(View.INVISIBLE);

            searchQueryText.setVisibility(View.VISIBLE);
            searchQueryText.setText("Searching wallpapers!");
            searchQuery.setText(query.toUpperCase());
            searchQuery.setVisibility(View.VISIBLE);

            animationView.setAnimation("wallFind.json");
            animationView.loop(true);
            animationView.playAnimation();
        }

        @Override
        protected void onPostExecute(String content) {
            wallpapersModels.clear();
            recyclerView.setVisibility(View.VISIBLE);
            animationView.pauseAnimation();
            animationView.setVisibility(View.INVISIBLE);
            searchQueryText.setVisibility(View.INVISIBLE);
            searchQuery.setVisibility(View.INVISIBLE);

            try {
                JSONObject jsonObject = new JSONObject(content);
                JSONArray jsonArray =  jsonObject.getJSONArray("wallpapers");

                for(int i =0;i<jsonArray.length(); i++){
                    JSONObject wallpaperObject = jsonArray.getJSONObject(i);
                    wallpapersModels.add(new WallpapersModel(
                            wallpaperObject.getString("url_thumb"),
                            wallpaperObject.getString("url_image"),
                            wallpaperObject.getString("file_type"),
                            wallpaperObject.getInt("id")

                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            searchFragmentCustomAdapter.notifyDataSetChanged();
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
}

