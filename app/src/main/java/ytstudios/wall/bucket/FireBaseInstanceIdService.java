package ytstudios.wall.bucket;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Yugansh Tyagi on 26-10-2017.
 */

public class FireBaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TOKEN = "------->TOKEN";

    @Override
    public void onTokenRefresh() {
        Log.d("THIS IS TOKEN", "YES IT IS");
        String recentToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TOKEN, recentToken);
    }
}
