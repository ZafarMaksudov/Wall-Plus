package ytstudios.wall.bucket;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yugansh Tyagi on 15-09-2017.
 */

public class WallpapersModel implements Parcelable{

    private String  wallpaperURL;
    private String  wallpaperFullURL;
    private String fileType;
    private int wallId;

    public WallpapersModel(String wallpaperURL, String wallpaperFullURL, String fileType, int wallId) {
        this.wallpaperURL = wallpaperURL;
        this.wallpaperFullURL = wallpaperFullURL;
        this.fileType = fileType;
        this.wallId = wallId;

    }

    private WallpapersModel(Parcel parcel){
        wallpaperURL = parcel.readString();
        wallpaperFullURL = parcel.readString();
        fileType = parcel.readString();
        wallId = parcel.readInt();
    }

    public static final Parcelable.Creator<WallpapersModel> CREATOR = new Parcelable.Creator<WallpapersModel>() {
        public WallpapersModel createFromParcel(Parcel parcel) {
            return new WallpapersModel(parcel);
        }

        public WallpapersModel[] newArray(int size) {
            return new WallpapersModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(wallpaperURL);
        parcel.writeString(wallpaperFullURL);
        parcel.writeString(fileType);
        parcel.writeInt(wallId);
    }

    public String getWallpaperFullURL() {
        return wallpaperFullURL;
    }

    public String getWallpaperURL() {
        return wallpaperURL;
    }


    public String getFileType() {
        return fileType;
    }

    public int getWallId() {
        return wallId;
    }
}
