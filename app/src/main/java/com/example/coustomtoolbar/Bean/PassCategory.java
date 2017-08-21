package com.example.coustomtoolbar.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SEELE on 2017/8/22.
 */

public class PassCategory implements Parcelable{
    private String id;
    private String name;


    protected PassCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<PassCategory> CREATOR = new Creator<PassCategory>() {
        @Override
        public PassCategory createFromParcel(Parcel in) {
            return new PassCategory(in);
        }

        @Override
        public PassCategory[] newArray(int size) {
            return new PassCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
