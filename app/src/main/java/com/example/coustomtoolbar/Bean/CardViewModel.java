package com.example.coustomtoolbar.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yaojian on 2017/7/4.
 */

public class CardViewModel implements Parcelable{

    private String TextName;
    private String EditName;

    protected CardViewModel(Parcel in) {
        TextName = in.readString();
        EditName = in.readString();
    }

    public static final Creator<CardViewModel> CREATOR = new Creator<CardViewModel>() {
        @Override
        public CardViewModel createFromParcel(Parcel in) {
            return new CardViewModel(in);
        }

        @Override
        public CardViewModel[] newArray(int size) {
            return new CardViewModel[size];
        }
    };

    public String getTextName() {
        return TextName;
    }

    public void setTextName(String textName) {
        TextName = textName;
    }

    public String getEditName() {
        return EditName;
    }

    public void setEditName(String editName) {
        EditName = editName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(TextName);
        dest.writeString(EditName);
    }
}
