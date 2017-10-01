package ytstudios.wall.bucket;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Yugansh Tyagi on 10-09-2017.
 */

public class Downloaded_Fragment extends Fragment {

    //GridView gridView;
    RecyclerView recyclerView;
    Context context;
    GridLayoutManager gridLayoutManager;
    DownloadFragmentAdapter downloadFragmentAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    //Directories
    private ArrayList<String> filePaths;
    private ArrayList<String> fileNames;
    private File[] files;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.downloaded_fragment, null);

        try {
            File imageDir = new File(Environment.getExternalStorageDirectory().toString() + "/Wall Bucket/Downloads");
            Log.i("DIR", imageDir.toString());
            if (imageDir.exists()) {
                files = imageDir.listFiles();
                Log.i("FILES", String.valueOf(files.length));
                filePaths = new ArrayList<>();
                fileNames = new ArrayList<>();

                for (int i = 0; i < files.length; i++) {
                    // Get the path of the image file
                    filePaths.add(i, files[i].getAbsolutePath());
                    Log.i("FILES", filePaths.get(i));
                    // Get the name image file
                    fileNames.add(i, files[i].getAbsolutePath());
                    Log.i("FILENAMES", fileNames.get(i));
                }
                Log.i("SIZE  ", String.valueOf(filePaths.size()));

            }
        } catch (Exception e) {
            Log.i("EXCEPTION ", e.toString());
        }

        downloadFragmentAdapter = new DownloadFragmentAdapter(getActivity().getApplicationContext(), filePaths, fileNames);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new refreshDownloads().execute();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
                recyclerView.invalidate();
                Log.i("DATA CHANGED", "DATA SET CHANGED");
            }
        });

        gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView = view.findViewById(R.id.downloaded_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new RecyclerItemDecoration(2));
        recyclerView.setAdapter(downloadFragmentAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        return;
    }


    public class refreshDownloads extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            try {
                File imageDir = new File(Environment.getExternalStorageDirectory().toString() + "/Wall Bucket/Downloads");
                Log.i("DIR", imageDir.toString());
                if (imageDir.exists()) {
                    files = imageDir.listFiles();
                    Log.i("FILES", String.valueOf(files.length));
                    filePaths.clear();
                    fileNames.clear();

                    for (int i = 0; i < files.length; i++) {
                        // Get the path of the image file
                        filePaths.add(i, files[i].getAbsolutePath());
                        Log.i("FILES", filePaths.get(i));
                        // Get the name image file
                        fileNames.add(i, files[i].getAbsolutePath());
                        Log.i("FILENAMES", fileNames.get(i));
                    }
                    Log.i("SIZE  ", String.valueOf(filePaths.size()));

                }
            } catch (Exception e) {
                Log.i("EXCEPTION ", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("REFRESHED", "CONGO!");
            //downloadFragmentAdapter = new DownloadFragmentAdapter(context, filePaths, fileNames);
            downloadFragmentAdapter.notifyDataSetChanged();
            //recyclerView.setAdapter(downloadFragmentAdapter);
            Log.i("ITEM RANGE CHANGED", "ITEM SET CHANGED");
        }
    }
}

