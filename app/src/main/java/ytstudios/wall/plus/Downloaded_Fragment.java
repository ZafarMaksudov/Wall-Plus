package ytstudios.wall.plus;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

/**
 * Created by Yugansh Tyagi on 10-09-2017.
 */

public class Downloaded_Fragment extends Fragment {

    //GridView gridView;
    RecyclerView recyclerView;
    DownloadFragmentAdapter adapter;
    Context context;
    GridLayoutManager gridLayoutManager;

    //Directories
    private String[] filePaths;
    private String[] fileNames;
    private File[] files;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.downloaded_fragment, null);

        try{
            File imageDir = new File(Environment.getExternalStorageDirectory().toString()+ "/Wall+/Wall+ Downloads");
            Log.i("DIR", imageDir.toString());
            if(imageDir.exists()){
                files = imageDir.listFiles();
                Log.i("FILES", String.valueOf(files.length));
                filePaths = new String [files.length];
                fileNames = new String [files.length];

                for (int i = 0; i < files.length; i++) {
                    // Get the path of the image file
                    filePaths[i] = files[i].getAbsolutePath();
                    Log.i("FILES", filePaths.toString());
                    // Get the name image file
                    fileNames[i] = files[i].getName();
                    Log.i("FILES", fileNames.toString());
                }

            }
        }
        catch (Exception e){
            Log.i("EXCEPTION ", e.toString());
        }

//
//        gridView = view.findViewById(R.id.grid_view);
//        adapter = new DownloadFragmentAdapter(getActivity(), filePaths, fileNames);
//        gridView.setAdapter(adapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(context, fileNames[i].toString(), Toast.LENGTH_SHORT).show();
//            }
//        });


        gridLayoutManager = new GridLayoutManager(context,2);
        recyclerView = view.findViewById(R.id.downloaded_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new RecyclerItemDecoration(2));
        recyclerView.setAdapter(new DownloadFragmentAdapter(getActivity().getApplicationContext(), filePaths, fileNames));

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu,menu);
        return;
    }

}

