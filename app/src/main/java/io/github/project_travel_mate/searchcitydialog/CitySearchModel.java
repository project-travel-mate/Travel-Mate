package io.github.project_travel_mate.searchcitydialog;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Items to be displayed in search list
 */
public class CitySearchModel implements Parcelable {

    private String mName;
    private String mImageUrl;
    private String mId;

    public CitySearchModel(String name, String imageUrl, String id) {
        mName = name;
        mImageUrl = imageUrl;
        mId = id;
    }

    protected CitySearchModel(Parcel in) {
        mName = in.readString();
        mImageUrl = in.readString();
        mId = in.readString();
    }

    public static final Creator<CitySearchModel> CREATOR = new Creator<CitySearchModel>() {
        @Override
        public CitySearchModel createFromParcel(Parcel in) {
            return new CitySearchModel(in);
        }

        @Override
        public CitySearchModel[] newArray(int size) {
            return new CitySearchModel[size];
        }
    };

    public String getTitle() {
        return mName;
    }

    public String getName() {
        return mName;
    }

    public CitySearchModel setName(String name) {
        mName = name;
        return this;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getId() {
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mImageUrl);
        parcel.writeString(mId);
    }
}